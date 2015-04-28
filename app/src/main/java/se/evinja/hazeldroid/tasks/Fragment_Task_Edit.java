package se.evinja.hazeldroid.tasks;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Qualifications_Selector;
import se.evinja.hazeldroid.workers.Fragment_Worker_Edit;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Fragment_Task_Edit extends Fragment {
    private Hazel hazel;
    private Object_Worker worker;
    private Activity_Main parent;

    public static Fragment_Task_Edit newInstance(int task_position) {
        Fragment_Task_Edit fragment = new Fragment_Task_Edit();
        Bundle args = new Bundle();
        args.putInt("task_position", task_position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(activity.getString(R.string.edit_task));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!parent.navigation_open()) {
            inflater.inflate(R.menu.task_edit, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        hazel = (Hazel) getActivity().getApplication();
        hazel.download_tasks(parent); //Probably unnecessary
        worker = hazel.get_worker(getArguments().getInt("task_position"));

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_task_edit_save){
            validate_and_save();
        }else if (id == R.id.action_task_edit_cancel){
            parent.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_and_save(){
        //TODO VALIDATE

        hazel.update_task_list();

        parent.onBackPressed();
    }
}
