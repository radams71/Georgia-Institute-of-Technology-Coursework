package edu.gatech.cs2340.testingdimockitoexample.dummy;

/**
 * Created by robertwaters on 3/16/17.
 */

public class StockPricer {
    private DataService myService;

    public StockPricer(DataService ds) {
        myService = ds;
    }

    public int portfolioValue(int shares, String stock) {
        int price = myService.getCurrentPrice(stock);
        return price * shares;
    }
}
