package com.example.genius;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
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

    Button forgetpassword_btn;
    ImageButton hiddenBtn;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    private String originalText1;
    private String originalText2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();

        emailEt = view.findViewById(R.id.login_email_txt);
        emailEt.setText("Email");
        emailEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText1 = emailEt.getText().toString();
                    emailEt.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (emailEt.getText().toString().isEmpty()) {
                        emailEt.setText(originalText1); // Restore original text if no changes
                    }
                }
            }
        });

        passwordEt = view.findViewById(R.id.login_password_txt);
        passwordEt.setText("Password");
        passwordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText2 = passwordEt.getText().toString();
                    passwordEt.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (passwordEt.getText().toString().isEmpty()) {
                        passwordEt.setText(originalText2); // Restore original text if no changes
                    }
                }
            }
        });

        login_progressBar = view.findViewById(R.id.login_progressBar);
        login_progressBar.setVisibility(View.GONE);

        forgetpassword_btn = view.findViewById(R.id.forgetpass_btn);
        forgetpassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog to prompt the user for their email address
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter your email address");

                // Set up the input
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailAddress = input.getText().toString();
                        sendPasswordResetEmail(emailAddress);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        sign_inBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.not_reg_btn);
        hiddenBtn = view.findViewById(R.id.hidden_btn);
        hiddenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
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

    private void sendPasswordResetEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        emailEt.setText("Email");
        passwordEt.setText("Password");
    }

    private boolean validate() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            emailEt.setError("Email is required");
            Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            emailEt.setError("Invalid email format");
            Toast.makeText(getActivity(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordEt.setError("Password is required");
            Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            passwordEt.setError("Password must be at least 6 characters");
            Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Validation passed
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