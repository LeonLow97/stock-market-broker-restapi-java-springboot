package com.stockmarket.stockmarketapi.stockmodel;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "StockDataDTO",
    description = "Data Transfer Object used to communicate with Yahoo Finance API to retrieve relevant stock data.")
public class StockData {

  @Schema(description = "Stock name retrieved from Yahoo Finance API", example = "Apple Inc.",
      required = true)
  private String stockName;

  @Schema(
      description = "Stock ticker used in path parameter to retrieve relevant stock data from Yahoo Finance API",
      example = "AAPL", required = true)
  private String stockTicker;

  @Schema(description = "Previous day close of the stock retrieved from Yahoo Finance API",
      example = "167.63", required = true)
  private BigDecimal previousDayClose;

  @Schema(description = "Stock Price of the stock retrieved from Yahoo Finance API",
      example = "166.65", required = true)
  private BigDecimal stockPrice;

  @Schema(description = "Day High of the stock retrieved from Yahoo Finance API",
      example = "167.87", required = true)
  private BigDecimal dayHigh;

  @Schema(description = "Day Low of the stock retrieved from Yahoo Finance API", example = "165.56",
      required = true)
  private BigDecimal dayLow;

  @Schema(description = "Day Change In Percent of the stock retrieved from Yahoo Finance API",
      example = "-0.58", required = true)
  private BigDecimal dayChangeInPercent;

  @Schema(description = "52 Week Change In Percent of the stock retrieved from Yahoo Finance API",
      example = "-5.39", required = true)
  private BigDecimal _52WeekChangeInPercent;

  @Schema(description = "52 Week High of the stock retrieved from Yahoo Finance API",
      example = "176.15", required = true)
  private BigDecimal _52WeekHigh;

  @Schema(
      description = "Annual Dividend of the stock in percentage retrieved from Yahoo Finance APi",
      example = "0.5428622500", required = true)
  private BigDecimal yearDividendInPercent;

}
