package se.evinja.hazeldroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import se.evinja.hazeldroid.qualifications.Fragment_Qualifications;
import se.evinja.hazeldroid.tasks.Fragment_Tasks;
import se.evinja.hazeldroid.schedules.Fragment_User_Schedule;
import se.evinja.hazeldroid.schedules.Fragment_Work_Schedule;
import se.evinja.hazeldroid.workers.Fragment_Workers;

public class Activity_Main extends ActionBarActivity implements Callback_Navigate, Callback_Hazel {
    private Hazel hazel;
    private Fragment_Navigate fragment_navigation;
    private CharSequence title;

    private Fragment_User_Schedule frag_user_schedule;
    private Fragment_Work_Schedule frag_work_schedule;
    private Fragment_Workers frag_workers;
    private Fragment_Tasks frag_tasks;
    private Fragment_Qualifications frag_qualifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hazel = (Hazel) getApplication();
        hazel.set_listener_and_parent(this, this); //Take over from Activity_Login
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
        fragment_navigation = (Fragment_Navigate) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        fragment_navigation.setup(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
//        Toast.makeText(this, "Welcome to Hazel @ " + hazel.get_client_name(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (fragment_navigation.isOpen()){
            fragment_navigation.close(); //Close navigation if open
        }else{
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            // check to see if stack is empty
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                ft.commit();
            }
            else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_log_out){ //Menu item log out, only menu item for main activity - fragments will override and use own methods
            hazel.logout();
            Intent intent = new Intent(this, Activity_Login.class);
            startActivity(intent);
            fragment_navigation = null;
            finish(); //Kill this activity
        }
        return super.onOptionsItemSelected(item);
    }



    public boolean navigation_open(){
        return fragment_navigation.isOpen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!fragment_navigation.isOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            getSupportActionBar().setTitle(title);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fm = getFragmentManager(); //Clear the backstack when menu item pressed
        for (int i = 0; i < fm.getBackStackEntryCount(); i++){
            fm.popBackStack();
        }

        if (hazel.access_adminlevel()){
            position += 1; //Admin has not "my schedule" item
        }
        switch (position){
            case 0:
                show_my_schedule();
                break;
            case 1:
                show_workplace_schedule();
                break;
            case 2:
                show_workers();
                break;
            case 3:
                show_tasks();
                break;
            case 4:
                show_qualifications();
                break;
        }
    }


    private void fragment_replace(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void show_my_schedule(){
        frag_user_schedule = Fragment_User_Schedule.newInstance();
        fragment_replace(frag_user_schedule);
    }

    private void show_workplace_schedule(){
        frag_work_schedule = Fragment_Work_Schedule.newInstance();
        fragment_replace(frag_work_schedule);
    }

    private void show_workers(){
        frag_workers = Fragment_Workers.newInstance();
        fragment_replace(frag_workers);
    }

    private void show_tasks(){
        frag_tasks = Fragment_Tasks.newInstance();
        fragment_replace(frag_tasks);
    }

    private void show_qualifications(){
        frag_qualifications= Fragment_Qualifications.newInstance();
        fragment_replace(frag_qualifications);
    }

    public void set_title(String new_title){
        title = new_title;
    }

    @Override
    public void onError(Hazel.HazelCommand duringCommand, String errorMsg) {
        Toast.makeText(this,getString(R.string.error_during) + duringCommand + getString(R.string.error_message) + errorMsg, Toast.LENGTH_LONG ).show();
    }

    @Override
    public void onConnected() {}

    @Override
    public void onUserSchedule() {}

    @Override
    public void onStaffSchedule() {}

    @Override
    public void onTasksDownloaded() {
//        Toast.makeText(this, "Downloaded tasks", Toast.LENGTH_LONG ).show();
    }

    @Override
    public void onStaffDownloaded() {

    }

    @Override
    public void onQualificationsDownloaded() {

    }

    @Override
    public void onWorkerAdded() {
        Toast.makeText(this, getString(R.string.worker_added_successfully), Toast.LENGTH_LONG ).show();
    }

}
