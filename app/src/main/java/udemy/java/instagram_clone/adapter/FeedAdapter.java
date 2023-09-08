package udemy.java.instagram_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.model.Feed;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private List<Feed> feedList;
    private Context context;

    public FeedAdapter(List<Feed> feedList, Context context) {
        this.feedList = feedList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photoProfile;
        TextView nameProfile, description, totalLikes;
        ImageView photoPost, viewComment;
        LikeButton likeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photoProfile= itemView.findViewById(R.id.circleImageView_fullImageView_profileImage);
            nameProfile = itemView.findViewById(R.id.textView_fullImageView_profileName);
            photoPost   = itemView.findViewById(R.id.imageView_fullImageView_selectedPhoto);
            viewComment = itemView.findViewById(R.id.imageView_fullImageView_comments);
            likeButton  = itemView.findViewById(R.id.buttonLike_fullImageView_likeFeed);
            totalLikes  = itemView.findViewById(R.id.textView_fullImageView_totalLikes);
            description = itemView.findViewById(R.id.textView_fullImageView_descriptions);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_feed_post, parent, false);

        return new MyViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Feed feed = feedList.get(position);

        Uri uriPhotoUser     = Uri.parse( feed.getUserPhoto() );
        Uri uriPhotoPost    = Uri.parse( feed.getPhotoUrl() );

        Glide.with(context).load( uriPhotoUser ).into(holder.photoProfile);
        Glide.with(context).load( uriPhotoPost ).into(holder.photoPost);

        holder.nameProfile.setText( feed.getUserName() );
        holder.description.setText( feed.getPostDescription() );


    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }


}
