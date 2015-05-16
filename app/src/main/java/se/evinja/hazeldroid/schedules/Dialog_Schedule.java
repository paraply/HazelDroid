package se.evinja.hazeldroid.schedules;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Dialog_Schedule  extends DialogFragment {
    private DialogInterface.OnClickListener onItemClickListener;
    public Calendar cal_start, cal_end;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Hazel hazel;
    private WeekView weekView;

    public void init(DialogInterface.OnClickListener onItemClickListener, Hazel hazel, WeekView weekView){
        this.onItemClickListener = onItemClickListener;
        this.hazel = hazel;
        cal_start = Calendar.getInstance();
        cal_end = Calendar.getInstance();
        cal_end.add(Calendar.DAY_OF_MONTH, 28);
        this.weekView = weekView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_schedule, null, false);
        final TextView start = (TextView) view.findViewById(R.id.schedule_starts);
        start.setText(dateFormat.format(cal_start.getTime()));
        start.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal_start.set(Calendar.YEAR, year);
                                cal_start.set(Calendar.MONTH, monthOfYear);
                                cal_start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                start.setText(dateFormat.format(cal_start.getTime()));
                            }
                        }, cal_start.get(Calendar.YEAR), cal_start.get(Calendar.MONTH), cal_start.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        final TextView end = (TextView) view.findViewById(R.id.schedule_ends);
        end.setText(dateFormat.format(cal_end.getTime()));
        end.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal_end.set(Calendar.YEAR, year);
                                cal_end.set(Calendar.MONTH, monthOfYear);
                                cal_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                end.setText(dateFormat.format(cal_end.getTime()));
                            }
                        }, cal_end.get(Calendar.YEAR), cal_end.get(Calendar.MONTH), cal_end.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });


        return new AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.schedule))
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hazel.set_schedule(cal_start, cal_end, weekView);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

    }

}
