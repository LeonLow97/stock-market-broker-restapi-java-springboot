package com.stockmarket.stockmarketapi.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order Controller", description = "To submit an order to purchase or sell a stock.")
@RestController
@RequestMapping("/api")
public class OrderController {

  @Autowired
  OrderService orderService;

  @Operation(summary = "Get All Orders", description = "Retrieves all the orders of the user.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200",
      description = "Successfully retrieved all the orders of the user.",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = Order.class))))})
  @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Order>> getAllOrders(HttpServletRequest request) {
    Integer userId = (Integer) request.getAttribute("userId");
    return new ResponseEntity<>(orderService.getAllOrders(userId), HttpStatus.OK);
  }

  @Operation(summary = "Get Order",
      description = "Retrieves a specific order of the user based on order id.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200",
      description = "Successfully retrieved the specified order of the user.",
      content = @Content(schema = @Schema(implementation = Order.class)))})
  @GetMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Order> getOrder(HttpServletRequest request,
      @PathVariable("orderId") Integer orderId) {
    Integer userId = (Integer) request.getAttribute("userId");
    return new ResponseEntity<>(orderService.getOrder(userId, orderId), HttpStatus.OK);
  }

  @Operation(summary = "Submit Order", description = "Submits a order to BUY or SELL a stock.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Successfully created an order when the stock is bought or sold.",
          content = @Content(schema = @Schema(implementation = OrderSubmitDTO.class))),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content),
      @ApiResponse(responseCode = "404", description = "NOT FOUND - Order Not Found",
          content = @Content),
      @ApiResponse(responseCode = "500",
          description = "INTERNAL SERVER ERROR - Unable to connect to Yahoo Finance API.",
          content = @Content)})
  @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Order> submitOrder(HttpServletRequest request,
      @RequestBody OrderSubmitDTO orderSubmitDTO) {
    Integer userId = (Integer) request.getAttribute("userId");
    Order order = orderService.submitOrder(userId, orderSubmitDTO);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

}
