package com.example.genius;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;


public class LessonTwoFragment extends Fragment {
    private static final String TAG = "LessonTwoFragment";
    View view;
    User user;
    ImageButton nextpage_l2_p1;
    ProgressBar lesson2_pb_p1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson_two, container, false);
        user = LessonTwoFragmentArgs.fromBundle(getArguments()).getUser();
        nextpage_l2_p1 = view.findViewById(R.id.next_page_l2_p1);
        lesson2_pb_p1 = view.findViewById(R.id.lesson2_progressBar_p1);
        lesson2_pb_p1.setVisibility(View.GONE);

        nextpage_l2_p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    lesson2_pb_p1.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LessonTwoFragmentDirections.ActionLessonTwoFragmentToLessonTwoP2Fragment action = LessonTwoFragmentDirections.actionLessonTwoFragmentToLessonTwoP2Fragment(user);
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