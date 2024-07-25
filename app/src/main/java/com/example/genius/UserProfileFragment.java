package com.example.genius;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genius.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class UserProfileFragment extends Fragment {
    User user;
    View view;
    ImageView avatarImg;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        user = UserProfileFragmentArgs.fromBundle(getArguments()).getUser();

        progressBar = view.findViewById(R.id.userprofile_progressBar);
        progressBar.setVisibility(View.GONE);

        avatarImg = view.findViewById(R.id.profile_pic);
        avatarImg.setImageResource(R.drawable.avatar);
        if(user.getAvatarUrl() != null)
        {
            Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.avatar).into(avatarImg);
        }

        TextView text_name = view.findViewById(R.id.profile_name_tv);
        text_name.setText(user.name);

        TextView text_email = view.findViewById(R.id.profile_email_tv);
        text_email.setText(user.email);

        TextView text_phone = view.findViewById(R.id.profile_phone_tv);
        text_phone.setText(user.phone);

        TextView text_group = view.findViewById(R.id.profile_group_tv);
        text_group.setText(user.group);

        Button editBtn= view.findViewById(R.id.editprofile_btn);
        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                UserProfileFragmentDirections.ActionUserProfileFragmentToEditUserProfileFragment action = UserProfileFragmentDirections.actionUserProfileFragmentToEditUserProfileFragment(user);
                Navigation.findNavController(v).navigate(action);
            }
        });

        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.homeorlogout_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.menu_home)
        {
            Navigation.findNavController(view).popBackStack();
        }
        else if (itemID == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_loginFragment);
        }
        return true;
    }
}