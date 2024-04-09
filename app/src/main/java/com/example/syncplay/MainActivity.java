package com.example.syncplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private Button botonInicioSesion;
    private EditText editTextDireccion;
    private EditText editTextPassword;
    private TextView textviewRegistro;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = findViewById(R.id.mainlayout);
        AnimationDrawable fondo = (AnimationDrawable) layout.getBackground();
        fondo.setEnterFadeDuration(2500);
        fondo.setExitFadeDuration(5000);
        fondo.start();

        db = FirebaseFirestore.getInstance();

        botonInicioSesion = (Button) findViewById(R.id.buttonIniciarSesion);
        editTextDireccion = (EditText) findViewById(R.id.editTextCorreoElectronico);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewRegistro = (TextView) findViewById(R.id.textViewRegistro);

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Oculta el teclado cuando se detecta un evento de toque en la vista raíz
                hideKeyboard(MainActivity.this);
                return false;
            }
        });

        textviewRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Accediendo al registro", Toast.LENGTH_SHORT).show();

                Intent actividadRegistro = new Intent(MainActivity.this, VentanaRegistro.class);
                startActivity(actividadRegistro);
            }
        });

        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextDireccion.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                    iniciarSesion(editTextDireccion.getText().toString(), editTextPassword.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Rellena todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void iniciarSesion(String correo, String contrasena){
        db.collection("users").document(correo).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // El documento del usuario existe en Firestore
                                // Recupera los datos del usuario
                                String storedEmail = document.getString("Correo");
                                String storedPassword = document.getString("Contrasena");

                                // Verifica si los datos proporcionados coinciden con los datos almacenados
                                if (correo.equals(storedEmail) && contrasena.equals(storedPassword)) {
                                    // Inicio de sesión exitoso
                                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                    Intent actividadPrincipal = new Intent(MainActivity.this, VentanaPrincipal.class);
                                    startActivity(actividadPrincipal);
                                } else {
                                    // Credenciales incorrectas
                                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // El usuario no existe en Firestore
                                Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error al obtener el documento del usuario
                            Log.d("LoginActivity", "Error getting document: ", task.getException());
                            Toast.makeText(MainActivity.this, "Error al obtener el documento del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void hideKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void alerta(String problema){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Establecemos el título del diálogo
        builder.setTitle("¡¡Atención!!");
        builder.setMessage(problema);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}