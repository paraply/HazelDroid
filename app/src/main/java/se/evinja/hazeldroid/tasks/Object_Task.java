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
    public String repeat_interval = null, repeat_month_date;
    public List<Object_Worker> task_workers = new ArrayList<>();
    public List<Object_Qualification> task_qualifications = new ArrayList<>();
    public Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
    public Calendar repeat_until;
    public int id;

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
            if (!jobj.isNull("intervalLength"))  repeat_interval = Integer.toString(jobj.getInt("intervalLength"));

            if (!jobj.isNull("endDate")){
                repeat_until = Calendar.getInstance();
                repeat_until.setTime(dateformat.parse(jobj.getString("endDate"))); //When the repeat ends
            }

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

            j.put("startTime", hazelformat.format(start.getTime()));
            j.put("endTime", hazelformat.format(end.getTime()));
            j.put("startDate", dateformat.format(start.getTime())); //When repeat starts. Same as task start in this app
            j.put("endDate", repeat_until == null ? JSONObject.NULL : dateformat.format(repeat_until.getTime()) ); //When the repeat ends

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
            j.put("intervalLength", repeat_interval == null ? JSONObject.NULL : Integer.parseInt(repeat_interval));
            j.put("client", client);
            j.put("name", title);
            j.put("requirements", jqual);
            j.put("minW", Integer.parseInt(min_workers));
            j.put("maxW", Integer.parseInt(max_workers));
        } catch (Exception e) {
            if (hazel != null) hazel.onError("Object_Task.toJSON: " + e.getMessage());
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

    public String getRepeatUntil(){
        if (repeat_until != null) {
            return dateformat.format(repeat_until.getTime());
        }else{
            return hazel.getString(R.string.infinitely);
        }
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


    public String get_short_repeat_string(Activity parent){
        String str = "";
        if (repeat == Repeat_Types.Once) {
            str = parent.getString(R.string.only_once);
        }else if (repeat == Repeat_Types.Daily){
            str = parent.getString(R.string.daily);
        }else if (repeat == Repeat_Types.Weekly){
            str = parent.getString(R.string.weekly);
        }else if (repeat == Repeat_Types.Monthly){
            str = parent.getString(R.string.monthly);
        }
        return str;
    }

    public String get_repeat_string(Activity parent){
        String str = "";
        if (repeat == Repeat_Types.Once) {
            return parent.getString(R.string.only_once);
        }
        if (repeat == Repeat_Types.Daily){
            str =  repeat_interval.equals("1") ? parent.getString(R.string.every_day) : parent.getString(R.string.every) + repeat_interval + " " + parent.getString(R.string.day);
        }else if (repeat == Repeat_Types.Weekly){
            str = repeat_interval.equals("1") ? parent.getString(R.string.every_week) : parent.getString(R.string.every) + repeat_interval  + parent.getString(R.string.week) ;

            //Could use locale to get translated weekday names and get first day of the week.
            str += " ";
            if (sel_weekdays[0]) str += parent.getString(R.string.monday);
            if (sel_weekdays[1]) str += parent.getString(R.string.tuesday);
            if (sel_weekdays[2]) str += parent.getString(R.string.wednesday);
            if (sel_weekdays[3]) str += parent.getString(R.string.thursday);
            if (sel_weekdays[4]) str += parent.getString(R.string.friday);
            if (sel_weekdays[5]) str += parent.getString(R.string.saturday);
            if (sel_weekdays[6]) str += parent.getString(R.string.sunday);

        }else if (repeat == Repeat_Types.Monthly){
            str = repeat_interval.equals("1") ? parent.getString(R.string.every_month) : parent.getString(R.string.every) + repeat_interval + parent.getString(R.string.month);
            str += parent.getString(R.string.on_the) + repeat_month_date + parent.getString(R.string.th);
        }
        if (repeat_until != null){
            str += " " + parent.getString(R.string.repeat_until) + " " + dateformat.format(repeat_until.getTime());
        }
        return str;
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
