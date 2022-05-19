package com.example.our_planner.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;

import java.util.ArrayList;

public class AdapterCalendarFiles extends RecyclerView.Adapter<AdapterCalendarFiles.ViewHolder> {

    ArrayList<Uri> uris;
    Context context;
    RecyclerView fileList;

    public AdapterCalendarFiles(ArrayList<Uri> uris, RecyclerView fileList) {
        this.uris = uris;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_files, null, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = fileList.getChildLayoutPosition(view);
                Uri uri = uris.get(itemPosition);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(uris.get(position));
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName);
        }

        public void setData(Uri uri) {
            fileName.setText(DataBaseAdapter.getFileName(uri, context));
        }
    }
}
