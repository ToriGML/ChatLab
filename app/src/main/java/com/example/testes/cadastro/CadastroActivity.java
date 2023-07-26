package com.example.testes.cadastro;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testes.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText senhaText;
    private TextInputEditText confirmarSenhaText;
    private TextInputEditText loginText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        senhaText = findViewById(R.id.textInpuTextSenha);
        confirmarSenhaText = findViewById(R.id.textInpuTextConfirmarSenha);
        loginText = findViewById(R.id.textInputTextEmail);

        Button cadastrarButton = findViewById(R.id.botaoCadastrar);
        Button voltarEntrarButton = findViewById(R.id.botaoVoltarEntrar);

        cadastrarButton.setOnClickListener(view -> {
            String senha = senhaText.getText().toString();
            String confirmarSenha = confirmarSenhaText.getText().toString();
            String email = loginText.getText().toString();
            if (email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                showSnackbar(view, "Há dados não preenchidos");
                return;
            }
            if (!senha.equals(confirmarSenha)) {
                showSnackbar(view, "As senha não conferem");
                return;
            }
            cadastrar(email, senha);
        });

        voltarEntrarButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void cadastrar(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCustomToken:success");
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Falha ao fazer o cadastro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }

}
