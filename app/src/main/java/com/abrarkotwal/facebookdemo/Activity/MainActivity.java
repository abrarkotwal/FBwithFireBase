package com.abrarkotwal.facebookdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.abrarkotwal.facebookdemo.Adapter.Pojo.Post;
import com.abrarkotwal.facebookdemo.Adapter.PostListAdapter;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.AlertDialogManager;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loading;
    private RecyclerView postListRecyclerView;
    private ImageView notFound;
    private PostListAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Post> postList = new ArrayList<>();
    private FloatingActionButton addPost;
    private LoginSessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new LoginSessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            finish();
            sessionManager.checkUserLogin();
        }

        initView();
    }

    private void initView() {
        loading     =  findViewById(R.id.progressBar);
        notFound    =  findViewById(R.id.notFound);
        addPost     = findViewById(R.id.addPost);
        postListRecyclerView =  findViewById(R.id.recyclerview);

        postListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postListRecyclerView.setHasFixedSize(true);
        postListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        databaseReference = FirebaseDatabase.getInstance().getReference("post");

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddPostActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                loading.setVisibility(View.GONE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                if (postList.size() == 0){
                    notFound.setVisibility(View.VISIBLE);
                }else {
                    adapter = new PostListAdapter(MainActivity.this, postList);
                    postListRecyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.logout:
                sessionManager.userLogout();
                finish();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialogManager.backPressed(this);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}