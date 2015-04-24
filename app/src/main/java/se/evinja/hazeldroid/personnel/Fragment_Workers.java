package se.evinja.hazeldroid.personnel;

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

public class Fragment_Workers extends Fragment {
    private Activity_Main parent;
    private Hazel hazel;

    public Fragment_Workers(){};

    public static Fragment_Workers newInstance(){
        return new Fragment_Workers();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        View view = inflater.inflate(R.layout.fragment_workers, container, false);
        ListView workers_listview = (ListView) view.findViewById(R.id.workers_list);
        hazel.download_workers(parent);
        workers_listview.setAdapter(hazel.getAdapter_workers());

        workers_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show_show_worker_fragment(position);
            }
        });
        if (hazel.access_adminlevel()) {
            registerForContextMenu(workers_listview);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(getString(R.string.workers));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((Activity_Main) getActivity()).navigation_open()) {
            if (hazel.access_adminlevel()) {
                inflater.inflate(R.menu.workers, menu);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.workers_long_click, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        if (id == R.id.long_click_edit_worker) {
            show_edit_fragment(info.position);
        }
        else if (id == R.id.long_click_delete_worker) {
            hazel.delete_worker(info.position);
        }
        return false;
    }

    private void show_show_worker_fragment(int position){
        Fragment_Show_Worker show_w = Fragment_Show_Worker.newInstance(position);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, show_w)
                .addToBackStack("worker show")
                .commit();
    }


    private void show_edit_fragment(int position){
        Fragment_Edit_Worker edit_w = Fragment_Edit_Worker.newInstance(position);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, edit_w)
                .addToBackStack("worker edit")
                .commit();
    }


}
