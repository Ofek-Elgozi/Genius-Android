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


public class LessonTwoP3Fragment extends Fragment {

    private static final String TAG = "LessonTwoP3Fragment";
    View view;
    User user;
    RadioButton radioButton8, radioButton9 , radioButton10, radioButton11;
    ProgressBar lesson2_pb_p3;
    ImageButton lesson2_nextpage_p3;
    int currentScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_lesson_two_p3, container, false);
        user = LessonTwoP3FragmentArgs.fromBundle(getArguments()).getUser();
        currentScore = Integer.parseInt(user.getScore());
        radioButton8 = view.findViewById(R.id.radioButton8);
        radioButton9 = view.findViewById(R.id.radioButton9);
        radioButton10 = view.findViewById(R.id.radioButton10);
        radioButton11 = view.findViewById(R.id.radioButton11);
        lesson2_nextpage_p3 = view.findViewById(R.id.next_page_l2_p3);
        lesson2_pb_p3 = view.findViewById(R.id.lesson2_progressBar_p3);
        lesson2_pb_p3.setVisibility(View.GONE);
        TextView textView = view.findViewById(R.id.link_click_msg_l2_p3);
        String text = "Click here and try running those questions alone to see how it works!";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.programiz.com/c-programming/online-compiler/"));
                startActivity(browserIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
                ds.setColor(textView.getTextColors().getDefaultColor()); // Set link color to match text color
            }
        };

        spannableString.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT); // Remove highlight effect when clicked

        //Question 1
        RadioButton[] radioButtons2 = {radioButton8, radioButton9, radioButton10, radioButton11};
        for (RadioButton rb : radioButtons2) {
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RadioButton otherRb : radioButtons2) {
                        if (otherRb != rb) {
                            otherRb.setChecked(false);
                        }
                    }
                }
            });
        }

        lesson2_nextpage_p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton8.isChecked()) {
                    if(currentScore < 13 && currentScore >=10) {
                        user.setScore(String.valueOf(currentScore + 3));
                        Model.instance.addUser(user, new Model.addUserListener() {
                            @Override
                            public void onComplete() {
                                // Handle the completion of the user update
                                Log.d(TAG, "User updated successfully");
                            }
                        });
                    }
                    if(user != null) {
                        lesson2_pb_p3.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LessonTwoP3FragmentDirections.ActionLessonTwoP3FragmentToLessonTwoP4Fragment action = LessonTwoP3FragmentDirections.actionLessonTwoP3FragmentToLessonTwoP4Fragment(user);
                                Navigation.findNavController(v).navigate(action);
                            }
                        }, 375);
                    }
                    Log.d(TAG, "Answer is correct! Score: " + user.getScore());
                    Toast.makeText(getActivity(), "Answer is correct! Score: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton8.isChecked() == false)
                {
                    Log.d(TAG, "Answer Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Answer Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
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