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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.example.genius.Model.User;


public class LessonTwoP2Fragment extends Fragment {
    private static final String TAG = "LessonTwoP2Fragment";
    View view;
    User user;
    ImageButton nextpage_l2_p2;
    ProgressBar lesson2_pb_p2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson_two_p2, container, false);
        user = LessonTwoP2FragmentArgs.fromBundle(getArguments()).getUser();
        nextpage_l2_p2 = view.findViewById(R.id.next_page_l2_p2);
        lesson2_pb_p2 = view.findViewById(R.id.lesson2_progressBar_p2);
        lesson2_pb_p2.setVisibility(View.GONE);

        nextpage_l2_p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    lesson2_pb_p2.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LessonTwoP2FragmentDirections.ActionLessonTwoP2FragmentToLessonTwoP3Fragment action = LessonTwoP2FragmentDirections.actionLessonTwoP2FragmentToLessonTwoP3Fragment(user);
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