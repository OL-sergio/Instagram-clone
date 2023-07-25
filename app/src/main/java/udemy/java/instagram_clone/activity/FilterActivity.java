package udemy.java.instagram_clone.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.databinding.ActivityFilterBinding;

public class FilterActivity extends AppCompatActivity {

    private ActivityFilterBinding binding;

    private ImageView imageViewSelectedPhoto;
    private Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        imageViewSelectedPhoto = binding.imageViewSelectedPhoto;

        Bundle bundle= getIntent().getExtras();
        if (bundle != null){

            byte [] dataImage = bundle.getByteArray( "selectedPhoto" );
            image = BitmapFactory.decodeByteArray (dataImage, 0 , dataImage.length );


                Glide.with(FilterActivity.this)
                        .load(image)
                        .placeholder(R.drawable.avatar)
                        .into(imageViewSelectedPhoto);


        }
    }
}