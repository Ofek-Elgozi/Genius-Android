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
    private String originalText1;
    private String originalText2;
    private String originalText3;
    private String originalText4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();


        emailEt = view.findViewById(R.id.register_email);
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

        passwordEt = view.findViewById(R.id.register_password);
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

        nameET = view.findViewById(R.id.register_name);
        nameET.setText("Name");
        nameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText3 = nameET.getText().toString();
                    nameET.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (nameET.getText().toString().isEmpty()) {
                        nameET.setText(originalText3); // Restore original text if no changes
                    }
                }
            }
        });

        phoneEt = view.findViewById(R.id.register_phone);
        phoneEt.setText("Phone");
        phoneEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    originalText4 = phoneEt.getText().toString();
                    phoneEt.setText(""); // Clear the text when EditText gains focus
                } else {
                    if (phoneEt.getText().toString().isEmpty()) {
                        phoneEt.setText(originalText4); // Restore original text if no changes
                    }
                }
            }
        });

        progressBar = view.findViewById(R.id.progressbar_register);
        progressBar.setVisibility(View.GONE);
        Button cancelBtn = view.findViewById(R.id.register_cancel_btn);

        hiddenBtn = view.findViewById(R.id.register_hidden_btn);
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

    @Override
    public void onResume() {
        super.onResume();
        emailEt.setText("Email");
        passwordEt.setText("Password");
        nameET.setText("Name");
        phoneEt.setText("Phone");
    }

    private boolean validate() {
        String name = nameET.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();

        // Validate Name
        if (name.isEmpty()) {
            nameET.setError("Name is required");
            Toast.makeText(getActivity(), "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.length() < 2) {
            nameET.setError("Name must be at least 2 characters");
            Toast.makeText(getActivity(), "Name must be at least 2 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.length() >= 15) {
            nameET.setError("Name must be less than or equal to 14 characters");
            Toast.makeText(getActivity(), "Name must be less than 14 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!name.matches("[a-zA-Z\\s]+")) {  // Check for letters and spaces only
            nameET.setError("Name must contain only letters");
            Toast.makeText(getActivity(), "Name must contain only letters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Email
        if (email.isEmpty()) {
            emailEt.setError("Email is required");
            Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            emailEt.setError("Invalid email format");
            Toast.makeText(getActivity(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Password
        if (password.isEmpty()) {
            passwordEt.setError("Password is required");
            Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            passwordEt.setError("Password must be at least 6 characters");
            Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Phone
        if (phone.isEmpty()) {
            phoneEt.setError("Phone number is required");
            Toast.makeText(getActivity(), "Phone number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() != 10) {
            phoneEt.setError("Phone number must be exactly 10 digits");
            Toast.makeText(getActivity(), "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phone.matches("\\d{10}")) {
            phoneEt.setError("Phone number must contain only digits");
            Toast.makeText(getActivity(), "Phone number must contain only digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }


}