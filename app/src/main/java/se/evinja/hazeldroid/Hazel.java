package se.evinja.hazeldroid;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.qualifications.Adapter_Qualifications;
import se.evinja.hazeldroid.tasks.Adapter_Tasks;
import se.evinja.hazeldroid.tasks.Object_Task;
import se.evinja.hazeldroid.workers.Adapter_Workers;
import se.evinja.hazeldroid.qualifications.Object_Qualification;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Hazel extends Application implements Http_Events {


    public enum HazelCommand {
        LOGIN,
        LOGOUT,
        CANCEL,
        DOWNLOAD_USER_SCHEDULE,
        DOWNLOAD_STAFF_SCHEDULE,
        ADD_QUALIFICATION,
        DOWNLOAD_QUALIFICATIONS,
        ADD_WORKER,
        DELETE_WORKER,
        DOWNLOAD_WORKERS,
        ADD_TASK,
        DOWNLOAD_TASKS
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
    private Http http;

    public List<Object_Qualification> qualifications = new ArrayList<>();
    private Adapter_Qualifications adapter_qualifications;
    private String current_qualification_adding;

    public List<Object_Worker> workers = new ArrayList<>();
    private Adapter_Workers adapter_workers;
    private boolean no_worker_download = false;
    private boolean workers_are_invalid;

    private List<Object_Task> tasks = new ArrayList<>();
    private Adapter_Tasks adapter_tasks;

    private Activity parent;
    private String client;
    private Object_Worker worker_logged_in;


    public void login(String username, String password, HazelEvents eventListener){
        access = AccessStatus.USER; //Reset before login
        login_procedure = true; // downloads needs to know that we are in login mode
        this.username = username;
        this.password = password;
        this.eventListener = eventListener;
        http = new Http(this, username,password);
        execute(HazelCommand.LOGIN, null);
    }

    public void set_new_eventListener(HazelEvents eventListener){
        this.eventListener = eventListener;
    }

    public void logout(){
       // execute(HazelCommand.LOGOUT, null);
        parent = null;
        client = null;
        current_qualification_adding = null;
        workers_are_invalid = false;
        username = null;
        password = null;
        login_procedure = false;
        workers.clear();
        qualifications.clear();
        currentCommand = null;
        commandBefore = null;
        access = AccessStatus.USER;
        adapter_qualifications = null;
        adapter_workers = null;

        connectionStatus = ConnectionStatus.NOT_CONNECTED;
        user_logged_out = true;
    }

    public String getFullName(){
        String str =  worker_logged_in.get_fullName() + "\n" + client;
        if (!access_userlevel()){
            str += " (" +  access.toString().toLowerCase() + ")";
        }
        return str;
    }

    public String get_client_name(){
        return client;
    }

    public void download_user_schedule(){
        execute(HazelCommand.DOWNLOAD_USER_SCHEDULE, null);
        eventListener.onUserSchedule(); //TODO MOVE
//        if (login_procedure){
//            download_staff_schedule();
//        }
    }

    public void download_staff_schedule(){
        execute(HazelCommand.DOWNLOAD_STAFF_SCHEDULE, null);
        eventListener.onStaffSchedule();
//        login_procedure = false; // Login procedure finished
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

    private void execute(HazelCommand cmd, JSONObject jsonData){
        Log.i("###### EXECUTE", cmd.toString());
        commandBefore = currentCommand;
        currentCommand = cmd;
        switch (cmd){

            case LOGIN:
                access = AccessStatus.USER; //Reset to make sure we are not in admin mode
                http.GET("login");
                break;

            case LOGOUT:
                if (connectionStatus != ConnectionStatus.NOT_CONNECTED) {
                    http.GET("logout");
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

            case ADD_QUALIFICATION:
                Log.i("##### POST", jsonData.toString());
                http.POST("qual", jsonData);
                break;

            case ADD_WORKER:
                Log.i("##### POST", jsonData.toString());
                http.POST("user", jsonData);
                break;

            case DELETE_WORKER:
                Log.i("##### POST", jsonData.toString());
                http.POST("user", jsonData);
                break;

            case DOWNLOAD_WORKERS:
                http.GET("worker");
                break;

            case DOWNLOAD_QUALIFICATIONS:
                http.GET("qual");
                break;

            case ADD_TASK:
                http.POST("task", jsonData);
                break;

            case DOWNLOAD_TASKS:
                http.GET("task");
                break;

            default:
                return;
        }
    }

//    private void download_qualifications(Activity parent){
//        if (adapter_qualifications == null){
//            adapter_qualifications = new Adapter_Qualifications(parent, qualifications);
//        }
//        if (qualifications.size() == 0 ){
//            this.parent = parent;
//            execute(HazelCommand.DOWNLOAD_QUALIFICATIONS, null);
//        }
//    }

    public int get_worker_has_qualification(Object_Qualification qualification){
        int amount = 0;
        for (Object_Worker w : workers){
            if (w.has_qualifications()){
                if (w.has_qualification(qualification)){
                    amount++;
                }
            }
        }
        return amount;
    }

    public void add_qualification(String title, Activity parent){
        JSONObject jo = new JSONObject();
        try {
            jo.put("qualname", title);
            jo.put("client", 1);
            current_qualification_adding = title;
        } catch (JSONException e) {
            onError("Qualification to json failed");
        }
        execute(HazelCommand.ADD_QUALIFICATION, jo);
    }

    public void delete_qualification(int position){
        qualifications.remove(position);
        adapter_qualifications.notifyDataSetChanged();
        workers_are_invalid = true; //Workers are now not valid since one could contain the deleted qualification
    }

    public void update_qualification(int position, String new_title){
        qualifications.get(position).title = new_title;
        adapter_qualifications.notifyDataSetChanged();
    }

    public Object_Qualification get_qualification(String str){
        for (Object_Qualification q : qualifications){
            if (q.title.equals(str)){
                return q;
            }
        }
        return null;
    }

    public Adapter_Qualifications getAdapter_qualifications(){
        return adapter_qualifications;
    }

    public List<Object_Qualification> get_qualifications(){
        return qualifications;
    }

    public void download_qualifications_and_workers(Activity parent){
        this.parent = parent;
        no_worker_download = false;
        if (adapter_qualifications == null){
            adapter_qualifications = new Adapter_Qualifications(parent, qualifications);
        }
        if (qualifications.size() == 0 ){
            this.parent = parent;
            execute(HazelCommand.DOWNLOAD_QUALIFICATIONS, null);
        }else{
            download_workers(parent);
        }
    }

    private void refresh_qualifications(){
        qualifications.clear();
        no_worker_download = true;
        execute(HazelCommand.DOWNLOAD_QUALIFICATIONS, null);
    }

    private void download_workers(Activity parent){ //This will download qualifications if not already downloaded.
        if (adapter_workers == null){
            adapter_workers = new Adapter_Workers(parent, workers);
        }
        if (workers.size() == 0 || workers_are_invalid ){ // Do not download if has valid worker list. It is automatically called from fragments needing it. No reason redownloading.
            workers.clear();
            this.parent = parent;
            execute(HazelCommand.DOWNLOAD_WORKERS, null);
            workers_are_invalid = false;
        }
    }

    public Adapter_Workers getAdapter_workers(Activity parent){
        if (adapter_workers == null){
            adapter_workers = new Adapter_Workers(parent, workers);
        }
        return adapter_workers;
    }

    public void add_worker(Object_Worker new_worker){
        execute(HazelCommand.ADD_WORKER, new_worker.getJSON(client, 2));
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


    public void add_task(Object_Task new_task){
        tasks.add(new_task);
        execute(HazelCommand.ADD_TASK, new_task.toJSON(client));
        adapter_tasks.notifyDataSetChanged();
    }

    public void delete_task(int position){
        tasks.remove(position);
        adapter_tasks.notifyDataSetChanged();
    }

    public void download_tasks(Activity parent){
        execute(HazelCommand.DOWNLOAD_TASKS, null);
//        Object_Task t = new Object_Task();
//        t.title = "Skapa hazel";
//        t.description = "Hur svårt kan det vara?";
//        t.set_repeats_weekly();
//        tasks.add(t);
        adapter_tasks.notifyDataSetChanged();

    }

    public Adapter_Tasks getAdapter_tasks(Activity parent){
        if (adapter_tasks == null) {
            adapter_tasks = new Adapter_Tasks(parent, tasks);
        }
        return adapter_tasks;
    }

    public void update_task_list(){
        adapter_tasks.notifyDataSetChanged();
    }

    public Object_Task get_task(int position){
        return tasks.get(position);
    }


    @Override
    public void onError(String error_msg) {
        Log.i("###### ERROR", currentCommand.toString() + " - " + error_msg);
        eventListener.onError(currentCommand, error_msg);
    }

    @Override
    public void onData(String data) {
        if (data == null){
            Log.i("###### GOT DATA", "NULL!!!");
            return;
        }
        Log.i("###### GOT DATA", data);
        switch (currentCommand){
            case LOGIN:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("status_code").equals("200")){
                        client = jo.getString("client"); //TODO UNCOMMENT
                        String level = jo.getString("access_lvl");
                        worker_logged_in = new Object_Worker(jo.getJSONObject("worker"), this);
                        if (level.equals("1")){
                            access = AccessStatus.USER;
                        }else if (level.equals("2")){
                            access = AccessStatus.ADMIN;
                        }else if (level.equals("3")){
                            access = AccessStatus.ROOT;
                        }else{
                            onError("Bad login message");
                        }
                        connectionStatus = ConnectionStatus.CONNECTED;
                        user_logged_out = false;
                        eventListener.onConnected();
                    }else if (jo.getString("status_code").equals("404")){
                        onError("Bad username");
                        connectionStatus = ConnectionStatus.NOT_CONNECTED;
                    }else{
                        onError("Login failed");
                        connectionStatus = ConnectionStatus.NOT_CONNECTED;
                    }
                } catch (JSONException e) {
                    onError("parsing login reponse: " + e.getMessage());
                }
                break;

            case LOGOUT:
                //Doesnt need to do anything
                break;

            case ADD_QUALIFICATION:
                try {
                    JSONObject jo = new JSONObject(data);
                   if (jo.getString("message").equals("Okidoki")){
                        refresh_qualifications(); // DOWNLOAD ALL QUALIFICATIONS AGAIN TODO JUST ADD NEW DO NOT DOWNLOAD AGAIN
                    }
                } catch (JSONException e) {
                    onError("parsing add qualification message");
                }

                 break;

            case DOWNLOAD_TASKS:
                eventListener.onTasksDownloaded();
                break;

            case DOWNLOAD_QUALIFICATIONS:
                try {
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        qualifications.add(new Object_Qualification(jo.getString("qualname"), parent, this));
                        Log.i("###### QUALIFICATION", jo.toString());
                    }
                    adapter_qualifications.notifyDataSetChanged();
                    if (!no_worker_download){
                        download_workers(parent); //Download workers when qualifications has been downloaded. NOT THE OTHER WAY AROUND. Workers need qualifications to generate objects.
                    }else{
                        no_worker_download = false;
                    }
                } catch (JSONException e) {
                    onError("parsing qualifications");
                }
                break;

            case ADD_WORKER: //{"message":"Ok","status_code":200}
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("message").equals("Ok")){
                        eventListener.onWorkerAdded();
                    }
                } catch (JSONException e) {
                    onError("Add worker failed -" + data);
                }
                //TODO ADD_WORKER SHOULD RETURN ID, WONT HAVE TO DOWNLOAD LIST AGAIN..
                workers.clear();            //TODO REMOVE PROBABLY
                download_workers(parent); //TODO REMOVE PROBABLY
                break;

            case DOWNLOAD_WORKERS:
                try {
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        workers.add(new Object_Worker(jo, this));
                        Log.i("###### WORKER", jo.toString());
                    }
                    adapter_workers.notifyDataSetChanged();


                } catch (JSONException e) {
                    onError("parsing workers");
                }

                break;

            default:
                onError("Received data on no command");
        }
    }


}
