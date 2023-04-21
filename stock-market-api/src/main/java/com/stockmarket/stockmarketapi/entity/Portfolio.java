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
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "PortfolioDTO",
    description = "Data Transfer Object of the portfolio which contains all the stocks the user has purchased.")
public class Portfolio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @Column(name = "portfolio_id", nullable = false)
  private long portfolioId;

  @NonNull
  @JsonIgnore
  @Column(name = "user_id", nullable = false)
  private long userId;

  @Schema(description = "Name of the stocks inside the portfolio of the user.",
      example = "Apple Inc.", required = true)
  @NonNull
  @Column(name = "stock_name", nullable = false)
  private String stockName;

  @Schema(description = "Stock ticker of the stocks inside the portfolio of the user.",
      example = "AAPL", required = true)
  @NonNull
  @Column(name = "stock_ticker", nullable = false)
  private String stockTicker;

  @Schema(description = "Number of shares of the stocks inside the portfolio of the user.",
      example = "60", required = true)
  @NonNull
  @Column(name = "no_of_shares", nullable = false)
  private int noOfShares;

  @Schema(description = "Cost of the stock purchased by the user.", example = "134.54",
      required = true)
  @NonNull
  @Column(name = "cost", nullable = false)
  private double cost;

  @Schema(description = "Current price of the stock in the market", example = "154.65",
      required = true)
  @NonNull
  @Column(name = "price", nullable = false)
  private double price;

  @Schema(
      description = "PNL In Percentage of the stock in the portfolio which will be calculated by the application",
      example = "10.43", required = true)
  @NonNull
  @Column(name = "pnl_in_percentage", nullable = false)
  private double PNLInPercentage;

  @Schema(
      description = "PNL IN Dollars of the stock in the portfolio which will be calculated by the application",
      example = "20.11", required = true)
  @NonNull
  @Column(name = "pnl_in_dollars", nullable = false)
  private double PNLInDollars;

  @Schema(description = "Added date of the stock in the portfolio.", required = true)
  @Column(name = "added_date", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime addedDate = LocalDateTime.now();

  @Schema(description = "Updated date of the stock in the portfolio.", required = true)
  @Column(name = "updated_date", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedDate = LocalDateTime.now();

}
