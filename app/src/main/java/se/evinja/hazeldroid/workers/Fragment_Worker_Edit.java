package se.evinja.hazeldroid.workers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Fragment_Worker_Edit extends Fragment{
    private Hazel hazel;
    private Object_Worker person;

    EditText ep_firstName, ep_lastName, ep_workPosition, ep_phone, ep_mail, ep_birthdate, ep_last4;
    Spinner ep_Competences;


    public static Fragment_Worker_Edit newInstance(int person_position) {
        Fragment_Worker_Edit fragment = new Fragment_Worker_Edit();
        Bundle args = new Bundle();
        args.putInt("worker_position", person_position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_worker_edit, container, false);
        hazel = (Hazel) getActivity().getApplication();
        person = hazel.get_worker(getArguments().getInt("worker_position"));

        ep_Competences = (Spinner) view.findViewById(R.id.worker_edit_qualifications);

//        ep_Competences.setItems(hazelServer.getCompetences()); //TODO

//        person = hazelServer.getPersonList().get(pos);
        ep_firstName = (EditText) view.findViewById(R.id.worker_edit_firstname);
        ep_lastName = (EditText) view.findViewById(R.id.worker_edit_lastname);
        ep_workPosition = (EditText) view.findViewById(R.id.worker_edit_position);
        ep_phone = (EditText) view.findViewById(R.id.worker_edit_phone);
        ep_mail = (EditText) view.findViewById(R.id.worker_edit_mail);
        ep_birthdate = (EditText) view.findViewById(R.id.worker_edit_birthday);
        ep_last4 = (EditText) view.findViewById(R.id.worker_edit_last4);

//        if (person.hasCompetences()){
//            ep_Competences.setSelection(person.getQualifications()); //TODO
//        }

        //ep_Competences.get_proxyAdapter().notifyDataSetChanged();
        ep_firstName.setText(person.firstName);
        ep_lastName.setText(person.lastName);
        ep_workPosition.setText(person.position);
        ep_phone.setText(person.phoneNr);
        ep_mail.setText(person.mailAddress);
        ep_birthdate.setText(person.birthday);
        ep_last4.setText(person.last4);


        return view;
    }


}
