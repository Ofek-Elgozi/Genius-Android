package com.example.genius;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FirstFragment extends Fragment {
    View view;

    ProgressBar FirstProgressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Button StartBtn = view.findViewById(R.id.start_button);
        FirstProgressBar = view.findViewById(R.id.first_progressBar);
        FirstProgressBar.setVisibility(View.GONE);
        StartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (user != null)
                {
                    FirstProgressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Welcome To Genius!", Toast.LENGTH_SHORT).show();
                    FirstFragmentDirections.ActionFirstFragmentToMainPageFragment action = FirstFragmentDirections.actionFirstFragmentToMainPageFragment(user.getEmail());
                    Navigation.findNavController(v).navigate(action);
                }
                else
                {
                    FirstProgressBar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(v).navigate(R.id.loginFragment);
                }
            }
        });
        return view;
    }
}