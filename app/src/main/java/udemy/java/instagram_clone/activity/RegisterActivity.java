package udemy.java.instagram_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Base64;

import udemy.java.instagram_clone.databinding.ActivityRegisterBinding;
import udemy.java.instagram_clone.config.FirebaseConfiguration;
import udemy.java.instagram_clone.helper.Base64Custom;
import udemy.java.instagram_clone.model.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth userAuthentication;

    private Button addUser;
    private EditText createName, createEmail, createPassword;

    private String textName, textEmail, textPassword;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        createName = binding.editTextRegisterName;
        createEmail = binding.editTextRegisterEmail;
        createPassword = binding.editTextRegisterPassword;
        addUser = binding.buttonRegisterUser;

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

    private void createUser() {

        if (!textName.isEmpty()){
            if(!textEmail.isEmpty()){
                if (!textPassword.isEmpty()){

                    user = new User();
                    user.setName(textName);
                    user.setEmail(textEmail);
                    user.setPassword(textPassword);

                    registerUser();

                }else {
                    Toast.makeText(this, "Intreduza uma password", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Intreduza uma email", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Intreduza uma nome", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {

        userAuthentication = FirebaseConfiguration.getUserAuthentication();
        userAuthentication.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Sucesso a criar utilizador! ", Toast.LENGTH_SHORT).show();

                    try {

                        String idUserIdentification = Base64Custom.encryption(user.getEmail());
                        user.setuID(idUserIdentification);
                        user.saveUser();

                    } catch (Exception exception){
                        exception.printStackTrace();
                    }


                    //startActivity( new Intent(RegisterActivity.this, LoginActivity.class));


                } else {

                }
            }
        });
    }
}