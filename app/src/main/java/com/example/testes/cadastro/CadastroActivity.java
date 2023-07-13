package com.example.testes.cadastro;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        senhaText = findViewById(R.id.textInpuTextSenha);
        confirmarSenhaText = findViewById(R.id.textInpuTextConfirmarSenha);
        loginText = findViewById(R.id.textInputTextEmail);

        Button cadastrarButton = findViewById(R.id.botaoCadastrar);
        Button voltarEntrarButton = findViewById(R.id.botaoVoltarEntrar);

        cadastrarButton.setOnClickListener(view -> {
            String senha = senhaText.getText().toString();
            String confirmarSenha = confirmarSenhaText.getText().toString();
            String login = loginText.getText().toString();
            if (login.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                showSnackbar(view, "Há dados não preenchidos");
                return;
            }
            if (!senha.equals(confirmarSenha)) {
                showSnackbar(view, "As senha não conferem");
                return;
            }
        });

        voltarEntrarButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }

}
