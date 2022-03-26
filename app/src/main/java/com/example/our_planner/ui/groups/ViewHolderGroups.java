package com.example.our_planner.ui.groups;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;

public class ViewHolderGroups extends RecyclerView.ViewHolder {

    private final TextView nameGroupTxt;
    private final Button editGroupBtn;
    private final ImageButton leaveGroupBtn;

    public ViewHolderGroups(@NonNull View itemView) {
        super(itemView);
        nameGroupTxt = itemView.findViewById(R.id.nameGroup);
        editGroupBtn = itemView.findViewById(R.id.editGroupBtn);
        leaveGroupBtn = itemView.findViewById(R.id.leaveGroupBtn);
    }

    public void setData(Group group) {
        getNameGroup().setText(group.getName());
    }

    public TextView getNameGroup() {
        return nameGroupTxt;
    }

    public Button getEditGroupBtn() {
        return editGroupBtn;
    }

    public ImageButton getLeaveGroupBtn() {
        return leaveGroupBtn;
    }
}
