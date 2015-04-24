package se.evinja.hazeldroid.personnel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Edit_Worker extends Fragment{
    private Hazel hazel;
    private Object_Worker w;

    public static Fragment_Edit_Worker newInstance(int person_position) {
        Fragment_Edit_Worker fragment = new Fragment_Edit_Worker();
        Bundle args = new Bundle();
        args.putInt("worker_position", person_position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_worker_edit, container, false);

        w = hazel.get_worker(getArguments().getInt("worker_position"));


        return view;
    }


}
