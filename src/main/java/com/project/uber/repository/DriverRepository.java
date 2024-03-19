package com.project.uber.repository;
import com.project.uber.model.Client;
import com.project.uber.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
}