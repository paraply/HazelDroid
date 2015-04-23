package se.evinja.hazeldroid.personnel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Qualifications extends Fragment {
    private Activity_Main parent;
    private Hazel hazel;

    public Fragment_Qualifications(){};

    public static Fragment_Qualifications newInstance(){
        return new Fragment_Qualifications();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        View view = inflater.inflate(R.layout.fragment_qualifications, container, false);
        ListView qualifications_listview = (ListView) view.findViewById(R.id.qualifications_list);
        hazel.download_qualifications(parent);
        qualifications_listview.setAdapter(hazel.getAdapter_qualifications());
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(getString(R.string.qualifications));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((Activity_Main) getActivity()).navigation_open()) {
            if (hazel.access_adminlevel()) {
                inflater.inflate(R.menu.qualifications, menu);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_new_qualification){

        }
        return super.onOptionsItemSelected(item);
    }
}
