package se.evinja.hazeldroid.schedules;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Work_Schedule extends Fragment implements WeekView.MonthChangeListener {
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
        hazel.getWorkplaceSchedule("2015-04-24 00:00:00", "2015-04-25 23:59:00");
        weekView = (WeekView) view.findViewById(R.id.workplace_scedule);
        weekView.setMonthChangeListener(this);

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
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, "titel", startTime, endTime);
        event.setColor(getResources().getColor(R.color.material_blue_grey_800));
        events.add(event);
        return events;
    }

}
