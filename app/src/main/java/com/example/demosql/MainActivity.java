package com.example.demosql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

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

        //CODIGO PARA CONECTARNOS A NUESTRA BD
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE,null);

        //1.- CREAR TABLA
        db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (ID INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE VARCHAR,APELLIDOS VARCHAR,EDAD INTEGER)");

        //2.- LEER LA TABLA Y CARGAR A LISTVIEW
        final Cursor cursor_listar = db.rawQuery("select * from ESTUDIANTES",null);

        //3.- Ubicar nuestras columnas
        int ID = cursor_listar.getColumnIndex("ID");
        int NOMBRE = cursor_listar.getColumnIndex("NOMBRE");
        int APELLIDOS = cursor_listar.getColumnIndex("APELLIDOS");

        //4.- RECORRER SELECT
        ArrayList<Estudiante> lista_estudiantes = new ArrayList<Estudiante>();
        ArrayList<String> arreglo = new ArrayList<>();

        while( cursor_listar.moveToNext() )
        {
            Estudiante obj = new Estudiante();
            obj.ID = cursor_listar.getInt(ID);
            obj.NOMBRE = cursor_listar.getString(NOMBRE);
            obj.APELLIDOS = cursor_listar.getString(APELLIDOS);

            lista_estudiantes.add(obj);

            //agregar a lista para ArrayAdapter
            arreglo.add("# "+obj.ID+" "+obj.NOMBRE+" "+obj.APELLIDOS);
        }

        //5.- Cargar informacion a ListView
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,arreglo);

        //6.- Declarar ListView
        ListView listViewDatos = findViewById(R.id.lista_estudiantes);
        listViewDatos.setAdapter(adapter);


    }
}










