package edu.gatech.cs2340.testingdimockitoexample.dummy;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by robertwaters on 3/15/17.
 */
@Module
public class TestDataSourceModule {
    private final boolean mockMode;

    public TestDataSourceModule(boolean provideMock) {
        mockMode = provideMock;
    }

    @Provides
    @Singleton
    IDataSource provideIDataSource() {
        if (mockMode) {
            return Mockito.mock(IDataSource.class);
        } else {
            return new DummyContent();
        }
    }
}
