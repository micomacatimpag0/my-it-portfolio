package com.example.mobilecomp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<ActivityHistory> activityHistoryList;

    public HistoryAdapter(List<ActivityHistory> activityHistoryList) {
        this.activityHistoryList = activityHistoryList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        ActivityHistory history = activityHistoryList.get(position);
        holder.activityName.setText(history.getActivityName());
        holder.status.setText(history.getStatus());
        holder.date.setText(history.getDate());
    }

    @Override
    public int getItemCount() {
        return activityHistoryList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView activityName;
        public TextView status;
        public TextView date;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityName);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
        }
    }
}
