package udemy.java.instagram_clone.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.adapter.ThumbnailsAdapter;
import udemy.java.instagram_clone.databinding.ActivityFilterBinding;
import udemy.java.instagram_clone.helper.ThumbnailsManager;
import udemy.java.instagram_clone.listener.ThumbnailListener;
import udemy.java.instagram_clone.model.ThumbnailItem;

public class FilterActivity extends AppCompatActivity implements ThumbnailListener {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private ActivityFilterBinding binding;

    private Activity activity;

    private ImageView imageViewSelectedPhoto;
    private Bitmap outputImage;
    private RecyclerView recyclerViewFilters;

    private Bundle bundle;

    private List <ThumbnailItem> listFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        activity = this;


        Toolbar toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);

        listFilters = new ArrayList<>();

        imageViewSelectedPhoto = binding.imageViewSelectedPhoto;
        recyclerViewFilters = binding.recyclerViewImageFilters;


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerViewFilters.setLayoutManager(layoutManager);
        recyclerViewFilters.setHasFixedSize(true);

        bundle = getIntent().getExtras();


        if (bundle != null){


            byte[] imageData = bundle.getByteArray("selectedPhoto");
            //outputImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length );
            //imageViewSelectedPhoto.setImageBitmap(outputImage);

            if (imageData != null ){

                imageViewSelectedPhoto.setImageBitmap(
                        Bitmap.createScaledBitmap(
                                BitmapFactory.decodeByteArray(imageData, 0, imageData.length),
                                640, 640, false)
                );

            }else {

                imageViewSelectedPhoto.setImageBitmap(
                        Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(getResources(), R.drawable.avatar)
                                , 640, 400, false)
                );

            }

           /*Glide.with(FilterActivity.this)
                    .load(outputImage)
                    .placeholder(R.drawable.avatar)
                    .into(imageViewSelectedPhoto);*/

            /*Glide.with(this)
                    .asBitmap()
                    .load(outputImage)
                    .into(new CustomTarget<Bitmap>(640,640 ) {
                          @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                         imageViewSelectedPhoto.setImageBitmap(resource);
                         }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            imageViewSelectedPhoto.setImageResource(R.drawable.avatar);
                        }
                    });*/




            //imageFilter = image.copy( image.getConfig(),true );
            //Filter myFilter = new Filter();


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


           /*
            *
           Filter fooFilter = SampleFilters.getStarLitFilter();
            Bitmap outputImage = fooFilter.processFilter(imageFilter);

            Glide.with(FilterActivity.this)
                    .load(outputImage)
                    .placeholder(R.drawable.avatar)
                    .into(imageViewSelectedPhoto);

                    * */

            recoverFilters();

        }

    }

    private void recoverFilters() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {

                Bitmap thumbImage;

                byte[] imageData = bundle.getByteArray("selectedPhoto");

                if(imageData != null ){

                  thumbImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeByteArray(imageData, 0, imageData.length ),
                            640, 640, false);

                }else {

                    thumbImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(getResources(), R.drawable.avatar),
                            640, 640, false);

                }

                ThumbnailItem brightnessSubFilter = new ThumbnailItem();
                ThumbnailItem colorOverlaySubFilter = new ThumbnailItem();
                ThumbnailItem contrastSubFilter = new ThumbnailItem();
                ThumbnailItem toneCurveSubFilter = new ThumbnailItem();
                ThumbnailItem vignetteSubFilter = new ThumbnailItem();
                ThumbnailItem saturationSubFilter = new ThumbnailItem();

                brightnessSubFilter.image = thumbImage;
                colorOverlaySubFilter.image = thumbImage;
                contrastSubFilter.image = thumbImage;
                toneCurveSubFilter.image = thumbImage;
                vignetteSubFilter.image = thumbImage;
                saturationSubFilter.image = thumbImage;

                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(brightnessSubFilter); // Original Image

                colorOverlaySubFilter.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(colorOverlaySubFilter);

                contrastSubFilter.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(contrastSubFilter);

                toneCurveSubFilter.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(toneCurveSubFilter);

                vignetteSubFilter.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(vignetteSubFilter);

                saturationSubFilter.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(saturationSubFilter);

                List<ThumbnailItem> thumbnailItemList = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbnailItemList, (ThumbnailListener) activity);
                recyclerViewFilters.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onThumbnailClick(Filter filter) {

        if (bundle != null) {

            byte[] imageData = bundle.getByteArray("selectedPhoto");
            Log.d("thumbnail", "click!!");

            /*Glide.with(this)
                    .asBitmap()
                    .load(imageData)
                    .into(new CustomTarget<Bitmap>(640,640 ) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageViewSelectedPhoto.setImageBitmap(
                                    filter.processFilter( Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeByteArray(imageData,0,imageData.length), 640, 640, false)
                            ));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            imageViewSelectedPhoto.setImageResource(R.drawable.avatar);
                        }
                    });*/

            if (imageData != null){

              imageViewSelectedPhoto.setImageBitmap(
                     filter.processFilter(
                            Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeByteArray(imageData, 0, imageData.length)
                                        , 640, 640, false)));

            } else {

                imageViewSelectedPhoto.setImageBitmap(
                        filter.processFilter(
                                Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeResource(getResources(), R.drawable.avatar)
                                        , 640, 400, false)));

            }
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