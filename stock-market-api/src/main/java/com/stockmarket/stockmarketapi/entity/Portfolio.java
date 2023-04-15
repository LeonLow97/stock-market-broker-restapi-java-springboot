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
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Portfolio {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @Column(name = "portfolio_id", nullable = false)
  private Long portfolioId;

  @NonNull
  @JsonIgnore
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @NonNull
  @Column(name = "stock_name", nullable = false)
  private String stockName;

  @NonNull
  @Column(name = "stock_ticker", nullable = false)
  private String stockTicker;

  @NonNull
  @Column(name = "no_of_shares", nullable = false)
  private int noOfShares;

  @NonNull
  @Column(name = "cost", nullable = false)
  private double cost;

  @NonNull
  @Column(name = "price", nullable = false)
  private double price;

  @NonNull
  @Column(name = "pnl_in_percentage", nullable = false)
  private double PNLInPercentage;

  @NonNull
  @Column(name = "pnl_in_dollars", nullable = false)
  private double PNLInDollars;

  @Column(name = "added_date", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime addedDate = LocalDateTime.now();

  @Column(name = "updated_date", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedDate = LocalDateTime.now();

}
