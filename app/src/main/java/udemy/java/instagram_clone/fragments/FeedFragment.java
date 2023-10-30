package udemy.java.instagram_clone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import udemy.java.instagram_clone.adapter.FeedAdapter;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.FragmentFeedBinding;
import udemy.java.instagram_clone.model.Feed;

public class FeedFragment extends Fragment {

    private FragmentFeedBinding binding;

    private RecyclerView recyclerViewFeed;
    private FeedAdapter feedAdapter;

    private List<Feed> feedList = new ArrayList<>();

    private ValueEventListener valueEventListenerPostFeed;

    private DatabaseReference databaseReferenceFeed;

    private String userLoggedId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userLoggedId = UserFirebase.getUserIdentification();

        databaseReferenceFeed = ConfigurationFirebase.getDatabaseReference()
                .child("feed" )
                .child( userLoggedId );


        recyclerViewFeed = binding.recyclerViewFeedFragment;

        feedAdapter = new FeedAdapter(feedList, getActivity());
        recyclerViewFeed.setHasFixedSize(true);
        recyclerViewFeed.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerViewFeed.setAdapter(feedAdapter);

        getListFeedPost();

    }

    private void getListFeedPost() {

        valueEventListenerPostFeed = databaseReferenceFeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                feedList.clear();

                for ( DataSnapshot dataSnapshot: snapshot.getChildren() ) {
                   feedList.add( dataSnapshot.getValue(Feed.class) );
                }
                Collections.reverse(feedList);
                feedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getListFeedPost();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReferenceFeed.removeEventListener( valueEventListenerPostFeed );
    }
}