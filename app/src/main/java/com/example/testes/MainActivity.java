package com.example.testes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.example.testes.login.LoginActivity;
import com.example.testes.friends.FriendsActivity;
import com.example.testes.perfil.PerfilActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private ImageView search;
    private ImageView settings;
    private DrawerLayout drawerLayout;
    private Dialog dialog;
    private LinearLayout dialogRootLayout;
    private float startY;
    private float initialY;
    private boolean isDragging = false;
    private ImageView friends;
    private ImageView profile;

    private ImageView iconeUsuario;
    private Button enviarImagem;
    FirebaseStorage storage;
    StorageReference mountainsRef;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
            finish();
        });

        friends = findViewById(R.id.friends);
        friends.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(v -> showDialogSettings());
        search = findViewById(R.id.search);
        search.setOnClickListener(v -> showDialogSearch());

        storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mountainsRef = storage.getReference().child("/images/" + currentUser.getUid());
        mountainsRef.getMetadata().addOnFailureListener(e -> showCustomDialog());
    }
// a
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.seletor_imagem_dialog);
        CardView cardView = dialog.findViewById(R.id.cardView);
        iconeUsuario = dialog.findViewById(R.id.icone);
        enviarImagem = dialog.findViewById(R.id.enviarImagem);
        cardView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                galleryLauncher.launch(intent);
            }
        });
        enviarImagem.setOnClickListener(view -> {
            coletandoImagem(dialog);
        });
        dialog.show();
    }

    private void coletandoImagem(Dialog dialog) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando");
        progressDialog.show();
        iconeUsuario.setDrawingCacheEnabled(true);
        iconeUsuario.buildDrawingCache();
        enviarImagemFireBase(dialog, progressDialog);
    }

    private void enviarImagemFireBase(Dialog dialog, ProgressDialog progressDialog) {
        String filename = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(selectedImageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            Toast.makeText(MainActivity.this, "Imagem enviada com sucesso", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            progressDialog.dismiss();
                        });
                    }})
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Erro ao enviar imagem", Toast.LENGTH_SHORT).show();
                });
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    iconeUsuario.setImageURI(selectedImageUri);
                }
            }
    );

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogSearch() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_search, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.searchFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String nomeContato;
        List<Contact> listaContatos = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            nomeContato = generateName();
            if (i % 2 == 0) {
                listaContatos.add(new Contact(nomeContato, R.drawable.ic_emoji));
            } else {
                listaContatos.add(new Contact(nomeContato, R.drawable.logo));
            }
        }
        Adapter adapter = new Adapter(listaContatos);
        recyclerView.setAdapter(adapter);

        dialog.setContentView(dialogView);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1600);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialogRootLayout = dialog.findViewById(R.id.search_root_layout);

        dialogRootLayout.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            float y = event.getRawY();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startY = y;
                    initialY = dialogRootLayout.getY();
                    isDragging = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = y - startY;

                    if (Math.abs(deltaY) > 10) {
                        isDragging = true;

                        float newY = initialY + deltaY;
                        if (newY >= 0 && newY <= dialogRootLayout.getHeight()) {
                            dialogRootLayout.setY(newY);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (isDragging) {
                        isDragging = false;
                        float finalY = dialogRootLayout.getY();
                        float threshold = dialogRootLayout.getHeight() / 6;

                        if (finalY < threshold) {
                            // Abre o bottom sheet completamente
                            dialogRootLayout.setY(0);
                        } else {
                            // Fecha o bottom sheet
                            dialogRootLayout.animate()
                                    .translationY(dialogRootLayout.getHeight())
                                    .setInterpolator(new AccelerateInterpolator())
                                    .setDuration(300)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Define o posicionamento correto do diálogo antes de descartá-lo
                                            dialog.getWindow().setAttributes(dialog.getWindow().getAttributes());
                                            dialog.dismiss();
                                        }
                                    })
                                    .start();
                        }
                    }
                    break;
            }

            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogSettings() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_settings);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1600);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialogRootLayout = dialog.findViewById(R.id.settings_root_layout);

        Button exit = dialog.findViewById(R.id.exit);
        exit.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Você deseja sair?");
            builder.setPositiveButton("Sair", (dialog, id) -> {
                FirebaseAuth.getInstance().signOut();
                voltarLogin();
            });
            builder.setNegativeButton("Cancelar", (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        dialogRootLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float y = event.getRawY();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startY = y;
                        initialY = dialogRootLayout.getY();
                        isDragging = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaY = y - startY;

                        if (Math.abs(deltaY) > 10) {
                            isDragging = true;

                            float newY = initialY + deltaY;
                            if (newY >= 0 && newY <= dialogRootLayout.getHeight()) {
                                dialogRootLayout.setY(newY);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (isDragging) {
                            isDragging = false;
                            float finalY = dialogRootLayout.getY();
                            float threshold = dialogRootLayout.getHeight() / 6;

                            if (finalY < threshold) {
                                // Abre o bottom sheet completamente
                                dialogRootLayout.setY(0);
                            } else {
                                // Fecha o bottom sheet
                                dialogRootLayout.animate()
                                        .translationY(dialogRootLayout.getHeight())
                                        .setInterpolator(new AccelerateInterpolator())
                                        .setDuration(300)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Define o posicionamento correto do diálogo antes de descartá-lo
                                                dialog.getWindow().setAttributes(dialog.getWindow().getAttributes());
                                                dialog.dismiss();
                                            }
                                        })
                                        .start();
                            }
                        }
                        break;
                }

                return true;
            }
        });
    }

    public void openSidebar(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static String generateName() {
        String[] firstNames = {"Alice", "Bob", "Claire", "David", "Emma", "Frank", "Grace", "Henry", "Isabella", "Jack",
                "Kate", "Liam", "Mia", "Noah", "Olivia", "Paul", "Sophia", "Thomas", "Victoria", "William"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Davis", "Miller", "Taylor", "Clark", "Wilson",
                "Anderson", "Brown", "Campbell", "Clarkson", "Cox", "Edwards", "Garcia", "Green", "Hall", "Harris"};

        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        return firstName + " " + lastName;
    }

    public void voltarLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
