package se.evinja.hazeldroid.tasks;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Calendar;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;


public class Fragment_Task_Add extends Fragment {
    private Hazel hazel;
    private Activity_Main parent;

    private EditText title, description, min_w, max_w;
    private Calendar start, end;


    public static Fragment_Task_Add newInstance() {
        return new Fragment_Task_Add();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        hazel.download_qualifications_and_workers(parent); //if somehow gotten here without doing that yet..
        final View view = inflater.inflate(R.layout.fragment_task_add, container, false);
        title = (EditText) view.findViewById(R.id.task_add_name);
        description = (EditText) view.findViewById(R.id.task_add_desc);
        min_w = (EditText) view.findViewById(R.id.task_add_min_work);
        max_w = (EditText) view.findViewById(R.id.task_add_max_work);
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(activity.getString(R.string.add_task));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!parent.navigation_open()) {
            inflater.inflate(R.menu.task_add, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_task_add_save){
            validate_and_save();
        }else if (id == R.id.action_task_add_cancel){
            parent.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_and_save(){
        Object_Task t = new Object_Task();
        //TODO VALIDATE
        t.title = title.getText().toString();
        t.description = description.getText().toString();
        t.min_workers = Integer.parseInt(min_w.getText().toString());
        t.max_workers = Integer.parseInt(max_w.getText().toString());
        t.start = Calendar.getInstance();
        t.end = Calendar.getInstance();
        t.end.add(Calendar.HOUR_OF_DAY, 1);
        t.repeat_length = 3;
        t.set_repeats_weekly();
        hazel.add_task(t);
        parent.onBackPressed();
    }


}
