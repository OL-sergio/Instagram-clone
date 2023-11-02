package udemy.java.instagram_clone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.adapter.CommentsAdapter;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.ActivityCommentsBinding;
import udemy.java.instagram_clone.model.Comments;
import udemy.java.instagram_clone.model.User;


public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;

    private EditText editComments;
    private RecyclerView recyclerViewComments;
    private ImageButton imageButtonSaveComments;

    private final List<Comments> commentsList = new ArrayList<>();

    private ValueEventListener childEventListenerComments;

    private DatabaseReference commentsReference;
    private DatabaseReference databaseReference;

    private CommentsAdapter commentsAdapter;

    private String idPost;
    private User idUserLogged;


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

        idUserLogged = UserFirebase.getLoggedUserData();

        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){
            idPost = bundle.getString("idPost");
        }

        databaseReference =  ConfigurationFirebase.getDatabaseReference();

        commentsAdapter = new CommentsAdapter(commentsList, this);

        components();
        saveComments();

        recyclerViewComments.setHasFixedSize(true);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager( this ));
        recyclerViewComments.setAdapter( commentsAdapter );


    }

    private void components() {
        editComments = binding.editTextTextTextComment;
        imageButtonSaveComments =  binding.buttonSaveComment;
        recyclerViewComments = binding.recyclerViewComments;
    }

    private void saveComments() {

        imageButtonSaveComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textComment = editComments.getText().toString();
                if ( textComment != null && !textComment.equals("")){

                    Comments comments = new Comments();
                    comments.setIdPosts( idPost );
                    comments.setIdUser( idUserLogged.getUID() );
                    comments.setUserName( idUserLogged.getName() );
                    comments.setPhotoUrl( idUserLogged.getUrlPhoto() );
                    comments.setComment( textComment );

                    if (comments.saveComment()){
                        Toast.makeText(getApplicationContext(), " save post!",
                                Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), " Insira um comentário!",
                            Toast.LENGTH_SHORT).show();
                }

                editComments.setText("");

            }
        });
    }


    private void getUsersComments() {

        commentsReference = databaseReference.child("comments")
                .child(idPost);

        childEventListenerComments = commentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()   ){
                    commentsList.add(
                            dataSnapshot.getValue( Comments.class ) );
                }
                commentsAdapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        super.onStart();

        getUsersComments();
    }

    @Override
    protected void onStop() {
        super.onStop();

        binding = null;
        commentsReference.removeEventListener(childEventListenerComments);
    }
}