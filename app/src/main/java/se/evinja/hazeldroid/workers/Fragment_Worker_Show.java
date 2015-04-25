package se.evinja.hazeldroid.workers;

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

public class Fragment_Worker_Show extends Fragment{

    private Activity_Main parent;
    private Hazel hazel;
    private Object_Worker w;

    public Fragment_Worker_Show(){}

    public static Fragment_Worker_Show newInstance(int person_position) {
        Fragment_Worker_Show fragment = new Fragment_Worker_Show();
        Bundle args = new Bundle();
        args.putInt("worker_position", person_position);
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
                inflater.inflate(R.menu.worker_show, menu);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_worker_edit) {
            show_edit_fragment(getArguments().getInt("worker_position"));
        }
        else if (id == R.id.action_show_worker_delete) {
            hazel.delete_worker(getArguments().getInt("worker_position"));
            parent.set_title(getString(R.string.workers)); //Restore title
            parent.onBackPressed();
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        View view = inflater.inflate(R.layout.fragment_worker_show, container, false);

        w = hazel.get_worker(getArguments().getInt("worker_position"));

        ((TextView) view.findViewById(R.id.show_worker_fullname)).setText(w.get_fullName());
        ((TextView) view.findViewById(R.id.show_worker_position)).setText(w.position);
        ((TextView) view.findViewById(R.id.show_worker_mail)).setText(w.mailAddress);
        ((TextView) view.findViewById(R.id.show_worker_phone)).setText(w.phoneNr);
        ((TextView) view.findViewById(R.id.show_worker_personnr)).setText(w.get_personnumber());
        ((TextView) view.findViewById(R.id.show_worker_qualifications)).setText(w.get_qualificationstring(parent));

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(getString(R.string.edit));
    }

    private void show_edit_fragment(int position){
        Fragment_Worker_Edit edit_w = Fragment_Worker_Edit.newInstance(position);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, edit_w)
                .addToBackStack("worker edit")
                .commit();
    }

}
