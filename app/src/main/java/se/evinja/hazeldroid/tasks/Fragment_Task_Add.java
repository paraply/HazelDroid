package se.evinja.hazeldroid.tasks;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Dialog_Qualifications;
import se.evinja.hazeldroid.workers.Dialog_Workers;


public class Fragment_Task_Add extends Fragment implements DialogInterface.OnClickListener {
    private Hazel hazel;
    private Activity_Main parent;

    private EditText title, description, min_w, max_w;
    private TextView start_d, end_d, start_t, end_t, repeat, qualifications, workers;
    private Calendar start, end;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");

    private Dialog_Workers wrk_dialog;
    private Dialog_Qualifications qual_dialog;
    private Object_Task t;

    public static Fragment_Task_Add newInstance() {
        return new Fragment_Task_Add();
    }

    public static Fragment_Task_Add newInstance(int edit_position) {
        Fragment_Task_Add fragment = new Fragment_Task_Add();
        Bundle args = new Bundle();
        args.putInt("edit_position", edit_position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        hazel.download_qualifications_and_workers(parent); //if gotten here without doing that yet..

        final View view = inflater.inflate(R.layout.fragment_task_add, container, false);
        title = (EditText) view.findViewById(R.id.task_add_name);
        description = (EditText) view.findViewById(R.id.task_add_desc);
        min_w = (EditText) view.findViewById(R.id.task_add_min_work);
        max_w = (EditText) view.findViewById(R.id.task_add_max_work);

        start = Calendar.getInstance();
        start_d = (TextView) view.findViewById(R.id.task_add_start_date);
        start_d.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                start.set(Calendar.YEAR, year);
                                start.set(Calendar.MONTH, monthOfYear);
                                start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                start_d.setText(dateFormat.format(start.getTime()));
                            }
                        }, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        start_d.setText(dateFormat.format(start.getTime()));

        start_t = (TextView) view.findViewById(R.id.task_add_start_time);
        start_t.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog tdp = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        start.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        start.set(Calendar.MINUTE, minute);

                        start_t.setText(timeFormat.format(start.getTime()));
                    }
                }, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true);
                tdp.show();
            }
        });
        start_t.setText(timeFormat.format(start.getTime()));

        end = Calendar.getInstance();
        end.add(Calendar.HOUR_OF_DAY, 8);
        end_d = (TextView) view.findViewById(R.id.task_add_end_date);
        end_d.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                end.set(Calendar.YEAR, year);
                                end.set(Calendar.MONTH, monthOfYear);
                                end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                end_d.setText(dateFormat.format(end.getTime()));
                            }
                        }, end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        end_d.setText(dateFormat.format(end.getTime()));
        end_t = (TextView) view.findViewById(R.id.task_add_end_time);
        end_t.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tdp = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        end.set(Calendar.MINUTE, minute);
                        end_t.setText(timeFormat.format(end.getTime()));
                    }
                }, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), true);
                tdp.show();
            }
        });
        end_t.setText(timeFormat.format(end.getTime()));

        repeat = (TextView) view.findViewById(R.id.task_add_repeat);
        qualifications = (TextView) view.findViewById(R.id.task_add_qualifications);
        wrk_dialog = new Dialog_Workers();
        wrk_dialog.init(parent, hazel, this);
        qual_dialog = new Dialog_Qualifications();
        qual_dialog.init(parent, hazel, this);
        qualifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qual_dialog.show(getFragmentManager(), "Required Qualifications");
            }
        });
        workers = (TextView) view.findViewById(R.id.task_add_workers);
        workers.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                wrk_dialog.show(getFragmentManager(), "Required workers");
            }
        });

        if (getArguments() == null ) { //Is in new mode && getArguments().getInt("edit_position",-1) == -1
            parent.set_title(parent.getString(R.string.add_task));
            t = new Object_Task();
        }else{ //Is in edit mode
            parent.set_title(parent.getString(R.string.edit_task));
            t = hazel.get_task(getArguments().getInt("edit_position"));
            title.setText(t.title);
            description.setText(t.description);
            start_d.setText(t.getStartDate());
            start_t.setText(t.getStartTime());
            end_d.setText(t.getEndDate());
            end_t.setText(t.getEndTime());
            repeat.setText(t.get_repeat_string());
            qual_dialog.setSelectedQualifications(t.task_qualifications);
            qualifications.setText(qual_dialog.getSelectedString());
            min_w.setText(Integer.toString(t.min_workers));
            max_w.setText(Integer.toString(t.max_workers));
            wrk_dialog.setSelectedWorkers(t.task_workers);
            workers.setText(wrk_dialog.getSelectedString());
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(activity.getString(R.string.add_task));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!parent.navigation_open()) {
            inflater.inflate(R.menu.task_add, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_task_add_save){
            validate_and_save();
        }else if (id == R.id.action_task_add_cancel){
            parent.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_and_save(){
        //TODO VALIDATE
        t.title = title.getText().toString();
        t.description = description.getText().toString();
        t.min_workers = Integer.parseInt(min_w.getText().toString());
        t.max_workers = Integer.parseInt(max_w.getText().toString());
        t.start = start;
        t.end = end;
        t.repeat_length = 3;
        t.task_workers = wrk_dialog.getSelectedWorkers();
        t.task_qualifications = qual_dialog.getSelectedQualifications();
        t.set_repeats_weekly();
        if (getArguments() == null) { // In ADD mode
            hazel.add_task(t);
        }
        parent.onBackPressed();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == 1){
            workers.setText(wrk_dialog.getSelectedString());
        }else if (which == 2){
            qualifications.setText(qual_dialog.getSelectedString());
        }
    }
}
