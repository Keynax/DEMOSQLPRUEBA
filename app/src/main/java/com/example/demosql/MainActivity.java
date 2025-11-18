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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        //CODIGO PARA CONECTARNOS A NUESTRA BD
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE, null);

        // Connect to database

        //2.- LEER LA TABLA Y CARGAR A LISTVIEW
        final Cursor cursor_listar = db.rawQuery("select * from ESTUDIANTES", null);

        //3.- Ubicar nuestras columnas
        int ID = cursor_listar.getColumnIndex("ID");
        int NOMBRE = cursor_listar.getColumnIndex("NOMBRE");
        int DESCRIPCION = cursor_listar.getColumnIndex("DESCRIPCION");

        //4.- RECORRER SELECT
        ArrayList<Estudiante> lista_estudiantes = new ArrayList<Estudiante>();
        ArrayList<String> arreglo = new ArrayList<>();

        while (cursor_listar.moveToNext()) {
            Estudiante obj = new Estudiante();
            obj.ID = cursor_listar.getInt(ID);
            obj.NOMBRE = cursor_listar.getString(NOMBRE);
            obj.DESCRIPCION = cursor_listar.getString(DESCRIPCION);

            lista_estudiantes.add(obj);

            //agregar a lista para ArrayAdapter
            arreglo.add("# " + obj.ID + " " + obj.NOMBRE + " " + obj.DESCRIPCION);
        }

        //5.- Cargar informacion a ListView
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arreglo);

        //6.- Declarar ListView
        ListView listViewDatos = findViewById(R.id.lista_estudiantes);
        listViewDatos.setAdapter(adapter);

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
        listViewDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
