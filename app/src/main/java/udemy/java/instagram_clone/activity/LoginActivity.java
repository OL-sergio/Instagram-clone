package udemy.java.instagram_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.config.ConfigurationFirebase;

import udemy.java.instagram_clone.databinding.ActivityLoginBinding;
import udemy.java.instagram_clone.model.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth userAuthentication;

    private User user;

    private TextView goToCreateUser;
    private EditText enterEmail, enterPassword;
    private Button longinUser;
    private ProgressBar progressBar;

    private String textEmail,textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        startComponents();


        longinUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                textEmail = binding.editTextLoginEmail.getText().toString();
                textPassword = binding.editTextLoginPassword.getText().toString();

                userAuthentication();

            }
        });

        goToCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }

    private void startComponents() {

        enterEmail = binding.editTextLoginEmail;
        enterPassword  = binding.editTextLoginPassword;
        longinUser = binding.buttonLoginApp;
        goToCreateUser = binding.textViewCreateAccount;
        progressBar = binding.progressBarLogin;

        enterEmail.requestFocus();

    }

    private void userAuthentication() {

        if (!textEmail.isEmpty()){
            if(!textPassword.isEmpty()){
                user = new User();
                user.setEmail(textEmail);
                user.setPassword(textPassword);

                loginToAccount();

            }else {
                Toast.makeText(this, R.string.intreduza_uma_password, Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, R.string.intreduza_uma_email, Toast.LENGTH_SHORT).show();
        }

    }

    private void loginToAccount() {
        progressBar.setVisibility(View.VISIBLE);

        userAuthentication = ConfigurationFirebase.getUserAuthentication();
        userAuthentication.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    goToApp();

                }else {
                    userValidation(task);

                }

            }
        });
    }

    private void userValidation(Task task) {

        progressBar.setVisibility(View.GONE);
        String exception;
        try {
            throw Objects.requireNonNull(task.getException());

        } catch (FirebaseAuthInvalidCredentialsException e ) {
            exception = getString(R.string.intreduza_eamil_password_validos) ;
        } catch (FirebaseAuthInvalidUserException e ){
            exception = getString(R.string.intreduza_utilizador_correcto);
        } catch (Exception e ){
            exception = getString(R.string.erro_realizar_login) + e.getMessage();
            e.printStackTrace();
        }
        Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();

    }

    private void goToApp() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userLogged = ConfigurationFirebase.getUserAuthentication().getCurrentUser();
        if(userLogged != null){
            goToApp();
        }
    }
}