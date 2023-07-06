package udemy.java.instagram_clone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.activity.EditProfileActivity;
import udemy.java.instagram_clone.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    private ProgressBar progressBar;
    private CircleImageView circleImageViewProfileImage;
    private GridView gridViewProfile;
    private TextView textViewPublications, textViewFollowers, textViewFollowing;
    private ImageButton buttonEditProfile;


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


        gridViewProfile = binding.gridViewProfile;
        progressBar = binding.progressBarAccountProfile;
        circleImageViewProfileImage = binding.circleImageViewProfileImage;
        textViewPublications = binding.textViewNumberPublications;
        textViewFollowers = binding.textViewFollowersProfile;
        textViewFollowing = binding.textViewFollowingProfile;
        buttonEditProfile = binding.buttonAccountEditProfile;

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });



    }
}
