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

import com.example.genius.Model.User;


public class LessonsFragment extends Fragment {
    View view;

    ImageButton lesson_1_btn;
    ImageButton lesson_2_btn;
    ImageButton lesson_3_btn;
    ImageButton lesson_4_btn;

    User u;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lessons, container, false);
        u = LessonsFragmentArgs.fromBundle(getArguments()).getUser();
        lesson_1_btn = view.findViewById(R.id.lesson1_btn);
        lesson_2_btn = view.findViewById(R.id.lesson2_btn);
        lesson_3_btn = view.findViewById(R.id.lesson3_btn);
        lesson_4_btn = view.findViewById(R.id.lesson4_btn);
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