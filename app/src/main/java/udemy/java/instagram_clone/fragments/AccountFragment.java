package udemy.java.instagram_clone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.activity.EditProfileActivity;
import udemy.java.instagram_clone.adapter.AdapterGrid;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.FragmentAccountBinding;
import udemy.java.instagram_clone.model.Posts;
import udemy.java.instagram_clone.model.User;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    private DatabaseReference referenceFirebase;
    private DatabaseReference referenceUserLogged;
    private DatabaseReference referenceUser;
    private DatabaseReference referenceUsersPosts;

    private ProgressBar progressBar;
    private CircleImageView circleImageViewProfileImage;
    private GridView gridViewProfilePosts;
    private TextView textViewPublications, textViewFollowers, textViewFollowing;
    private Button buttonActionProfile;

    private ValueEventListener valueEventListenerUserProfile;

    private AdapterGrid adapterGrid;

    private User userLogged;


    public AccountFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Retrieved configurations
        userLogged = UserFirebase.getLoggedUserData();
        referenceFirebase = ConfigurationFirebase.getDatabaseReference();
        referenceUser = referenceFirebase.child("users");

        //Configurar referencia postagens utilizador
        referenceUsersPosts = ConfigurationFirebase.getDatabaseReference()
                .child("posts" )
                .child( userLogged.getUID() );

        startComponents();

        getProfileImage();

        retrievingPhotosPosts();

        startImageLoader();
    }

    private void getProfileImage() {

        //Recover data from user
        String urlPhoto = userLogged.getUrlPhoto();

        if (urlPhoto != null ) {

            Glide.with(AccountFragment.this)
                    .asBitmap()
                    .load(urlPhoto)
                    .placeholder(R.drawable.avatar)
                    .into(circleImageViewProfileImage);

        }


        buttonActionProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), EditProfileActivity.class));

                getActivity().getFragmentManager().popBackStackImmediate();
            }
        });


    }


    private void startImageLoader(){

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(requireActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(configuration);

    }


   private void retrievingPhotosPosts() {

        referenceUsersPosts.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    urlPhotos.add(posts.getPhotoUrl());

                }

                int totalPost = urlPhotos.size();
                textViewPublications.setText( String.valueOf(totalPost) );

                //Configuration of Adapter
               adapterGrid = new AdapterGrid(
                       requireActivity(), R.layout.row_view_gridview_post, urlPhotos
               );
                gridViewProfilePosts.setAdapter(adapterGrid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrievedCurrentUserLogged() {

        referenceUserLogged = referenceUser.child( userLogged.getUID() );
        valueEventListenerUserProfile = referenceUserLogged.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);

                        //String posts = String.valueOf( user.getPosts() );

                        String follow = String.valueOf( user.getFollow() );
                        String followers = String.valueOf( user.getFollowers() );

                        //textViewPublications.setText(posts);
                        textViewFollowing.setText(follow);
                        textViewFollowers.setText(followers);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }


    private void startComponents() {

        gridViewProfilePosts = binding.gridViewProfilePosts;
        progressBar = binding.progressBarAccountProfile;
        circleImageViewProfileImage = binding.circleImageViewProfileImage;
        textViewPublications = binding.textViewNumberPublications;
        textViewFollowers = binding.textViewNumberFollowers;
        textViewFollowing = binding.textViewNumberFollowing;
        buttonActionProfile = binding.buttonAccountActionProfile;

    }


    @Override
    public void onStart() {
        super.onStart();

       retrievedCurrentUserLogged();

    }


    @Override
    public void onStop() {
        binding = null;
        super.onStop();

        referenceUserLogged.removeEventListener(valueEventListenerUserProfile);

    }
}
