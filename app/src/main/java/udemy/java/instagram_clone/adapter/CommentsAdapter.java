package udemy.java.instagram_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.model.Comments;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private List<Comments> commentsList;
    private Context context;

    public CommentsAdapter(List<Comments> commentsList, Context context) {
        this.commentsList = commentsList;
        this.context = context;
    }

    public List<Comments> getCommentsList(){
        return this.commentsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userPhoto;
        TextView titleName, textComments;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.circleImageView_profileImageRowComments);
            titleName = itemView.findViewById(R.id.textView_userNameRowComments);
            textComments = itemView.findViewById(R.id.textView_commentRowComments);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View listView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_comments, parent, false);

        return new CommentsAdapter.MyViewHolder(listView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comments comments = commentsList.get(position);
        holder.titleName.setText( comments.getUserName() );
        holder.textComments.setText( comments.getComment() );

        if (comments.getPhotoUrl() != null ){

            Uri url = Uri.parse( comments.getPhotoUrl() );

            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.avatar)
                    .into(holder.userPhoto);

        }
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

}
