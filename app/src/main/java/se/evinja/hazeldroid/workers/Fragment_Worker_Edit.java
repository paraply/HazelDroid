package se.evinja.hazeldroid.workers;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Dialog_Qualifications;

public class Fragment_Worker_Edit extends Fragment implements DialogInterface.OnClickListener{
    private Hazel hazel;
    private Object_Worker worker;
    private Activity_Main parent;

    private EditText firstname, lastname, position, phone, mail, last4, birthdate, minhours, maxhours;
    private TextView qualifications ;
    private Dialog_Qualifications qual_dialog;
    private Calendar birthdate_calendar = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    public static Fragment_Worker_Edit newInstance(int worker_position) {
        Fragment_Worker_Edit fragment = new Fragment_Worker_Edit();
        Bundle args = new Bundle();
        args.putInt("worker_position", worker_position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(activity.getString(R.string.edit_worker));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!parent.navigation_open()) {
            inflater.inflate(R.menu.worker_edit, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_worker_edit, container, false);
        hazel = (Hazel) getActivity().getApplication();
//        hazel.download_qualifications_and_workers(parent); //Download qualifications if not already downloaded. Automatically fills Selector
        hazel.download_workers();
        worker = hazel.get_worker(getArguments().getInt("worker_position"));

        qualifications = (TextView) view.findViewById(R.id.worker_edit_qualifications);
        qual_dialog = new Dialog_Qualifications();
        qual_dialog.init(parent, hazel, this);
        qual_dialog.setSelectedQualifications(worker.qualifications);
        qualifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qual_dialog.show(getFragmentManager(), "Required Qualifications");
            }
        });
        qualifications.setText(qual_dialog.getSelectedString());

        firstname = (EditText) view.findViewById(R.id.worker_edit_firstname);
        lastname = (EditText) view.findViewById(R.id.worker_edit_lastname);
        position = (EditText) view.findViewById(R.id.worker_edit_position);
        phone = (EditText) view.findViewById(R.id.worker_edit_phone);
        mail = (EditText) view.findViewById(R.id.worker_edit_mail);
        birthdate = (EditText) view.findViewById(R.id.worker_edit_birthday);

        minhours = (EditText) view.findViewById(R.id.worker_edit_min_work);
        maxhours = (EditText) view.findViewById(R.id.worker_edit_max_work);

        last4 = (EditText) view.findViewById(R.id.worker_edit_last4);

        firstname.setText(worker.firstName);
        lastname.setText(worker.lastName);
        position.setText(worker.position);
        phone.setText(worker.phoneNr);
        mail.setText(worker.mailAddress);
        birthdate.setText(worker.birthday);
        last4.setText(worker.last4);
        minhours.setText(worker.getMinHours());
        maxhours.setText(worker.getMaxHours());

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_worker_edit_save){
            validate_and_save();
        }else if (id == R.id.action_worker_edit_cancel){
            parent.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_and_save(){
        //TODO VALIDATE
        worker.lastName = lastname.getText().toString();
        worker.firstName = firstname.getText().toString();
        worker.position = position.getText().toString();
        worker.qualifications = qual_dialog.getSelectedQualifications();

        worker.phoneNr = phone.getText().toString();
        worker.mailAddress = mail.getText().toString();
        worker.birthday = birthdate.getText().toString();
        worker.last4 = last4.getText().toString();

        worker.minhours = minhours.getText().toString();
        worker.maxhours = maxhours.getText().toString();

        hazel.update_worker(getArguments().getInt("worker_position"));

        parent.onBackPressed();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        qualifications.setText(qual_dialog.getSelectedString());
    }
}
