package udemy.java.instagram_clone.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;


import udemy.java.instagram_clone.databinding.ActivityEditProfileBinding;
import udemy.java.instagram_clone.model.User;


public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private CircleImageView circleImageViewPhotoProfile;
    private TextInputEditText textInputEditTextUpdateName, textInputEditTextUpdateEmail;
    private TextView textViewEditProfilePhoto;
    private Button buttonSaveUpdateUser;

    private StorageReference storageReference;
    private String userIdentification;

    private User userLogged;
    private Bitmap image = null;

    public EditProfileActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);

        startComponents();

        editProfilePhoto();


        //Retrieved User
        userLogged = UserFirebase.getLoggedUserData();
        storageReference = ConfigurationFirebase.getFirebaseStorage();
        userIdentification = UserFirebase.getUserIdentification();


        //Retrieved user Profile
        FirebaseUser firebaseUser = UserFirebase.getCurrentUser();
        textInputEditTextUpdateName.setText(firebaseUser.getDisplayName());
        textInputEditTextUpdateEmail.setText(firebaseUser.getEmail());

        //Recover data from user
        Uri url = firebaseUser.getPhotoUrl();

        if (url != null ) {

            Glide.with(EditProfileActivity.this)
                    .asBitmap()
                    .load(url)
                    .into(circleImageViewPhotoProfile);

        } else {

            circleImageViewPhotoProfile.setImageResource(R.drawable.avatar);
        }

        buttonSaveUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUpdated = textInputEditTextUpdateName.getText().toString();

                UserFirebase.updateUserName(nameUpdated);

                userLogged.setName(nameUpdated);
                userLogged.updateUser();

                Toast.makeText(EditProfileActivity.this, "Utilizador atualizado", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void startComponents() {

        circleImageViewPhotoProfile = binding.circleImageViewEditProfile;
        textViewEditProfilePhoto = binding.textViewEditeProfilePhoto;
        textInputEditTextUpdateName = binding.textInputEditTextEditeProfileName;
        textInputEditTextUpdateEmail = binding.textInputEditTextEditeProfileEmail;
        buttonSaveUpdateUser = binding.buttonSaveEditedUser;

        textInputEditTextUpdateEmail.setFocusable(false);

    }

    private void editProfilePhoto() {

        textViewEditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                insertFromGalleryActivityResultLauncher.launch(intent);
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
                        try {

                            image = MediaStore.Images.Media.getBitmap(getContentResolver(), url);

                            if (image != null){

                                Glide.with(EditProfileActivity.this)
                                        .asBitmap()
                                        .load(image)
                                        .into(circleImageViewPhotoProfile);

                                saveImageOnFirebase();

                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });

    private void saveImageOnFirebase() {

        //Retrieve image from firebase
        circleImageViewPhotoProfile.setDrawingCacheEnabled(true);
        circleImageViewPhotoProfile.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 65, baos);
        byte [] dataImage = baos.toByteArray();

        //Save image on firebase
        final StorageReference imageRef = storageReference
                .child("images")
                .child("profile")
                .child(userIdentification)
                //.child(userIdentification + ".jpeg");
                .child("profile.jpeg");

        UploadTask uploadTask = imageRef.putBytes( dataImage );

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Falha á criar imagen ",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfileActivity.this, "Sucesso á criar imagen ",
                        Toast.LENGTH_SHORT).show();

                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        updatePhotoUser(url);
                    }
                });
            }
        });
    }

    public void updatePhotoUser(Uri url) {

        UserFirebase.updateUserPhoto(url);
            userLogged.setUrlPhoto(url.toString());
            userLogged.updateUser();
            Toast.makeText(this, "Foto actualizada", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionResult : grantResults ) {
            if (permissionResult == PackageManager.PERMISSION_DENIED){
                alertValidationPermission();
            }
        }
    }

    private void alertValidationPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para poder utilizar a app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Comfirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onSupportNavigateUp() {

        //Log.d("Edit","Error on change activity!" );
        finish();
        return false;
    }
}