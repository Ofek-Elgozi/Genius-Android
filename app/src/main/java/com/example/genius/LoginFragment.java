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
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    User u;
    View view;
    EditText emailEt;
    EditText passwordEt;
    ProgressBar login_progressBar;
    Button registerBtn;
    Button sign_inBtn;
    ImageButton hiddenBtn;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailEt = view.findViewById(R.id.login_email_txt);
        passwordEt = view.findViewById(R.id.login_password_txt);
        login_progressBar = view.findViewById(R.id.login_progressBar);
        login_progressBar.setVisibility(View.GONE);
        sign_inBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.not_reg_btn);
        hiddenBtn = view.findViewById(R.id.hidden_btn);
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

        sign_inBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(validate()==false)
                {
                    Toast.makeText(getActivity(), "Login Failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                validateUser(v);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(v).navigate(R.id.registerFragment);
            }
        });
        return view;
    }

    private boolean validate()
    {
        return (emailEt.getText().length() > 2 && passwordEt.getText().length() > 5);
    }

    private void validateUser(View v)
    {
        mAuth.signInWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser userAuth = mAuth.getCurrentUser();
                            Model.instance.getUserByEmail(userAuth.getEmail(), new Model.getUserByEmailListener()
                            {
                                @Override
                                public void onComplete(User user)
                                {
                                    registerBtn.setEnabled(false);
                                    passwordEt.setEnabled(false);
                                    emailEt.setEnabled(false);
                                    login_progressBar.setVisibility(View.VISIBLE);
                                    u = user;
                                    Toast.makeText(getActivity(), "Welcome To Genius!", Toast.LENGTH_SHORT).show();
                                    LoginFragmentDirections.ActionLoginFragmentToMainPageFragment action = LoginFragmentDirections.actionLoginFragmentToMainPageFragment(u.getEmail());
                                    Navigation.findNavController(v).navigate(action);
                                }
                            });
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            login_progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}