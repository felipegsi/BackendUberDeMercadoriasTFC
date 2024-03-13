package com.project.uber.repository;


import com.project.uber.enums.OrderStatus;
import com.project.uber.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Método existente
    List<Order> findByClientId(Long clientId);

    // Novos métodos sugeridos

    // Encontrar pedidos por status
    List<Order> findByStatus(OrderStatus status);

    // Encontrar pedidos pendentes (PENDING) que ainda não foram aceitos por nenhum motorista
    List<Order> findByStatusAndDriverIsNull(OrderStatus status);

    // Encontrar pedidos aceitos por um determinado motorista
    List<Order> findByDriverEmailAndStatus(String email, OrderStatus status);

    // Encontrar um pedido específico que está pendente e ainda não foi aceito por nenhum motorista
    Optional<Order> findByIdAndStatusAndDriverIsNull(Long orderId, OrderStatus status);
}
