package se.evinja.hazeldroid.workers;

import java.util.List;

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

    public String get_worker_qualifications(){
        return "none yet";
    }


}
