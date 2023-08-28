package udemy.java.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import udemy.java.instagram_clone.config.ConfigurationFirebase;

public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private String password;
    private String urlPhoto;
    private int totalFollowers = 0;
    private int totalFollow = 0;
    private int totalPosts = 0;

    public User() {
    }


    public void saveUser(){
        DatabaseReference databaseReference = ConfigurationFirebase.getDatabaseReference();
        DatabaseReference users = databaseReference
                .child("users")
                .child(getUID());
                users.setValue(this);
    }

    public void updateUser(){

        DatabaseReference firebaseReference = ConfigurationFirebase.getDatabaseReference();
        DatabaseReference usersReference = firebaseReference
                .child("users")
                .child(getUID());

        Map<String, Object> userValues = convertToMAp();
        usersReference.updateChildren(userValues);

    }
    public void updateTotalPost(){

        DatabaseReference firebaseReference = ConfigurationFirebase.getDatabaseReference();
        DatabaseReference usersReference = firebaseReference
                .child("users")
                .child(getUID());

        HashMap<String, Object> data = new HashMap<>();
        data.put("totalPosts", getTotalPosts());

        usersReference.updateChildren( data );

    }


    public Map<String, Object > convertToMAp(){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("name", getName());
        userMap.put("uid", getUID());
        userMap.put("urlPhoto", getUrlPhoto());
        //userMap.put("totalFollowers", getTotalFollowers());
        //userMap.put("totalFollow", getTotalFollow());
        //userMap.put("totalPosts", getTotalPosts());

        return userMap;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public int getTotalFollow() {
        return totalFollow;
    }

    public void setTotalFollow(int totalFollow) {
        this.totalFollow = totalFollow;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int posts) {
        this.totalPosts = posts;
    }
}
