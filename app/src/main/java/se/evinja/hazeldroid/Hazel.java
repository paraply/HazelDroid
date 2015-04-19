package se.evinja.hazeldroid;

import android.app.Application;

public class Hazel extends Application {
    public enum HazelCommand {
        LOGIN,
        LOGOUT,
        CANCEL,
        GET_USER_SCHEDULE,
        GET_COMPANY_SCHEDULE,
        ADD_WORKER,
        DOWNLOAD_PERSONNEL
    }

    private boolean user_logged_out;

    public void login(String username, String password, HazelEvents eventListener){
        eventListener.onConnected();
    }

    public void download_personnel(){

    }

    public boolean user_has_logged_out(){
        return user_logged_out;
    }

    public void cancel(){

    }

    public void disconnect_on_fail(){

    }

}
