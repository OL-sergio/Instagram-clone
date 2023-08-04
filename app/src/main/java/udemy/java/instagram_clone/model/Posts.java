package udemy.java.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;

import udemy.java.instagram_clone.config.ConfigurationFirebase;

public class Posts {

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


    public boolean savePost(){

        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();
        DatabaseReference postsRef = databaseReference.child("posts")
                .child(getIdUser())
                .child(getId());
        postsRef.setValue(this);

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
