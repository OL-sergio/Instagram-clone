package udemy.java.instagram_clone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.adapter.ThumbnailsAdapter;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.ActivityFilterBinding;
import udemy.java.instagram_clone.helper.CustomAlertDialog;
import udemy.java.instagram_clone.helper.ThumbnailsManager;
import udemy.java.instagram_clone.listener.ThumbnailListener;
import udemy.java.instagram_clone.model.Posts;
import udemy.java.instagram_clone.model.ThumbnailItem;
import udemy.java.instagram_clone.model.User;

public class FilterActivity extends AppCompatActivity implements ThumbnailListener {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private ActivityFilterBinding binding;

    private Activity activity;

    private DatabaseReference referenceUserLogged;
    private DatabaseReference referenceUser;
    private DatabaseReference firebaseReference;
    private DataSnapshot followersSnapshot;

    private User userLogged;

    private List <ThumbnailItem> listFilters;

    private ImageView imageViewSelectedPhoto;
    private TextInputEditText textInpEditTextDescription;

    private Bitmap image;
    private Bitmap imageFilter;
    private Bitmap outputImage;
    private RecyclerView recyclerViewFilters;

    private AlertDialog alertDialog;
    private CustomAlertDialog dialogCustom;

    private boolean statusUpload;

    private Bundle bundle;
    private Context context = null;

    private String idUserLogged;
    private String alertTitle;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
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

        firebaseReference = ConfigurationFirebase.getDatabaseReference();
        idUserLogged = UserFirebase.getUserIdentification();
        referenceUser = ConfigurationFirebase.getDatabaseReference().child("users");

        imageViewSelectedPhoto = binding.imageViewSelectedPhoto;
        textInpEditTextDescription = binding.textInputEditTextFilterDescription;
        recyclerViewFilters = binding.recyclerViewImageFilters;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerViewFilters.setLayoutManager(layoutManager);
        recyclerViewFilters.setHasFixedSize(true);

        retrieveDataPost();

        bundle = getIntent().getExtras();
        if (bundle != null){

            byte[] imageData = bundle.getByteArray("selectedPhoto");
            image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length );

            //imageViewSelectedPhoto.setImageBitmap(outputImage);

            if (image != null ){

                imageViewSelectedPhoto.setImageBitmap(
                        Bitmap.createScaledBitmap(
                               image, 640, 640, false)
                );

                imageFilter = image.copy(image.getConfig(), true );

            }else {

                imageViewSelectedPhoto.setImageBitmap(
                        Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(getResources(), R.drawable.avatar)
                                , 640, 400, false)
                );

            }

           /*Glide.with(FilterActivity.this)
                    .load(image)
                    .placeholder(R.drawable.avatar)
                    .into(imageViewSelectedPhoto);*/

            /*Glide.with(this)
                    .asBitmap()
                    .load(image)
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
                image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length );

                if(image != null ){

                  thumbImage = Bitmap.createScaledBitmap(
                            image, 640, 640, false);

                }else {

                    thumbImage = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(getResources(), R.drawable.avatar),
                            640, 400, false);

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

        byte[] imageData = bundle.getByteArray("selectedPhoto");
        image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length  );

        if (bundle != null) {

            //byte[] imageData = baos.s("selectedPhoto");

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

            if (image != null){

                Bitmap  imagePhotoData = filter.processFilter(
                        Bitmap.createScaledBitmap( image, 640, 640, false)
                );

                imageFilter = imagePhotoData.copy(imagePhotoData.getConfig(), true );
                imageViewSelectedPhoto.setImageBitmap( imagePhotoData );



            } else {

                imageViewSelectedPhoto.setImageBitmap(
                        filter.processFilter(
                                Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeResource(getResources(), R.drawable.avatar)
                                        , 640, 400, false)));

            }
        }
    }


    private void showAlertProgressBar( String title ){


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setCancelable(false);
        alert.setView(R.layout.alert_dialog_loading);

        alertDialog = alert.create();
        alertDialog.show();

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
            savePost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePost( ) {
            //showAlertProgressBar("Carregando dados, aguarde!");
            CustomAlertDialog.setAlertDialog(this, "Carregando dados, aguarde!");

            final Posts posts = new Posts();
            posts.setIdUser( idUserLogged );
            //posts.setPhotoUrl();
            posts.setPostDescription( textInpEditTextDescription.getText().toString() );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageFilter.compress(Bitmap.CompressFormat.JPEG, 60, baos);

            byte[] imageData = baos.toByteArray();

            StorageReference storageRef = ConfigurationFirebase.getFirebaseStorage();
            final StorageReference imageRef = storageRef
                    .child("images")
                    .child("posts")
                    .child(posts.getId() + ".jpg");

            UploadTask uploadTask = imageRef.putBytes( imageData );

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FilterActivity.this, "Falha á criar imagen, tente novamente! ",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri url = task.getResult();
                            posts.setPhotoUrl( url.toString() );

                            //Update total posts
                            int totalPosts  = ( userLogged.getTotalPosts() + 1 ) ;
                            userLogged.setTotalPosts( totalPosts );
                            userLogged.updateTotalPost();


                            if (posts.savePost(  followersSnapshot )){

                                Toast.makeText(FilterActivity.this, "Sucesso á criar o post! ",
                                        Toast.LENGTH_SHORT).show();

                               //alertDialog.cancel();
                                CustomAlertDialog.dismissAlertDialog();

                                finish();
                            }
                        }
                    });
                }
            });
    }



    private void retrieveDataPost() {

        //showAlertProgressBar("Carregando dados, aguarde!");
        CustomAlertDialog.setAlertDialog(this, "Carregando dados, aguarde!");

        referenceUserLogged = referenceUser.child( idUserLogged );
        referenceUserLogged.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        userLogged = snapshot.getValue(User.class);
                        //Log.d("userLogged", String.valueOf(userLogged));


                        DatabaseReference followersReference = firebaseReference
                                .child("followers" )
                                .child( idUserLogged );
                        followersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                followersSnapshot = snapshot;

                                //alertDialog.cancel();
                                CustomAlertDialog.dismissAlertDialog();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}