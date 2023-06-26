package udemy.java.instagram_clone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfiguration {

    private static FirebaseAuth userAuthentication;
    private static DatabaseReference databaseReference;

    public static FirebaseAuth getUserAuthentication() {
        if(userAuthentication == null ){
            userAuthentication = FirebaseAuth.getInstance();
        }
        return userAuthentication;
    }

    public static DatabaseReference getDatabaseReference() {
        if(databaseReference == null ){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
