package com.stockmarket.stockmarketapi.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "watchlist")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Watchlist {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @Column(name = "watchlist_id", nullable = false)
  private Long watchlistId;

  @NonNull
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @NonNull
  @Column(name = "stock_name", nullable = false)
  private String stockName;

  @NonNull
  @Column(name = "stock_ticker", nullable = false)
  private String stockTicker;

  @NonNull
  @Column(name = "price", nullable = false)
  private Double price;

  @NonNull
  @Column(name = "previous_day_close", nullable = false)
  private Double previousDayClose;

  @NonNull
  @Column(name = "_52_week_high", nullable = false)
  private Double _52WeekHigh;

  @NonNull
  @Column(name = "_52_week_low", nullable = false)
  private Double _52WeekLow;

  @NonNull
  @Column(name = "market_cap_in_billions", nullable = false)
  private Double marketCapInBillions;

  @NonNull
  @Column(name = "annual_dividend_yield", nullable = false)
  private Double annualDividendYield;

  @NonNull
  @JsonIgnore
  @Column(name = "updated_date", nullable = false)
  private LocalDateTime updatedDate = LocalDateTime.now();

}
