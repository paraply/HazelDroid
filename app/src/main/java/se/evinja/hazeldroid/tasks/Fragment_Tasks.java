package se.evinja.hazeldroid.tasks;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.workers.Fragment_Worker_Edit;
import se.evinja.hazeldroid.workers.Fragment_Worker_Show;

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
        hazel = (Hazel) parent.getApplication();
        tasks_listview.setAdapter(hazel.getAdapter_tasks(parent));
        parent.set_title(getString(R.string.tasks));
        hazel.download_tasks(parent);

        tasks_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show_show_task_fragment(position);
            }
        });
        if (hazel.access_adminlevel()) {
            registerForContextMenu(tasks_listview);
        }

        return  view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((Activity_Main) getActivity()).navigation_open()) {
            if (hazel.access_adminlevel()) {
                inflater.inflate(R.menu.tasks, menu);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.tasks_long_click, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_new_task){
            show_add_fragment();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        if (id == R.id.long_click_edit_task) {
            show_edit_fragment(info.position);
        }
        else if (id == R.id.long_click_delete_task) {
            hazel.delete_task(info.position);
        }
        return false;
    }

    private void show_show_task_fragment(int position){
        Fragment_Task_Show show_t = Fragment_Task_Show.newInstance(position);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, show_t)
                .addToBackStack("task show")
                .commit();
    }

    private void show_edit_fragment(int position){
        Fragment_Task_Add edit_t = Fragment_Task_Add.newInstance(position);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, edit_t)
                .addToBackStack("task edit")
                .commit();
    }

    private void show_add_fragment(){
        Fragment_Task_Add show_t = Fragment_Task_Add.newInstance();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, show_t)
                .addToBackStack("task add")
                .commit();
    }

}
