package udemy.java.instagram_clone.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.databinding.ActivityCommentsBinding;


public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Comentários");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}