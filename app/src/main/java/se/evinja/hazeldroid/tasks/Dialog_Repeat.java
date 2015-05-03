package se.evinja.hazeldroid.tasks;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import se.evinja.hazeldroid.R;

public class Dialog_Repeat extends DialogFragment{
    private DialogInterface.OnClickListener onItemClickListener;

    private enum repeat_types{
        Once,
        Daily,
        Weekly,
        Monthly
    } private repeat_types repeat = repeat_types.Once;
    private String repeat_interval = "1", repeat_month_date = "1";
    private boolean mo = true,tu,we,th,fr,sa,su;

    public void init(DialogInterface.OnClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public String getString(){
        String str = "";
        if (repeat == repeat_types.Once) {
            str = "Only once";
        }else if (repeat == repeat_types.Daily){
            str =  repeat_interval.equals("1") ? "Every day" : "Every " + repeat_interval + " day";
        }else if (repeat == repeat_types.Weekly){
            str = repeat_interval.equals("1") ? "Every week" : "Every " + repeat_interval + " week, ";

            if (mo) str += "Monday ";
            if (tu) str += "Tuesday ";
            if (we) str += "Wednesday ";
            if (th) str += "Thursday ";
            if (fr) str += "Friday ";
            if (sa) str += "Saturday ";
            if (su) str += "Sunday ";

        }else if (repeat == repeat_types.Monthly){
            str = repeat_interval.equals("1") ? "Every month" : "Every " + repeat_interval + " month";
            str += " on the " + repeat_month_date + ":th";
        }
        return str;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
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

        final CheckBox mon = (CheckBox) view.findViewById(R.id.repeat_mon);
        mon.setChecked(mo);
        final CheckBox tue = (CheckBox) view.findViewById(R.id.repeat_tue);
        tue.setChecked(tu);
        final CheckBox wed = (CheckBox) view.findViewById(R.id.repeat_wed);
        wed.setChecked(we);
        final CheckBox thu = (CheckBox) view.findViewById(R.id.repeat_thu);
        thu.setChecked(th);
        final CheckBox fri = (CheckBox) view.findViewById(R.id.repeat_fri);
        fri.setChecked(fr);
        final CheckBox sat = (CheckBox) view.findViewById(R.id.repeat_sat);
        sat.setChecked(sa);
        final CheckBox sun = (CheckBox) view.findViewById(R.id.repeat_sun);
        sun.setChecked(su);

        final TextView month_date = (TextView) view.findViewById(R.id.repeat_monthly_date);
        month_date.setText(repeat_month_date);

        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_repeat_spinner);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  daily.setVisibility(View.GONE);
                                                  weekly.setVisibility(View.GONE);
                                                  monthly.setVisibility(View.GONE);

                                                  if (position == 1){
                                                      daily.setVisibility(View.VISIBLE);
                                                      repeat = repeat_types.Daily;
                                                      repeat_interval = daily_day.getText().toString();
                                                  }else if (position == 2){
                                                      weekly.setVisibility(View.VISIBLE);
                                                      repeat = repeat_types.Weekly;

                                                      repeat_interval = weekly_week.getText().toString();
                                                  }else if (position == 3) {
                                                      monthly.setVisibility(View.VISIBLE);
                                                      repeat = repeat_types.Monthly;
                                                      repeat_interval = monthly_month.getText().toString();
                                                  }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {

                                              }
                                          });

            String[] repeat_choices = new String[]{"Once", "Daily", "Weekly", "Monthly"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, repeat_choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (repeat == repeat_types.Once) spinner.setSelection(0);
        if (repeat == repeat_types.Daily) spinner.setSelection(1);
        if (repeat == repeat_types.Weekly) spinner.setSelection(2);
        if (repeat == repeat_types.Monthly) spinner.setSelection(3);

        return new AlertDialog.Builder(ctx)
                .setTitle("Repeat this task")
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mo = mon.isChecked();
                        tu = tue.isChecked();
                        we = wed.isChecked();
                        th = thu.isChecked();
                        fr = fri.isChecked();
                        sa = sat.isChecked();
                        su = sun.isChecked();

                        repeat_month_date = month_date.getText().toString();
                        if (repeat == repeat_types.Daily) repeat_interval =  daily_day.getText().toString();
                        if (repeat == repeat_types.Weekly) repeat_interval =  weekly_week.getText().toString();
                        if (repeat == repeat_types.Monthly) repeat_interval =  monthly_month.getText().toString();

                        onItemClickListener.onClick(dialog, 3);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

}
