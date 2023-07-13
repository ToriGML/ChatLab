package com.example.testes.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testes.MainActivity;
import com.example.testes.R;
import com.example.testes.cadastro.CadastroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Button botaoCadastro = findViewById(R.id.botaoCadastro);
        botaoCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        Button botaoEntrar = findViewById(R.id.botaoEntrar);
        botaoEntrar.setOnClickListener(view -> {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
            singin("gmail@gmail.com", "123");
        });

    }

    public void singin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCustomToken:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    // updateUI(user);
                } else {
                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }

    public void singout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

}
