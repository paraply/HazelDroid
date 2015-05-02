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
import android.widget.TextView;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.workers.Fragment_Worker_Edit;

public class Fragment_Task_Show extends Fragment {
    private Activity_Main parent;
    private Hazel hazel;
    private  Object_Task t;

    public Fragment_Task_Show(){}

    public static Fragment_Task_Show newInstance(int task_position) {
        Fragment_Task_Show fragment = new Fragment_Task_Show();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((Activity_Main) getActivity()).navigation_open()) {
            if (hazel.access_adminlevel()) {
                inflater.inflate(R.menu.task_show, menu);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_task_edit) {
            show_edit_fragment(getArguments().getInt("task_position"));
        }
        else if (id == R.id.action_show_task_delete) {
            hazel.delete_task(getArguments().getInt("task_position"));
            parent.set_title(getString(R.string.tasks)); //Restore title
            parent.onBackPressed();
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        View view = inflater.inflate(R.layout.fragment_task_show, container, false);

        t = hazel.get_task(getArguments().getInt("task_position"));
        ((TextView) view.findViewById(R.id.task_show_name)).setText(t.title);
        ((TextView) view.findViewById(R.id.task_show_desc)).setText(t.description);
        ((TextView) view.findViewById(R.id.task_show_start_date)).setText(t.getStartDate());
        ((TextView) view.findViewById(R.id.task_show_start_time)).setText(t.getStartTime());
        ((TextView) view.findViewById(R.id.task_show_end_date)).setText(t.getEndDate());
        ((TextView) view.findViewById(R.id.task_show_end_time)).setText(t.getEndTime());
        ((TextView) view.findViewById(R.id.task_show_repeat)).setText(t.get_repeat_string());
        ((TextView) view.findViewById(R.id.task_show_qualifications)).setText(t.getQualifications());
        ((TextView) view.findViewById(R.id.task_show_min_work)).setText(Integer.toString(t.min_workers));
        ((TextView) view.findViewById(R.id.task_show_max_work)).setText(Integer.toString(t.max_workers));
        ((TextView) view.findViewById(R.id.task_show_workers)).setText(t.getWorkers());


//        ((TextView) view.findViewById(R.id.show_worker_fullname)).setText(w.get_fullName());
//        ((TextView) view.findViewById(R.id.show_worker_position)).setText(w.position);
//        ((TextView) view.findViewById(R.id.show_worker_mail)).setText(w.mailAddress);
//        ((TextView) view.findViewById(R.id.show_worker_phone)).setText(w.phoneNr);
//        ((TextView) view.findViewById(R.id.show_worker_personnr)).setText(w.get_personnumber());
//        ((TextView) view.findViewById(R.id.show_worker_qualifications)).setText(w.get_qualificationstring(parent));
        parent.set_title(t.title);
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;

    }

    private void show_edit_fragment(int position){
        Fragment_Task_Add edit_t = Fragment_Task_Add.newInstance(position);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, edit_t)
                .addToBackStack("task edit")
                .commit();
    }
}
