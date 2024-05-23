package com.example.soulscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.soulscribe.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        activityMainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogEmailPassUser(activityMainBinding.emailid.getText().toString().trim() , activityMainBinding.password.getText().toString().trim());
            }
        });

        activityMainBinding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , SignUpActivity.class);
                startActivity(i);
            }
        });

    }
    private void LogEmailPassUser(String email , String pass){
        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(pass)){
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    Intent i = new Intent(getApplicationContext(), Journal_list_Actiivty.class);
                    startActivity(i);
                }
            });
        }

    }
}
