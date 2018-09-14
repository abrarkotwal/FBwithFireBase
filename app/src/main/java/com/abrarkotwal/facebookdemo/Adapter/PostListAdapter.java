package com.abrarkotwal.facebookdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abrarkotwal.facebookdemo.Activity.SinglePostCommentActivty;
import com.abrarkotwal.facebookdemo.Activity.SinglePostDisplayActivity;
import com.abrarkotwal.facebookdemo.Adapter.Pojo.Comment;
import com.abrarkotwal.facebookdemo.Adapter.Pojo.FBLike;
import com.abrarkotwal.facebookdemo.Adapter.Pojo.Post;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewItemHolder> implements Filterable {

    private Context context;
    private List<Post> postList;
    private List<Post> mFilteredList;
    private DatabaseReference databaseReference;
    private LoginSessionManager sessionManager;

    public PostListAdapter(Context context, List<Post> postList) {
        this.context    = context;
        this.postList   = postList;
        mFilteredList   =postList;
        sessionManager  = new LoginSessionManager(context);
    }

    @Override
    public ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_single_post, parent, false);
        return new ViewItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewItemHolder holder, int position) {
        final Post post = postList.get(position);
        final String shareBody = post.getTitleName()+"\n"+post.getDescription();
        databaseReference = FirebaseDatabase.getInstance().getReference("likes").child(post.getPostId());

        Glide.with(context)
                .load(post.getImageLink())
                .into(holder.image);
        holder.title.setText(post.getTitleName());
        holder.description.setText(post.getDescription());
        holder.owner.setText(post.getPostOwner());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SinglePostDisplayActivity.class);
                intent.putParcelableArrayListExtra("postList", (ArrayList<? extends Parcelable>) postList);
                context.startActivity(intent);
            }
        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userDetails = sessionManager.getUserDetails();
                String email = userDetails.get(LoginSessionManager.KEY_EMAIL);

                String postId = databaseReference.push().getKey();
                FBLike like = new FBLike(email);

                databaseReference.child(postId).setValue(like);
                Toast.makeText(context,"You like this post",Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context,SinglePostCommentActivty.class);
                commentIntent.putExtra("postId",post.getPostId());
                context.startActivity(commentIntent);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Post");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(sharingIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewItemHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title,description,owner,btnLike,btnComment,btnShare;

        public ViewItemHolder(View itemView) {
            super(itemView);
            image       = itemView.findViewById(R.id.postImage);
            title       = itemView.findViewById(R.id.titleName);
            owner       = itemView.findViewById(R.id.ownerName);
            description = itemView.findViewById(R.id.description);
            btnLike     = itemView.findViewById(R.id.btnLike);
            btnComment  = itemView.findViewById(R.id.btnComment);
            btnShare    = itemView.findViewById(R.id.btnShare);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    postList = mFilteredList;
                } else {

                    List<Post> filteredList = new ArrayList<>();

                    for (Post categories : postList) {

                        if (categories.getTitleName().toLowerCase().contains(charString)
                                || categories.getDescription().toLowerCase().contains(charString)) {
                            filteredList.add(categories);
                        }
                    }

                    postList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = postList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                postList = (List<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}