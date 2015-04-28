package se.evinja.hazeldroid.tasks;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import se.evinja.hazeldroid.R;
import se.evinja.hazeldroid.workers.Object_Worker;

public class Adapter_Tasks extends BaseAdapter {
    private List<Object_Task> task_list;
    private LayoutInflater inflater;

    private Activity activity;


    public Adapter_Tasks(){}

    public Adapter_Tasks(Activity parent, List<Object_Task> tasks){
        this.task_list = tasks;
    }

    @Override
    public int getCount() {
        return task_list != null ? task_list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return task_list != null ? task_list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Uses viewholder to optimize listview. Stores findviewbyid:s since they are costly
    static class ViewHolder{
        TextView title;
        TextView description;
        TextView workers;
        TextView repeats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            if (inflater == null){
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.item_task, null);
            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.task_title);
            holder.description = (TextView) convertView.findViewById(R.id.task_description);
            holder.workers = (TextView) convertView.findViewById(R.id.task_workers);
            holder.repeats = (TextView) convertView.findViewById(R.id.task_repeats);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Object_Task ot = task_list.get(position);

        holder.title.setText(ot.title);
        holder.description.setText(ot.description);
        holder.workers.setText(ot.get_worker_amount_string());
        holder.repeats.setText(ot.get_repeat_string());

        return convertView;
    }
}
