package se.evinja.hazeldroid.personnel;

import android.app.Fragment;
import android.os.Bundle;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;

public class Fragment_Show_Worker extends Fragment{

    private Activity_Main parent;
    private Hazel hazel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


}
