package udemy.java.instagram_clone.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import udemy.java.instagram_clone.activity.FilterActivity;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Solution?
       // https://github.com/phamtdat/CameraDemo

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

                  if( result.getResultCode() == Activity.RESULT_OK && result.getData() != null ) {

                     try {

                         Uri selectedImageUri = result.getData().getData();


                         assert selectedImageUri != null;
                         InputStream inputStream = requireActivity().getContentResolver().openInputStream(selectedImageUri);
                         image = BitmapFactory.decodeStream(inputStream);


                         ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();
                         image.compress(Bitmap.CompressFormat.JPEG, 15, dataOutput);
                         byte [] dataImage = dataOutput.toByteArray();

                          if (image != null){

                              Intent intent = new Intent(getActivity(), FilterActivity.class );
                              intent.putExtra("selectedPhoto", dataImage);
                              startActivity(intent);
                            }

                        } catch (Exception e) {
                           e.printStackTrace();
                        }

                    }
                }
            });

    ActivityResultLauncher<Intent> takePhotoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result ) {

                    if( result.getResultCode() == Activity.RESULT_OK && result.getData() != null  ) {

                        Bundle photoData  = result.getData().getExtras();
                        image  = (Bitmap) photoData.get("data");

                        try {


                            ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 60, dataOutput);

                            byte [] dataImage = dataOutput.toByteArray();

                            if (image != null){

                                Intent intent = new Intent(getActivity(), FilterActivity.class );
                                intent.putExtra("selectedPhoto", dataImage);
                                startActivity(intent);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}