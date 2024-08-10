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


public class LessonFourFragment extends Fragment {
    View view;

    User user;
    private static final String TAG = "LessonFourFragment";

    RadioButton radioButton32, radioButton33, radioButton34, radioButton35 ,radioButton36, radioButton37 , radioButton38
            , radioButton39;

    Button lesson4_doneBtn;
    ProgressBar lesson4_pb;
    int currentScore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson_four, container, false);
        user = LessonFourFragmentArgs.fromBundle(getArguments()).getUser();
        currentScore = Integer.parseInt(user.getScore());
        radioButton32 = view.findViewById(R.id.radioButton32);
        radioButton33 = view.findViewById(R.id.radioButton33);
        radioButton34 = view.findViewById(R.id.radioButton34);
        radioButton35 = view.findViewById(R.id.radioButton35);
        radioButton36 = view.findViewById(R.id.radioButton36);
        radioButton37 = view.findViewById(R.id.radioButton37);
        radioButton38 = view.findViewById(R.id.radioButton38);
        radioButton39 = view.findViewById(R.id.radioButton39);
        lesson4_doneBtn = view.findViewById(R.id.lesson4_done_btn);
        lesson4_pb = view.findViewById(R.id.lesson4_progressBar);
        lesson4_pb.setVisibility(View.GONE);

        TextView textView = view.findViewById(R.id.link_click_msg_4);
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
        RadioButton[] radioButtons = {radioButton32, radioButton33, radioButton34, radioButton35};
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
        RadioButton[] radioButtons2 = {radioButton36, radioButton37, radioButton38, radioButton39};
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

        lesson4_doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton32.isChecked() && radioButton37.isChecked()) {
                    if(currentScore < 40 && currentScore >=30) {
                        user.setScore(String.valueOf(currentScore + 10));
                        Model.instance.addUser(user, new Model.addUserListener() {
                            @Override
                            public void onComplete() {
                                // Handle the completion of the user update
                                Log.d(TAG, "User updated successfully");
                            }
                        });
                    }
                    lesson4_pb.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).popBackStack();
                    Log.d(TAG, "All answers are correct! Score: " + user.getScore());
                    Toast.makeText(getActivity(), "All answers are correct! Score: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton32.isChecked() == false)
                {
                    Log.d(TAG, "Question 1 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 1 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton37.isChecked() == false)
                {
                    Log.d(TAG, "Question 2 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 2 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
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