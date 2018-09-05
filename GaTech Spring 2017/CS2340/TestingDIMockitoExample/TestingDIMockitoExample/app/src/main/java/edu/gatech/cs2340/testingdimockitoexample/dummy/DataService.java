package edu.gatech.cs2340.testingdimockitoexample.dummy;

/**
 * Created by robertwaters on 3/16/17.
 */

public interface DataService {

    int getCurrentPrice(String stock);
    boolean setCurrentPrice(String stock, int price);


}
