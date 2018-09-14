package com.abrarkotwal.facebookdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abrarkotwal.facebookdemo.Adapter.CommentsListAdapter;
import com.abrarkotwal.facebookdemo.Adapter.Pojo.Comment;
import com.abrarkotwal.facebookdemo.Adapter.Pojo.Post;
import com.abrarkotwal.facebookdemo.Adapter.PostListAdapter;
import com.abrarkotwal.facebookdemo.Other.CustomToast;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SinglePostCommentActivty extends AppCompatActivity {
    private ProgressBar loading;
    private RecyclerView commentListRecyclerView;
    private CommentsListAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Comment> commentList = new ArrayList<>();
    private EditText etComment;
    private ImageButton btnSend;
    private LoginSessionManager sessionManager;
    private String email,postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_comment_activty);

        initView();
    }

    private void initView() {
        postId      =  getIntent().getStringExtra("postId");
        etComment   =  findViewById(R.id.etComment);
        btnSend     =  findViewById(R.id.btnSend);
        loading     =  findViewById(R.id.progressBar);
        commentListRecyclerView =  findViewById(R.id.recyclerview);

        commentListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentListRecyclerView.setHasFixedSize(true);
        commentListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        databaseReference = FirebaseDatabase.getInstance().getReference("comments").child(postId);
        sessionManager  = new LoginSessionManager(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if (comment.length() == 0){
                    new CustomToast().Show_Toast(SinglePostCommentActivty.this, v,"Enter your comment");
                }else{
                    postComment(comment);
                }
            }
        });
    }

    private void postComment(String comment) {
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        email = userDetails.get(LoginSessionManager.KEY_EMAIL);

        String postId = databaseReference.push().getKey();
        Comment cmt = new Comment(comment,email);

        databaseReference.child(postId).setValue(cmt);
        Toast.makeText(this,"Comment posted Successfully",Toast.LENGTH_SHORT).show();
        etComment.setText("");

        if (commentList.size() != 0 ) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = postSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                loading.setVisibility(View.GONE);

                if (commentList.size() != 0){
                    adapter = new CommentsListAdapter(SinglePostCommentActivty.this, commentList);
                    commentListRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
            }
        });
    }
}
