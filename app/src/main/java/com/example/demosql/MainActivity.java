package com.example.demosql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Estudiante> lista_estudiantes = new ArrayList<Estudiante>();
        ArrayList<String> arreglo = new ArrayList<>();
        ListView listViewDatos = findViewById(R.id.lista_estudiantes);

        FirebaseFirestore db_firebase = FirebaseFirestore.getInstance();

        //Obtener datos desde Firebase
        db_firebase.collection("estudiantes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() )
                {
                    //LLAMADA COMPLETA A FIREBASE
                    //iterar resultados obtenidos
                    for(QueryDocumentSnapshot documento :  task.getResult() )
                    {
                        //poblar objetos
                        Estudiante obj = new Estudiante();
                        obj.ID = documento.getString("ID");
                        obj.NOMBRE = documento.getString("NOMBRE");
                        obj.APELLIDOS = documento.getString("APELLIDOS");
                        obj.EDAD = documento.getLong("EDAD").intValue();

                        lista_estudiantes.add(obj);

                        //agregar a lista para ArrayAdapter
                        arreglo.add("# "+obj.ID+" "+obj.NOMBRE+" "+obj.APELLIDOS);
                    }

                    //agregar informacion al adapter
                    //5.- Cargar informacion a ListView
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1 ,arreglo);

                    //6.- Declarar ListView
                    listViewDatos.setAdapter(adapter);
                }
                else
                {
                    //ERROR CON FIREBASE
                }
            }
        });

        //boton agregar
        Button boton_agregar = findViewById(R.id.boton_editar);
        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarActivity.class);
                startActivity(intent);
            }
        });

        //11-11-2025
        //Listview al hacer click ir a detalle activity
        listViewDatos.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Buscar ID del elemento a ver en detalle
                Estudiante objeto_actual = lista_estudiantes.get(position);
                //Mandar a Activity Detalle
                Intent intent_detalle = new Intent(MainActivity.this, DetalleActivity.class);
                intent_detalle.putExtra("ID", objeto_actual.ID);
                startActivity(intent_detalle);
            }
        });

    }
}










