package com.example.genius;

import static android.app.Activity.RESULT_CANCELED;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditUserProfileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2; // Changed from 1 to 2 to avoid conflict
    static final int RESULT_SUCCESS = 0;
    User user;
    View view;
    ImageView avatarImg;
    ProgressBar progressBar;
    Bitmap bitmap;

    public String temp_name = " ";
    public String temp_phone = " ";
    public String temp_group = " ";
    public String temp_url = "Test";

    EditText text_name;
    TextView text_email;
    EditText text_phone;
    EditText text_group;
    private String originalText1;
    private String originalText2;
    private String originalText3;
    private String originalText4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
        user = EditUserProfileFragmentArgs.fromBundle(getArguments()).getUser();

        progressBar = view.findViewById(R.id.edit_userprofile_progressBar);
        progressBar.setVisibility(View.GONE);

        avatarImg = view.findViewById(R.id.edit_profile_pic);
        avatarImg.setImageResource(R.drawable.avatar);
        if (user.getAvatarUrl() != null) {
            Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.avatar).into(avatarImg);
        }

        text_name = view.findViewById(R.id.edit_profile_name_et);
        text_name.setText(user.getName());
        text_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText1 = text_name.getText().toString();
                    text_name.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (text_name.getText().toString().isEmpty()) {
                        text_name.setText(originalText1); // Restore original text if no changes
                    }
                }
            }
        });

        text_email = view.findViewById(R.id.edit_profile_email_et);
        text_email.setText(user.getEmail());
        text_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText2 = text_email.getText().toString();
                    text_email.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (text_email.getText().toString().isEmpty()) {
                        text_email.setText(originalText2); // Restore original text if no changes
                    }
                }
            }
        });

        text_phone = view.findViewById(R.id.edit_profile_phone_et);
        text_phone.setText(user.getPhone());
        text_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText3 = text_phone.getText().toString();
                    text_phone.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (text_phone.getText().toString().isEmpty()) {
                        text_phone.setText(originalText3); // Restore original text if no changes
                    }
                }
            }
        });

        text_group = view.findViewById(R.id.edit_profile_group_et);
        text_group.setText(user.getGroup());

        text_group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText4 = text_group.getText().toString();
                    text_group.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (text_group.getText().toString().isEmpty()) {
                        text_group.setText(originalText4); // Restore original text if no changes
                    }
                }
            }
        });

        ImageButton editImageBtn = view.findViewById(R.id.edit_picture_btn);

        Button saveBtn = view.findViewById(R.id.edit_save_btn);
        Button cancelBtn = view.findViewById(R.id.edit_cancel_btn);

        saveBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            cancelBtn.setEnabled(false);
            text_name.setEnabled(false);
            text_phone.setEnabled(false);
            text_group.setEnabled(false);
            editImageBtn.setEnabled(false);

            temp_name = text_name.getText().toString();
            temp_phone = text_phone.getText().toString();
            temp_group = text_group.getText().toString();

            if (bitmap != null) {
                Model.instance.saveImage(bitmap, user.getEmail(), url -> {
                    temp_url = url;
                    saveUser(temp_url);
                });
            } else {
                saveUser(user.getAvatarUrl());
            }
        });

        cancelBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            saveBtn.setEnabled(false);
            text_name.setEnabled(false);
            text_phone.setEnabled(false);
            text_group.setEnabled(false);
            editImageBtn.setEnabled(false);
            Navigation.findNavController(v).popBackStack();
        });

        editImageBtn.setOnClickListener(v -> selectImage());

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        text_name.setText(user.getName());
        text_email.setText(user.getEmail());
        text_phone.setText(user.getPhone());
        text_group.setText(user.getGroup());
    }

    private void saveUser(String avatarUrl) {
        User updatedUser = new User(temp_name, user.getPassword(), user.getEmail(), temp_phone, user.getScore(), temp_group, user.getIsTeacher());
        updatedUser.setAvatarUrl(avatarUrl);
        Model.instance.addUser(updatedUser, () -> {
            Toast.makeText(getActivity(), "User Details successfully Edited", Toast.LENGTH_SHORT).show();
            EditUserProfileFragmentDirections.ActionEditUserProfileFragmentToMainPageFragment action = EditUserProfileFragmentDirections.actionEditUserProfileFragmentToMainPageFragment(updatedUser.getEmail());
            Navigation.findNavController(view).navigate(action);
        });
    }

    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE: // return from camera
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        avatarImg.setImageBitmap(bitmap);
                    }
                    break;
                case REQUEST_IMAGE_PICK: // return from gallery
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            avatarImg.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}
