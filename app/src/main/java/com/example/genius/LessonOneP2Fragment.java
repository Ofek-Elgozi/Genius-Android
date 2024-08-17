package com.example.genius;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.example.genius.Model.User;

public class LessonOneP2Fragment extends Fragment {
    private static final String TAG = "LessonOneP2Fragment";
    View view;
    User user;
    ProgressBar lesson1_pb_p2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson_one_p2, container, false);
        user = LessonOneP2FragmentArgs.fromBundle(getArguments()).getUser();
        ImageButton nextpage = view.findViewById(R.id.next_page_l1_p2);
        lesson1_pb_p2 = view.findViewById(R.id.lesson1_progressBar_p2);
        lesson1_pb_p2.setVisibility(View.GONE);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    lesson1_pb_p2.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LessonOneP2FragmentDirections.ActionLessonOneP2FragmentToLessonOneP3Fragment action = LessonOneP2FragmentDirections.actionLessonOneP2FragmentToLessonOneP3Fragment(user);
                            Navigation.findNavController(v).navigate(action);
                        }
                    }, 375);
                }
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.back_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.back_btn) {
            Navigation.findNavController(view).popBackStack();
        }
        return true;
    }
}