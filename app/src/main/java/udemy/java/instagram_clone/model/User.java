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

    public User() {
    }

    public void saveUser(){
        DatabaseReference databaseReference = FirebaseConfiguration.getDatabaseReference();
        DatabaseReference user = databaseReference
                .child("Users")
                .child(getuID());
        user.setValue(this);
    }

    @Exclude
    public String getuID() {
        return UID;
    }

    public void setuID(String uID) {
        this.UID = uID;
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
}
