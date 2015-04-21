package se.evinja.hazeldroid.navigation;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.evinja.hazeldroid.R;

public class Adapter_Navigate extends RecyclerView.Adapter<Adapter_Navigate.ViewHolder> {
    private List<Item_Navigate> list_items;
    private Callback_Navigate callback_navigate;
    private int current_selected_position;
    private int touched_position = -1;

    public Adapter_Navigate(List<Item_Navigate> list_items){ //Get all items in constructor
        this.list_items = list_items;
    }

    public void setNavigationDrawerCallbacks(Callback_Navigate callback_navigate) {
        this.callback_navigate = callback_navigate;
    }


    @Override
    public Adapter_Navigate.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_row, parent, false); //Inflate this item
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_Navigate.ViewHolder holder, final int position) {
        holder.textView.setText(list_items.get(position).getTitle());
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(list_items.get(position).getIcon(), null, null, null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (callback_navigate != null)
                                                       callback_navigate.onNavigationDrawerItemSelected(position);
                                               }
                                           }
        );


        if (current_selected_position == position || touched_position == position) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.accent));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void selectPosition(int new_position) {
        int last_position = current_selected_position;
        current_selected_position = new_position;
        notifyItemChanged(last_position);
        notifyItemChanged(new_position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }

    @Override
    public int getItemCount() {
        return list_items != null ? list_items.size() : 0;
    }
}
