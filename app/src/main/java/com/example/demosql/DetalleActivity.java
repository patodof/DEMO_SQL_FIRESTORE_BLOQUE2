package com.example.demosql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //CODIGO PARA CONECTARNOS A NUESTRA BD
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE,null);

        //1.- CREAR TABLA
        db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (ID INTEGER PRIMARY KEY AUTOINCREMENT,NOMBRE VARCHAR,APELLIDOS VARCHAR,EDAD INTEGER)");

        //2.- OBTENER DATOS DESDE INTENT
        int ID_ELEMENTO = getIntent().getIntExtra("ID", 0);

        //3 Hacer consulta SQL
        final Cursor cursor_detalle = db.rawQuery("select * from ESTUDIANTES WHERE ID="+ID_ELEMENTO,null);

        //4 Obtener Objeto Estudiante a partir del cursor
        Estudiante objeto_estudiante = new Estudiante();

        //5 Obtener Indices para la consulta
        int INDICE_ID = cursor_detalle.getColumnIndex("ID");
        int INDICE_NOMBRE = cursor_detalle.getColumnIndex("NOMBRE");
        int INDICE_APELLIDO = cursor_detalle.getColumnIndex("APELLIDOS");
        int INDICE_EDAD = cursor_detalle.getColumnIndex("EDAD");

        //6 iterar consulta
        while( cursor_detalle.moveToNext() )
        {
            //Poblamos nuestro objeto con la informacion de la base de datos
            //los metodos getInt y getString reciben un int que representa al indice obtenido en el paso 5
            objeto_estudiante.ID = cursor_detalle.getInt(INDICE_ID);
            objeto_estudiante.NOMBRE = cursor_detalle.getString(INDICE_NOMBRE);
            objeto_estudiante.APELLIDOS = cursor_detalle.getString(INDICE_APELLIDO);
            objeto_estudiante.EDAD = cursor_detalle.getInt(INDICE_EDAD);
        }

        //7 Cargar informacion a UI
        //cajas de texto
        TextView detalle_id = findViewById(R.id.detalle_id);
        TextView detalle_nombre = findViewById(R.id.detalle_nombre);
        TextView detalle_apellido = findViewById(R.id.detalle_apellido);
        TextView detalle_edad = findViewById(R.id.detalle_edad);

        //botones
        Button boton_volver = findViewById(R.id.boton_detalle);
        Button boton_editar = findViewById(R.id.boton_editar);
        Button boton_eliminar = findViewById(R.id.boton_eliminar);

        //cargar informacion a cajas de texto
        detalle_id.setText("ID : "+objeto_estudiante.ID);
        detalle_nombre.setText("NOMBRE : "+objeto_estudiante.NOMBRE);
        detalle_apellido.setText("APELLIDOS : "+objeto_estudiante.APELLIDOS);
        detalle_edad.setText("EDAD : "+objeto_estudiante.EDAD);

        //Clicks de los botones
        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_listar = new Intent(DetalleActivity.this, MainActivity.class);
                startActivity(intent_listar);
            }
        });

        boton_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_editar = new Intent(DetalleActivity.this, EditarActivity.class );
                intent_editar.putExtra("ID", ID_ELEMENTO );
                startActivity(intent_editar);
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PROGRAMAR CODIGO PARA ELIMINAR ELEMENTO DE LA BASE DE DATOS
            }
        });


    }
}