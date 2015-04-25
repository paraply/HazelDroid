package se.evinja.hazeldroid.workers;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Object_Qualification;

public class Object_Worker {
    public String id , firstName, lastName, position, mailAddress,birthday, last4, phoneNr, username, password, company;
//    public List<String> qualifications = new ArrayList<>(); //TODO CHANGE TO List<Object_Qualification> CANT RENAME QUALIFICATION NOW!!!!!!!!!!!
    public List<Object_Qualification> qualifications;

    public Object_Worker(){}

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
