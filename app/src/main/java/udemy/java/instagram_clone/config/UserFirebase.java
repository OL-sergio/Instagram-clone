package udemy.java.instagram_clone.config;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import udemy.java.instagram_clone.helper.Base64Custom;
import udemy.java.instagram_clone.model.User;

public class UserFirebase {

    public static String getUserIdentification(){

        FirebaseAuth userRef = ConfigurationFirebase.getUserAuthentication();
        String email = Objects.requireNonNull(userRef.getCurrentUser()).getEmail();
        assert email != null;
        String userIdentification = Base64Custom.encryption(email);

        return  userIdentification;
    }

    public static FirebaseUser getCurrentUser(){
        FirebaseAuth user = ConfigurationFirebase.getUserAuthentication();
        return user.getCurrentUser();
    }

    public static void  updateUserName(String name){
        try {
            //USer logged to app
            FirebaseUser userLogged = getCurrentUser();
            //Configuration of object for user profile
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(name)
                    .build();
            userLogged.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Profile","Error on updating user the name!" );
                    }
                }
            });
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static User getLoggedUserData() {

        FirebaseUser firebaseUser = getCurrentUser();

        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());
        user.setUID(firebaseUser.getUid());

        if (firebaseUser.getPhotoUrl() == null){
            user.setUrlPhoto("");
        }else {
            user.setUrlPhoto(firebaseUser.getPhotoUrl().toString());
        }

        return user;
    }

    public static boolean updateUserPhoto(Uri url){
        try {
            FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("perfil","Erro ao actualizar foto");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static User getCurrentUserData() {
        FirebaseUser firebaseUser = getCurrentUser();
        User userdata = new User();
        userdata.setEmail(firebaseUser.getEmail() );
        userdata.setName(firebaseUser.getDisplayName() );

        if (firebaseUser.getPhotoUrl() == null){
            userdata.setUrlPhoto("");
        } else {
            userdata.setUrlPhoto(firebaseUser.getPhotoUrl().toString() );
        }
        return userdata;
    }

}
