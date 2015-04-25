package se.evinja.hazeldroid.workers;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.R;

public class Object_Worker {
    public String id , firstName, lastName, position, mailAddress,birthday, last4, phoneNr, username, password, company;
    public List<String> qualifications;

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
        if (qualifications == null){
            return false;
        }else{
            return qualifications.size() != 0;
        }
    }

    public void add_qualification(String new_q){
        if (qualifications == null){
            qualifications = new ArrayList<>();
        }
        qualifications.add(new_q);
    }

    public String get_worker_qualifications(Activity parent){
        if (qualifications != null)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            {
            return TextUtils.join(", ", qualifications);
        }else{
            return parent.getString(R.string.no_qualifications);
        }
    }


}
