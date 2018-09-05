package edu.gatech.cs2340.testingdimockitoexample;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import edu.gatech.cs2340.testingdimockitoexample.dummy.DaggerIDataSourceComponent;
import edu.gatech.cs2340.testingdimockitoexample.dummy.DummyContent;
import edu.gatech.cs2340.testingdimockitoexample.dummy.IDataSource;
import edu.gatech.cs2340.testingdimockitoexample.dummy.TestDataSourceModule;

public class MainActivity extends AppCompatActivity implements BookFragment.OnListFragmentInteractionListener ,
        DashboardFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener {

    @Inject
    IDataSource dataSource;

    public void onAddItem(View v) {
        Log.d("APP", "Add item pressed");
        dataSource.addItem();
    }

    public void onRemoveItem(View v) {
        Log.d("APP", "Remove item pressed");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showHomeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    showDashboardFragment();
                    return true;
                case R.id.navigation_notifications:
                    showNotificationsFragment();
                    return true;
            }
            return false;
        }

    };

    private void showHomeFragment() {
       // getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.main_fragment)).commit();
        Fragment fragment = BookFragment.newInstance(1);
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void showDashboardFragment() {
        //getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.main_fragment)).commit();
        Fragment fragment = DashboardFragment.newInstance("Foo", "Bar");
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void showNotificationsFragment() {
        Fragment fragment = NotificationsFragment.newInstance("Foo", "Bar");
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dataSource = DaggerIDataSourceComponent.builder().testDataSourceModule
                (new TestDataSourceModule(false)).build().makeDataSource();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
         Log.d("APP", "ListFragment Interaction called: " + item );
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
         Log.d("APP", "Fragment interaction called: " + uri);
    }
}
