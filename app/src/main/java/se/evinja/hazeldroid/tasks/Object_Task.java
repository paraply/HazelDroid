package se.evinja.hazeldroid.tasks;


import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Object_Qualification;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Object_Task {
    public String title, description;
    public String min_workers, max_workers;
    public List<Object_Worker> task_workers = new ArrayList<>();
    public List<Object_Qualification> task_qualifications = new ArrayList<>();
    public Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
    public int id, repeat_length;

    public Calendar nextrun = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm");
    private SimpleDateFormat hazelformat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private Hazel hazel;

    public Repeat_Types repeat;

    public boolean sel_weekdays[] = new boolean[7];
    private List<String> weekday_output = new ArrayList<>();

    public Object_Task(){}

    public Object_Task(JSONObject jobj, Hazel hazel){ //Need hazel reference to get qualification from string
        this.hazel = hazel;
        try {
            id = jobj.getInt("id");
            title = jobj.getString("name");
            if (!jobj.isNull("minW")) min_workers = Integer.toString(jobj.getInt("minW"));
            if (!jobj.isNull("maxW")) max_workers = Integer.toString(jobj.getInt("maxW"));
            start.setTime(hazelformat.parse(jobj.getString("startTime")));
            end.setTime(hazelformat.parse(jobj.getString("endTime")));
            repeat_length = jobj.getInt("intervalLength");
            description = hazel.getString(R.string.no_description);
            switch (jobj.getString("interval")){
                case "Once": repeat = Repeat_Types.Once; break;
                case "Daily": repeat = Repeat_Types.Daily; break;
                case "Weekly": repeat = Repeat_Types.Weekly; break;
                case "Monthly": repeat = Repeat_Types.Monthly; break;
            }

            JSONArray jArr = jobj.getJSONArray("requirements");
            if (jArr != null){
                for (int i = 0; i < jArr.length(); i++){
                    Object_Qualification q = hazel.get_qualification(jArr.getString(i));
                    if (q != null){
                        task_qualifications.add(q);
                    }
                }
            }

        } catch (Exception e) {
            hazel.onError("Parsing task: " + e.getMessage());
        }

    }

    public JSONObject toJSON(String client){
        JSONObject j = new JSONObject();
        JSONArray jqual = new JSONArray();
        for (Object_Qualification q : task_qualifications ){
            jqual.put(q);
        }

        try {
            j.put("id", JSONObject.NULL);
            j.put("startDate", getStartDate());
            j.put("endDate", getEndDate());
//            j.put("startTime", getStartTime());
            j.put("startTime", "2015-04-24 15:00:00");


//            j.put("endTime", getEndTime());
            j.put("endTime",  "2015-04-24 17:00:00");
            if (repeat == Repeat_Types.Weekly){
                if (sel_weekdays[0]) weekday_output.add("Monday");
                if (sel_weekdays[1]) weekday_output.add("Tuesday");
                if (sel_weekdays[2]) weekday_output.add("Wednesday");
                if (sel_weekdays[3]) weekday_output.add("Thursday");
                if (sel_weekdays[4]) weekday_output.add("Friday");
                if (sel_weekdays[5]) weekday_output.add("Saturday");
                if (sel_weekdays[6]) weekday_output.add("Sunday");
                String weekdaylist = " [" +  TextUtils.join(",", weekday_output) + "]";
                j.put("interval", repeat + weekdaylist);
            }else{
                j.put("interval", repeat);
            }
            j.put("intervalLength", repeat_length);
            j.put("client", client);
            j.put("name", title);
            j.put("requirements", jqual);
            j.put("minW", Integer.parseInt(min_workers));
            j.put("maxW", Integer.parseInt(max_workers));
        } catch (Exception e) {
            if (hazel != null) hazel.onError("Task to JSON: " + e.getMessage());
        }

        return j;
    }

    public String getStartDate(){
        return dateformat.format(start.getTime());
    }

    public String getStartTime(){
        return timeformat.format(start.getTime());
    }

    public String getEndDate(){
        return dateformat.format(end.getTime());
    }


    public String getEndTime() {
        return timeformat.format(end.getTime());
    }

    public String get_next_run_month(){
        return new SimpleDateFormat("MMMM").format(nextrun.getTime());
    }

    public String get_next_run_date(){
        return Integer.toString(nextrun.get(Calendar.DAY_OF_MONTH));
    }

    public String get_min_workers(){
        if (min_workers == null) {
            return "-";
        }else{
            return min_workers;
        }
    }

    public String get_max_workers(){
        if (max_workers == null) {
            return "-";
        }else{
            return max_workers;
        }
    }

    public String get_worker_amount_string(Activity parent){
        if (task_workers.size() == 0){
            return parent.getString(R.string.no_workers);
        }else if (task_workers.size() == 1){
            return parent.getString(R.string.one_worker);
        }else{
            return task_workers.size() + parent.getString(R.string.space_workers);
        }
    }

    public String get_repeat_string(Activity parent){
        if (repeat == Repeat_Types.Once){
            return parent.getString(R.string.repeat_once);
        }else if (repeat == Repeat_Types.Daily){
            return parent.getString(R.string.repeat_daily);
        }else if (repeat == Repeat_Types.Weekly){
            return parent.getString(R.string.repeat_weekly);
        }else if (repeat == Repeat_Types.Monthly){
            return parent.getString(R.string.repeat_monthly);
        }
        return null;
    }

    public String getQualifications(Activity parent){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (Object_Qualification q : task_qualifications) {
            if (foundOne) {
                sb.append(", ");
            }
            foundOne = true;
            sb.append(q.toString());
        }
        if (sb.toString().isEmpty()) sb.append(parent.getString(R.string.none));
        return sb.toString();
    }

    public String getWorkers(Activity parent){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (Object_Worker w : task_workers) {
            if (foundOne) {
                sb.append("\n");
            }
            foundOne = true;

            sb.append(w.get_fullName());
        }
        if (sb.toString().isEmpty()) sb.append(parent.getString(R.string.none));
        return sb.toString();
    }


}
