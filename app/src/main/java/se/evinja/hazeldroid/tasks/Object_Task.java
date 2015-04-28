package se.evinja.hazeldroid.tasks;


import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.qualifications.Object_Qualification;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Object_Task {
    public String title, description;
    public int min_workers, max_workers;
    private List<Object_Worker> task_workers = new ArrayList<>();
    private List<Object_Qualification> task_qualifications = new ArrayList<>();

    private enum repeat_types{
        ONCE,
        DAILY,
        WEEKLY,
        MONTHLY
    }
    repeat_types repeat;


    public void set_repeats_weekly(){
        repeat = repeat_types.WEEKLY;
    }

    public void set_repeats_monthly(){
        repeat = repeat_types.MONTHLY;
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
        if (repeat == repeat_types.ONCE){ //TODO GETSTRING.....
            return "repeat once";
        }else if (repeat == repeat_types.DAILY){
            return "repeat daily";
        }else if (repeat == repeat_types.WEEKLY){
            return "repeat weekly";
        }else if (repeat == repeat_types.MONTHLY){
            return "repeat monthly";
        }
        return null;
    }


}
