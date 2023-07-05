package com.example.testes.cadastro;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testes.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CadastroActivity extends AppCompatActivity {

    private TextInputLayout senhaLayout;
    private TextInputLayout confirmarSenhaLayout;
    private TextInputEditText senhaText;
    private TextInputEditText confirmarSenhaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        senhaLayout = findViewById(R.id.textInputLayoutSenha);
        confirmarSenhaLayout = findViewById(R.id.textInputLayoutConfirmarSenha);
        senhaText = findViewById(R.id.textInpuTextSenha);
        confirmarSenhaText = findViewById(R.id.textInpuTextConfirmarSenha);

        Button cadastrarButton = findViewById(R.id.botaoCadastrar);
        Button voltarEntrarButton = findViewById(R.id.botaoVoltarEntrar);

        cadastrarButton.setOnClickListener(view -> {
            String senha = senhaText.getText().toString();
            String confirmarSenha = confirmarSenhaText.getText().toString();
            if (!senha.equals(confirmarSenha)) {
                confirmarSenhaLayout.setError("As senha não conferem");
            } else {
                confirmarSenhaLayout.setError(null);
            }
        });

        voltarEntrarButton.setOnClickListener(view -> {
            finish();
        });
    }

}
