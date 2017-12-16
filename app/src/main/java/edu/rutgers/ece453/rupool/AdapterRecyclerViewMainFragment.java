package edu.rutgers.ece453.rupool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhu_z on 2017/12/16.
 */

public class AdapterRecyclerViewMainFragment extends RecyclerView.Adapter<AdapterRecyclerViewMainFragment.ViewHolder> {

    private ArrayList<PoolActivity> mData;

    public AdapterRecyclerViewMainFragment(ArrayList<PoolActivity> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoolActivity poolActivity = mData.get(position);
        holder.textViewDateMonth.setText("TEST");
        holder.textViewDateDay.setText("TEST");
        holder.textViewStartPoint.setText("TEST");
        holder.textViewDestination.setText("TEST");
        holder.textViewDescription.setText("TEST");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDateMonth;
        public TextView textViewDateDay;
        public TextView textViewStartPoint;
        public TextView textViewDestination;
        public TextView textViewDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDateMonth = itemView.findViewById(R.id.Date_Month);
            textViewDateDay = itemView.findViewById(R.id.Date_Day);
            textViewStartPoint = itemView.findViewById(R.id.start_point);
            textViewDestination = itemView.findViewById(R.id.destination);
            textViewDescription = itemView.findViewById(R.id.description);
        }
    }


}
