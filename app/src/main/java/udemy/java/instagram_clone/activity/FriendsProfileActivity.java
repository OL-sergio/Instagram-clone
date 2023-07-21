package udemy.java.instagram_clone.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.ActivityFriendsProfileBinding;
import udemy.java.instagram_clone.model.User;

public class FriendsProfileActivity extends AppCompatActivity {

    private ActivityFriendsProfileBinding binding;

    private Button buttonActionProfile;
    private CircleImageView circleImageViewProfile;

    private DatabaseReference firebaseReference;
    private DatabaseReference referenceUsers;
    private DatabaseReference referenceUserFriend;
    private DatabaseReference referenceFollowers;

    private GridView gridViewProfile;
    private TextView textViewPublications, textViewFollowers, textViewFollow;

    private ValueEventListener valueEventListenerFriendProfile;

    private String userLogged;

    private User userSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFriendsProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Friends profile");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);

        firebaseReference = ConfigurationFirebase.getDatabaseReference();
        userLogged = UserFirebase.getUserIdentification();

        referenceUsers = firebaseReference.child("users");
        referenceFollowers = firebaseReference.child("followers");

        startComponents();


        //Get user selected
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            userSelected = (User) bundle.getSerializable("selectedUser" );

            getSupportActionBar().setTitle(userSelected.getName());

            String photoUrl = userSelected.getUrlPhoto();
            if( photoUrl != null ){

                Uri url = Uri.parse(userSelected.getUrlPhoto());

                Glide.with(FriendsProfileActivity.this)
                        .load(url)
                        .placeholder(R.drawable.avatar)
                        .into(circleImageViewProfile);
            }
        }

        verificationUserFriendFollow();

    }

    private void activateButtonFollow( boolean followUser ) {

        if (followUser) {

            buttonActionProfile.setText( "Seguindo" );

        }else {

            buttonActionProfile.setText( "Seguir" );

        }

    }

    private void verificationUserFriendFollow() {

        DatabaseReference followReference = referenceFollowers
                .child( userLogged )
                .child( userSelected.getUID() );

        followReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() ){

                            Log.i("FriendFollow", "Follow ");
                            activateButtonFollow(true);

                        }else {

                            Log.i("FriendFollow", "Followers " );
                            activateButtonFollow(false);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }

        );

    }




    private void retrieveDataFromProfile() {

        referenceUserFriend = referenceUsers.child( userSelected.getUID() );
        valueEventListenerFriendProfile = referenceUserFriend.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);

                        String posts = String.valueOf( user.getPosts() );
                        String follow = String.valueOf( user.getFollow() );
                        String followers = String.valueOf( user.getFollowers() );

                        textViewPublications.setText(posts);
                        textViewFollow.setText(follow);
                        textViewFollowers.setText(followers);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
        });


    }

    private void startComponents() {


        gridViewProfile = binding.profileFragment.gridViewProfile;
        textViewPublications = binding.profileFragment.textViewNumberPublications;
        textViewFollowers = binding.profileFragment.textViewNumberFollowers;
        textViewFollow = binding.profileFragment.textViewNumberFollowing;

        circleImageViewProfile = binding.profileFragment.circleImageViewProfileImage;

        buttonActionProfile = binding.profileFragment.buttonAccountActionProfile;
        buttonActionProfile.setText("Seguir");

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDataFromProfile();
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceUsers.removeEventListener(valueEventListenerFriendProfile);
    }
}