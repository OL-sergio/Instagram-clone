package udemy.java.instagram_clone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

import udemy.java.instagram_clone.config.FirebaseConfiguration;

public class User implements Serializable {

    private String UID;
    private String name;
    private String email;
    private String password;
    private String urlPhoto;

    public User() {
    }

    public void saveUser(User user){
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference users = databaseReference
                .child("Users")
                .child(getUID());
                users.setValue(this);
    }



    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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
