package com.example.genius;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class RegisterFragment extends Fragment {
    View view;
    ProgressBar progressBar;
    EditText nameET;
    EditText passwordEt;
    EditText emailEt;
    EditText phoneEt;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    ImageButton hiddenBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.register_email);
        passwordEt = view.findViewById(R.id.register_password);
        nameET = view.findViewById(R.id.register_name);
        phoneEt = view.findViewById(R.id.register_phone);
        progressBar = view.findViewById(R.id.progressbar_register);
        progressBar.setVisibility(View.GONE);
        Button cancelBtn = view.findViewById(R.id.register_cancel_btn);

        hiddenBtn = view.findViewById(R.id.register_hidden_btn);
        hiddenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordVisible) {
                    // Hide the password
                    passwordEt.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    // Show the password
                    passwordEt.setTransformationMethod(null);
                }
                // Toggle the flag
                isPasswordVisible = !isPasswordVisible;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(v).popBackStack();
            }
        });
        Button signupBtn = view.findViewById(R.id.register_btn);
        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validate()==false)
                {
                    return;
                }
                mAuth.createUserWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    progressBar.setVisibility(View.VISIBLE);
                                    User user = new User();
                                    user.setName(nameET.getText().toString());
                                    user.setPassword(passwordEt.getText().toString());
                                    user.setEmail(emailEt.getText().toString());
                                    user.setPhone(phoneEt.getText().toString());
                                    nameET.setEnabled(false);
                                    passwordEt.setEnabled(false);
                                    emailEt.setEnabled(false);
                                    phoneEt.setEnabled(false);
                                    cancelBtn.setEnabled(false);
                                    // Sign up success, update UI with the signed-in user's information
                                    Model.instance.addUser(user,()->
                                    {
                                        Navigation.findNavController(v).popBackStack();
                                        Toast.makeText(getActivity(), "User " + user.name + " Added Successfully!", Toast.LENGTH_SHORT).show();
                                    });
                                } else {
                                    // If sign up fails, display a message to the user.
                                    Toast.makeText(getActivity(), "Register Failed!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
        return view;
    }

    private boolean validate()
    {
        if(nameET.getText().length() < 3)
        {
            Toast.makeText(getActivity(), "Name Invaild!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (passwordEt.getText().length() < 6)
        {
            Toast.makeText(getActivity(), "Password Invaild!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (emailEt.getText().length() < 3)
        {
            Toast.makeText(getActivity(), "Email Invaild!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (phoneEt.getText().length()!=10)
        {
            Toast.makeText(getActivity(), "Phone Invalid!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}