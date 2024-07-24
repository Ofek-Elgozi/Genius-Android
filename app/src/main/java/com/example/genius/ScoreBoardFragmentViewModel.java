package com.example.genius;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;

import java.util.List;


public class ScoreBoardFragmentViewModel extends ViewModel {

    LiveData<List<User>> data = Model.instance.getAllUsersData();

    public LiveData<List<User>> getData()
    {
        return data;
    }
}