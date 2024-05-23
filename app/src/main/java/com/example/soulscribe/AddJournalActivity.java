package com.example.soulscribe;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.soulscribe.databinding.ActivityAddJournalBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {
    ActivityAddJournalBinding addJournalBinding;

    ActivityResultLauncher<String>mTakePhoto;
    Uri imageUri;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Journal");

    private StorageReference storageReference;

    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        addJournalBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_journal);
        addJournalBinding.postProgressBar.setVisibility(View.INVISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if(user!=null){
            currentUserId=user.getUid();
            currentUserName=user.getDisplayName();
        }
        addJournalBinding.postSaveJournalButton.setOnClickListener(v -> {
            SaveJournal();

        });

        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                addJournalBinding.postImageView.setImageURI(result);
                imageUri = result;
            }
        });

        addJournalBinding.postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhoto.launch("image/*");

            }
        });


    }

    private void SaveJournal() {
        String title = addJournalBinding.postTitleEt.getText().toString().trim();
        String thoughts = addJournalBinding.postDescriptionEt.getText().toString().trim();
        addJournalBinding.postProgressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri!=null){
            final  StorageReference filePath = storageReference.child("journal_images").child("my_image_"+ Timestamp.now().getSeconds());

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            Journal journal= new Journal();
                            journal.setTitle(title);
                            journal.setThoughts(thoughts);
                            journal.setImageUrl(imageUri);
                            journal.setTimeadded(new Timestamp(new Date()));
                            journal.setUserName(currentUserName);
                            journal.setUserID(currentUserId);

                            collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    addJournalBinding.postProgressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(AddJournalActivity.this, Journal_list_Actiivty.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddJournalActivity.this, "Failed :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddJournalActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            addJournalBinding.postProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }
}