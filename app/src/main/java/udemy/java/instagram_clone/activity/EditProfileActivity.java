package udemy.java.instagram_clone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.config.UserFirebase;

import udemy.java.instagram_clone.databinding.ActivityEditProfileBinding;
import udemy.java.instagram_clone.model.User;


public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private CircleImageView circleImageViewPhotoProfile;
    private TextInputEditText textInputEditTextUpdateName, textInputEditTextUpdateEmail;
    private Button buttonSaveUpdateUser;
    private Toolbar toolbar;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);



        startComponents();

        //Retrieved User
        userLogged = UserFirebase.getLoggedUserData();


        //Retrieved user Profile
        FirebaseUser firebaseUser = UserFirebase.getCurrentUser();

        textInputEditTextUpdateName.setText(firebaseUser.getDisplayName());
        textInputEditTextUpdateEmail.setText(firebaseUser.getEmail());


    }

    private void startComponents() {

        circleImageViewPhotoProfile = binding.circleImageViewEditProfile;
        textInputEditTextUpdateName = binding.textInputEditTextEditeProfileName;
        textInputEditTextUpdateEmail = binding.textInputEditTextEditeProfileEmail;
        buttonSaveUpdateUser = binding.buttonSaveEditedUser;

        textInputEditTextUpdateEmail.setFocusable(false);


        buttonSaveUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUpdated = Objects.requireNonNull(textInputEditTextUpdateName.getText()).toString();

                userLogged.setName(nameUpdated);
                userLogged.updateUSer();

                Toast.makeText(EditProfileActivity.this, "Utilizador atualizado", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        //Log.d("Edit","Error on change activity!" );
        finish();
        return false;


    }
}