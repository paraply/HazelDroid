package se.evinja.hazeldroid.schedules;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.R;

public class Fragment_Work_Schedule extends Fragment {
    Activity_Main parent;

    public Fragment_Work_Schedule(){};

    public static Fragment_Work_Schedule newInstance(){
        return  new Fragment_Work_Schedule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_schedule, container, false);
        return  view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(getString(R.string.work_schedule));
    }

}
