package udemy.java.instagram_clone.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.databinding.ActivityFilterBinding;

public class FilterActivity extends AppCompatActivity {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private ActivityFilterBinding binding;

    private ImageView imageViewSelectedPhoto;
    private Bitmap image = null;
    private Bitmap imageFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);


        imageViewSelectedPhoto = binding.imageViewSelectedPhoto;

        Bundle bundle= getIntent().getExtras();
        if (bundle != null){

            byte [] dataImage = bundle.getByteArray( "selectedPhoto" );
            image = BitmapFactory.decodeByteArray (dataImage, 0 , dataImage.length );

            /*Glide.with(FilterActivity.this)
                    .load(image)
                    .placeholder(R.drawable.avatar)
                    .into(imageViewSelectedPhoto);*/

            imageFilter = image.copy( image.getConfig(),true );
            Filter myFilter = new Filter();


            //myFilter.addSubFilter(new BrightnessSubFilter(50));
            //myFilter.addSubFilter(new ContrastSubFilter(2.5f));
            //myFilter.addSubFilter(new SaturationSubFilter(9));
            //myFilter.addSubFilter( new ColorOverlaySubFilter(12,5f,2f,2f));

            /*
            *Point[] rgbKnots;
            rgbKnots = new Point[3];
            rgbKnots[0] = new Point(0, 0);
            rgbKnots[1] = new Point(15, 100);
            rgbKnots[2] = new Point(25, 125);
            myFilter.addSubFilter( new ToneCurveSubFilter(null,null,null,null));
            */

            //myFilter.addSubFilter( new BrightnessSubFilter(150));
            //myFilter.addSubFilter( new VignetteSubFilter(getBaseContext(), 50 ));

            Filter fooFilter = SampleFilters.getStarLitFilter();
            Bitmap outputImage = fooFilter.processFilter(imageFilter);

            Glide.with(FilterActivity.this)
                    .load(outputImage)
                    .placeholder(R.drawable.avatar)
                    .into(imageViewSelectedPhoto);




        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflate = getMenuInflater();
        inflate.inflate( R.menu.menu_filter, menu );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.ic_save_post) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}