package udemy.java.instagram_clone.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;

public class Posts implements Serializable {

    private String id;
    private String idUser;
    private String postDescription;
    private String photoUrl;

    public Posts() {

        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();
        DatabaseReference postsRef = databaseReference.child("posts");
        String idPost = postsRef.push().getKey();
        setId(idPost);

    }


    // Fan-out to update multiple strings on the database
    public boolean savePost(DataSnapshot followersSnapshot ){

        Map object = new HashMap();
        User userLogged = UserFirebase.getLoggedUserData();

        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();


        //Post reference
        String combinationId = "/" + getIdUser() + "/" + getId();
        object.put( "/posts" + combinationId, this );



        for ( DataSnapshot followers: followersSnapshot.getChildren() ){

            /****
             *
             * feed
             *   id_followers < friend User >
             *      id_posts < id >
             *          posts< current user >
             */

            String idFollowers = followers.getKey();

            HashMap<String, Object> followerData = new HashMap<>();
            followerData.put("photoUrl", getPhotoUrl() );
            followerData.put("postDescription", getPostDescription());
            followerData.put("id", getId());

            followerData.put("userName", userLogged.getName() );
            followerData.put("userPhoto", userLogged.getUrlPhoto() );

            String idsUpdated = "/" + idFollowers + "/" + getId();
            object.put("/feed" + idsUpdated, followerData);

        }


        //object.put( "/feed" + combinationId, this );

        //object.put( "/highlights" + combinationId, this );

       /*
       * DatabaseReference postsRef = databaseReference.child("posts")
                .child(getIdUser())
                .child(getId());*/

       // postsRef.setValue(this);

        databaseReference.updateChildren(object);

        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
