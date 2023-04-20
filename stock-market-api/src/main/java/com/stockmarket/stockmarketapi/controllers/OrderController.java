package com.stockmarket.stockmarketapi.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.DTOs.OrderSubmitDTO;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

  @Autowired
  OrderService orderService;

  @GetMapping("/orders")
  public ResponseEntity<List<Order>> getAllOrders(HttpServletRequest request) {
    Integer userId = (Integer) request.getAttribute("userId");
    return new ResponseEntity<>(orderService.getAllOrders(userId), HttpStatus.OK);
  }

  @GetMapping("/orders/{orderId}")
  public ResponseEntity<Order> getOrder(HttpServletRequest request, @PathVariable("orderId") Integer orderId) {
    Integer userId = (Integer) request.getAttribute("userId");
    return new ResponseEntity<>(orderService.getOrder(userId, orderId), HttpStatus.OK);
  }

  @PostMapping("/orders")
  public ResponseEntity<Order> submitOrder(HttpServletRequest request, @RequestBody OrderSubmitDTO orderSubmitDTO) {
    Integer userId = (Integer) request.getAttribute("userId");
    Order order = orderService.submitOrder(userId, orderSubmitDTO);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }
  
}