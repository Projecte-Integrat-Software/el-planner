package com.example.our_planner.ui.groups;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.ViewHolderGroups> {

    private final ArrayList<Group> groups;
    private final Context context;

    public AdapterGroups(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_groups, parent, false);
        return new ViewHolderGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGroups holder, int position) {
        Group g = groups.get(position);
        holder.setData(g);
        Resources r = LocaleLanguage.getLocale(context).getResources();
        holder.editGroupBtn.setText(r.getString(R.string.edit));
        holder.editGroupBtn.setOnClickListener(view -> {
            Context c = view.getContext();
            Intent i = new Intent(c, EditGroupActivity.class);
            i.putExtra("group", g);
            c.startActivity(i);
        });
        holder.leaveGroupBtn.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_leave_group, null, false), 900, 500, true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);

            ((TextView)pw.getContentView().findViewById(R.id.txtLeaveGroup)).setText(r.getString(R.string.leave_group_description));
            ((TextView)pw.getContentView().findViewById(R.id.txtConfirmationLeaving)).setText(r.getString(R.string.confirmation_leaving));

            Button cancelBtn = pw.getContentView().findViewById(R.id.cancelBtn);
            cancelBtn.setText(r.getString(R.string.cancel));
            cancelBtn.setOnClickListener(view1 -> pw.dismiss());

            Button yesBtn = pw.getContentView().findViewById(R.id.yesBtn);
            yesBtn.setText(r.getString(R.string.yes));
            yesBtn.setOnClickListener(view1 -> {
                DataBaseAdapter.leaveGroup(g);
                pw.dismiss();
                groups.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            });
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolderGroups extends RecyclerView.ViewHolder {

        private final TextView titleGroupTxt;
        private final Button editGroupBtn;
        private final ImageButton leaveGroupBtn;

        public ViewHolderGroups(@NonNull View itemView) {
            super(itemView);
            titleGroupTxt = itemView.findViewById(R.id.titleGroup);
            editGroupBtn = itemView.findViewById(R.id.editGroupBtn);
            leaveGroupBtn = itemView.findViewById(R.id.leaveGroupBtn);
        }

        public void setData(Group group) {
            titleGroupTxt.setText(group.getTitle());
        }
    }
}
