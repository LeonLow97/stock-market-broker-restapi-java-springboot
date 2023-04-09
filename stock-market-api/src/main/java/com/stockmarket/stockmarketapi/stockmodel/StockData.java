package com.stockmarket.stockmarketapi.stockmodel;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockData {
  
  private String stockName;
  private String stockTicker;
  private BigDecimal previousDayClose;
  private BigDecimal stockPrice;
  private BigDecimal dayHigh;
  private BigDecimal dayLow;
  private BigDecimal dayChangeInPercent;
  private BigDecimal _52WeekChangeInPercent;
  private BigDecimal _52WeekHighHigh;
  private BigDecimal yearDividendInPercent;

}
