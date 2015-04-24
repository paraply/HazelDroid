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

public class Fragment_Worker_Add extends Fragment{
    private Hazel hazel;
    private  Activity_Main parent;

    EditText username, password, firstname, lastname, position, phone, mail, birthdate, last4;
    Spinner qualifications;

    public static Fragment_Worker_Add newInstance() {
//        Fragment_Worker_Add fragment = new Fragment_Worker_Add();
        return new Fragment_Worker_Add();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        hazel = (Hazel) getActivity().getApplication();
        final View view = inflater.inflate(R.layout.fragment_worker_add, container, false);

        username = (EditText) view.findViewById(R.id.worker_add_username);
        password = (EditText) view.findViewById(R.id.worker_add_password);
        firstname = (EditText) view.findViewById(R.id.worker_add_firstname);
        lastname = (EditText) view.findViewById(R.id.worker_add_lastname);
        position = (EditText) view.findViewById(R.id.worker_add_position);
        qualifications = (Spinner) view.findViewById(R.id.worker_add_qualifications);
        phone = (EditText) view.findViewById(R.id.worker_add_phone);
        mail = (EditText) view.findViewById(R.id.worker_add_mail);
        birthdate = (EditText) view.findViewById(R.id.worker_add_birthday);
        last4 = (EditText) view.findViewById(R.id.worker_add_last4);

//        qualifications.setItems(hazelServer.getCompetences());

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
        Object_Worker person = new Object_Worker();
        //TODO VALIDATE
        person.username = username.getText().toString();
        person.password = password.getText().toString();
        person.lastName = lastname.getText().toString();
        person.firstName = firstname.getText().toString();
        person.position = position.getText().toString();
//        person.setQualifications(qualifications.getSelectedStrings());
        person.phoneNr = phone.getText().toString();
        person.mailAddress = mail.getText().toString();
        person.birthday = birthdate.getText().toString();
        person.last4 = last4.getText().toString();

        hazel.add_worker(person);
        parent.onBackPressed();
    }
}
