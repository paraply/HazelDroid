package se.evinja.hazeldroid.tasks;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.evinja.hazeldroid.qualifications.Object_Qualification;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Object_Task {
    public String title, description;
    public int min_workers, max_workers;
    public List<Object_Worker> task_workers = new ArrayList<>();
    public List<Object_Qualification> task_qualifications = new ArrayList<>();
    public Calendar start, end;
    public int id = 123, repeat_length;

    public Calendar nextrun = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm");

    private enum repeat_types{
        Once,
        Daily,
        Yearly,
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
            j.put("id", id);
            j.put("startDate", getStartDate());
            j.put("endDate", getEndDate());
            j.put("weekdays", jweekdays);
            j.put("startTime", getStartTime());
            j.put("endTime", getEndTime());
            j.put("interval", repeat);
            j.put("intervalLength", repeat_length);
            j.put("client", client);
            j.put("name", title);
//            j.put("qualname", jqual);
            j.put("qualname", "Kobent");
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


    public void set_repeats_weekly(){
        repeat = repeat_types.Yearly;
    }

    public void set_repeats_monthly(){
        repeat = repeat_types.Monthly;
    }

    public String get_worker_amount_string(){
        if (task_workers.size() == 0){
            return "no workers";//TODO GETSTRING...
        }else if (task_workers.size() == 1){
            return "1 worker";
        }else{
            return task_workers.size() + " workers";
        }
    }

    public String get_repeat_string(){
        if (repeat == repeat_types.Once){ //TODO GETSTRING.....
            return "repeat Once";
        }else if (repeat == repeat_types.Daily){
            return "repeat Daily";
        }else if (repeat == repeat_types.Yearly){
            return "repeat weekly";
        }else if (repeat == repeat_types.Monthly){
            return "repeat Monthly";
        }
        return null;
    }


}
