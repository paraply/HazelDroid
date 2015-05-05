package se.evinja.hazeldroid.qualifications;


import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Object_Qualification {
    public String title;
    private Activity parent;
    private Hazel hazel;
    private String update_not_confirmed;

    public Object_Qualification(String title, Hazel hazel){
        this.title = title;
        this.hazel = hazel;
        this.parent = hazel.parent;
    }

    @Override
    public String toString(){
        return title;
    }


    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("qualname", title);
            jo.put("client", hazel.client);
        } catch (JSONException e) {
            hazel.onError("Qualification to json failed");
        }
        return jo;
    }

    public JSONObject update(String new_title){
        JSONObject jo = new JSONObject();
        try {
            jo.put("old_qual", title);
            jo.put("new_qual", new_title);
            update_not_confirmed = new_title;

        } catch (JSONException e) {
            hazel.onError("Qualification Update to JSON");
            e.printStackTrace();
        }
    return jo;
    }

    public void confirm_update(){
        title = update_not_confirmed;
        update_not_confirmed = null;
    }



    public String getWorkerString(){
//        if (amount == null){ //Comment out if take to much cpu
        int new_amount = hazel.get_worker_has_qualification(this);
//        }
        if (new_amount == 0){
            return parent.getString(R.string.no_worker_has_qualification);
        }else if (new_amount == 1){
            return parent.getString(R.string.one_worker_has_qualification);
        }else{
            return new_amount + parent.getString(R.string.workers_has_qualification);
        }
    }
}
