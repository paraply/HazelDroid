package se.evinja.hazeldroid.workers;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Object_Qualification;

public class Object_Worker {
    public String id , firstName, lastName, position, mailAddress,birthday, last4, phoneNr, username, password, company;
    public List<Object_Qualification> qualifications;

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
                id = jobj.getString("id");


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
                e.printStackTrace();
            }

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
