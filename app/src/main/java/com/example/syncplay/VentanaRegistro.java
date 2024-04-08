package com.example.syncplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String correo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registro);

        ConstraintLayout layout = findViewById(R.id.constraintlayoutid);
        AnimationDrawable fondo = (AnimationDrawable) layout.getBackground();
        fondo.setEnterFadeDuration(2500);
        fondo.setExitFadeDuration(5000);
        fondo.start();

        Bundle bundle = getIntent().getExtras();
        correo = bundle.getString("correo");

        editTextNombreRegistro = (EditText) findViewById(R.id.editTextNombreRegistro);
        editTextCorreoRegistro = (EditText) findViewById(R.id.editTextCorreoRegistro);
        editTextContrasena = (EditText) findViewById(R.id.editTextContrasena);
        editTextTelefonoRegistro = (EditText) findViewById(R.id.editTextTelefonoRegistro);
        editTextUsuarioRegistro = (EditText) findViewById(R.id.editTextUsuario);
        botonRegistro = (Button) findViewById(R.id.buttonEnviar);

        editTextCorreoRegistro.setText(correo);

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
                annadirRegistro();
            }
        });






    }

    synchronized private void annadirRegistro(){
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

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