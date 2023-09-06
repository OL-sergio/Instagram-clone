package udemy.java.instagram_clone.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.databinding.ActivityImageFullViewBinding;
import udemy.java.instagram_clone.model.Posts;
import udemy.java.instagram_clone.model.User;

public class ImageFullViewActivity extends AppCompatActivity {

    private ActivityImageFullViewBinding binding;


    private TextView textViewProfileName, textViewTotalLikes, textViewLastDescritions, textViewAllComments;
    private ImageView imageViewSelectedPost;
    private CircleImageView circleImageViewUserProfilePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityImageFullViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = binding.fullImageActivity.toolbarMain;
        toolbar.setTitle("Visualizar publicações");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);

        startComponents();

        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){

            Posts posts = (Posts) bundle.getSerializable("posts");
            User user = (User) bundle.getSerializable("user");

            Uri url = Uri.parse( user.getUrlPhoto());

            Glide.with(ImageFullViewActivity.this )
                    .load( url )
                    .placeholder( R.drawable.avatar )
                    .into( circleImageViewUserProfilePost );
            textViewProfileName.setText( user.getName() );


            Uri urlPost = Uri.parse(posts.getPhotoUrl());

            Glide.with(ImageFullViewActivity.this )
                    .load( urlPost )
                    .placeholder( R.drawable.avatar )
                    .into( imageViewSelectedPost );
            textViewLastDescritions.setText( posts.getPostDescription() );

        }

    }

    private void startComponents() {

        circleImageViewUserProfilePost = binding.fullImageActivityPosts.circleImageViewFullImageViewProfileImage;
        textViewProfileName = binding.fullImageActivityPosts.textViewFullImageViewProfileName;
        imageViewSelectedPost = binding.fullImageActivityPosts.imageViewFullImageViewSelectedPhoto;
        textViewLastDescritions = binding.fullImageActivityPosts.textViewFullImageViewDescriptions;
        textViewAllComments = binding.fullImageActivityPosts.textViewFullImageViewDescriptions;

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

}