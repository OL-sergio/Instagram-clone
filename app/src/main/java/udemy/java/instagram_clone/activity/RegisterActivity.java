package udemy.java.instagram_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.config.ConfigurationFirebase;
import udemy.java.instagram_clone.config.UserFirebase;
import udemy.java.instagram_clone.databinding.ActivityRegisterBinding;
import udemy.java.instagram_clone.model.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth userAuthentication;

    private Button addUser;
    private EditText createName, createEmail, createPassword;

    private String textName, textEmail, textPassword;
    private ProgressBar progressBar;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        startComponents();


        progressBar.setVisibility(View.GONE);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textName = createName.getText().toString();
                textEmail = createEmail.getText().toString();
                textPassword = createPassword.getText().toString();

                createUser();
            }
        });
    }

    private void startComponents() {
        createName = binding.editTextRegisterName;
        createEmail = binding.editTextRegisterEmail;
        createPassword = binding.editTextRegisterPassword;
        addUser = binding.buttonRegisterUser;
        progressBar = binding.progressBarRegister;

        createName.requestFocus();
    }

    private void createUser() {
        if (!textName.isEmpty()){
            if(!textEmail.isEmpty()){
                if (!textPassword.isEmpty()){

                    user = new User();
                    user.setName(textName);
                    user.setEmail(textEmail);
                    user.setPassword(textPassword);

                    register(user);

                }else {
                    Toast.makeText(RegisterActivity.this, R.string.intreduza_uma_password, Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RegisterActivity.this, R.string.intreduza_uma_email, Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(RegisterActivity.this, R.string.intreduza_uma_nome, Toast.LENGTH_SHORT).show();
        }
    }

    private void register(User user) {

        progressBar.setVisibility(View.VISIBLE);

        userAuthentication = ConfigurationFirebase.getUserAuthentication();
        userAuthentication.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    try {

                        progressBar.setVisibility(View.GONE);


                        //Saving user data on firebase
                        String idUserIdentification = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        user.setUID(idUserIdentification);
                        user.saveUser();

                        //Saving profile user data on firebase
                        UserFirebase.updateUserName(user.getName());

                        Toast.makeText(getBaseContext(), R.string.sucesso_a_criar_conta, Toast.LENGTH_SHORT).show();

                        startActivity( new Intent(RegisterActivity.this, MainActivity.class));

                    } catch (Exception exception){
                        exception.printStackTrace();
                    }



                } else {
                    progressBar.setVisibility(View.GONE);
                    userValidation(task);
                    
                }
            }
        });
    }

    private void userValidation(Task<AuthResult> task) {

            String exception;
            try {
                throw  task.getException();
            } catch (FirebaseAuthWeakPasswordException e ) {
                exception = getString(R.string.intreduza_senha_mais_forte);
            } catch (FirebaseAuthInvalidCredentialsException e ) {
                exception = getString(R.string.intreduza_email_valido);
            } catch (FirebaseAuthUserCollisionException e ) {
                exception = getString(R.string.Esta_conta_existe);
            } catch (Exception e ){
                exception = getString(R.string.Erro_criar_utilizador) + e.getMessage();
                e.printStackTrace();
            }
            Toast.makeText(RegisterActivity.this, exception, Toast.LENGTH_SHORT).show();

    }
}