package udemy.java.instagram_clone.fragments;

import static udemy.java.instagram_clone.config.UserFirebase.getCurrentUser;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.activity.EditProfileActivity;
import udemy.java.instagram_clone.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    private ProgressBar progressBar;
    private CircleImageView circleImageViewProfileImage;
    private GridView gridViewProfile;
    private TextView textViewPublications, textViewFollowers, textViewFollowing;
    private Button buttonActionProfile;

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


        startComponents();

        refreshProfileImage();

    }

    private void refreshProfileImage() {
        //Recover data from user
        FirebaseUser user = getCurrentUser();

        Uri url = user.getPhotoUrl();

        if (url != null ) {

            Glide.with(AccountFragment.this)
                    .asBitmap()
                    .load(url)
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

    private void startComponents() {
        gridViewProfile = binding.gridViewProfile;
        progressBar = binding.progressBarAccountProfile;
        circleImageViewProfileImage = binding.circleImageViewProfileImage;
        textViewPublications = binding.textViewNumberPublications;
        textViewFollowers = binding.textViewFollowersProfile;
        textViewFollowing = binding.textViewFollowingProfile;
        buttonActionProfile = binding.buttonAccountActionProfile;
    }

    @Override
    public void onStart() {
        refreshProfileImage();
        super.onStart();
    }

    @Override
    public void onResume() {
        refreshProfileImage();
        super.onResume();
    }

    @Override
    public void onStop() {
        binding = null;
        super.onStop();
    }
}
