package com.palagincom.server.dao;

import com.palagincom.server.domain.Order;
import com.palagincom.server.dto.OrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

}
