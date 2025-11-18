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

        // Connect to database
        SQLiteDatabase db = openOrCreateDatabase("BD_ESTUDIANTES", Context.MODE_PRIVATE, null);

        // Associate button
        Button boton_agregar = findViewById(R.id.boton_agregar);

        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                EditText input_nombre = findViewById(R.id.input_nombre);
                EditText input_descripcion = findViewById(R.id.input_descripcion);

                String nombre = input_nombre.getText().toString();
                String descripcion = input_descripcion.getText().toString();

                String sql = "insert into ESTUDIANTES (NOMBRE, DESCRIPCION) values(?, ?)";

                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1, nombre);
                statement.bindString(2, descripcion);
                statement.execute();

                // Redirect to ListarActivity
                Intent intent = new Intent(AgregarActivity.this, ListarActivity.class);
                startActivity(intent);
            }
        });
    }
}
