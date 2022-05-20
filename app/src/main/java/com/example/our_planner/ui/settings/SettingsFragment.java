package com.example.our_planner.ui.settings;

import static com.example.our_planner.LocaleLanguage.getLocale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.LocaleLanguage;
import com.example.our_planner.NavigationDrawer;
import com.example.our_planner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner themeSpinner;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private final int PICK_PHOTO_CODE = 1046;
    private Button changeProfilePictureBtn;
    private ImageView profilePicture, esPicture, enPicture;
    private TextView themeTxt, languageTxt;
    private PopupWindow pw;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        themeSpinner = view.findViewById(R.id.themeSpinner);

        profilePicture = view.findViewById(R.id.profilePicture);
        byte[] byteArray = DataBaseAdapter.getByteArray();
        profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

        changeProfilePictureBtn = view.findViewById(R.id.changeProfilePictureBtn);
        pw = new PopupWindow(inflater.inflate(R.layout.popup_profile_picture, null, false), 900, 400, true);

        themeTxt = view.findViewById(R.id.theme);
        languageTxt = view.findViewById(R.id.languageTxt);

        esPicture = view.findViewById(R.id.spanishImage);
        esPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("es");
                changeLanguage();
            }
        });
        enPicture = view.findViewById(R.id.englishImage);
        enPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("en");
                changeLanguage();
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
                        ((NavigationDrawer) getActivity()).updateProfilePicture();
                    }
                });
                Bitmap bitmapUpdated = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        changeLanguage();
    }

    private void changeLanguage() {
        Resources r = LocaleLanguage.getLocale(getContext()).getResources();

        NavigationDrawer n = (NavigationDrawer) getActivity();
        n.getSupportActionBar().setTitle(r.getString(R.string.settings));
        n.changeLanguage();

        changeProfilePictureBtn.setText(r.getString(R.string.change_profile_picture));
        themeTxt.setText(r.getString(R.string.theme));
        languageTxt.setText(r.getString(R.string.language));
        ArrayAdapter<String> adapterTheme = new ArrayAdapter<>(SettingsFragment.this.getActivity(), android.R.layout.simple_spinner_item, r.getStringArray(R.array.themeOptions));
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapterTheme);
        themeSpinner.setSelection(0, false);
        themeSpinner.setOnItemSelectedListener(this);

        changeProfilePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.showAtLocation(view.findViewById(R.id.changeProfilePictureBtn), Gravity.CENTER, 0, 0);

                ((TextView)pw.getContentView().findViewById(R.id.txtProfilePicture)).setText(r.getString(R.string.change_profile_picture));
                ((TextView)pw.getContentView().findViewById(R.id.txtPictureSource)).setText(r.getString(R.string.select_the_picture_source));

                Button cameraBtn = pw.getContentView().findViewById(R.id.cameraBtn);
                cameraBtn.setText(r.getString(R.string.camera));
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
                galleryBtn.setText(r.getString(R.string.gallery));
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
    }

    private void setLanguage(String language) {
        LocaleLanguage.setLocale(getContext(), language);
    }
}