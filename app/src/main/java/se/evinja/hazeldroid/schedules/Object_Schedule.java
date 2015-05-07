package se.evinja.hazeldroid.schedules;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Object_Schedule {
    public Calendar startTime,endTime;
    public int id;
    public boolean scheduled;
    public String name;
    public List<Object_Worker> workers = new ArrayList<>();
    private SimpleDateFormat hazelformat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    public Object_Schedule(){}

    public Object_Schedule(JSONObject jo, Hazel hazel){
        try {
            startTime = Calendar.getInstance();
            startTime.setTime(hazelformat.parse(jo.getString("startTime")));
            endTime = Calendar.getInstance();
            endTime.setTime(hazelformat.parse(jo.getString("endTime")));
            name = jo.getString("name");
            scheduled = jo.getBoolean("scheduled");
            //client do not care about...
            id = jo.getInt("taskID");
            //workers do not care about yet...
        } catch (Exception e) {
            hazel.onError("Object.Schedule constructor parse JSON: " + e.getMessage());
        }


    }

}
