package se.evinja.hazeldroid.workers;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Object_Qualification;

public class Object_Worker {
    public String id , firstName, lastName, position, mailAddress,birthday, last4, phoneNr, username, password, company;
    public List<Object_Qualification> qualifications = new ArrayList<>();

    public Object_Worker(){}

    public Object_Worker(JSONObject jobj,Hazel hazel){ //Need hazel reference to get qualification from string
            try {
                id = jobj.getString("id");
                firstName = jobj.getString("fstname");
                lastName = jobj.getString("lstname");
                position = jobj.getString("position");
                mailAddress = jobj.getString("mail");
                phoneNr = jobj.getString("phoneNr");
                birthday = jobj.getString("birthday");
                last4 = jobj.getString("last4");


                JSONArray jArr = jobj.getJSONArray("qualifications");
                if (jArr != null){
                    for (int i = 0; i < jArr.length(); i++){
                        Object_Qualification q = hazel.get_qualification(jArr.getString(i));
                        if (q != null){
                            qualifications.add(q);
                        }
                    }
                }

            } catch (Exception e) {
                Log.i("##### ERROR WRK JSON", e.getMessage());
            }

    }

    public JSONObject getJSON(String client_name, int access_level){
        JSONObject jo_outer = new JSONObject();
        JSONObject jo_inner = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            jo_outer.put("username", username);
            jo_outer.put("password", Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP)); //Encode as base64
            jo_outer.put("access_lvl", access_level);
            jo_outer.put("client", client_name);
            jo_outer.put("work_id", JSONObject.NULL); //Not my problem

            jo_inner.put("id", JSONObject.NULL); //Should never put ID..
            jo_inner.put("client", client_name);
            jo_inner.put("fstname", firstName);
            jo_inner.put("lstname", lastName);
            jo_inner.put("position", position);
            jo_inner.put("mail", mailAddress);
            jo_inner.put("phoneNr", phoneNr);
            jo_inner.put("birthday", birthday);
            jo_inner.put("last4", last4);

            for (Object_Qualification q : qualifications ){
                ja.put(q.title);
            }

            jo_inner.put("qualifications", ja);
            jo_outer.put("worker", jo_inner);

        } catch (JSONException e) {
            Log.i("##### WRK JSON CREATE", e.getMessage());
        }

        return jo_outer;
    }

    public String get_fullName(){
        return  firstName + " " + lastName;
    }

    public String get_personnumber(){
        if (birthday != null) {
            return birthday + " - " + last4;
        }else{
            return "";
        }
    }

    public boolean has_qualifications(){
            return qualifications != null && qualifications.size() != 0;
    }

    public boolean has_qualification(Object_Qualification q){
        for (Object_Qualification oq : qualifications) {
            if (oq == q) {
                return true;
            }
        }
            return false;
    }

//    public void add_qualification(String new_q){
//        qualifications.add(new_q);
//    }

    public String get_qualificationstring(Activity parent){
        if (qualifications != null && qualifications.size() != 0)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            {
            return TextUtils.join(", ", qualifications);
        }else{
            return parent.getString(R.string.no_qualifications);
        }
    }


}
