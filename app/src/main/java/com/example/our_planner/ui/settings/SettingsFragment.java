package com.example.our_planner.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.NavigationDrawer;
import com.example.our_planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner notificationsAlertSpinner, themeSpinner;
    private Button changeProfilePictureBtn;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private final int PICK_PHOTO_CODE = 1046;
    private ImageView profilePicture;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        notificationsAlertSpinner = view.findViewById(R.id.notificationsAlertSpinner);
        ArrayAdapter<String> adapterNotificationsAlert = new ArrayAdapter<>(SettingsFragment.this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.notificationsAlterOptions));
        adapterNotificationsAlert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationsAlertSpinner.setAdapter(adapterNotificationsAlert);
        notificationsAlertSpinner.setSelection(0,false);
        notificationsAlertSpinner.setOnItemSelectedListener(this);

        themeSpinner = view.findViewById(R.id.themeSpinner);
        ArrayAdapter<String> adapterTheme = new ArrayAdapter<>(SettingsFragment.this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.themeOptions));
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapterTheme);
        themeSpinner.setSelection(0,false);
        themeSpinner.setOnItemSelectedListener(this);

        profilePicture = view.findViewById(R.id.profilePicture);
        byte[] byteArray = DataBaseAdapter.getByteArray();
        profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.length));

        changeProfilePictureBtn = view.findViewById(R.id.changeProfilePictureBtn);
        changeProfilePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_profile_picture, null, false), 900, 400, true);
                pw.showAtLocation(view.findViewById(R.id.changeProfilePictureBtn), Gravity.CENTER, 0, 0);

                Button cameraBtn = pw.getContentView().findViewById(R.id.cameraBtn);
                cameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,
                                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        pw.dismiss();
                    }
                });

                Button galleryBtn = pw.getContentView().findViewById(R.id.galleryBtn);
                galleryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_PHOTO_CODE);
                        pw.dismiss();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == PICK_PHOTO_CODE) {
                Bitmap bitmap = null;
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    Uri photoUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                UploadTask uploadTask = DataBaseAdapter.setProfilePicture(byteArray);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        ((NavigationDrawer)getActivity()).updateProfilePicture();
                    }
                });
                Bitmap bitmapUpdated = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                profilePicture.setImageBitmap(bitmapUpdated);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}