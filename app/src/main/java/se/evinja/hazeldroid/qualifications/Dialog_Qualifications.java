package se.evinja.hazeldroid.qualifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Dialog_Qualifications extends DialogFragment  {
    private Activity parent;
//    private Hazel hazel;
    private DialogInterface.OnClickListener onItemClickListener;
    private Object_Qualification[] qualifications;
    private boolean[] selected;

    public void init(Activity parent, Hazel hazel, DialogInterface.OnClickListener onItemClickListener){
        this.parent = parent;
//        this.hazel = hazel;
        this.onItemClickListener = onItemClickListener;

        qualifications = new Object_Qualification[hazel.qualifications.size()];
        qualifications = hazel.qualifications.toArray(new Object_Qualification[hazel.qualifications.size()]);
        selected = new boolean[qualifications.length];
        Arrays.fill(selected, false);
    }

    private String[] getStringArray(){
        String[] ret = new String[qualifications.length];
        for (int index = 0; index < qualifications.length; index++){
            ret[index] = qualifications[index].toString();
        }
        return ret;
    }

    public String getSelectedString(){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < qualifications.length; ++i) {
            if (selected[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(qualifications[i].toString());
            }
        }
        return sb.toString();
    }

    public List<Object_Qualification> getSelectedQualifications(){
        List<Object_Qualification> q = new ArrayList<>();
        for (int i = 0; i < qualifications.length; i++){
            if (selected[i]){
                q.add(qualifications[i]);
            }
        }
        return q;
    }

    public void setSelectedQualifications(List<Object_Qualification> sel_q){
        for (int i = 0; i < qualifications.length; i++) {
            for (Object_Qualification q : sel_q) {
                if (q.toString().equals(qualifications[i].toString())){
                    selected[i] = true;
                }
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(parent.getString(R.string.select_qualifications))
                .setMultiChoiceItems(getStringArray(), selected, new DialogSelectionClickHandler())
                .setPositiveButton(getString(R.string.ok), new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickListener.onClick(dialog, 2);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
    }



    class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener  {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            selected[which] = isChecked;
        }
    }


}
