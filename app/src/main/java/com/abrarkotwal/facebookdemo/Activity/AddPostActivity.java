package com.abrarkotwal.facebookdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abrarkotwal.facebookdemo.Adapter.Pojo.Post;
import com.abrarkotwal.facebookdemo.Other.CustomToast;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {
    private EditText etTitle,etDescription,etImageUrl;
    private Button btnPost;
    private String title,description,imageUrl,email;
    private LoginSessionManager sessionManager;
    private DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initView();
    }

    public void initView(){
        etTitle         = findViewById(R.id.etTitle);
        etDescription   = findViewById(R.id.etDescription);
        etImageUrl      = findViewById(R.id.etImageUrl);
        btnPost         = findViewById(R.id.btnPost);
        sessionManager  = new LoginSessionManager(this);

        databaseArtists = FirebaseDatabase.getInstance().getReference("post");

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPost(v);
            }
        });
    }

    private void addNewPost(View v) {
        title       = etTitle.getText().toString();
        description = etDescription.getText().toString();
        imageUrl    = etImageUrl.getText().toString();

        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        email = userDetails.get(LoginSessionManager.KEY_EMAIL);

        if (title.length() == 0 || description.length() == 0
                || imageUrl.length() == 0){
            new CustomToast().Show_Toast(AddPostActivity.this, v,"All Field are mandatory");
        }
        else {
            String postId = databaseArtists.push().getKey();

            Post post = new Post(postId,title,description,imageUrl,email);

            databaseArtists.child(postId).setValue(post);

            Toast.makeText(this,"Post Added Successfully",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
