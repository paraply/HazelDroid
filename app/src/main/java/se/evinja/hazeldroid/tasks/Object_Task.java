package se.evinja.hazeldroid.tasks;


import android.app.Activity;
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

    private enum repeat_types{
        Once,
        Daily,
        Weekly,
        Monthly
    } private repeat_types repeat;

    private enum weekdays{
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    } private List<weekdays> week_d = new ArrayList<>();


    public Object_Task(){}

    public Object_Task(JSONObject jobj,Hazel hazel){ //Need hazel reference to get qualification from string
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
                case "Once": repeat = repeat_types.Once; break;
                case "Daily": repeat = repeat_types.Daily; break;
                case "Weekly": repeat = repeat_types.Weekly; break;
                case "Monthly": repeat = repeat_types.Monthly; break;
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
            Log.i("##### ERROR TASK PARSE", e.getMessage());
        }

    }

    public JSONObject toJSON(String client){
        JSONObject j = new JSONObject();
        JSONArray jweekdays = new JSONArray();
        for (weekdays wd : week_d ){
            jweekdays.put(wd);
        }
        JSONArray jqual = new JSONArray();
        for (Object_Qualification q : task_qualifications ){
            jqual.put(q);
        }

        try {
            j.put("id", JSONObject.NULL);
            j.put("startDate", getStartDate());
            j.put("endDate", getEndDate());
            j.put("weekdays", jweekdays);
            j.put("startTime", getStartTime());
            j.put("endTime", getEndTime());
            j.put("interval", repeat);
            j.put("intervalLength", repeat_length);
            j.put("client", client);
            j.put("name", title);
            j.put("requirements", jqual);
        } catch (Exception e) {
            Log.i("##### TASK JSON", e.getMessage());
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


    public void set_repeats_weekly(){
        repeat = repeat_types.Weekly;
    }

    public void set_repeats_monthly(){
        repeat = repeat_types.Monthly;
    }

    public String get_worker_amount_string(Activity parent){
        if (task_workers.size() == 0){
            return parent.getString(R.string.no_workers);//TODO GETSTRING...
        }else if (task_workers.size() == 1){
            return parent.getString(R.string.one_worker);
        }else{
            return task_workers.size() + parent.getString(R.string.space_workers);
        }
    }

    public String get_repeat_string(Activity parent){
        if (repeat == repeat_types.Once){ //TODO GETSTRING.....
            return parent.getString(R.string.repeat_once);
        }else if (repeat == repeat_types.Daily){
            return parent.getString(R.string.repeat_daily);
        }else if (repeat == repeat_types.Weekly){
            return parent.getString(R.string.repeat_weekly);
        }else if (repeat == repeat_types.Monthly){
            return parent.getString(R.string.repeat_monthly);
        }
        return null;
    }

    public String getQualifications(){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (Object_Qualification q : task_qualifications) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(q.toString());
        }

        return sb.toString();
    }

    public String getWorkers(){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (Object_Worker w : task_workers) {
            if (foundOne) {
                sb.append("\n");
            }
            foundOne = true;

            sb.append(w.get_fullName());
        }

        return sb.toString();
    }


}
