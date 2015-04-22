package se.evinja.hazeldroid.navigation;


import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Navigate extends Fragment implements Callback_Navigate {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private RecyclerView recyclerView; //Use modern recyclerview instead of old listview. Has to set dependecy in build.gradle to use it.
    private View containerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Callback_Navigate drawer_callbacks;
    private int current_selected_position;
    private Hazel hazel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hazel = (Hazel) getActivity().getApplication();
        if (savedInstanceState != null) {
            current_selected_position = savedInstanceState.getInt(STATE_SELECTED_POSITION); //Get selected position from stored preferences
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        ((TextView) view.findViewById(R.id.navigation_name)).setText(hazel.getFullName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager); //Set layout of recyclerview
        recyclerView.setHasFixedSize(true); //Since menu items are constant

        final List<Item_Navigate> navigation_items = get_navigation_items();
        Adapter_Navigate adapter_navigation = new Adapter_Navigate(navigation_items); //Create adapter that can handle our items
        adapter_navigation.setNavigationDrawerCallbacks(this);
        recyclerView.setAdapter(adapter_navigation); //set adapter to our recyclerView
        select_item(current_selected_position); //Set as last position or default = 0
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            drawer_callbacks = (Callback_Navigate) activity;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity has not implemented NavigationDrawerCallbacks"); //If forgotten to implement in activity
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        drawer_callbacks = null; //reset
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) { //Called from mainActivity
        containerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return; //If fragment is not added to its activity then return
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }
        };

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarDrawerToggle.syncState();
            }
        });

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    public void open() {
        drawerLayout.openDrawer(containerView);
    }

    public boolean isOpen(){
        return drawerLayout != null && drawerLayout.isDrawerOpen(containerView);
    }

    public void close() {
        drawerLayout.closeDrawer(containerView);
    }

    private List<Item_Navigate> get_navigation_items(){
        List<Item_Navigate> items = new ArrayList<>();
        if (!hazel.access_rootlevel()){ //Root has no schedule
            items.add(new Item_Navigate("My schedule", getResources().getDrawable(R.drawable.my_schedule)));
        }
        items.add(new Item_Navigate("Workplace schedule", getResources().getDrawable(R.drawable.work_schedule)));
        items.add(new Item_Navigate("Workers", getResources().getDrawable(R.drawable.workers)));
        if (hazel.access_adminlevel()) { //Only admin can edit tasks and qualifications
            items.add(new Item_Navigate("Tasks", getResources().getDrawable(R.drawable.tasks)));
            items.add(new Item_Navigate("Qualifications", getResources().getDrawable(R.drawable.qualifications)));
        }
        return items;
    }

    private void select_item(int position){
        current_selected_position = position;
        if (drawerLayout != null){
            drawerLayout.closeDrawer(containerView);
        }
        if (drawer_callbacks != null){
            drawer_callbacks.onNavigationDrawerItemSelected(position);
        }
        ((Adapter_Navigate) recyclerView.getAdapter()).selectPosition(position);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        select_item(position);
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putInt(STATE_SELECTED_POSITION, current_selected_position);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig); // Forward the new configuration the drawer toggle component.
    }

}
