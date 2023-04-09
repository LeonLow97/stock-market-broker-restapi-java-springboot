package com.stockmarket.stockmarketapi.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.entity.Order;
import com.stockmarket.stockmarketapi.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

  @Autowired
  OrderService orderService;

  @PostMapping("/orders")
  public ResponseEntity<Order> submitOrder(HttpServletRequest request, @RequestBody Order order) {
    int userId = (Integer) request.getAttribute("userId");
    orderService.submitOrder(userId, order);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }
  
}
