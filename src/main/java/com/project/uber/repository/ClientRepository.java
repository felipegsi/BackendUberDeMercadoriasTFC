package com.project.uber.repository;

import com.project.uber.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);//felipe@

// encontrar cliente por id
    Client findById(int id);


    Client findByPhoneNumber(int phone);


}

