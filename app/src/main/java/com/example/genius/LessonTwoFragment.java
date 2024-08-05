package com.example.genius;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
    RadioButton radioButton4, radioButton5, radioButton6, radioButton7 ,radioButton8, radioButton9 , radioButton10
            , radioButton11, radioButton12 , radioButton13, radioButton14, radioButton15
            , radioButton16, radioButton17, radioButton18, radioButton19;

    Button lesson2_doneBtn;
    ProgressBar lesson2_pb;
    int currentScore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson_two, container, false);
        user = LessonTwoFragmentArgs.fromBundle(getArguments()).getUser();
        currentScore = Integer.parseInt(user.getScore());
        radioButton4 = view.findViewById(R.id.radioButton4);
        radioButton5 = view.findViewById(R.id.radioButton5);
        radioButton6 = view.findViewById(R.id.radioButton6);
        radioButton7 = view.findViewById(R.id.radioButton7);
        radioButton8 = view.findViewById(R.id.radioButton8);
        radioButton9 = view.findViewById(R.id.radioButton9);
        radioButton10 = view.findViewById(R.id.radioButton10);
        radioButton11 = view.findViewById(R.id.radioButton11);
        radioButton12 = view.findViewById(R.id.radioButton12);
        radioButton13 = view.findViewById(R.id.radioButton13);
        radioButton14 = view.findViewById(R.id.radioButton14);
        radioButton15 = view.findViewById(R.id.radioButton15);
        radioButton16 = view.findViewById(R.id.radioButton16);
        radioButton17 = view.findViewById(R.id.radioButton17);
        radioButton18 = view.findViewById(R.id.radioButton18);
        radioButton19 = view.findViewById(R.id.radioButton19);
        lesson2_doneBtn = view.findViewById(R.id.lesson2_done_btn);
        lesson2_pb = view.findViewById(R.id.lesson2_progressBar);
        lesson2_pb.setVisibility(View.GONE);

        TextView textView = view.findViewById(R.id.link_click_msg);
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
        RadioButton[] radioButtons = {radioButton4, radioButton5, radioButton6, radioButton7};
        for (RadioButton rb : radioButtons) {
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RadioButton otherRb : radioButtons) {
                        if (otherRb != rb) {
                            otherRb.setChecked(false);
                        }
                    }
                }
            });
        }
        //Question 2
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
        //Question 3
        RadioButton[] radioButtons3 = {radioButton12, radioButton13, radioButton14, radioButton15};
        for (RadioButton rb : radioButtons3) {
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RadioButton otherRb : radioButtons3) {
                        if (otherRb != rb) {
                            otherRb.setChecked(false);
                        }
                    }
                }
            });
        }
        //Question 4
        RadioButton[] radioButtons4 = {radioButton16, radioButton17, radioButton18, radioButton19};
        for (RadioButton rb : radioButtons4) {
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RadioButton otherRb : radioButtons4) {
                        if (otherRb != rb) {
                            otherRb.setChecked(false);
                        }
                    }
                }
            });
        }

        lesson2_doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton5.isChecked() && radioButton8.isChecked() && radioButton14.isChecked() && radioButton17.isChecked()) {
                    if(currentScore < 20 && currentScore >=10) {
                        user.setScore(String.valueOf(currentScore + 10));
                        Model.instance.addUser(user, new Model.addUserListener() {
                            @Override
                            public void onComplete() {
                                // Handle the completion of the user update
                                Log.d(TAG, "User updated successfully");
                            }
                        });
                    }
                    lesson2_pb.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).popBackStack();
                    Log.d(TAG, "All answers are correct! Score: " + user.getScore());
                    Toast.makeText(getActivity(), "All answers are correct! Score: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton5.isChecked() == false)
                {
                    Log.d(TAG, "Question 1 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 1 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton8.isChecked() == false)
                {
                    Log.d(TAG, "Question 2 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 2 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton14.isChecked() == false)
                {
                    Log.d(TAG, "Question 3 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 3 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton17.isChecked() == false)
                {
                    Log.d(TAG, "Question 4 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 4 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
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