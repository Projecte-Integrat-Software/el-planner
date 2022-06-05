package com.example.our_planner.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    ArrayList<ViewHolder> holders;
    EditEventActivity parent;

    public AdapterCalendarFiles(ArrayList<Uri> uris, RecyclerView fileList, EditEventActivity parent) {
        this.uris = uris;
        this.fileList = fileList;
        this.parent = parent;
        holders = new ArrayList<>();
    }

    public void updateParent(EditEventActivity parent) {
        this.parent = parent;
    }

    public void setVisibilityRemoveButton(int visibility) {
        for (ViewHolder holder : holders) {
            holder.setVisibilityRemoveButton(visibility);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_files, parent, false);
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
        holders.add(holder);
        if (parent.isEditing()) holder.setVisibilityRemoveButton(View.VISIBLE);
        else holder.setVisibilityRemoveButton(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Uri uri;
        TextView fileName;
        ImageButton deleteFileBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName);
            deleteFileBtn = itemView.findViewById(R.id.deleteFileBtn);
            deleteFileBtn.setVisibility(View.VISIBLE);

            deleteFileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uris.remove(uri);
                    parent.getViewModel().removeNewUri(uri);
                    parent.swapAdapter();
                }
            });
        }

        public void setData(Uri uri) {
            this.uri = uri;
            fileName.setText(DataBaseAdapter.getFileName(uri, context));
        }


        public void setVisibilityRemoveButton(int visibility) {
            deleteFileBtn.setVisibility(visibility);
        }
    }
}
