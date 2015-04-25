package se.evinja.hazeldroid.qualifications;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import se.evinja.hazeldroid.Hazel;
import se.evinja.hazeldroid.R;

public class Adapter_Qualifications extends BaseAdapter {
    private List<Object_Qualification> qualifications_list;
    private LayoutInflater inflater;
    private final Activity activity;

    public Adapter_Qualifications(Activity activity, List<Object_Qualification> qualifications_list){

        this.activity = activity;
        this.qualifications_list = qualifications_list;
    }

    //Uses viewholder to optimize listview
    static class ViewHolder{
        TextView title;
        TextView workers;
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
        ViewHolder holder;

        if (convertView == null) {
            if (inflater == null){
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.item_qualification, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.qualification_title);
            holder.workers = (TextView) convertView.findViewById(R.id.qualification_workers);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Object_Qualification oq = qualifications_list.get(position);

        holder.title.setText(oq.title);
        holder.workers.setText(oq.getWorkerString());

        return convertView;
    }
}
