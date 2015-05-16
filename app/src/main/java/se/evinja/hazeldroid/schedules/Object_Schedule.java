package se.evinja.hazeldroid.schedules;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Object_Qualification;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Object_Schedule {
    public Calendar startTime,endTime;
    public int id;
    public boolean scheduled;
    public String name;
    private List<String>  workers = new ArrayList<>();
    private String workerlist;
    private SimpleDateFormat hazelformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public String getWorkerList(){
        return workerlist;
    }

    public Object_Schedule(JSONObject jo, Hazel hazel){
        try {
            startTime = Calendar.getInstance();
            startTime.setTime(hazelformat.parse(jo.getString("startTime")));
            endTime = Calendar.getInstance();
            endTime.setTime(hazelformat.parse(jo.getString("endTime")));
            name = jo.getString("name");
            scheduled = jo.getBoolean("scheduled");

            JSONArray jo2 = jo.getJSONArray("workers");
            workerlist  = "";
            for (int x = 0; x < jo2.length(); x++){
                JSONObject w = jo2.getJSONObject(x);
                workerlist += w.getString("fstname") + " " + w.getString("lstname") + "\n";
            }
            //client do not care about...
            id = jo.getInt("taskID");

//            JSONArray jArr = jo.getJSONArray("workers"); //Would rather want array of worker ID's
//            workerlist = jArr.toString();
//            for (int i = 0; i < jArr.length(); i++) {
//
//                JSONObject jw = jArr.getJSONObject(i);
//                Object_Worker w = new Object_Worker(jw, hazel);
//                Log.i("### WORKER", w.get_fullName());
//                workers.add(w.get_fullName().trim());
//            }

        } catch (JSONException e) {
            hazel.onError("Object.Schedule constructor parse JSON: " + e.getMessage());
        }catch (ParseException ex) {
            hazel.onError("Object.Schedule constructor parse DATE: " + ex.getMessage());
        }
    }


    public int getMonth(){
        return Integer.parseInt(new SimpleDateFormat("M").format(startTime.getTime()));
    }

    public String getStart(){
        return hazelformat.format(startTime.getTime());
    }

    public String getEnd(){
        return hazelformat.format(endTime.getTime());
    }

    public String getWorkers(Activity parent){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        for (String w : workers) {
            if (foundOne) {
                sb.append(",");
            }
            foundOne = true;

            sb.append(w);
        }
        if (sb.toString().isEmpty()) sb.append(parent.getString(R.string.none));
        return workerlist;
    }

}
