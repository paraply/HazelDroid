package se.evinja.hazeldroid.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import se.evinja.hazeldroid.R;

public class Dialog_Repeat extends DialogFragment{
    private DialogInterface.OnClickListener onItemClickListener;

    public Repeat_Types repeat = Repeat_Types.Once;
    public String repeat_interval = "1", repeat_month_date;
//    private boolean mo = true,tu,we,th,fr,sa,su;

    public boolean sel_weekdays[] = new boolean[7];



    public void init(DialogInterface.OnClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public String getString(Activity parent){
        String str = "";
        if (repeat == Repeat_Types.Once) {
            str = parent.getString(R.string.only_once);
        }else if (repeat == Repeat_Types.Daily){
            str =  repeat_interval.equals("1") ? parent.getString(R.string.every_day) : parent.getString(R.string.every) + repeat_interval + " " + parent.getString(R.string.days);
        }else if (repeat == Repeat_Types.Weekly){
            str = repeat_interval.equals("1") ? parent.getString(R.string.every_week) : parent.getString(R.string.every) + repeat_interval + " " + parent.getString(R.string.weeks) ;

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
        return str;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
        sel_weekdays[0] = true; // Set monday as true as default
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_repeat, null, false);
        final RelativeLayout daily = (RelativeLayout) view.findViewById(R.id.repeat_daily);
        final RelativeLayout weekly = (RelativeLayout) view.findViewById(R.id.repeat_weekly);
        final RelativeLayout monthly = (RelativeLayout) view.findViewById(R.id.repeat_monthly);
        final TextView daily_day = (TextView) view.findViewById(R.id.repeat_daily_day);
        final TextView weekly_week = (TextView) view.findViewById(R.id.repeat_weekly_week);
        final TextView monthly_month = (TextView) view.findViewById(R.id.repeat_monthly_month);
        daily_day.setText(repeat_interval);
        weekly_week.setText(repeat_interval);
        monthly_month.setText(repeat_interval);

        final CheckBox mon = (CheckBox) view.findViewById(R.id.repeat_mon); mon.setChecked(sel_weekdays[0]);
        final CheckBox tue = (CheckBox) view.findViewById(R.id.repeat_tue); tue.setChecked(sel_weekdays[1]);
        final CheckBox wed = (CheckBox) view.findViewById(R.id.repeat_wed); wed.setChecked(sel_weekdays[2]);
        final CheckBox thu = (CheckBox) view.findViewById(R.id.repeat_thu); thu.setChecked(sel_weekdays[3]);
        final CheckBox fri = (CheckBox) view.findViewById(R.id.repeat_fri); fri.setChecked(sel_weekdays[4]);
        final CheckBox sat = (CheckBox) view.findViewById(R.id.repeat_sat); sat.setChecked(sel_weekdays[5]);
        final CheckBox sun = (CheckBox) view.findViewById(R.id.repeat_sun); sun.setChecked(sel_weekdays[6]);

        final TextView month_date = (TextView) view.findViewById(R.id.repeat_monthly_date);
        month_date.setText(repeat_month_date);

        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_repeat_spinner);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                daily.setVisibility(View.GONE);
                weekly.setVisibility(View.GONE);
                monthly.setVisibility(View.GONE);
                if (position == 0) {
                    repeat = Repeat_Types.Once;
                }else if (position == 1){
                    daily.setVisibility(View.VISIBLE);
                    repeat = Repeat_Types.Daily;
                    repeat_interval = daily_day.getText().toString();
                }else if (position == 2){
                    weekly.setVisibility(View.VISIBLE);
                    repeat = Repeat_Types.Weekly;

                    repeat_interval = weekly_week.getText().toString();
                }else if (position == 3) {
                    monthly.setVisibility(View.VISIBLE);
                    repeat = Repeat_Types.Monthly;
                    repeat_interval = monthly_month.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] repeat_choices = new String[]{ctx.getString(R.string.once), ctx.getString(R.string.daily), ctx.getString(R.string.weekly), ctx.getString(R.string.monthly)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, repeat_choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (repeat == Repeat_Types.Once) spinner.setSelection(0);
        if (repeat == Repeat_Types.Daily) spinner.setSelection(1);
        if (repeat == Repeat_Types.Weekly) spinner.setSelection(2);
        if (repeat == Repeat_Types.Monthly) spinner.setSelection(3);

        return new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.repeat_this_task))
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sel_weekdays[0] = mon.isChecked();
                        sel_weekdays[1] = tue.isChecked();
                        sel_weekdays[2] = wed.isChecked();
                        sel_weekdays[3] = thu.isChecked();
                        sel_weekdays[4] = fri.isChecked();
                        sel_weekdays[5] = sat.isChecked();
                        sel_weekdays[6] = sun.isChecked();

                        repeat_month_date = month_date.getText().toString();
                        if (repeat == Repeat_Types.Daily) repeat_interval =  daily_day.getText().toString();
                        if (repeat == Repeat_Types.Weekly) repeat_interval =  weekly_week.getText().toString();
                        if (repeat == Repeat_Types.Monthly) repeat_interval =  monthly_month.getText().toString();

                        onItemClickListener.onClick(dialog, 3);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

}
