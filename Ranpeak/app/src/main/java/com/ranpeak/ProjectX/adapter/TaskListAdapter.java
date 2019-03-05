package com.ranpeak.ProjectX.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ranpeak.ProjectX.R;
import com.ranpeak.ProjectX.dto.TaskDTO;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.RemindViewHolder>{

    private List<TaskDTO> data;

    public TaskListAdapter(List<TaskDTO> data) {
        this.data = data;
    }

    @Override
    public RemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);

        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {
        TaskDTO item = data.get(position);
        holder.title.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<TaskDTO> data) {
        this.data = data;
    }

    public static class RemindViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;

        public RemindViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
