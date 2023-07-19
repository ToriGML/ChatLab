package com.example.testes.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.testes.Adapter;
import com.example.testes.Contact;
import com.example.testes.MainActivity;
import com.example.testes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FriendsActivity extends AppCompatActivity {

    private ImageView search;
    private ImageView settings;
    private Dialog dialog;
    private float startY;
    private float initialY;
    private boolean isDragging = false;
    private LinearLayout dialogRootLayout;
    private ImageView chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        chat = findViewById(R.id.chat);
        chat.setOnClickListener(view -> {
            Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSettings();
            }
        });
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSearch();
            }
        });

    }

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
}