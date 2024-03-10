package com.project.uber.repository;


import com.project.uber.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Métodos personalizados, se necessário
    List<Order> findByClientId(Long clientId);
}