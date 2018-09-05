package edu.gatech.cs2340.testingdimockitoexample.dummy;

import java.util.List;

/**
 * Created by robertwaters on 3/15/17.
 */

public interface IDataSource {
    List<DummyContent.DummyItem> getItems();
    void addItem();
    void removeItem();
    int getItemCount();
}
