package udemy.java.instagram_clone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.databinding.ActivityFriendsProfileBinding;
import udemy.java.instagram_clone.model.User;

public class FriendsProfileActivity extends AppCompatActivity {

    private ActivityFriendsProfileBinding binding;

    private Button buttonActionProfile;

    private User userSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFriendsProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Friends profile");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);

        buttonActionProfile = binding.profileFragment.buttonAccountActionProfile;
        buttonActionProfile.setText("Seguir");

        //Get user selected
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            userSelected = (User) bundle.getSerializable("selectedUser" );

            getSupportActionBar().setTitle(userSelected.getName());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}