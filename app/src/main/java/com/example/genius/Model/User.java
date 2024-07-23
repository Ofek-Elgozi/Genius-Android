package com.example.genius.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.genius.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User implements Parcelable
{
    public final static String LAST_UPDATED="LAST_UPDATED";
    @PrimaryKey
    @NonNull
    public String email;
    public String name;
    public String password;
    public String phone;
    public String score;
    Long lastUpdated= new Long(0);

    @NonNull
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public User(String name, String password, String email,String phone, String score)
    {
        this.name=name;
        this.password=password;
        this.email=email;
        this.phone=phone;
        this.score=score;
    }

    public User()
    {
        name=" ";
        password=" ";
        email=" ";
        phone=" ";
        score=" ";
    }
    public User(User u)
    {
        this.name=u.name;
        this.password=u.password;
        this.email=u.email;
        this.phone=u.phone;
        this.score=u.score;
    }

    public Map<String,Object> toJson()
    {
        Map<String, Object> json = new HashMap<>();
        json.put("name", getName());
        json.put("password", getPassword());
        json.put("email", getEmail());
        json.put("phone", getPhone());
        json.put("score", getScore());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    static User fromJson(Map<String, Object> json)
    {
        String email=(String)json.get("email");
        if(email==null)
            return null;
        String password=(String)json.get("password");
        String name=(String)json.get("name");
        String phone=(String)json.get("phone");
        String score=(String)json.get("score");
        User user = new User(name,password,email,phone, score);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        user.setLastUpdated(new Long(ts.getSeconds()));
        return user;
    }

    static long getLocalLastUpdated()
    {
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("USERS_LAST_UPDATE", 0 );
        Log.d("TAG","localLastUpdate: " + localLastUpdate);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date)
    {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("USERS_LAST_UPDATE",date);
        editor.commit();
        Log.d("TAG", "new lud" + date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeString(score);
        if (lastUpdated == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lastUpdated);
        }
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        password = in.readString();
        phone = in.readString();
        score = in.readString();
        if (in.readByte() == 0) {
            lastUpdated = null;
        } else {
            lastUpdated = in.readLong();
        }
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
