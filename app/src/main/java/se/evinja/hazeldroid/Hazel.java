package se.evinja.hazeldroid;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.personnel.Adapter_Qualifications;
import se.evinja.hazeldroid.personnel.Adapter_Workers;
import se.evinja.hazeldroid.personnel.Object_Qualification;
import se.evinja.hazeldroid.personnel.Object_Worker;

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
    private String username, password;
    private HazelEvents eventListener;

    private List<Object_Qualification> qualifications = new ArrayList<>();
    private Adapter_Qualifications adapter_qualifications;

    private List<Object_Worker> workers = new ArrayList<>();
    private Adapter_Workers adapter_workers;

    public void login(String username, String password, HazelEvents eventListener){
        access = AccessStatus.USER; //Reset before login
        login_procedure = true; // downloads needs to know that we are in login mode
        this.username = username;
        this.password = password;
        this.eventListener = eventListener;
        execute(HazelCommand.LOGIN, null);
        access = AccessStatus.ADMIN; //TODO REMOVE
        eventListener.onConnected(); //TODO MOVE
        download_personnel();
    }

    public void logout(){
        user_logged_out = true;
    }

    public String getFullName(){
        return username + " - " + access; //TODO FIX REAL
    }

    public void download_personnel(){
        execute(HazelCommand.DOWNLOAD_PERSONNEL, null);
        eventListener.onStaffDownloaded(); //TODO MOVE
        if (login_procedure){
            if (access != AccessStatus.ROOT) {
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

    public boolean access_userlevel(){
        return access == AccessStatus.USER;
    }
    public boolean access_adminlevel(){
        return access == AccessStatus.ADMIN;
    }
    public boolean access_rootlevel(){
        return access == AccessStatus.ROOT;
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

    public void download_qualifications(Activity parent){
        if (adapter_qualifications == null){
            adapter_qualifications = new Adapter_Qualifications(parent, qualifications);
        }
        if (qualifications.size() == 0 ){
            qualifications.add(new Object_Qualification("A-körkort", 3));
            qualifications.add(new Object_Qualification("Pratar Hindi", 993263520));
            adapter_qualifications.notifyDataSetChanged();
        }
    }

    public void add_qualification(String title){
        qualifications.add(new Object_Qualification(title, 3));
        adapter_qualifications.notifyDataSetChanged();
    }

    public void delete_qualification(int position){
        qualifications.remove(position);
        adapter_qualifications.notifyDataSetChanged();
    }

    public void update_qualification(int position, String new_title){
        qualifications.get(position).title = new_title;
        adapter_qualifications.notifyDataSetChanged();
    }

    public Adapter_Qualifications getAdapter_qualifications(){
        return adapter_qualifications;
    }

    public List<Object_Qualification> get_qualifications(){
        return qualifications;
    }


    public void download_workers(Activity parent){
        if (adapter_workers == null){
            adapter_workers = new Adapter_Workers(parent, workers);
        }
        if (workers.size() == 0 ){
            Object_Worker p = new Object_Worker();
            p.firstName = "Bänkt";
            p.lastName = "Olof";
            p.position = "Skurk";
            workers.add(p);
            adapter_workers.notifyDataSetChanged();
        }
    }

    public Adapter_Workers getAdapter_workers(){
        return adapter_workers;
    }

    public void add_worker(Object_Worker new_worker){
        workers.add(new_worker);
        adapter_workers.notifyDataSetChanged();
    }

    public void delete_worker(int position){
        workers.remove(position);
        adapter_workers.notifyDataSetChanged();
    }

    public void update_worker_list(){
        adapter_workers.notifyDataSetChanged();
    }

    public Object_Worker get_worker(int position){
        return workers.get(position);
    }

}
