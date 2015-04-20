package se.evinja.hazeldroid;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

public class Hazel extends Application {
    public enum HazelCommand {
        LOGIN,
        LOGOUT,
        CANCEL,
        DOWNLOAD_USER_SCHEDULE,
        DOWNLOAD_STAFF_SCHEDULE,
        ADD_WORKER,
        DOWNLOAD_PERSONNEL
    }
    HazelCommand currentCommand,commandBefore;

    private enum AccessStatus{
        USER,ADMIN,ROOT
    }
    AccessStatus access;

    private enum ConnectionStatus{
        CONNECTED, NOT_CONNECTED
    }
    ConnectionStatus connectionStatus;

    private boolean user_logged_out, login_procedure;
    private HazelEvents eventListener;

    public void login(String username, String password, HazelEvents eventListener){
        access = AccessStatus.USER; //Reset before login
        login_procedure = true; // downloads needs to know that we are in login mode
        this.eventListener = eventListener;
        execute(HazelCommand.LOGIN, null);
        eventListener.onConnected(); //TODO MOVE
        download_personnel();
    }

    public void logout(){
        user_logged_out = true;
    }

    public void download_personnel(){
        execute(HazelCommand.DOWNLOAD_PERSONNEL, null);
        eventListener.onStaffDownloaded(); //TODO MOVE
        if (login_procedure){
            if (access == AccessStatus.USER) {
                download_user_schedule();
            }else{
                download_staff_schedule();
            }
        }
    }

    public void download_user_schedule(){
        execute(HazelCommand.DOWNLOAD_USER_SCHEDULE, null);
        eventListener.onUserSchedule(); //TODO MOVE
        if (login_procedure){
            download_staff_schedule();
        }
    }

    public void download_staff_schedule(){
        execute(HazelCommand.DOWNLOAD_STAFF_SCHEDULE,null);
        eventListener.onStaffSchedule();
        login_procedure = false; // Login procedure finished
    }

    public boolean user_has_logged_out(){
        return user_logged_out;
    }

    public void cancel(){
        execute(HazelCommand.CANCEL,null);
    }

    private void raise_error(String error_msg){
        Log.i("###### ERROR", currentCommand.toString() + " - " + error_msg);
        if (login_procedure){ // if during login/first downloading set as disconnected
            connectionStatus = ConnectionStatus.NOT_CONNECTED;
            login_procedure = false;
        }
        eventListener.onError(currentCommand, error_msg);
    }

    private void execute(HazelCommand cmd, JSONObject jsonData){
        Log.i("###### EXECUTE", cmd.toString());
        commandBefore = currentCommand;
        currentCommand = cmd;
        switch (cmd){
            case LOGIN:
                access = AccessStatus.USER; //Reset to make sure we are not in admin mode
                //httpGet("login");
                break;
            case LOGOUT:
                if (connectionStatus != ConnectionStatus.NOT_CONNECTED) {
                    //httpGet("logout");
                    user_logged_out = true;
                    connectionStatus = ConnectionStatus.NOT_CONNECTED;
                }
                break;
            case CANCEL:
                if (login_procedure){
                    connectionStatus = ConnectionStatus.NOT_CONNECTED;
                    login_procedure = false;
                }
                break;
            case DOWNLOAD_PERSONNEL:
               // httpGet("worker");
                break;
            case ADD_WORKER:
              //  http_post("user", jsonData);
                break;
            default:
                return;
        }
    }
}
