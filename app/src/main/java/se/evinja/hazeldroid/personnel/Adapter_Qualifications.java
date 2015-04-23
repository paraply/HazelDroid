package se.evinja.hazeldroid.personnel;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.evinja.hazeldroid.R;

public class Adapter_Qualifications extends BaseAdapter {
    private List<Object_Qualification> qualifications_list;
    private LayoutInflater inflater;
    private final Activity activity;

    public Adapter_Qualifications(Activity activity, List<Object_Qualification> qualifications_list){

        this.activity = activity;
        this.qualifications_list = qualifications_list;
    }

    @Override
    public int getCount() {
        return qualifications_list == null ? 0 : qualifications_list.size();
    }

    @Override
    public Object getItem(int position) {
        return qualifications_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_qualification,null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.qualification_title);
        TextView workers = (TextView) convertView.findViewById(R.id.qualification_workers);

        Object_Qualification oq = qualifications_list.get(position);

        title.setText(oq.title);
        workers.setText(oq.getWorkerString());

        return convertView;
    }
}
