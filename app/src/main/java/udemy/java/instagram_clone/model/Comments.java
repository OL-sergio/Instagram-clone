package udemy.java.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;

import udemy.java.instagram_clone.config.ConfigurationFirebase;

public class Comments {

    private String idComments;
    private String idPosts;
    private String idUser;
    private String photoUrl;
    private String userName;
    private String comment;

    public Comments() {
    }

    public boolean saveComment(){


        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference()
                .child("Comments")
                .child(getIdPosts());

        String keyComments = databaseReference.push().getKey();
        setIdComments(keyComments);
        databaseReference.child( getIdComments() ).setValue(this);

        return true;
    }

    public String getIdComments() {
        return idComments;
    }

    public void setIdComments(String idComments) {
        this.idComments = idComments;
    }

    public String getIdPosts() {
        return idPosts;
    }

    public void setIdPosts(String idPosts) {
        this.idPosts = idPosts;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
