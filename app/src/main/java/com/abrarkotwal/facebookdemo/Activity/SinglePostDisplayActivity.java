package com.abrarkotwal.facebookdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abrarkotwal.facebookdemo.Adapter.Pojo.Post;
import com.abrarkotwal.facebookdemo.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SinglePostDisplayActivity extends AppCompatActivity {
    private List<Post> postList = new ArrayList<>();
    private TextView title,description,owner;
    private ImageView image;
    private FloatingActionButton btnShare;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_display);

        initView();
        displayData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        title       = findViewById(R.id.singleTitle);
        description = findViewById(R.id.singleDescription);
        image       = findViewById(R.id.singleImage);
        owner       = findViewById(R.id.singleOwner);

        btnShare    = findViewById(R.id.btnSharePost);

        postList = getIntent().getParcelableArrayListExtra("postList");
    }

    private void displayData() {
        Post post = postList.get(0);

        toolbar.setTitle(post.getTitleName());
        title.setText(post.getTitleName());
        owner.setText(post.getPostOwner());
        description.setText(post.getDescription());
        Glide.with(this)
                .load(post.getImageLink())
                .into(image);

        final String shareBody = post.getTitleName()+"\n"+post.getDescription();
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Post");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(sharingIntent);

            }
        });
    }
}
