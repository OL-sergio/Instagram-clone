package udemy.java.instagram_clone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import udemy.java.instagram_clone.databinding.FragmentSearchBinding;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private SearchView searchForUsers;
    private RecyclerView recyclerViewSearchedUsers;


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




                return true;
            }
        });


    }
}