package se.evinja.hazeldroid.personnel;

import java.util.List;

public class Object_Worker {
    public String id , firstName, lastName, position, mailAddress,birthday, last4, phoneNr, username, password, company;
    public List<String> qualifications;

    public Object_Worker(){}

    public String get_FullName(){
        return  firstName + " " + lastName;
    }

    public String get_Personnumber(){
        if (birthday != null) {
            return birthday + " - " + last4;
        }else{
            return "";
        }
    }



}
