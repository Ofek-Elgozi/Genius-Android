package com.example.genius;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;

import java.util.List;

public class UserProfileFragmentViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private LiveData<List<User>> allUsersData = Model.instance.getAllUsersData();

    User user;

    public LiveData<List<User>> getAllUsersData() {
        return allUsersData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void setUser(User user) {
        this.user = user;
        refreshUser();
    }

    public void refreshUser() {
        Model.instance.getUserByEmail(user.getEmail(), updatedUser -> userLiveData.postValue(updatedUser));
    }
}
