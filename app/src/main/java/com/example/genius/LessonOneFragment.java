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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;

public class LessonOneFragment extends Fragment {
    View view;
    User user;
    CheckBox checkBox, checkBox1, checkBox2, checkBox3;
    Button doneBtn;

    ProgressBar lesson1_pb;

    int currentScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lession_one, container, false);
        user = LessonOneFragmentArgs.fromBundle(getArguments()).getUser();
        currentScore = Integer.parseInt(user.getScore());
        checkBox = view.findViewById(R.id.checkBox);
        checkBox1 = view.findViewById(R.id.checkBox1);
        checkBox2 = view.findViewById(R.id.checkBox2);
        checkBox3 = view.findViewById(R.id.checkBox3);
        doneBtn = view.findViewById(R.id.done_btn);
        lesson1_pb = view.findViewById(R.id.lesson1_progressBar);
        lesson1_pb.setVisibility(View.GONE);

        TextView textView = view.findViewById(R.id.textView46);
        String text = "Click me and try running this code alone to see how it works!";
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

        // Set up CheckBox logic
        CheckBox[] checkBoxes = {checkBox, checkBox1, checkBox2, checkBox3};
        for (CheckBox cb : checkBoxes) {
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (CheckBox otherCb : checkBoxes) {
                            if (otherCb != buttonView) {
                                otherCb.setEnabled(false);
                            }
                        }
                    } else {
                        for (CheckBox otherCb : checkBoxes) {
                            otherCb.setEnabled(true);
                        }
                    }
                }
            });
        }

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if(currentScore <10)
                    {
                        user.setScore(String.valueOf(currentScore + 10));
                    }
                    lesson1_pb.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).popBackStack();
                    Toast.makeText(getActivity(), "Correct! Score: " + user.getScore(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Incorrect! Score still: " + user.getScore(), Toast.LENGTH_SHORT).show();
                }
                // Here you should update the user object in your database
                Model.instance.addUser(user, new Model.addUserListener() {
                    @Override
                    public void onComplete() {
                        // Handle the completion of the user update
                    }
                });
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
