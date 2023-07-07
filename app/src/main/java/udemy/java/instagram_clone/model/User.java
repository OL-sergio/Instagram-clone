package udemy.java.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import udemy.java.instagram_clone.config.FirebaseConfiguration;

public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private String password;
    private String urlPhoto;

    public User() {
    }

    public void saveUser(User user){
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference users = databaseReference
                .child("users")
                .child(getUID());
                users.setValue(this);
    }

    public void updateUSer(){
        DatabaseReference firebaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference usersReference = firebaseReference
                .child("users")
                .child(getUID());
        Map<String, Object> userValues = convertToMAp();
        usersReference.updateChildren(userValues);


    }

    public Map<String, Object > convertToMAp(){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("name", getName());
        userMap.put("uid", getUID());
        userMap.put("urlPhoto", getUrlPhoto());

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
        this.name = name;
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
}
