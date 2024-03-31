package com.project.uber.service.implementation;

import com.project.uber.dtos.OrderDto;
import com.project.uber.enums.Category;
import com.project.uber.enums.OrderStatus;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Client;
import com.project.uber.model.Order;
import com.project.uber.repository.OrderRepository;
import com.project.uber.service.interfac.ClientService;
import com.project.uber.service.interfac.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public BigDecimal estimateOrderCost(OrderDto orderDto) {
        OkHttpClient client = new OkHttpClient();

        String[] originCoordinates = orderDto.getOrigin().split(",");
        String[] destinationCoordinates = orderDto.getDestination().split(",");

        Request request = new Request.Builder()
                .url("https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248b8fc3d76941643ee9de00a23820316b7&start="
                        + originCoordinates[1] + "," + originCoordinates[0] + "&end=" + destinationCoordinates[1] + ","
                        + destinationCoordinates[0])
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            // Assumindo que a distância está dentro do primeiro objeto do array "segments"
            // dentro do primeiro objeto "features", dentro da propriedade "properties".
            int distanceInMeters = jsonObject.getJSONArray("features")
                    .getJSONObject(0)
                    .getJSONObject("properties")
                    .getJSONArray("segments")
                    .getJSONObject(0) // Assumindo que queremos o primeiro segmento
                    .getInt("distance");

            BigDecimal distanceInKm = new BigDecimal(distanceInMeters).divide(new BigDecimal(1000));


            return calculateOrderCostBasedOnDimensionsAndCategory(orderDto, distanceInKm);
        } catch (Exception e) {
            throw new BusinessException("Failed to estimate order cost");
        }
    }


/*
*
* verifyOrderDimensionsWithCategory
* 1. Pequeno
Descrição: Ideal para pacotes e itens pequenos que podem ser facilmente carregados por uma pessoa.
Especificações: Itens até 40x40x40 cm e até 10 kg.
Exemplos: Caixas, pequenos eletrodomésticos, documentos.
2. Médio
Descrição: Adequado para cargas que necessitam de mais espaço do que um item pequeno, mas ainda são manejáveis para uma entrega rápida e eficiente.
Especificações: Itens até 100x50x50 cm e até 30 kg.
Exemplos: Móveis desmontados, grandes caixas de mudança, bicicletas.
3. Grande
Descrição: Para itens volumosos ou pesados que não se encaixam nas categorias menores, requerendo veículos maiores para transporte.
Especificações: Itens maiores que 100x50x50 cm ou mais pesados que 30 kg, mas que cabem em um veículo de carga padrão.
Exemplos: Eletrodomésticos de grande porte, móveis grandes, equipamento de ginástica.
4. Motorizados
Descrição: Especializada para o transporte de veículos e equipamentos motorizados de todos os tamanhos, incluindo máquinas de obras.
Especificações: Todos os tipos de veículos e equipamentos que possuem motor próprio. Não há limitações específicas de dimensão ou peso, mas requer veículos especiais para transporte.
Exemplos: Carros, motocicletas, tratores, Bobcats, empilhadeiras.
*
* */

    public BigDecimal calculateOrderCostBasedOnDimensionsAndCategory(OrderDto orderDto, BigDecimal distanceInKm) {
        BigDecimal value = BigDecimal.valueOf(1.00);
        BigDecimal additionalValue = BigDecimal.ZERO;

        //using swithcase
        switch (orderDto.getCategory()) {
            case SMALL:
                if (orderDto.getWidth() <= 40 && orderDto.getHeight() <= 40 && orderDto.getLength() <= 40 && orderDto.getWeight() <= 10) {
                    additionalValue = BigDecimal.valueOf(1.00);
                } else {
                    throw new BusinessException("Order dimensions do not match the category");
                }
                break;
            case MEDIUM:
                if (orderDto.getWidth() <= 100 && orderDto.getHeight() <= 50 && orderDto.getLength() <= 50 && orderDto.getWeight() <= 30) {
                    additionalValue = BigDecimal.valueOf(2.00);
                } else {
                    throw new BusinessException("Order dimensions do not match the category");
                }
                break;
            case LARGE:
                if (orderDto.getWidth() > 150 || orderDto.getHeight() > 150 || orderDto.getLength() > 150 || orderDto.getWeight() > 30) {
                    additionalValue = BigDecimal.valueOf(3.00);
                } else {
                    throw new BusinessException("Order dimensions do not match the category");
                }
                break;
            case MOTORIZED:
                //adicionar a categoria motorizado+++++++
                additionalValue = BigDecimal.valueOf(20.00);
                break;
            default:
                throw new BusinessException("Invalid category");
        }

        return value.add(additionalValue).multiply(distanceInKm);
    }



    @Override
    public OrderDto saveOrder(OrderDto orderDto, Long clientId) {
        // Chama a função estimateOrderCost e armazena o resultado na variável value
        BigDecimal value = estimateOrderCost(orderDto);
        OrderStatus status = OrderStatus.PENDING;
        Client client = clientService.getClientById(clientId);

        Order order = new Order(orderDto.getOrigin(), orderDto.getDestination(),
                value, status, LocalDate.now(), LocalTime.now(),
                orderDto.getDescription(), orderDto.getFeedback(), client, orderDto.getCategory());

        orderRepository.save(order);

        return new OrderDto(order.getOrigin(), order.getDestination(), order.getDescription(),
                order.getFeedback(), order.getCategory(),
                orderDto.getWidth(),orderDto.getHeight(), orderDto.getLength(), orderDto.getWeight());
    }


    @Override
    public List<OrderDto> getClientOrderHistory(Long clientId) {
        // Localize todos os pedidos do cliente pelo ID do cliente
        List<Order> orders = orderRepository.findByClientId(clientId);

        return orders.stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToOrderDto(Order order) {
        // Implemente a conversão aqui
        return new OrderDto(
                order.getOrigin(),
                order.getDestination(),
                // order.getValue(),
                order.getDescription(),
                order.getFeedback(),
                order.getCategory()


        );
    }


}
