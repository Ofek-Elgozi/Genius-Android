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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    final static int RESAULT_SUCCESS = 0;
    User user;
    View view;
    ImageView avatarImg;
    ProgressBar progressBar;
    Bitmap bitmap;

    public String temp_name=" ";
    public String temp_phone=" ";
    public String temp_group=" ";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
        user = EditUserProfileFragmentArgs.fromBundle(getArguments()).getUser();

        progressBar = view.findViewById(R.id.edit_userprofile_progressBar);
        progressBar.setVisibility(View.GONE);

        avatarImg = view.findViewById(R.id.edit_profile_pic);
        avatarImg.setImageResource(R.drawable.avatar);
        if(user.getAvatarUrl() != null)
        {
            Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.avatar).into(avatarImg);
        }

        EditText text_name = view.findViewById(R.id.edit_profile_name_et);
        text_name.setText(user.name);

        TextView text_email = view.findViewById(R.id.edit_profile_email_et);
        text_email.setText(user.email);

        EditText text_phone = view.findViewById(R.id.edit_profile_phone_et);
        text_phone.setText(user.phone);

        EditText text_group = view.findViewById(R.id.edit_profile_group_et);
        text_group.setText(user.group);

        ImageButton editImagebTn = view.findViewById(R.id.edit_picture_btn);

        Button saveBtn= view.findViewById(R.id.edit_save_btn);
        Button cancelBtn= view.findViewById(R.id.edit_cancel_btn);
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                cancelBtn.setEnabled(false);
                text_name.setEnabled(false);
                text_phone.setEnabled(false);
                text_group.setEnabled(false);
                editImagebTn.setEnabled(false);
                User updateduser = new User();
                temp_name=text_name.getText().toString();
                temp_phone=text_phone.getText().toString();
                temp_group=text_group.getText().toString();
                updateduser.setName(temp_name);
                updateduser.setPhone(temp_phone);
                updateduser.setGroup(temp_group);
                updateduser.setPassword(user.password);
                updateduser.setEmail(user.email);
                updateduser.setScore(user.score);
                if(bitmap != null)
                {

                    Model.instance.addUser(updateduser,()->
                    {
                        Model.instance.saveImage(bitmap, user.getEmail(), url ->
                        {
                            updateduser.setAvatarUrl(url);
                        });
                        Toast.makeText(getActivity(), "User Details successfully Edited", Toast.LENGTH_SHORT).show();
                        EditUserProfileFragmentDirections.ActionEditUserProfileFragmentToMainPageFragment action = EditUserProfileFragmentDirections.actionEditUserProfileFragmentToMainPageFragment(updateduser.getEmail());
                        Navigation.findNavController(v).navigate(action);
                    });
                }
                else
                {
                    Model.instance.addUser(updateduser,()->
                    {
                        Toast.makeText(getActivity(), "User Details successfully Edited", Toast.LENGTH_SHORT).show();
                        EditUserProfileFragmentDirections.ActionEditUserProfileFragmentToMainPageFragment action = EditUserProfileFragmentDirections.actionEditUserProfileFragmentToMainPageFragment(updateduser.getEmail());
                        Navigation.findNavController(v).navigate(action);
                    });
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                saveBtn.setEnabled(false);
                text_name.setEnabled(false);
                text_phone.setEnabled(false);
                text_group.setEnabled(false);
                editImagebTn.setEnabled(false);
                Navigation.findNavController(v).popBackStack();
            }
        });
        editImagebTn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });



        setHasOptionsMenu(true);
        return view;
    }

    public void selectImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your car picture");

        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                if (options[item].equals("Take Photo"))
                {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery"))
                {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_CANCELED)
        {
            switch (requestCode)
            {
                case 0://return from camera
                    if (resultCode == getActivity().RESULT_OK && data != null)
                    {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        avatarImg.setImageBitmap(bitmap);
                    }
                    break;
                case 1://return from gallery
                    if (resultCode == getActivity().RESULT_OK && data != null)
                    {
                        InputStream inputStream = null;
                        try
                        {
                            inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        } catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        avatarImg.setImageBitmap(bitmap);
                    }
                    break;
            }
        }
    }
}