package com.abrarkotwal.facebookdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.abrarkotwal.facebookdemo.Adapter.Pojo.Comment;
import com.abrarkotwal.facebookdemo.Adapter.Pojo.Comment;
import com.abrarkotwal.facebookdemo.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewItemHolder> {

    private Context context;
    private List<Comment> commentList;

    public CommentsListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public CommentsListAdapter.ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_single_comment, parent, false);
        return new CommentsListAdapter.ViewItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentsListAdapter.ViewItemHolder holder, int position) {
        final Comment Comment = commentList.get(position);

        holder.comment.setText(Comment.getComment());
        holder.name.setText(Comment.getEmail());

    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewItemHolder extends RecyclerView.ViewHolder {
        public TextView comment, name;

        public ViewItemHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.tvComment);
            name = itemView.findViewById(R.id.tvEmail);
        }
    }
}
