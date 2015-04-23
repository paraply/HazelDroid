package se.evinja.hazeldroid.personnel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Qualifications extends Fragment {
    private Activity_Main parent;
    private ListView qualifications_listview;
    private Adapter_Qualifications adapter_qualifications;
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
        View view = inflater.inflate(R.layout.fragment_qualifications, container, false);
        qualifications_listview = (ListView) view.findViewById(R.id.qualifications_list);
        List<Object_Qualification> qualifications = new ArrayList<>();
        adapter_qualifications = new Adapter_Qualifications(parent, qualifications);
        qualifications_listview.setAdapter(adapter_qualifications);

        qualifications.add(new Object_Qualification("A-körkort", 3));
        qualifications.add(new Object_Qualification("Pratar Hindi", 993263520));
        adapter_qualifications.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(getString(R.string.qualifications));
    }

}
