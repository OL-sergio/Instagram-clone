package udemy.java.instagram_clone.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.activity.CommentsActivity;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.helper.Constants;
import udemy.java.instagram_clone.model.Feed;
import udemy.java.instagram_clone.model.PostsLike;
import udemy.java.instagram_clone.model.User;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    Constants constants = new Constants();
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



        final Feed feed = feedList.get(position);
        final User userLogged = UserFirebase.getLoggedUserData();

        Uri uriPhotoUser     = Uri.parse( feed.getUserPhoto() );
        Uri uriPhotoPost    = Uri.parse( feed.getPhotoUrl() );

        Glide.with(context).load( uriPhotoUser ).into(holder.photoProfile);
        Glide.with(context).load( uriPhotoPost ).into(holder.photoPost);

        holder.nameProfile.setText( feed.getUserName() );
        holder.description.setText( feed.getPostDescription() );

        holder.viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("idPost", feed.getId());


                context.startActivity(intent);

            }
        });


        //Retrieved the liked posts
        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference()
                .child("liked-posts")
                .child( feed.getId() );
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalLikes = 0;
                if ( snapshot.hasChild("totalLiked" ) ) {

                    PostsLike postsLike =  snapshot.getValue( PostsLike.class );
                    assert postsLike != null;
                    totalLikes = postsLike.getTotalLiked();

                }

                if ( snapshot.hasChild ( userLogged.getUID() ) ) {

                    holder.likeButton.setLiked(true);

                }else {
                    holder.likeButton.setLiked(false);
                }

                final PostsLike liked = new PostsLike();
                liked.setFeed( feed );
                liked.setUser( userLogged );
                liked.setTotalLiked( totalLikes );

                holder.likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        // Log.i("likeButton", "liked");
                        liked.saveLikedPosts();
                        holder.totalLikes.setText( liked.getTotalLiked() + constants.liked );

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        //Log.i("likeButton", "unliked");
                        liked.removedTotalLiked();
                        holder.totalLikes.setText( liked.getTotalLiked() + constants.liked  );

                    }
                });

                holder.totalLikes.setText( liked.getTotalLiked() + constants.liked  );

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }


}
