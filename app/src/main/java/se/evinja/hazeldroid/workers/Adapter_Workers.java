package se.evinja.hazeldroid.workers;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import se.evinja.hazeldroid.R;

public class Adapter_Workers extends BaseAdapter{
    private List<Object_Worker> worker_list;
    private LayoutInflater inflater;
    private final Activity activity;

    public Adapter_Workers(Activity activity, List<Object_Worker> worker_list){

        this.activity = activity;
        this.worker_list = worker_list;
    }

    //Uses viewholder to optimize listview
    static class ViewHolder{
        TextView fullname;
        TextView position;
    }


    @Override
    public int getCount() {
         return worker_list == null ? 0 : worker_list.size();
    }

    @Override
    public Object getItem(int position) {
        return worker_list.get(position);
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
            convertView = inflater.inflate(R.layout.item_worker, null);
            holder = new ViewHolder();
            holder.fullname = (TextView) convertView.findViewById(R.id.worker_fullname);
            holder.position = (TextView) convertView.findViewById(R.id.worker_position);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Object_Worker ow = worker_list.get(position);

        holder.fullname.setText(ow.get_fullName());
        holder.position.setText(ow.position);

        return convertView;
    }
}
