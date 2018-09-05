package edu.gatech.cs2340.testingdimockitoexample.dummy;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by robertwaters on 3/15/17.
 */
@Singleton
@Component (modules={TestDataSourceModule.class})
public interface IDataSourceComponent {
    IDataSource makeDataSource();

}
