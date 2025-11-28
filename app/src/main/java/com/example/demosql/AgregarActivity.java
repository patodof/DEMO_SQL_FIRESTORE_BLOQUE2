package com.example.demosql;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        //CODIGO PARA CONECTARNOS A NUESTRA BD
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE,null);

        //1.- CREAR TABLA
        db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (ID INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE VARCHAR,APELLIDOS VARCHAR,EDAD INTEGER)");
        */
        //2.- Asociar Boton
        Button boton_agregar = findViewById(R.id.boton_editar);

        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cuando se presione agregar

                //obtener valores de input
                EditText input_nombre = findViewById(R.id.input_nombre);
                EditText input_apellidos = findViewById(R.id.input_apellidos);
                EditText input_edad = findViewById(R.id.input_edad);

                //Crear INSERT para nuestra base de datos

                //obtener valores de input
                String nombre = input_nombre.getText().toString();
                String apellido = input_apellidos.getText().toString();
                String edad = input_edad.getText().toString();

                //Declar conexion firebase
                FirebaseFirestore db_firebase = FirebaseFirestore.getInstance();

                //Declarar referencias
                CollectionReference coleccion = db_firebase.collection("estudiantes");
                DocumentReference nuevo_documento = coleccion.document(); //creamos nuevo documento en firebase

                //obtener ID del nuevo documento desde firebase
                String id_documento = nuevo_documento.getId();

                //Generar Objeto Firebase de Estudiante
                Map<String, Object> objeto_firebase = new HashMap<>();
                objeto_firebase.put("ID", id_documento);
                objeto_firebase.put("NOMBRE", nombre);
                objeto_firebase.put("APELLIDOS", apellido);
                objeto_firebase.put("EDAD", Integer.parseInt(edad) );

                //guardar objeto en firebase
                nuevo_documento.set(objeto_firebase);

                //Redireccionar a Listar
                Intent  intent = new Intent(AgregarActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });


    }
}