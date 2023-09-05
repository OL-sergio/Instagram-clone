package udemy.java.instagram_clone.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.adapter.AdapterGrid;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.ActivityFriendsProfileBinding;
import udemy.java.instagram_clone.model.Posts;
import udemy.java.instagram_clone.model.User;

public class FriendsProfileActivity extends AppCompatActivity {

    private ActivityFriendsProfileBinding binding;

    private  List<Posts> postsList;

    private Button buttonActionProfile;
    private CircleImageView circleImageViewProfile;

    private DatabaseReference referenceFirebase;
    private DatabaseReference referenceUsers;
    private DatabaseReference referenceUserFriend;
    private DatabaseReference referenceFollowers;
    private DatabaseReference referenceUserLogged;
    private DatabaseReference referencePostUsers;

    private AdapterGrid adapterGrid;

    private GridView gridViewProfilePosts;
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

            //Configurar referencia postagens utilizador
            referencePostUsers = ConfigurationFirebase.getDatabaseReference()
                    .child("posts" )
                    .child( userSelected.getUID() );

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

        startImageLoader();

        retrievingPhotosPosts();

        selectedPhotoFullView();

        gridViewProfilePosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Posts posts = postsList.get( position );
                Intent intent = new Intent( getApplicationContext(), ImageFullViewActivity.class );

                intent.putExtra("posts", posts );
                intent.putExtra("user", userSelected );

                startActivity(intent);
            }
        } );

    }

    private void selectedPhotoFullView() {


    }

    private void startImageLoader(){

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(configuration);

    }

    private void retrievingPhotosPosts() {

        postsList = new ArrayList<>();

        referencePostUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Configuration the size of Grid
                int gridSize = getResources().getDisplayMetrics().widthPixels;
                int imageSize = gridSize / 3;
                gridViewProfilePosts.setColumnWidth( imageSize );

                List<String> urlPhotos = new ArrayList<>();
                for ( DataSnapshot ds: snapshot.getChildren()  ){
                    Posts posts = ds.getValue(Posts.class);
                    //Log.d("post","urlPhoto" + posts.getPhotoUrl() );

                    postsList.add(posts);

                    urlPhotos.add(posts.getPhotoUrl());

                }

                int totalPost = urlPhotos.size();
                textViewPublications.setText( String.valueOf(totalPost) );

                //Configuration of Adapter
                adapterGrid = new AdapterGrid(
                        getApplicationContext(), R.layout.row_view_gridview_post, urlPhotos
                );

                gridViewProfilePosts.setAdapter(adapterGrid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                }

        );
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

        /*
         * followers
         *   id_Current_user (id friend)
         *      id_user_logged ( id user logged)
         *          data_logged
         * */


        HashMap<String, Object> loggedUserData = new HashMap<>();
        loggedUserData.put( "name", userLogged.getName() );
        loggedUserData.put( "urlPhoto", userLogged.getUrlPhoto() );

        DatabaseReference followerReference = referenceFollowers
                .child( userFriend.getUID() )
                .child( loggUser.getUID() );
        followerReference.setValue( loggedUserData );

        buttonActionProfile.setText("Seguindo");
        buttonActionProfile.setOnClickListener(null);


        int follow = loggUser.getTotalFollow() + 1;
        HashMap<String, Object> followData = new HashMap<>();
        followData.put( "totalFollow", follow );

        DatabaseReference userFollow = referenceUsers
                .child(loggUser.getUID());
        userFollow.updateChildren(followData);


        int followers = userFriend.getTotalFollowers() + 1;
        HashMap<String, Object> followersData = new HashMap<>();
        followersData.put( "totalFollowers", followers );

        DatabaseReference userFollowers = referenceUsers
                .child(userFriend.getUID());
        userFollowers.updateChildren(followersData);



    }

    private void verificationUserFriendFollow() {

        DatabaseReference followReference = referenceFollowers
                .child( userSelected.getUID() )
                .child( userLoggedId );

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

                        //String posts = String.valueOf( user.getPosts() );
                        String follow = String.valueOf( user.getTotalFollow() );
                        String followers = String.valueOf( user.getTotalFollowers() );

                        //textViewPublications.setText(posts);
                        textViewFollow.setText(follow);
                        textViewFollowers.setText(followers);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
        });

    }

    private void startComponents() {

        gridViewProfilePosts = binding.profileFragment.gridViewProfilePosts;
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
        binding = null;
        super.onStop();
        referenceUsers.removeEventListener(valueEventListenerFriendProfile);
    }
}