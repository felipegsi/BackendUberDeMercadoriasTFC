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

}
