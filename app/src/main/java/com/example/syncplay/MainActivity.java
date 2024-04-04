package com.example.syncplay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button botonRegistro,botonInicioSesion;
    private EditText editTextDireccion;

    private TextView textviewRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener una referencia a la vista personalizada
        AnimatedShapesView animatedShapesView = findViewById(R.id.animatedShapesView);

        // Iniciar la animación
        animatedShapesView.startAnimation();

        //botonRegistro = (Button) findViewById(R.id.buttonRegistrarse);
        botonInicioSesion = (Button) findViewById(R.id.buttonIniciarSesion);
        editTextDireccion = (EditText) findViewById(R.id.editTextCorreoElectronico);
        textviewRegistro = (TextView) findViewById(R.id.textViewRegistro);

        textviewRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse();
            }
        });

        /*botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse();
            }
        });*/

        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

    }

    private void registrarse(){
        /*if(editTextDireccion.getText().length()<1){
            alerta("No has introducido ningún correo");
        }else{
            String email = editTextDireccion.getText().toString();
            String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]+$";
            if(email.matches(regex)){*/
                Toast.makeText(this, "Accediendo al registro", Toast.LENGTH_SHORT).show();

                Intent actividadRegistro = new Intent(this, VentanaRegistro.class);
                actividadRegistro.putExtra("correo",String.valueOf(editTextDireccion.getText()));
                startActivity(actividadRegistro);
           /* }else{
                alerta("Ese correo no existe");
            }

        }*/
    }

    private void iniciarSesion(){

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