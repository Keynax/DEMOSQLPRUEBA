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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Connect to database
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (CODIGO INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR, DESCRIPCION VARCHAR)");

        // Get CODIGO from intent
        int CODIGO_ELEMENTO = getIntent().getIntExtra("CODIGO", 0);

        // Make SQL query
        final Cursor cursor_detalle = db.rawQuery("select * from ESTUDIANTES WHERE CODIGO=" + CODIGO_ELEMENTO, null);

        // Get Student object from cursor
        Estudiante objeto_estudiante = new Estudiante();

        // Get indices for query
        int INDICE_CODIGO = cursor_detalle.getColumnIndex("CODIGO");
        int INDICE_NOMBRE = cursor_detalle.getColumnIndex("NOMBRE");
        int INDICE_DESCRIPCION = cursor_detalle.getColumnIndex("DESCRIPCION");

        // Iterate through query
        while (cursor_detalle.moveToNext()) {
            objeto_estudiante.ID = cursor_detalle.getInt(INDICE_CODIGO);
            objeto_estudiante.NOMBRE = cursor_detalle.getString(INDICE_NOMBRE);
            objeto_estudiante.DESCRIPCION = cursor_detalle.getString(INDICE_DESCRIPCION);
        }

        // Load information to UI
        TextView editar_titulo = findViewById(R.id.editar_titulo);
        EditText input_nombre = findViewById(R.id.input_nombre);
        EditText input_descripcion = findViewById(R.id.input_descripcion);

        // Load info to inputs
        editar_titulo.setText("Editar Estudiante #" + objeto_estudiante.ID);
        input_nombre.setText(objeto_estudiante.NOMBRE);
        input_descripcion.setText(objeto_estudiante.DESCRIPCION);

        // Associate button
        Button boton_editar = findViewById(R.id.boton_editar);

        boton_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                EditText input_nombre = findViewById(R.id.input_nombre);
                EditText input_descripcion = findViewById(R.id.input_descripcion);

                String nombre = input_nombre.getText().toString();
                String descripcion = input_descripcion.getText().toString();

                String sql = "UPDATE ESTUDIANTES SET NOMBRE=?, DESCRIPCION=? WHERE CODIGO=?";

                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1, nombre);
                statement.bindString(2, descripcion);
                statement.bindString(3, String.valueOf(CODIGO_ELEMENTO));
                statement.execute();

                // Redirect to ListarActivity
                Intent intent = new Intent(EditarActivity.this, ListarActivity.class);
                startActivity(intent);
            }
        });
    }
}
