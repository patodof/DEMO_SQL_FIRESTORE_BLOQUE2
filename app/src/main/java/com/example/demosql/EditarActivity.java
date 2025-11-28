package com.example.demosql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //obtener ID desde Intent
        String ID_ELEMENTO = getIntent().getStringExtra("ID");
        Estudiante objeto_estudiante = new Estudiante();

        //buscar id en firebase
        FirebaseFirestore db_firebase = FirebaseFirestore.getInstance();

        db_firebase.collection("estudiantes").document(ID_ELEMENTO).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    //todo ok con firebase
                    DocumentSnapshot documento = task.getResult();

                    objeto_estudiante.ID = documento.getString("ID");
                    objeto_estudiante.NOMBRE = documento.getString("NOMBRE");
                    objeto_estudiante.APELLIDOS = documento.getString("APELLIDOS");
                    objeto_estudiante.EDAD = documento.getLong("EDAD").intValue();


                    //7 cargar informacion a UI
                    //obtener elementos desde la UI
                    TextView editar_titulo = findViewById(R.id.editar_titulo);
                    EditText input_nombre = findViewById(R.id.input_nombre);
                    EditText input_apellidos = findViewById(R.id.input_apellidos);
                    EditText input_edad = findViewById(R.id.input_edad);

                    //cargar info a inputs
                    editar_titulo.setText("Editar Estudiante #"+objeto_estudiante.ID);
                    input_nombre.setText(objeto_estudiante.NOMBRE);
                    input_apellidos.setText(objeto_estudiante.APELLIDOS);
                    input_edad.setText(String.valueOf(objeto_estudiante.EDAD)); //String.valueOf(objeto_estudiante.EDAD) -> devuelve EDAD (actualmente es int) como string

                }
                else
                {
                    //error con firebase
                }
            }
        });

        //8 Asociar Boton
        Button boton_editar = findViewById(R.id.boton_editar);

        boton_editar.setOnClickListener(new View.OnClickListener() {
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

                //Actualizar elemento en Firebase
                FirebaseFirestore db_firebase = FirebaseFirestore.getInstance();

                //Crear objeto que se editara en Firebase
                Map<String, Object> objeto_firebase = new HashMap<>();
                objeto_firebase.put("ID", ID_ELEMENTO);
                objeto_firebase.put("NOMBRE", nombre);
                objeto_firebase.put("APELLIDOS", apellido);
                objeto_firebase.put("EDAD", Integer.parseInt(edad));

                //Actualizar en firebase
                db_firebase.collection("estudiantes").document(ID_ELEMENTO).update(objeto_firebase);

                //Redireccionar a Listar
                Intent intent = new Intent(EditarActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });
    }
}