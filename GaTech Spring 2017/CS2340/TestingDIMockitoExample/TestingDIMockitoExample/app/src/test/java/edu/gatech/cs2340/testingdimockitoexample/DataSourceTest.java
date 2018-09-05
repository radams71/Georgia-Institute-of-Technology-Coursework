package edu.gatech.cs2340.testingdimockitoexample;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.testingdimockitoexample.dummy.DataService;
import edu.gatech.cs2340.testingdimockitoexample.dummy.DummyContent;
import edu.gatech.cs2340.testingdimockitoexample.dummy.IDataSource;
import edu.gatech.cs2340.testingdimockitoexample.dummy.IDataSourceComponent;
import edu.gatech.cs2340.testingdimockitoexample.dummy.StockPricer;
import edu.gatech.cs2340.testingdimockitoexample.dummy.TestDataSourceModule;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by robertwaters on 3/16/17.
 */

public class DataSourceTest {
    StockPricer pricer;

    @Before
    public void setUp() {
        DataService service = mock(DataService.class);
        when(service.getCurrentPrice("MSFT")).thenReturn(230);
        when(service.setCurrentPrice("MSFT", 135)).thenReturn(true);

        pricer = new StockPricer(service);
    }

    @Test
    public void testPortfolioValue() {
        assertEquals(pricer.portfolioValue(5, "MSFT"), 1150);
    }

}
