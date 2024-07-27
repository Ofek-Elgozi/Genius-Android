package com.example.genius;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
    UserProfileFragmentViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserProfileFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        user = UserProfileFragmentArgs.fromBundle(getArguments()).getUser();
        viewModel.setUser(user);

        progressBar = view.findViewById(R.id.userprofile_progressBar);
        progressBar.setVisibility(View.GONE);

        avatarImg = view.findViewById(R.id.profile_pic);
        avatarImg.setImageResource(R.drawable.avatar);

        TextView text_name = view.findViewById(R.id.profile_name_tv);
        TextView text_email = view.findViewById(R.id.profile_email_tv);
        TextView text_phone = view.findViewById(R.id.profile_phone_tv);
        TextView text_group = view.findViewById(R.id.profile_group_tv);

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), updatedUser -> {
            if (updatedUser != null) {
                user = updatedUser;
                if (user.getAvatarUrl() != null) {
                    Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.avatar).into(avatarImg);
                }
                text_name.setText(user.getName());
                text_email.setText(user.getEmail());
                text_phone.setText(user.getPhone());
                text_group.setText(user.getGroup());
            }
        });

        Button editBtn = view.findViewById(R.id.editprofile_btn);
        editBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            UserProfileFragmentDirections.ActionUserProfileFragmentToEditUserProfileFragment action = UserProfileFragmentDirections.actionUserProfileFragmentToEditUserProfileFragment(user);
            Navigation.findNavController(v).navigate(action);
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
        if (itemID == R.id.menu_home) {
            Navigation.findNavController(view).popBackStack();
        } else if (itemID == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(view).navigate(R.id.action_userProfileFragment_to_loginFragment);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh user data when fragment is resumed
        viewModel.refreshUser();
    }
}
