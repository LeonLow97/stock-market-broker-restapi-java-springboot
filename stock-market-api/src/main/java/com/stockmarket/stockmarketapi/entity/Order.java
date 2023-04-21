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
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "OrderDTO",
    description = "Data Transfer Object of the orders associated with the user, returns the history of all the orders.")
public class Order {

  @Schema(
      description = "Unique identifier of the user which is a foreign key to the 'accounts' table.",
      example = "1", required = true)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private long orderId;

  @NonNull
  @JsonIgnore
  @Column(name = "user_id", nullable = false)
  private long userId;

  @Schema(description = "Stock ticker of the stock associated with the order.", example = "AAPL",
      required = true)
  @NonNull
  @Column(name = "stock_ticker", nullable = false)
  private String stockTicker;

  @Schema(description = "Order type of the stock associated with the order.", example = "BUY",
      required = true)
  @NonNull
  @Column(name = "order_type", nullable = false)
  private String orderType;

  @Schema(description = "Number of shares transacted in the order for the stock.", example = "60",
      required = true)
  @NonNull
  @Column(name = "no_of_shares", nullable = false)
  private int noOfShares;

  @Schema(description = "Price purchased/sold for each order.", example = "143.21", required = true)
  @NonNull
  @Column(name = "cost", nullable = false)
  private double cost;

  @Schema(description = "Order date for each order.", example = "2023-04-22 12:10:33", required = true)
  @Column(name = "order_date", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime orderDate = LocalDateTime.now();

}
