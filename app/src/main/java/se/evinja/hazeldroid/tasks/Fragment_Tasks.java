package se.evinja.hazeldroid.tasks;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Tasks extends Fragment {
    private Activity_Main parent;
    private Hazel hazel;

    public Fragment_Tasks(){};

    public static Fragment_Tasks newInstance(){
        return new Fragment_Tasks();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        ListView tasks_listview = (ListView) view.findViewById(R.id.tasks_list);
        tasks_listview.setAdapter(hazel.getAdapter_tasks(parent));
        hazel = (Hazel) parent.getApplication();
        parent.set_title(getString(R.string.tasks));
        hazel.download_tasks(parent);
        return  view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
    }

}
