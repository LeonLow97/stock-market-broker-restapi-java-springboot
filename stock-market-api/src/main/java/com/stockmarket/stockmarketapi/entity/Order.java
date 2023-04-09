package com.stockmarket.stockmarketapi.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long orderId;

  @NonNull
  @JsonIgnore
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @NonNull
  @Column(name = "stock_ticker", nullable = false)
  private String stockTicker;

  @NonNull
  @Column(name = "order_type", nullable = false)
  private String orderType;

  @Column(name = "no_of_shares", nullable = false)
  private int noOfShares;

  @NonNull
  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "order_date", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime orderDate = LocalDateTime.now();

}
