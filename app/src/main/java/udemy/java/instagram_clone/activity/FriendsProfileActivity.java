package udemy.java.instagram_clone.activity;

import android.net.Uri;
import android.os.Bundle;
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

import java.util.HashMap;
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

    private DatabaseReference referenceFirebase;
    private DatabaseReference referenceUsers;
    private DatabaseReference referenceUserFriend;
    private DatabaseReference referenceFollowers;
    private DatabaseReference referenceUserLogged;

    private GridView gridViewProfile;
    private TextView textViewPublications, textViewFollowers, textViewFollow;

    private ValueEventListener valueEventListenerFriendProfile;

    private String userLoggedId;

    private User userSelected;
    private User userLogged;

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

        referenceFirebase = ConfigurationFirebase.getDatabaseReference();
        userLoggedId = UserFirebase.getUserIdentification();

        referenceUsers = referenceFirebase.child("users");
        referenceFollowers = referenceFirebase.child("followers");

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



    }

    private void retrievedCurrentUserLogged(){

        referenceUserLogged = referenceUsers.child( userLoggedId );
        referenceUserLogged.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userLogged = snapshot.getValue(User.class);

                        verificationUserFriendFollow();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void activateButtonFollow( boolean followUser ) {

        if (followUser) {

            buttonActionProfile.setText( "Seguindo" );

        }else {

            buttonActionProfile.setText( "Seguir" );

            buttonActionProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    saveFollowers( userLogged, userSelected );

                }
            });

        }

    }

    private void saveFollowers( User loggUser, User userFriend ) {

        HashMap<String, Object> friendsData = new HashMap<>();
        friendsData.put( "name", userFriend.getName() );
        friendsData.put( "urlPhoto", userFriend.getUrlPhoto() );

        DatabaseReference followerReference = referenceFollowers
                .child( loggUser.getUID() )
                .child( userFriend.getUID() );
        followerReference.setValue(friendsData);

        buttonActionProfile.setText("Seguindo");
        buttonActionProfile.setOnClickListener(null);


        int follow = loggUser.getFollow() + 1;
        HashMap<String, Object> followData = new HashMap<>();
        followData.put( "follow", follow );

        DatabaseReference userFollow = referenceUsers
                .child(loggUser.getUID());
        userFollow.updateChildren(followData);


        int followers = userFriend.getFollowers() + 1;
        HashMap<String, Object> followersData = new HashMap<>();
        followersData.put( "followers", followers );

        DatabaseReference userFollowers = referenceUsers
                .child(userFriend.getUID());
        userFollowers.updateChildren(followersData);



    }

    private void verificationUserFriendFollow() {

        DatabaseReference followReference = referenceFollowers
                .child( userLoggedId )
                .child( userSelected.getUID() );

        followReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() ){

                            //Log.i("FriendFollow", "Follow ");
                            activateButtonFollow(true);

                        }else {

                           //Log.i("FriendFollow", "Followers " );
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
        buttonActionProfile.setText("Carregando");

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

        retrievedCurrentUserLogged();

    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceUsers.removeEventListener(valueEventListenerFriendProfile);
    }
}