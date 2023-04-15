// package com.stockmarket.stockmarketapi.controllertests;

// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import com.stockmarket.stockmarketapi.entity.Portfolio;
// import com.stockmarket.stockmarketapi.service.PortfolioService;
// import com.stockmarket.stockmarketapi.web.PortfolioController;
// import org.json.JSONObject;
// import org.json.JSONArray;
// import java.util.Arrays;
// import java.util.List;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;
// import javax.servlet.http.HttpServletRequest;

// @RunWith(SpringRunner.class)
// @WebMvcTest(PortfolioController.class)
// @AutoConfigureMockMvc(addFilters = false)
// public class PortfolioControllerTests {

//     private static final String GET_PORTFOLIO_PATH = "/api/portfolio";
//     private static final String GET_PORTFOLIO_STOCK = "/api/portfolio/{portfolioId}";

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private PortfolioService portfolioService;

//     @Test
//     public void Test_GetPortfolioShouldReturn200OK() throws Exception {
//         List<Portfolio> testPortfolio = Arrays.asList(
//             new Portfolio(Long.valueOf(1), "Tesla, Inc.", "TSLA", 600, 128.33, 208.15, 80.0, 0.0),
//             new Portfolio(Long.valueOf(1), "Alibaba Group Holding Limited", "BABA", 210, 98.12, 138.17, 70.0, 0.0),
//             new Portfolio(Long.valueOf(1), "Apple Inc.", "AAPL", 45, 134.50, 162.89, 65.0, 0.0)
//         );

//         // Mock the service method to return the mock testPortfolio
//         when(portfolioService.getPortfolio(1)).thenReturn(testPortfolio);

//         // Mock the HttpServletRequest which contains the userId attribute
//         HttpServletRequest request = mock(HttpServletRequest.class);
//         when(request.getAttribute("userId")).thenReturn(1);

//         MvcResult mvcResult = mockMvc.perform(
//             get(GET_PORTFOLIO_PATH).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
//                 requestBuilder.setAttribute("userId", 1);
//                 return requestBuilder;
//             })).andExpect(status().isOk()).andReturn();

//         int status = mvcResult.getResponse().getStatus();
//         assertEquals(200, status);

//         String responseBody = mvcResult.getResponse().getContentAsString();
//         System.out.println(responseBody);
//         JSONArray jsonArray = new JSONArray(responseBody);
//         for (int i = 0; i < jsonArray.length(); i++) {
//             JSONObject jsonObject = jsonArray.getJSONObject(i);
//             assertEquals(testPortfolio.get(i).getStockName(), jsonObject.getString("stockName"));
//             assertEquals(testPortfolio.get(i).getStockTicker(), jsonObject.getString("stockTicker"));
//             assertEquals(testPortfolio.get(i).getNoOfShares(), Integer.parseInt(jsonObject.getString("noOfShares")));
//             assertEquals(testPortfolio.get(i).getCost(), Double.parseDouble(jsonObject.getString("cost")));
//             assertEquals(testPortfolio.get(i).getPrice(), Double.parseDouble(jsonObject.getString("price")));
//             assertEquals(testPortfolio.get(i).getPNLInPercentage(), Double.parseDouble(jsonObject.getString("pnlinPercentage")));
//             assertEquals(testPortfolio.get(i).getPNLInDollars(), Double.parseDouble(jsonObject.getString("pnlinDollars")));
//         }
//     }

//     @Test
//     public void Test_GetPortfolioStockShouldReturn200OK() throws Exception {
//         Portfolio portfolio = new Portfolio(Long.valueOf(1), "Apple Inc.", "AAPL", 45, 134.50, 162.89, 65.0, 0.0);

//         // Mocking the portfolioService to return the expected portfolio
//         when(portfolioService.getPortfolioStock(1, 1)).thenReturn(portfolio);

//         HttpServletRequest request = mock(HttpServletRequest.class);
//         when(request.getAttribute("userId")).thenReturn(1);

//         MvcResult mvcResult = mockMvc.perform(
//             get(GET_PORTFOLIO_STOCK, 1).accept(MediaType.APPLICATION_JSON).with(requestBuilder -> {
//                 requestBuilder.setAttribute("userId", 1);
//                 return requestBuilder;
//             })).andExpect(status().isOk()).andReturn();
        
//         // Extract value from path parameter (regex to capture one or more digits 0-9)
//         Pattern pattern = Pattern.compile("/api/portfolio/(\\d+)");
//         Matcher matcher = pattern.matcher(mvcResult.getRequest().getRequestURI());
//         matcher.find();
//         int portfolioId = Integer.parseInt(matcher.group(1));

//         // Calling the orderService method with the extracted path parameter portfolioId
//         Portfolio returnedPortfolio = portfolioService.getPortfolioStock(1, portfolioId);
//         assertEquals(portfolio, returnedPortfolio);

//         int status = mvcResult.getResponse().getStatus();
//         assertEquals(200, status);

//         String responseBody = mvcResult.getResponse().getContentAsString();
//         JSONObject jsonObject = new JSONObject(responseBody);
//         assertEquals(portfolio.getStockName(), jsonObject.getString("stockName"));
//         assertEquals(portfolio.getStockTicker(), jsonObject.getString("stockTicker"));
//         assertEquals(portfolio.getNoOfShares(), Integer.parseInt(jsonObject.getString("noOfShares")));
//         assertEquals(portfolio.getCost(), Double.parseDouble(jsonObject.getString("cost")));
//         assertEquals(portfolio.getPrice(), Double.parseDouble(jsonObject.getString("price")));
//         assertEquals(portfolio.getPNLInPercentage(), Double.parseDouble(jsonObject.getString("pnlinPercentage")));
//         assertEquals(portfolio.getPNLInDollars(), Double.parseDouble(jsonObject.getString("pnlinDollars")));
//     }
    
// }
