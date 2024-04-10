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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    private Button botonInicioSesion;
    private EditText editTextDireccion, editTextPassword;
    private TextView textviewRegistro;
    private FirebaseFirestore db;
    boolean correoOK;

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
        correoOK = false;
        db.collection("users").whereEqualTo("Correo", correo).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Obtener la contraseña y el correo almacenads en Firebase para el usuario
                                correoOK = true;
                                String contrasenaFirebase = document.getString("Contrasena");

                                // Verificar si la contraseña ingresada coincide con la almacenada en Firebase
                                if (contrasenaFirebase.equals(contrasena)) {
                                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                    Intent actividadPrincipal = new Intent(MainActivity.this, VentanaPrincipal.class);
                                    actividadPrincipal.putExtra("Usuario", document.getString("Usuario"));
                                    startActivity(actividadPrincipal);
                                } else {
                                    // Si no se encuentra el correo, mostrar un mensaje de error
                                    Toast.makeText(MainActivity.this, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!correoOK) {
                                Toast.makeText(MainActivity.this, "Correo no registrado", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
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
}