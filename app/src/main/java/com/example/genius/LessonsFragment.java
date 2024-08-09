package com.example.genius;

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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;


public class LessonsFragment extends Fragment {
    View view;

    ImageButton lesson_1_btn;
    ImageButton lesson_2_btn;
    ImageButton lesson_3_btn;
    ImageButton lesson_4_btn;

    ProgressBar my_lessons_progressBar;
    private String scoreString;
    private int score;
    User u;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lessons, container, false);
        u = LessonsFragmentArgs.fromBundle(getArguments()).getUser();
        scoreString = u.getScore();
        my_lessons_progressBar = view.findViewById(R.id.mylessons_progressBar);
        my_lessons_progressBar.setVisibility(View.GONE);

        // Check if the user is a teacher and update the score to 40 if it is less than 40
        if (u.getIsTeacher().equals("1") && Integer.parseInt(u.getScore()) < 40) {
            u.setScore("40");
            Model.instance.addUser(u, new Model.addUserListener() {
                @Override
                public void onComplete() {
                    // Optionally, handle completion of the user update here
                    Toast.makeText(getActivity(), "Teacher privileges granted: All lessons unlocked.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        lesson_1_btn = view.findViewById(R.id.lesson1_btn);
        lesson_1_btn.setOnClickListener(v -> {
            if(u != null) {
                my_lessons_progressBar.setVisibility(View.VISIBLE);
                LessonsFragmentDirections.ActionLessonsFragmentToLessonOneFragment action = LessonsFragmentDirections.actionLessonsFragmentToLessonOneFragment(u);
                Navigation.findNavController(v).navigate(action);
            }
        });

        lesson_2_btn = view.findViewById(R.id.lesson2_btn);
        lesson_2_btn.setOnClickListener(v -> {
            score = Integer.parseInt(u.getScore());
            if(u != null && score >= 10) {
                my_lessons_progressBar.setVisibility(View.VISIBLE);
                LessonsFragmentDirections.ActionLessonsFragmentToLessonTwoFragment action = LessonsFragmentDirections.actionLessonsFragmentToLessonTwoFragment(u);
                Navigation.findNavController(v).navigate(action);
            } else if(u != null && score < 10) {
                Toast.makeText(getActivity(), "All previous lessons must be successfully done first.", Toast.LENGTH_SHORT).show();
            }
        });

        lesson_3_btn = view.findViewById(R.id.lesson3_btn);
        lesson_3_btn.setOnClickListener(v -> {
            score = Integer.parseInt(u.getScore());
            if(u != null && score >= 20) {
                my_lessons_progressBar.setVisibility(View.VISIBLE);
                LessonsFragmentDirections.ActionLessonsFragmentToLessonThreeFragment action = LessonsFragmentDirections.actionLessonsFragmentToLessonThreeFragment(u);
                Navigation.findNavController(v).navigate(action);
            } else if(u != null && score < 20) {
                Toast.makeText(getActivity(), "All previous lessons must be successfully done first.", Toast.LENGTH_SHORT).show();
            }
        });

        lesson_4_btn = view.findViewById(R.id.lesson4_btn);
        lesson_4_btn.setOnClickListener(v -> {
            score = Integer.parseInt(u.getScore());
            if(u != null && score >= 30) {
                my_lessons_progressBar.setVisibility(View.VISIBLE);
                LessonsFragmentDirections.ActionLessonsFragmentToLessonFourFragment action = LessonsFragmentDirections.actionLessonsFragmentToLessonFourFragment(u);
                Navigation.findNavController(v).navigate(action);
            } else if(u != null && score < 30) {
                Toast.makeText(getActivity(), "All previous lessons must be successfully done first.", Toast.LENGTH_SHORT).show();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gethomepage_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.home_menu) {
            Navigation.findNavController(view).popBackStack();
        }
        return true;
    }
}
