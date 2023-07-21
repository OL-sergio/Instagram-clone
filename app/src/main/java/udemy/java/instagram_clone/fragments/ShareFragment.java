package udemy.java.instagram_clone.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import udemy.java.instagram_clone.databinding.FragmentShareBinding;
import udemy.java.instagram_clone.helper.Permissions;


public class ShareFragment extends Fragment {


    private FragmentShareBinding binding;

    private Button buttonOpenCamera, buttonOpenGallery;

    private Bitmap image = null;

    private String[] necessaryPermissions = new  String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    public ShareFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        buttonOpenCamera = binding.buttonOpenCamara;
        buttonOpenGallery = binding.buttonOpenGallery;

        int requestCode = 1;
        Permissions.validatePermissions(necessaryPermissions, getActivity(), requestCode);

        buttonOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                insertFromGalleryActivityResultLauncher.launch(intent);
            }
        });

        buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                takePhotoActivityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> insertFromGalleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result ) {

                  if( result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        Uri url = result.getData().getData();
                       /* try {

                           //image = MediaStore.Images.Media.getBitmap(getContentResolver(), url);

                          if (image != null){

                                Glide.with(ShareFragment.this)
                                        .asBitmap()
                                        .load(image)
                                        .into(circleImageViewPhotoProfile);

                                //saveImageOnFirebase();

                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }*/

                    }
                }
            });

    ActivityResultLauncher<Intent> takePhotoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result ) {
                    if( result.getResultCode() == Activity.RESULT_OK) {

                        assert result.getData() != null;
                        Bundle localImageSelection  = result.getData().getExtras();
                        image  = (Bitmap) localImageSelection.get("data");

                       /**if (image != null){

                            Glide.with(ConfigurationsActivity.this)
                                    .asBitmap()
                                    .load(image)
                                    .into(circleImageViewSetImage);

                            saveImageOnFirebase();

                        }*/
                    }
                }
            });
}