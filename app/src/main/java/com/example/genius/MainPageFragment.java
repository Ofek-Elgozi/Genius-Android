package com.example.genius;

import static com.example.genius.R.id.menu_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.genius.Model.Model;
import com.example.genius.Model.User;


public class MainPageFragment extends Fragment {
    User updateduser;
    String temp_email;
    View view;
    ImageButton scoreboard_btn;
    ProgressBar MainPage_progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_page, container, false);
        temp_email = MainPageFragmentArgs.fromBundle(getArguments()).getEmail();
        MainPage_progressBar = view.findViewById(R.id.mainpage_progressBar);
        MainPage_progressBar.setVisibility(View.GONE);
        Model.instance.getUserByEmail(temp_email, new Model.getUserByEmailListener() {
            @Override
            public void onComplete(User user)
            {
                updateduser = user;
            }
        });
        scoreboard_btn = view.findViewById(R.id.scoreboard_btn);
        scoreboard_btn.setOnClickListener(v -> {
            MainPage_progressBar.setVisibility(View.VISIBLE);
            if(updateduser != null)
            {
                MainPageFragmentDirections.ActionMainPageFragmentToScoreBoardFragment action = MainPageFragmentDirections.actionMainPageFragmentToScoreBoardFragment(updateduser);
                Navigation.findNavController(v).navigate(action);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toprofile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.menu_profile && updateduser!=null)
        {
            MainPageFragmentDirections.ActionMainPageFragmentToUserProfileFragment action = MainPageFragmentDirections.actionMainPageFragmentToUserProfileFragment(updateduser);
            Navigation.findNavController(view).navigate(action);
        }
        return true;
    }
}
