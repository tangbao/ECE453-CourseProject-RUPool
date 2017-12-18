package edu.rutgers.ece453.rupool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhu_z on 2017/12/18.
 */

public class AdapterRecyclerViewUsers extends RecyclerView.Adapter<AdapterRecyclerViewUsers.ViewHolder> {

    private List<User> mData;
    private OnItemClickListener mOnItemClickListener;

    AdapterRecyclerViewUsers(List<User> users, OnItemClickListener onItemClickListener) {
        mData = users;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_users_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(mData.get((int) v.getTag()));
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mData.get(position);
        holder.view.setTag(position);
        // TODO
        holder.textViewName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textViewName;
        TextView textViewEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textViewName = itemView.findViewById(R.id.TextView_Name_ItemUsers);
            textViewEmail = itemView.findViewById(R.id.TextView_Email_ItemUsers);
        }
    }
}
