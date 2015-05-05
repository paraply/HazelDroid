package se.evinja.hazeldroid;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

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
        UPDATE_QUALIFICATION,
        DOWNLOAD_QUALIFICATIONS,
        ADD_WORKER,
        UPDATE_WORKER,
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

    private boolean user_logged_out, on_login_download_all;
    private String username, password;
    private Callback_Hazel eventListener;
    private Http http;

    public List<Object_Qualification> qualifications;
    private Adapter_Qualifications adapter_qualifications;
    private Object_Qualification qualification_waiting_to_be_added;
    private int qualification_update_waiting;

    public List<Object_Worker> workers;
    private Adapter_Workers adapter_workers;
    private boolean no_worker_download = false;
    private boolean workers_are_invalid;
    public Object_Task task_waiting_to_be_added;

    private List<Object_Task> tasks;
    private Adapter_Tasks adapter_tasks;

    public Activity parent;
    public String client;
    private Object_Worker worker_logged_in;
    private JSONObject worker_logged_in_JSON; //Store the currenly logged in user as JSON since we need to download qualifications first to be able to create the Object_worker worker_logged_in


    public void login(String username, String password){
        access = AccessStatus.USER; //Reset before login
        on_login_download_all = true; // downloads needs to know that we are in login mode
        this.username = username;
        this.password = password;
        http = new Http(this, username,password);
        execute(HazelCommand.LOGIN, null);
    }

    public void set_listener_and_parent(Callback_Hazel eventListener, Activity parent){
        this.eventListener = eventListener;
        this.parent = parent;
    }

    public void logout(){ //Reset everything TODO CHECK IF EVERYTHING
        // execute(HazelCommand.LOGOUT, null);
        parent = null;
        client = null;
        currentCommand = null;
        commandBefore = null;
        access = AccessStatus.USER;
        connectionStatus = ConnectionStatus.NOT_CONNECTED;
        user_logged_out = true;

        username = null;
        password = null;
        on_login_download_all = false;

        qualifications = null;
        adapter_qualifications = null;
        qualification_waiting_to_be_added = null;

        workers = null;
        adapter_workers = null;
        workers_are_invalid = false;
        worker_logged_in = null;
        worker_logged_in_JSON = null;

        tasks = null;
        adapter_tasks = null;
    }

    public String get_navigation_title(){
        if (worker_logged_in == null){
            return username;
        }else {
            return worker_logged_in.get_fullName();
        }
    }

    public String get_navigation_client(){
        String str = client;
        if (!access_userlevel()){
            str += " (" +  access.toString().toLowerCase() + ")";
        }
        return str;
    }

    public void download_user_schedule(){
        execute(HazelCommand.DOWNLOAD_USER_SCHEDULE, null);
        eventListener.onUserSchedule(); //TODO MOVE
//        if (on_login_download_all){
//            download_staff_schedule();
//        }
    }

    public void download_staff_schedule(){
        execute(HazelCommand.DOWNLOAD_STAFF_SCHEDULE, null);
        eventListener.onStaffSchedule();
//        on_login_download_all = false; // Login procedure finished
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
                if (on_login_download_all){
                    connectionStatus = ConnectionStatus.NOT_CONNECTED;
                    on_login_download_all = false;
                }
                break;

            case ADD_QUALIFICATION:
                Log.i("##### POST", jsonData.toString());
                http.POST("qual", jsonData);
                break;

            case UPDATE_QUALIFICATION:
                Log.i("##### PUT", jsonData.toString());
                http.PUT("qual", jsonData);
                break;

            case DOWNLOAD_QUALIFICATIONS:
                http.GET("qual");
                break;

            case ADD_WORKER:
                Log.i("##### POST", jsonData.toString());
                http.POST("user", jsonData);
                break;

            case UPDATE_WORKER:
                Log.i("##### PUT", jsonData.toString());
                try {
                    http.PUT(jsonData.getString("usrname"), jsonData);
                } catch (JSONException e) {
                    onError("Update worker to JSON failed");
                }
                break;

            case DELETE_WORKER:
                Log.i("##### POST", jsonData.toString());
                http.POST("user", jsonData);
                break;

            case DOWNLOAD_WORKERS:
                Log.i("##### GET","worker");
                http.GET("worker");
                break;

            case ADD_TASK:
                Log.i("##### POST", jsonData.toString());
                http.POST("task", jsonData);
                break;

            case DOWNLOAD_TASKS:
                Log.i("##### GET","task");
                http.GET("task");
                break;

            default:
                return;
        }
    }

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

    public void add_qualification(String title){
        qualification_waiting_to_be_added = new Object_Qualification(title, this);
        execute(HazelCommand.ADD_QUALIFICATION, qualification_waiting_to_be_added.toJSON());
    }

    public void delete_qualification(int position){
        qualifications.remove(position);
        adapter_qualifications.notifyDataSetChanged();
        workers_are_invalid = true; //Workers are now not valid since one could contain the deleted qualification
    }

    public void update_qualification(int position, String new_title){
        JSONObject jo =  qualifications.get(position).update(new_title);
        qualification_update_waiting = position;
        execute(HazelCommand.UPDATE_QUALIFICATION,jo);
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

//    public void download_qualifications_and_workers(Activity parent){
//        this.parent = parent;
//        no_worker_download = false;
//        if ((qualifications == null) || qualifications.size() == 0 ){
//            download_qualifications(parent);
//        }else{
//            download_workers(parent);
//        }
//    }

    public void download_qualifications(){
        if (qualifications == null){
            qualifications = new ArrayList<>();
        }
        if (adapter_qualifications == null){
            adapter_qualifications = new Adapter_Qualifications(parent, qualifications);
        }
        if (qualifications.size() == 0){
            execute(HazelCommand.DOWNLOAD_QUALIFICATIONS, null);
        }
    }

    private void refresh_qualifications(){
        qualifications.clear();
        no_worker_download = true;
        execute(HazelCommand.DOWNLOAD_QUALIFICATIONS, null);
    }

    public void download_workers(){ //This will download qualifications if not already downloaded.
        if (workers == null){
            workers = new ArrayList<>();
        }
        if (adapter_workers == null){
            adapter_workers = new Adapter_Workers(parent, workers);
        }
        if (workers.size() == 0 || workers_are_invalid ){ // Do not download if has valid worker list. It is automatically called from fragments needing it. No reason redownloading.
            workers.clear();
            execute(HazelCommand.DOWNLOAD_WORKERS, null);
            workers_are_invalid = false;
        }
    }

    public Adapter_Workers getAdapter_workers(){
        if (adapter_workers == null){
            adapter_workers = new Adapter_Workers(parent, workers);
        }
        return adapter_workers;
    }

    public void add_worker(Object_Worker new_worker){
        execute(HazelCommand.ADD_WORKER, new_worker.getJSON(client));
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
        task_waiting_to_be_added = new_task;
        execute(HazelCommand.ADD_TASK, new_task.toJSON(client));
    }

    public void delete_task(int position){
        tasks.remove(position);
        adapter_tasks.notifyDataSetChanged();
    }

    public void download_tasks(){
        if (tasks == null){
            tasks = new ArrayList<>();
        }
        if (tasks.size() == 0){ //Only download if has none..
            execute(HazelCommand.DOWNLOAD_TASKS, null);
        }
    }

    public Adapter_Tasks getAdapter_tasks(){
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
            onError("Got null data");
            return;
        }
        Log.i("###### GOT DATA", data);
        switch (currentCommand){
            case LOGIN:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("status_code").equals("200")){
                        client = jo.getString("client");
                        String level = jo.getString("access_lvl");
                        if (!jo.isNull("worker")) {
                            worker_logged_in_JSON = jo.getJSONObject("worker");
//                            worker_logged_in = new Object_Worker(jo.getJSONObject("worker"), this);
                        }
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
                        if (on_login_download_all){
                            //Download qualifications first since:
                            //workers needs qualifications objects
                            //tasks needs worker objects and qualification objects
                            download_qualifications();
                        }
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
                //Does not need to do anything
                break;

            case ADD_QUALIFICATION:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("message").equals("Okidoki")){
                        qualifications.add(qualification_waiting_to_be_added);
                        Toast.makeText(parent,getString(R.string.qualification_added), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    onError("Failed to add qualification");
                }
                break;

            case UPDATE_QUALIFICATION:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("message").equals("Ok")){
                        if (qualification_update_waiting != -1){
                            qualifications.get(qualification_update_waiting).confirm_update(); // Confirmed
                            adapter_qualifications.notifyDataSetChanged();
                            qualification_update_waiting = -1;
                        }
                    }
                } catch (JSONException e) {
                    onError("Failed to update qualification");
                    qualification_update_waiting = -1;
                }


            case DOWNLOAD_QUALIFICATIONS:
                try {
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        qualifications.add(new Object_Qualification(jo.getString("qualname"), this));
                        Log.i("###### QUALIFICATION", jo.toString());
                    }
                    if (adapter_qualifications != null){ //It is null when on_login_download_all
                        adapter_qualifications.notifyDataSetChanged();
                    }
                    eventListener.onQualificationsDownloaded();
                    if (on_login_download_all){ //if is in login procedure
                        if (worker_logged_in_JSON != null) worker_logged_in = new Object_Worker(worker_logged_in_JSON, this); //Now we can create our object since we have a list of qualifications
                        download_workers();
                    }else {
                        if (!no_worker_download) {
                            download_workers(); //Download workers when qualifications has been downloaded. NOT THE OTHER WAY AROUND. Workers need qualifications to generate objects.
                        } else {
                            no_worker_download = false;
                        }
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
                download_workers(); //TODO REMOVE PROBABLY
                break;

            case UPDATE_WORKER:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("message").equals("Ok")){
                        eventListener.onWorkerAdded();
                    }
                } catch (JSONException e) {
                    onError("Add worker failed -" + data);
                }

            case DOWNLOAD_WORKERS:
                try {
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        workers.add(new Object_Worker(jo, this));
                        Log.i("###### WORKER", jo.toString());
                    }
                    if (adapter_workers != null){ //It is null when on_login_download_all
                        adapter_workers.notifyDataSetChanged();
                    }
                    if (on_login_download_all){
                        download_tasks();
                    }


                } catch (JSONException e) {
                    onError("parsing workers");
                }

                break;

            case ADD_TASK:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.getString("message").equals("Ok")){
                        tasks.add(task_waiting_to_be_added);
                        adapter_tasks.notifyDataSetChanged();
                        Toast.makeText(parent, "Added task ok", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    onError("parsing add task: " + data);
                }
                break;


            case DOWNLOAD_TASKS:
                try {
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        tasks.add(new Object_Task(jo, this));
                        Log.i("###### NEW TASK", jo.toString());
                    }
                    if (adapter_tasks != null){ //It is null when on_login_download_all
                        adapter_tasks.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    onError("parsing workers: " + e.getMessage());
                }
                eventListener.onTasksDownloaded();
                break;


            default:
                onError("Received data on no command");
        }
    }


}
