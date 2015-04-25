package se.evinja.hazeldroid.qualifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import se.evinja.hazeldroid.Hazel;


public class Qualifications_Selector extends Spinner implements DialogInterface.OnMultiChoiceClickListener {
    private String[] qualifications;
    private boolean[] selected_qualifications;
    private ArrayAdapter<String> adapter;

    public Qualifications_Selector(Context context) { //When instanced directly
        super(context);
        adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    public Qualifications_Selector(Context context, AttributeSet attrs) { //When instanced from layout
        super(context, attrs);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    public void update(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (selected_qualifications != null && which < selected_qualifications.length) {
            selected_qualifications[which] = isChecked;

            adapter.clear();
            adapter.add(buildSelectedItemString());
            setSelection(0);
        }
        else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(qualifications, selected_qualifications, this);
        builder.setCancelable(true);
        builder.show();
        return true;
    }

    public void setQualifications(List<Object_Qualification> new_q) {
        this.qualifications = new String[new_q.size()];
        int index = 0;
        for (Object_Qualification oq : new_q){
            qualifications[index] = oq.toString();
            index++;
        }

        selected_qualifications = new boolean[this.qualifications.length];

        Arrays.fill(selected_qualifications, false);
    }

    public void setSelection(List<String> selection) {
        for (String sel : selection) {
            for (int j = 0; j < qualifications.length; ++j) {
                if (qualifications[j].equals(sel)) {
                    selected_qualifications[j] = true;
                }
            }
        }
        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<String>();
        for (int i = 0; i < qualifications.length; ++i) {
            if (selected_qualifications[i]) {
                selection.add(qualifications[i]);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < qualifications.length; ++i) {
            if (selected_qualifications[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(qualifications[i]);
            }
        }

        return sb.toString();
    }


}
