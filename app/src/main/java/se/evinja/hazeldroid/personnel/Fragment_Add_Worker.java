package se.evinja.hazeldroid.personnel;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Add_Worker extends Fragment{
    private Hazel hazel;
    private Object_Worker new_worker = new Object_Worker();
    private  Activity_Main parent;

    public static Fragment_Add_Worker newInstance() {
        Fragment_Add_Worker fragment = new Fragment_Add_Worker();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_worker_add, container, false);

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
    }

}
