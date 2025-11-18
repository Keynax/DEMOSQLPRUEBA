package com.example.demosql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

        // Connect to database
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS ESTUDIANTES (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR, DESCRIPCION VARCHAR)");

        // Get CODIGO from intent
        int CODIGO_ELEMENTO = getIntent().getIntExtra("ID", 0);

        // Make SQL query
        final Cursor cursor_detalle = db.rawQuery("select * from ESTUDIANTES WHERE ID=" + CODIGO_ELEMENTO, null);

        // Get Student object from cursor
        Estudiante objeto_estudiante = new Estudiante();

        // Get indices for query
        int INDICE_ID = cursor_detalle.getColumnIndex("ID");
        int INDICE_NOMBRE = cursor_detalle.getColumnIndex("NOMBRE");
        int INDICE_DESCRIPCION = cursor_detalle.getColumnIndex("DESCRIPCION");

        // Iterate through query
        while (cursor_detalle.moveToNext()) {
            objeto_estudiante.ID = cursor_detalle.getInt(INDICE_ID);
            objeto_estudiante.NOMBRE = cursor_detalle.getString(INDICE_NOMBRE);
            objeto_estudiante.DESCRIPCION = cursor_detalle.getString(INDICE_DESCRIPCION);
        }

        // Load information to UI
        TextView detalle_codigo = findViewById(R.id.detalle_codigo);
        TextView detalle_nombre = findViewById(R.id.detalle_nombre);
        TextView detalle_descripcion = findViewById(R.id.detalle_descripcion);

        // Buttons
        Button boton_volver = findViewById(R.id.boton_volver);
        Button boton_editar = findViewById(R.id.boton_editar);
        Button boton_eliminar = findViewById(R.id.boton_eliminar);

        // Load information to TextViews
        detalle_codigo.setText("ID : " + objeto_estudiante.ID);
        detalle_nombre.setText("NOMBRE : " + objeto_estudiante.NOMBRE);
        detalle_descripcion.setText("DESCRIPCION : " + objeto_estudiante.DESCRIPCION);

        // Button clicks
        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_listar = new Intent(DetalleActivity.this, ListarActivity.class);
                startActivity(intent_listar);
            }
        });

        boton_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_editar = new Intent(DetalleActivity.this, EditarActivity.class);
                intent_editar.putExtra("ID", CODIGO_ELEMENTO);
                startActivity(intent_editar);
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create delete SQL
                String sql = "DELETE FROM ESTUDIANTES WHERE ID=?";

                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1, String.valueOf(CODIGO_ELEMENTO));
                statement.execute();

                // Redirect to ListarActivity
                Intent intent = new Intent(DetalleActivity.this, ListarActivity.class);
                startActivity(intent);
            }
        });
    }
}
