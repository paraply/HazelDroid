package se.evinja.hazeldroid.workers;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import se.evinja.hazeldroid.Activity_Main;
import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Qualifications_Selector;

public class Fragment_Worker_Add extends Fragment{
    private Hazel hazel;
    private  Activity_Main parent;

    EditText username, password, firstname, lastname, position, phone, mail, birthdate, last4;
    Qualifications_Selector qualifications;

    public static Fragment_Worker_Add newInstance() {
        return new Fragment_Worker_Add();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) parent.getApplication();
        hazel.download_qualifications_and_workers(parent);
        final View view = inflater.inflate(R.layout.fragment_worker_add, container, false);

        username = (EditText) view.findViewById(R.id.worker_add_username);
        password = (EditText) view.findViewById(R.id.worker_add_password);
        firstname = (EditText) view.findViewById(R.id.worker_add_firstname);
        lastname = (EditText) view.findViewById(R.id.worker_add_lastname);
        position = (EditText) view.findViewById(R.id.worker_add_position);
        qualifications = (Qualifications_Selector) view.findViewById(R.id.worker_add_qualifications);
        phone = (EditText) view.findViewById(R.id.worker_add_phone);
        mail = (EditText) view.findViewById(R.id.worker_add_mail);
        birthdate = (EditText) view.findViewById(R.id.worker_add_birthday);
        last4 = (EditText) view.findViewById(R.id.worker_add_last4);

        qualifications.setQualifications(hazel.get_qualifications());

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parent = (Activity_Main) activity;
        parent.set_title(activity.getString(R.string.add_worker));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!parent.navigation_open()) {
                inflater.inflate(R.menu.worker_add, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_worker_add_save){
            validate_and_save();
        }else if (id == R.id.action_worker_add_cancel){
            parent.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate_and_save(){
        Object_Worker worker = new Object_Worker();
        //TODO VALIDATE
        worker.username = username.getText().toString();
        worker.password = password.getText().toString();
        worker.lastName = lastname.getText().toString();
        worker.firstName = firstname.getText().toString();
        worker.position = position.getText().toString();
        worker.qualifications = qualifications.getSelected();

        worker.phoneNr = phone.getText().toString();
        worker.mailAddress = mail.getText().toString();
        worker.birthday = birthdate.getText().toString();
        worker.last4 = last4.getText().toString();

        hazel.add_worker(worker);
        parent.onBackPressed();
    }
}
