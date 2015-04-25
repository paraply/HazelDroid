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

public class Fragment_Worker_Edit extends Fragment{
    private Hazel hazel;
    private Object_Worker worker;
    private Activity_Main parent;

    private EditText firstname, lastname, position, phone, mail, birthdate, last4;
    private Spinner qualifications;


    public static Fragment_Worker_Edit newInstance(int worker_position) {
        Fragment_Worker_Edit fragment = new Fragment_Worker_Edit();
        Bundle args = new Bundle();
        args.putInt("worker_position", worker_position);
        fragment.setArguments(args);
        return fragment;
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
        worker = hazel.get_worker(getArguments().getInt("worker_position"));

        qualifications = (Spinner) view.findViewById(R.id.worker_edit_qualifications);

//        qualifications.setItems(hazelServer.getCompetences()); //TODO

//        worker = hazelServer.getPersonList().get(pos);
        firstname = (EditText) view.findViewById(R.id.worker_edit_firstname);
        lastname = (EditText) view.findViewById(R.id.worker_edit_lastname);
        position = (EditText) view.findViewById(R.id.worker_edit_position);
        phone = (EditText) view.findViewById(R.id.worker_edit_phone);
        mail = (EditText) view.findViewById(R.id.worker_edit_mail);
        birthdate = (EditText) view.findViewById(R.id.worker_edit_birthday);
        last4 = (EditText) view.findViewById(R.id.worker_edit_last4);

//        if (worker.hasCompetences()){
//            qualifications.setSelection(worker.getQualifications()); //TODO
//        }

        //qualifications.get_proxyAdapter().notifyDataSetChanged();
        firstname.setText(worker.firstName);
        lastname.setText(worker.lastName);
        position.setText(worker.position);
        phone.setText(worker.phoneNr);
        mail.setText(worker.mailAddress);
        birthdate.setText(worker.birthday);
        last4.setText(worker.last4);

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
//        person.setQualifications(qualifications.getSelectedStrings());
        worker.phoneNr = phone.getText().toString();
        worker.mailAddress = mail.getText().toString();
        worker.birthday = birthdate.getText().toString();
        worker.last4 = last4.getText().toString();

        parent.onBackPressed();
    }


}
