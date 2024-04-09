package com.example.syncplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VentanaRegistro extends AppCompatActivity {

    private Spinner spinerSexo;
    private EditText editTextNombreRegistro,editTextCorreoRegistro,editTextContrasena,editTextTelefonoRegistro,editTextUsuarioRegistro;
    private Button botonRegistro;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registro);

        ConstraintLayout layout = findViewById(R.id.constraintlayoutid);
        AnimationDrawable fondo = (AnimationDrawable) layout.getBackground();
        fondo.setEnterFadeDuration(2500);
        fondo.setExitFadeDuration(5000);
        fondo.start();

        db = FirebaseFirestore.getInstance();

        spinerSexo = (Spinner) findViewById(R.id.spinnerSexo);
        editTextNombreRegistro = (EditText) findViewById(R.id.editTextNombreRegistro);
        editTextCorreoRegistro = (EditText) findViewById(R.id.editTextCorreoRegistro);
        editTextContrasena = (EditText) findViewById(R.id.editTextContrasena);
        editTextTelefonoRegistro = (EditText) findViewById(R.id.editTextTelefonoRegistro);
        editTextUsuarioRegistro = (EditText) findViewById(R.id.editTextUsuario);
        botonRegistro = (Button) findViewById(R.id.buttonEnviar);
        editTextCorreoRegistro = (EditText) findViewById(R.id.editTextCorreoRegistro);

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Oculta el teclado cuando se detecta un evento de toque en la vista raíz
                hideKeyboard(VentanaRegistro.this);
                return false;
            }
        });

        editTextNombreRegistro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });



        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextContrasena.getText().toString().equals("") && !editTextCorreoRegistro.getText().toString().equals("") &&
                    !editTextNombreRegistro.getText().toString().equals("") && spinerSexo.getSelectedItem() != null &&
                    !editTextTelefonoRegistro.getText().toString().equals("") && !editTextUsuarioRegistro.getText().toString().equals("")) {
                    annadirRegistro(editTextContrasena.getText().toString(), editTextCorreoRegistro.getText().toString(),
                                    editTextNombreRegistro.getText().toString(), spinerSexo.getSelectedItem().toString(),
                                    editTextTelefonoRegistro.getText().toString(), editTextUsuarioRegistro.getText().toString());
                } else {
                    Toast.makeText(VentanaRegistro.this, "Rellena todos los campos", Toast.LENGTH_LONG).show();
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

    synchronized private void annadirRegistro(String contrasena, String correo, String nombre, String sexo, String telefono, String usuario){
        Map<String, Object> user = new HashMap<>();
        user.put("Contrasena", contrasena);
        user.put("Correo", correo);
        user.put("Nombre", nombre);
        user.put("Sexo",sexo);
        user.put("Telefono",telefono);
        user.put("Usuario",usuario);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("hola", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("adios", "Error adding document", e);
                    }
                });
    }

}