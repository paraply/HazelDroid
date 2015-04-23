package se.evinja.hazeldroid.personnel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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



        qualifications_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show_edit_dialog(position);
            }
        });
        registerForContextMenu(qualifications_listview);

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

    private void show_edit_dialog(final int position){
        final EditText q_title = new EditText(parent);
        q_title.setText(hazel.get_qualifications().get(position).title);
        new AlertDialog.Builder(parent)
                .setTitle(parent.getString(R.string.edit_qualification))
                .setView(q_title)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        hazel.update_qualification(position,q_title.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_new_qualification){
            final EditText q_title = new EditText(parent);
            new AlertDialog.Builder(parent)
                    .setTitle(parent.getString(R.string.add_qualification))
                    .setView(q_title)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            hazel.add_qualification(q_title.getText().toString());
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.qualifications_long_click, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        if (id == R.id.long_click_edit_qualification) {
            show_edit_dialog(info.position);
        }
        else if (id == R.id.long_click_delete_qualification) {
            hazel.delete_qualification(info.position);
        }
        return false;
    }

}
