package com.example.our_planner.ui.groups;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.R;
import com.example.our_planner.ThemeSwitcher;
import com.example.our_planner.model.User;

import java.util.ArrayList;
import java.util.Map;

public class AdapterParticipants extends RecyclerView.Adapter<AdapterParticipants.ViewHolderParticipants> {

    private final Context context;
    private final Map<String, User> participants;
    private final Map<String, Boolean> admins;
    private final ArrayList<String> users;
    private final boolean userAdmin;

    public AdapterParticipants(Context context, Map<String, User> participants, Map<String, Boolean> admins) {
        this.context = context;
        this.participants = participants;
        this.admins = admins;
        this.users = new ArrayList<>(participants.keySet());
        String e = DataBaseAdapter.getEmail();
        users.remove(e);
        userAdmin = admins.get(e);
    }

    @NonNull
    @Override
    public ViewHolderParticipants onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_participants, parent, false);
        return new ViewHolderParticipants(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderParticipants holder, int position) {
        Resources r = LocaleLanguage.getLocale(context).getResources();
        String e = users.get(position);
        holder.setData(participants.get(e).getUsername(), admins.get(e));
        if (userAdmin) {
            holder.adminCheckBox.setOnClickListener(view -> admins.replace(e, holder.adminCheckBox.isChecked()));
            holder.expelParticipantBtn.setOnClickListener(view -> {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_expel_user, null, false), 900, 600, true);
                pw.setBackgroundDrawable(context.getDrawable(ThemeSwitcher.lightThemeSelected() ? R.drawable.rounded_corners : R.drawable.rounded_corners_dark));
                pw.showAtLocation(view, Gravity.CENTER, 0, 0);

                ((TextView) pw.getContentView().findViewById(R.id.txtExpelUser)).setText(r.getString(R.string.expel_participant_description));
                ((TextView) pw.getContentView().findViewById(R.id.textConfirmationExpelling)).setText(r.getString(R.string.confirmation_expelling));

                Button cancelBtn = pw.getContentView().findViewById(R.id.cancelBtn);
                cancelBtn.setText(r.getString(R.string.cancel));
                cancelBtn.setOnClickListener(view1 -> pw.dismiss());

                Button yesBtn = pw.getContentView().findViewById(R.id.yesBtn);
                yesBtn.setText(r.getString(R.string.yes));
                yesBtn.setOnClickListener(view1 -> {
                    users.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    pw.dismiss();
                    participants.remove(e);
                    admins.remove(e);
                });
            });
        } else {
            holder.adminCheckBox.setClickable(false);
            holder.expelParticipantBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolderParticipants extends RecyclerView.ViewHolder {

        private final TextView participantTxt;
        private final CheckBox adminCheckBox;
        private final ImageButton expelParticipantBtn;

        public ViewHolderParticipants(@NonNull View itemView) {
            super(itemView);
            participantTxt = itemView.findViewById(R.id.txtParticipant);
            adminCheckBox = itemView.findViewById(R.id.cbAdmin);
            expelParticipantBtn = itemView.findViewById(R.id.expelParticipantBtn);
        }

        public void setData(String user, boolean b) {
            participantTxt.setText(user);
            adminCheckBox.setChecked(b);
        }
    }
}
