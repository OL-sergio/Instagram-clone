package udemy.java.instagram_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.config.ConfigurationFirebase;

import udemy.java.instagram_clone.databinding.ActivityMainBinding;
import udemy.java.instagram_clone.fragments.AccountFragment;
import udemy.java.instagram_clone.fragments.HomeFragment;
import udemy.java.instagram_clone.fragments.SearchFragment;
import udemy.java.instagram_clone.fragments.ShareFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;
    private BottomNavigationView btmNav;


    final Fragment homeFragment = new HomeFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment shareFragment = new ShareFragment();
    final Fragment accountFragment = new AccountFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        toolbar = binding.toolbar.toolbarMain;
        toolbar.setTitle("Instagram");
        setSupportActionBar(toolbar);

        firebaseAuth = ConfigurationFirebase.getUserAuthentication();

        btmNav = binding.navigation.bottomNavigation;
        btmNav.setActivated(true);
        btmNav.animate();
        btmNav.clearAnimation();
        //btmNav.getMenu().setGroupCheckable(0, true, true);
        //btmNav.getMenu().findItem(R.id.home).setChecked(true);
        //btmNav.findViewById(R.id.home).setVisibility(View.GONE);

        bottomNavigation();

    }

    private void bottomNavigation() {

        Bundle bundle = null;

        if (bundle == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.viewPager, searchFragment).commit();
        }

        btmNav.setOnItemSelectedListener(item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (item.getItemId() == R.id.home) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewPager, homeFragment).commit();

            } else if (item.getItemId() == R.id.search) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewPager, searchFragment).commit();

            } else if (item.getItemId() == R.id.share) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewPager, shareFragment).commit();

            } else if (item.getItemId() == R.id.account) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewPager, accountFragment).commit();

            }

            return true;
        });



        Menu menu = btmNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //BadgeDrawable badge = btmNav.getOrCreateBadge(R.id.home);
        //badge.setNumber(7);
        //badge.setVisible(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_logOut) {
            logOutUser();
            startActivity( new Intent(MainActivity.this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOutUser() {
        try {

            firebaseAuth.signOut();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}