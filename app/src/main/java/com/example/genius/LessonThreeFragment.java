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


public class LessonThreeFragment extends Fragment {
    View view;
    User user;
    private static final String TAG = "LessonTwoFragment";

    RadioButton radioButton20, radioButton21, radioButton22, radioButton23 ,radioButton24, radioButton25 , radioButton26
            , radioButton27, radioButton28 , radioButton29, radioButton30, radioButton31;

    Button lesson3_doneBtn;
    ProgressBar lesson3_pb;
    int currentScore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson_three, container, false);
        user = LessonThreeFragmentArgs.fromBundle(getArguments()).getUser();
        currentScore = Integer.parseInt(user.getScore());
        radioButton20 = view.findViewById(R.id.radioButton20);
        radioButton21 = view.findViewById(R.id.radioButton21);
        radioButton22 = view.findViewById(R.id.radioButton22);
        radioButton23 = view.findViewById(R.id.radioButton23);
        radioButton24 = view.findViewById(R.id.radioButton24);
        radioButton25 = view.findViewById(R.id.radioButton25);
        radioButton26 = view.findViewById(R.id.radioButton26);
        radioButton27 = view.findViewById(R.id.radioButton27);
        radioButton28 = view.findViewById(R.id.radioButton28);
        radioButton29 = view.findViewById(R.id.radioButton29);
        radioButton30 = view.findViewById(R.id.radioButton30);
        radioButton31 = view.findViewById(R.id.radioButton31);
        lesson3_doneBtn = view.findViewById(R.id.lesson3_done_btn);
        lesson3_pb = view.findViewById(R.id.lesson3_progressBar);
        lesson3_pb.setVisibility(View.GONE);

        TextView textView = view.findViewById(R.id.link_click_msg_3);
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
        RadioButton[] radioButtons = {radioButton20, radioButton21, radioButton22, radioButton23};
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
        RadioButton[] radioButtons2 = {radioButton24, radioButton25, radioButton26, radioButton27};
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
        RadioButton[] radioButtons3 = {radioButton28, radioButton29, radioButton30, radioButton31};
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

        lesson3_doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton20.isChecked() && radioButton26.isChecked() && radioButton29.isChecked()) {
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
                    lesson3_pb.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).popBackStack();
                    Log.d(TAG, "All answers are correct! Score: " + user.getScore());
                    Toast.makeText(getActivity(), "All answers are correct! Score: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton20.isChecked() == false)
                {
                    Log.d(TAG, "Question 1 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 1 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton26.isChecked() == false)
                {
                    Log.d(TAG, "Question 2 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 2 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                else if(radioButton29.isChecked() == false)
                {
                    Log.d(TAG, "Question 3 Incorrect! Score still: " + user.getScore());
                    Toast.makeText(getActivity(), "Question 3 Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
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