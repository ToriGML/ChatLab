package com.example.testes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import com.example.testes.contact.Adapter;
import com.example.testes.contact.Contact;
import com.example.testes.groups.AdapterGroups;
import com.example.testes.groups.Groups;
import com.example.testes.login.LoginActivity;
import com.example.testes.friends.FriendsActivity;
import com.example.testes.messages.AdapterMessages;
import com.example.testes.messages.Messages;
import com.example.testes.perfil.PerfilActivity;
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
    private List<Groups> listaGrupos;
    private RecyclerView groups;
    private Integer selectedGroup;
    private LinearLayout dialogRootLayout;
    private float startY;
    private float initialY;
    private boolean isDragging = false;
    private ImageView friends;
    private ImageView profile;
    private ImageView iconeUsuario;
    private Button enviarImagem;
    private ImageView sendIcon;
    private EditText inputMessage;
    FirebaseStorage storage;
    StorageReference mountainsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);

        inputMessage = findViewById(R.id.editTextTextPersonName);

///////////////Banco de dados/////////////////////////////////////////
        List<Messages> messagesList = new ArrayList<>();
        for (int j = 1; j <= 50; j++) {
            messagesList.add(new Messages("cccccc - "+ j, null, "ccc - " + j));
        }
//////////////////////////////////////////////////////////////////////

        groups = findViewById(R.id.grupos);

        trocarChat(messagesList);

        copularGrupos();

        groups.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                boolean result = gestureDetector.onTouchEvent(e);
                if (result) {
                    int position = rv.getChildAdapterPosition(rv.findChildViewUnder(e.getX(), e.getY()));
                    if (position != RecyclerView.NO_POSITION) {
                        selectedGroup = position;
                        System.out.println("Clicou no item: " + position);
                        AdapterGroups adapterGroups = new AdapterGroups(listaGrupos);
                        //Group grupo = adapterGroups.getItem(position)
                        //List<Messages> messagesList = chamando o banco com o id do grupo: grupo.getId()
                        List<Messages> messagesList = adapterGroups.getItem(position).getMessagesList();
                        trocarChat(messagesList);
                    }
                }
                return result;
            }
        });

        sendIcon = findViewById(R.id.imagemSend);
        sendIcon.setOnClickListener(view -> {
            AdapterGroups adapterGroups = new AdapterGroups(listaGrupos);
            List<Messages> groupMessages = adapterGroups.getItem(selectedGroup).getMessagesList();
            groupMessages.add(new Messages(inputMessage.getText().toString(), null, "eu mesmo"));
            inputMessage.setText("");
            trocarChat(groupMessages);
            System.out.println("Nova mensagem no grupo: " + selectedGroup);
        });

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
        mountainsRef = storage.getReference().child("imagens/" + currentUser.getEmail() + ".jpg");
        mountainsRef.getMetadata().addOnFailureListener(e -> showCustomDialog());
    }

    private void trocarChat(List<Messages> messagesList) {
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView messages = findViewById(R.id.mensagens);
        messages.setLayoutManager(new LinearLayoutManager(this));
        AdapterMessages adapterMessages = new AdapterMessages(messagesList);
        messages.setAdapter(adapterMessages);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    // a
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
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
            enviarImagemFireStorege(dialog);
        });
        dialog.show();
    }

    private void enviarImagemFireStorege(Dialog dialog) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando");
        progressDialog.show();
        iconeUsuario.setDrawingCacheEnabled(true);
        iconeUsuario.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) iconeUsuario.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Falha ao enviar a imagem", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            dialog.dismiss();
        });
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    iconeUsuario.setImageURI(selectedImageUri);
                }
            }
    );

///////////////Banco de dados/////////////////////////////////////////
    private void copularGrupos(){
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView groups = findViewById(R.id.grupos);
        groups.setLayoutManager(new LinearLayoutManager(this));
        listaGrupos = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            if (i % 2 == 0) {
                List<Messages> messagesList = new ArrayList<>();
                for (int j = 1; j <= 3; j++) {
                    messagesList.add(new Messages("aaaaaa - "+ j, null, "aaa - " + j));
                }
                listaGrupos.add(new Groups(R.drawable.ic_emoji, messagesList));
            } else {
                List<Messages> messagesList = new ArrayList<>();
                for (int j = 1; j <= 3; j++) {
                    messagesList.add(new Messages("bbbbbb - "+ j, null, "bbb - " + j));
                }
                listaGrupos.add(new Groups(R.drawable.logo, messagesList));
            }
        }
        AdapterGroups adapterGroups = new AdapterGroups(listaGrupos);
        groups.setAdapter(adapterGroups);
    }
///////////////////////////////////////////////////////////////////////

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogSearch() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout_search, null);
        RecyclerView searchFriends = dialogView.findViewById(R.id.searchFriends);
        searchFriends.setLayoutManager(new LinearLayoutManager(this));
///////////////Banco de dados/////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////
        Adapter adapter = new Adapter(listaContatos);
        searchFriends.setAdapter(adapter);

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
///////////////Banco de dados/////////////////////////////////////////
    public static String generateName() {
        String[] firstNames = {"Alice", "Bob", "Claire", "David", "Emma", "Frank", "Grace", "Henry", "Isabella", "Jack",
                "Kate", "Liam", "Mia", "Noah", "Olivia", "Paul", "Sophia", "Thomas", "Victoria", "William"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Malkova", "Davis", "Miller", "Taylor", "Clark", "Wilson",
                "Anderson", "Brown", "Campbell", "Clarkson", "Cox", "Edwards", "Garcia", "Green", "Hall", "Jones"};

        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        return firstName + " " + lastName;
    }
///////////////////////////////////////////////////////////////////////

    public void voltarLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
