package se.evinja.hazeldroid.schedules;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Work_Schedule extends Fragment implements WeekView.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener {
    private Hazel hazel;
    private Activity_Main parent;
    private WeekView weekView;


    public Fragment_Work_Schedule(){};

    public static Fragment_Work_Schedule newInstance(){
        return new Fragment_Work_Schedule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_schedule, container, false);
        hazel = (Hazel) parent.getApplication();

        weekView = (WeekView) view.findViewById(R.id.workplace_scedule);
        weekView.setMonthChangeListener(this);
        // Show a toast message about the touched event.
        weekView.setOnEventClickListener(this);

        // Set long press listener for events.
        weekView.setEventLongPressListener(this);


        return  view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(getString(R.string.work_schedule));
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Log.i("##### WEEKV", "newyear " + newYear + " newmonth " + newMonth);
        List<WeekViewEvent> events = new ArrayList<>();
        for (Object_Schedule os : hazel.workplace_schedule){
            if (os.getMonth() == newMonth){
                Log.i("### EVENT ADD", os.name);
                WeekViewEvent event = new WeekViewEvent(os.id, os.name, os.startTime, os.endTime);
                if (os.scheduled){
                    event.setColor(getResources().getColor(R.color.event_color_green));
                }else{
                    event.setColor(getResources().getColor(R.color.event_color_red));
                }
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {
        Object_Schedule os = hazel.getWorkSchedFromID((int) weekViewEvent.getId());
        if (os != null){
            Dialog dialog = new Dialog(parent);
            dialog.setTitle(os.name);
            dialog.setCanceledOnTouchOutside(true);
            LayoutInflater inflater = parent.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_task_info, null);
            ((TextView) view.findViewById(R.id.sched_info_starts)).setText(os.getStart());
            ((TextView) view.findViewById(R.id.sched_info_ends)).setText(os.getEnd());
            ((TextView) view.findViewById(R.id.sched_info_workers)).setText(os.getWorkers(parent));
            dialog.setContentView(view);

            dialog.show();
        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {
//        Toast.makeText(parent, "LONGClicked " + weekViewEvent.getName(), Toast.LENGTH_SHORT).show();
    }
}
