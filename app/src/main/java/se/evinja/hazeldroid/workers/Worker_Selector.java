package se.evinja.hazeldroid.workers;


import android.app.Activity;
import android.app.DialogFragment;
import android.widget.Toast;

import se.evinja.hazeldroid.Hazel;

public class Worker_Selector extends DialogFragment {
    private Hazel hazel;
    private Activity parent;

    public void init(Hazel hazel, Activity parent){
        this.hazel = hazel;
        this.parent = parent;
    }

    public void show(){
        Toast.makeText(parent,"SHOW", Toast.LENGTH_SHORT).show();
    }



}
