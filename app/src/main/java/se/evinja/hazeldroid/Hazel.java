package se.evinja.hazeldroid;

import android.app.Application;

public class Hazel extends Application {
    public enum HazelCommand {
        LOGIN,
        LOGOUT,
        CANCEL,
        GET_USER_SCHEDULE,
        GET_STAFF_SCHEDULE,
        ADD_WORKER,
        DOWNLOAD_PERSONNEL
    }

    private enum AccessStatus{
        USER,ADMIN,ROOT
    }
    AccessStatus access;

    private boolean user_logged_out, login_procedure;
    private HazelEvents eventListener;

    public void login(String username, String password, HazelEvents eventListener){
        access = AccessStatus.USER; //Reset before login
        login_procedure = true; // downloads needs to know that we are in login mode
        this.eventListener = eventListener;
        eventListener.onConnected();
        download_personnel();
    }

    public void download_personnel(){
        eventListener.onStaffDownloaded();
        if (login_procedure){
            if (access == AccessStatus.USER) {
                download_user_schedule();
            }else{
                download_staff_schedule();
            }
        }
    }

    public void download_user_schedule(){
        eventListener.onUserSchedule();
        if (login_procedure){
            download_staff_schedule();
        }
    }

    public void download_staff_schedule(){
        eventListener.onStaffSchedule();
        login_procedure = false; // Login procedure finished
    }

    public boolean user_has_logged_out(){
        return user_logged_out;
    }

    public void cancel(){

    }

    private void disconnect_on_fail(){

    }

}
