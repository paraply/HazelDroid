package se.evinja.hazeldroid;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import se.evinja.hazeldroid.navigation.Callback_Navigate;
import se.evinja.hazeldroid.navigation.Fragment_Navigate;


public class Activity_Main extends ActionBarActivity implements Callback_Navigate {
    private Hazel hazel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hazel = (Hazel) getApplication();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                }
                return false;
            }
        });

        setSupportActionBar(toolbar); //Toolbar is only for lollipop, set it as an actionbar for older systems
        getSupportActionBar().setElevation(10); //Nice shadow from toolbar, not for API < 21
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Show home button
        Fragment_Navigate fragment_navigation = (Fragment_Navigate) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        fragment_navigation.setup(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); //inflate menu_main.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_log_out){ //Menu item log out, only menu item for main activity - fragments will override and use own methods
            hazel.logout();
            Intent intent = new Intent(this, Activity_Login.class);
            startActivity(intent);
            finish(); //Kill this activity
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onDrawerItemSelected(View view, int position) {
//        display_fragment(position);
//    }
//
//    private void display_fragment(int position){
//        Fragment fragment = null;
//        String title = getString(R.string.app_name);
//        switch (position) {
//            case 0:
//                fragment = new Fragment_User_Schedule();
//                title = getString(R.string.my_schedule);
//                break;
//        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_body, fragment);
//        fragmentTransaction.commit();
//
//
//        getSupportActionBar().setTitle(title); // set the toolbar title
//    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }
}
