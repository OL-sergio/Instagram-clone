package udemy.java.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import udemy.java.instagram_clone.config.ConfigurationFirebase;

public class PostsLike {

    public Feed feed;
    public User user;

    public int totalLiked = 0;


    public PostsLike() {
    }

    public void saveLikedPosts(){

        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();

        //Object map update user
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("userName", user.getName() );
        userData.put("photoUrl", user.getUrlPhoto() );

        DatabaseReference postLiked = databaseReference
                .child("liked-posts")
                .child( feed.getId() )
                .child( user.getUID() );
        postLiked.setValue( userData );

        updatedTotalLiked( 1);
    }


    public void removedTotalLiked ( ){

        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();

        DatabaseReference postLiked = databaseReference
                .child("liked-posts")
                .child( feed.getId() )
                .child( user.getUID() );
        postLiked.removeValue();

        updatedTotalLiked(-1 );

    }


    public void updatedTotalLiked( int value  ) {
        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();

        DatabaseReference postLiked = databaseReference
                .child("liked-posts")
                .child( feed.getId() )
                .child( "totalLiked");
        setTotalLiked( getTotalLiked() + value );
        postLiked.setValue( getTotalLiked() );


    }

    public int getTotalLiked() {
        return totalLiked;
    }

    public void setTotalLiked(int totalLiked) {
        this.totalLiked = totalLiked;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
