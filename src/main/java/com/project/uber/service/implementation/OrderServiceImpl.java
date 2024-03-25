package com.project.uber.service.implementation;

import com.project.uber.dtos.OrderDto;
import com.project.uber.enums.OrderStatus;
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
    public BigDecimal estimateOrderCost(String origin, String destination) {
        OkHttpClient client = new OkHttpClient();

        String[] originCoordinates = origin.split(",");
        String[] destinationCoordinates = destination.split(",");

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
            BigDecimal rate = new BigDecimal("2.0"); // taxa fixa por km

            return distanceInKm.multiply(rate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to estimate order cost", e);
        }
    }

    @Override
    public OrderDto saveOrder(OrderDto orderDto, Long clientId) {
//entra  uma orderDto e sai uma orderDto porem no meio disso
// a Order é salva no banco de dados

        BigDecimal value = BigDecimal.valueOf(10.70);
        OrderStatus status = OrderStatus.PENDING;
        Client client = clientService.getClientById(clientId);

        Order order = new Order(orderDto.getOrigin(), orderDto.getDestination(),
                value, status, LocalDate.now(), LocalTime.now(),
                orderDto.getDescription(), orderDto.getFeedback(), client);

        orderRepository.save(order);

        return new OrderDto(order.getOrigin(), order.getDestination(), order.getDescription(), order.getFeedback());

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
                order.getFeedback()

        );
    }


}
