package com.example.genius.Model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.genius.MyApplication;

import java.util.List;

public class Model
{
    public final static Model instance = new Model();
    ModelFireBase modelFireBase = new ModelFireBase();
    private Model()
    {
        reloadUserList();
    }

    public interface getAllUsersListener
    {
        void onComplete(List<User> user_data);
    }

    MutableLiveData<List<User>> userListLd = new MutableLiveData<List<User>>();

    public void reloadUserList()
    {
        //1.get local last update
        Long localLastUpdate = User.getLocalLastUpdated();
        //2.get all users record since local last update from firebase
        modelFireBase.getAllUsers(localLastUpdate, new getAllUsersListener()
        {
            @Override
            public void onComplete(List<User> user_data)
            {
                //3.update local last update date
                //4.add new records to the local db
                MyApplication.executorService.execute(()->
                {
                    Long lLastUpdate = new Long(0);
                    Log.d("TAG", "FB returned " + user_data.size());
                    for(User u: user_data)
                    {
                        AppLocalDB.db.userDao().insertAll(u);
                        if(u.getLastUpdated() > lLastUpdate)
                        {
                            lLastUpdate=u.getLastUpdated();
                        }
                    }
                    User.setLocalLastUpdated(lLastUpdate);
                    List<User> userList = AppLocalDB.db.userDao().getAllUsers();
                    userListLd.postValue(userList);
                });
            }
        });
    }

    public MutableLiveData<List<User>> getAllUsersData()
    {
        return userListLd;
    }

    public LiveData<List<User>> getUserBymail(String email)
    {
        return AppLocalDB.db.userDao().getUserByEmail(email);
    }

    public interface getUserByEmailListener
    {
        void onComplete(User user);
    }

    public void getUserByEmail(String email, getUserByEmailListener listener)
    {
        modelFireBase.getUserByEmail(email,listener);
    }

    public interface addUserListener
    {
        void onComplete();
    }

    public void addUser(User user, addUserListener listener)
    {
        modelFireBase.addUser(user, new addUserListener()
        {
            @Override
            public void onComplete()
            {
                reloadUserList();
                listener.onComplete();
            }
        });
    }

    public interface SaveImageListener
    {
        void onComplete(String url);
    }

    public void saveImage(Bitmap bitmap, String username, SaveImageListener listener)
    {
        modelFireBase.saveImage(bitmap,username, listener);
    }
}
