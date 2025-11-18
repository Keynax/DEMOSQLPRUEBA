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

public class ListarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Connect to database
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR, DESCRIPCION VARCHAR)");

        // Read from database and load to ListView
        final Cursor cursor_listar = db.rawQuery("select * from ESTUDIANTES", null);

        // Get column indices
        int ID = cursor_listar.getColumnIndex("ID");
        int NOMBRE = cursor_listar.getColumnIndex("NOMBRE");
        int DESCRIPCION = cursor_listar.getColumnIndex("DESCRIPCION");

        // Read all students from database
        ArrayList<Estudiante> lista_estudiantes = new ArrayList<Estudiante>();
        ArrayList<String> arreglo = new ArrayList<>();

        while (cursor_listar.moveToNext()) {
            Estudiante obj = new Estudiante();
            obj.ID = cursor_listar.getInt(ID);
            obj.NOMBRE = cursor_listar.getString(NOMBRE);
            obj.DESCRIPCION = cursor_listar.getString(DESCRIPCION);

            lista_estudiantes.add(obj);

            // Add to list for ArrayAdapter
            arreglo.add("# " + obj.ID + " " + obj.NOMBRE);
        }

        // Load information to ListView
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arreglo);

        // Declare ListView
        ListView listViewDatos = findViewById(R.id.lista_estudiantes);
        listViewDatos.setAdapter(adapter);

        // Add button
        Button boton_agregar = findViewById(R.id.boton_agregar);
        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarActivity.this, AgregarActivity.class);
                startActivity(intent);
            }
        });

        // ListView item click to go to detail activity
        listViewDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Estudiante objeto_actual = lista_estudiantes.get(position);
                Intent intent_detalle = new Intent(ListarActivity.this, DetalleActivity.class);
                intent_detalle.putExtra("ID", objeto_actual.ID);
                startActivity(intent_detalle);
            }
        });
    }
}
