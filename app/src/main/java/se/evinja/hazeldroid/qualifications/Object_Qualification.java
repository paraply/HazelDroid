package se.evinja.hazeldroid.qualifications;


import android.app.Activity;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Object_Qualification {
    public String title;
    private String amount;
    private Activity parent;
    private Hazel hazel;

    public Object_Qualification(String title, Activity parent, Hazel hazel){
        this.title = title;
        this.hazel = hazel;
        this.parent = parent;
    }

    @Override
    public String toString(){
        return title;
    }

    public String getWorkerString(){
//        if (amount == null){ //Comment out if take to much cpu
            int new_amount = hazel.get_worker_has_qualification(title);
//        }
        if (new_amount == 0){
            return parent.getString(R.string.no_worker_has_qualification);
        }else if (new_amount == 1){
            return parent.getString(R.string.one_worker_has_qualification);
        }else{
            return amount + parent.getString(R.string.workers_has_qualification);
        }
    }
}
