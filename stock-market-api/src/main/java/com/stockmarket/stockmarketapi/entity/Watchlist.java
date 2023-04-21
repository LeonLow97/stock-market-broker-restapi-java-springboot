package com.stockmarket.stockmarketapi.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "WatchlistDTO",
    description = "Data Transfer Object for the watchlist of the user, which returns the list of stocks added into the watchlist by the user.")
public class Watchlist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @Column(name = "watchlist_id", nullable = false)
  private long watchlistId;

  @Schema(
      description = "Unique Identifier of the user that is a foreign key to the 'accounts' table.",
      example = "1", required = true)
  @NonNull
  @Column(name = "user_id", nullable = false)
  private long userId;

  @Schema(description = "Name of the stocks inside the watchlist of the user.",
      example = "JPMorgan Chase & Co.", required = true)
  @NonNull
  @Column(name = "stock_name", nullable = false)
  private String stockName;

  @Schema(description = "Stock ticker of the stocks inside the watchlist of the user.",
      example = "JPM", required = true)
  @NonNull
  @Column(name = "stock_ticker", nullable = false)
  private String stockTicker;

  @Schema(description = "Price of the stock inside the watchlist", example = "141.01",
      required = true)
  @NonNull
  @Column(name = "price", nullable = false)
  private double price;

  @Schema(description = "Previous day close of the stock inside the watchlist", example = "138.76",
      required = true)
  @NonNull
  @Column(name = "previous_day_close", nullable = false)
  private double previousDayClose;

  @Schema(description = "52 Week High of the stock inside the watchlist", example = "167.87",
      required = true)
  @NonNull
  @Column(name = "_52_week_high", nullable = false)
  private double _52WeekHigh;

  @Schema(description = "52 Week Low of the stock inside the watchlist", example = "102.32",
      required = true)
  @NonNull
  @Column(name = "_52_week_low", nullable = false)
  private double _52WeekLow;

  @Schema(description = "Market Capitalisation of the business in billions", example = "123.21",
      required = true)
  @NonNull
  @Column(name = "market_cap_in_billions", nullable = false)
  private double marketCapInBillions;

  @Schema(description = "Annual dividend yield payout by the business in percentage",
      example = "4.23", required = true)
  @NonNull
  @Column(name = "annual_dividend_yield", nullable = false)
  private double annualDividendYield;

  @NonNull
  @JsonIgnore
  @Column(name = "updated_date", nullable = false)
  private LocalDateTime updatedDate = LocalDateTime.now();

}
