package com.example.soulscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.soulscribe.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currrentuser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        activitySignUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currrentuser = firebaseAuth.getCurrentUser();
                if(currrentuser!=null){
                    //user Already logged in
                }else {
                    //user Signed out

                }
            }
        };
        activitySignUpBinding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!TextUtils.isEmpty(activitySignUpBinding.emailidsignup.getText().toString())
               && !TextUtils.isEmpty(activitySignUpBinding.username.getText().toString())
               && !TextUtils.isEmpty(activitySignUpBinding.passwordsignup.getText().toString())){

                   String email = activitySignUpBinding.emailidsignup.getText().toString().trim();
                   String password = activitySignUpBinding.passwordsignup.getText().toString().trim();
                   String username = activitySignUpBinding.username.getText().toString().trim();

                   CreateUserEmailAccount(email,password,username);

               }else {
                   Toast.makeText(SignUpActivity.this,
                           "No Empty Fields Are Allowed",
                           Toast.LENGTH_SHORT).show();
               }
            }
        });

        activitySignUpBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void CreateUserEmailAccount(String email,String pass, String username){
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this,
                                "Account is Created Succesfully",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}