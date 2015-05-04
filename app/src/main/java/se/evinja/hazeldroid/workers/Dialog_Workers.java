package se.evinja.hazeldroid.workers;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.qualifications.Object_Qualification;

public class Dialog_Workers extends DialogFragment  {
    private Activity parent;
//    private Hazel hazel;
    private DialogInterface.OnClickListener onItemClickListener;
    private Object_Worker[] workers;
    private boolean[] selected;

    public void init(Activity parent, Hazel hazel, DialogInterface.OnClickListener onItemClickListener){
        this.parent = parent;
//        this.hazel = hazel; //TODO neccessary??
        this.onItemClickListener = onItemClickListener;

        workers = new Object_Worker[hazel.workers.size()];
        workers = hazel.workers.toArray(new Object_Worker[hazel.workers.size()]);
        selected = new boolean[workers.length];
        Arrays.fill(selected, false);
    }

    private String[] getStringArray(){
        String[] ret = new String[workers.length];
        for (int index = 0; index < workers.length; index++){
            ret[index] = workers[index].get_fullName();
        }
        return ret;
    }

    public String getSelectedString(){
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < workers.length; ++i) {
            if (selected[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(workers[i].get_fullName());
            }
        }
        return sb.toString();
    }

    public List<Object_Worker> getSelectedWorkers(){
        List<Object_Worker> w = new ArrayList<>();
        for (int i = 0; i < workers.length; i++){
            if (selected[i]){
                w.add(workers[i]);
            }
        }
        return w;
    }

    public void setSelectedWorkers(List<Object_Worker> sel_w){
     for (int i = 0; i < workers.length; i++) {
         for (Object_Worker w : sel_w) {
            if (w.id == workers[i].id){
                selected[i] = true;
            }
         }
     }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(parent.getString(R.string.select_workers))
                .setMultiChoiceItems(getStringArray(), selected, new DialogSelectionClickHandler())
                .setPositiveButton(getString(R.string.ok), new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickListener.onClick(dialog, 1);
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
