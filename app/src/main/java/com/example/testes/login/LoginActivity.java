package com.example.testes.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testes.MainActivity;
import com.example.testes.R;
import com.example.testes.cadastro.CadastroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

        TextInputEditText email = findViewById(R.id.txtEmail);
        TextInputEditText senha = findViewById(R.id.txtSenha);

        Button botaoCadastro = findViewById(R.id.botaoCadastro);
        botaoCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
            startActivity(intent);
        });

        Button botaoEntrar = findViewById(R.id.botaoEntrar);
        botaoEntrar.setOnClickListener(view -> {
            singin(email.getText().toString(), senha.getText().toString());
        });

    }

    public void singin(String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCustomToken:success");
                entrar();
            } else {
                Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                Toast.makeText(getApplicationContext(), "Falha ao fazer o login", Toast.LENGTH_SHORT).show();
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
        if(currentUser != null){
            entrar();
        }
    }

    public void entrar() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
