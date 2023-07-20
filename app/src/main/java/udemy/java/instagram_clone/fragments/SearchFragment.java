package udemy.java.instagram_clone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import udemy.java.instagram_clone.adapter.SearchAdapter;
import udemy.java.instagram_clone.config.ConfigurationFirebase;

import udemy.java.instagram_clone.databinding.FragmentSearchBinding;
import udemy.java.instagram_clone.model.User;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private SearchView searchForUsers;
    private RecyclerView recyclerViewSearchedUsers;

    private List<User> userList;
    private DatabaseReference usersReference;

    public SearchAdapter searchAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        searchForUsers = binding.searchViewSearchUsers;
        recyclerViewSearchedUsers = binding.recyclerViewSearchUsers;

        userList = new ArrayList<>();
        usersReference = ConfigurationFirebase.getDatabaseReference()
                .child("users");

        recyclerViewSearchedUsers.setHasFixedSize(true);
        recyclerViewSearchedUsers.setLayoutManager(new LinearLayoutManager( getActivity() ));

        searchAdapter = new SearchAdapter(userList, getActivity());
        recyclerViewSearchedUsers.setAdapter(searchAdapter);


        searchForUsers.setQueryHint("Procurar por amigos");
        searchForUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("onQueryTextSubmit", "intredução de texto " +  query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("onQueryTextChange", "intredução de texto " +  newText);

                String textInput = newText.toUpperCase();
                textSearchForUsers( textInput );
                return true;
            }
        });


    }

    private void textSearchForUsers(String textSearch) {

        userList.clear();

        //Search for users case text on search
        if (textSearch.length() > 2 ){

            Query query = usersReference.orderByChild("name")
                    .startAt(textSearch)
                    .endAt(textSearch + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    userList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        userList.add(dataSnapshot.getValue(User.class));

                    }

                    searchAdapter.notifyDataSetChanged();
                    /*
                    *
                    int totalUsers = userList.size();
                    Log.i("dataSnapshot", "SearchForUsers: " +  totalUsers);
                    */
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}