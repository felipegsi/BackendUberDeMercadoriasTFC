package com.project.uber.repository;

import com.project.uber.model.ClientTest123;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepositoryTest123 extends JpaRepository<ClientTest123, Long>{
    ClientTest123 findByEmail(String email);
}
