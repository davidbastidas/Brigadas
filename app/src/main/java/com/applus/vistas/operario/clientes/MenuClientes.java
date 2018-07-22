package com.applus.vistas.operario.clientes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.applus.R;

public class MenuClientes extends AppCompatActivity {

    Button irADescargar, irAActualizar;
    Intent intent = null;
    Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clientes);
        activity = this;
        irADescargar = (Button) findViewById(R.id.irADescargar);
        irAActualizar = (Button) findViewById(R.id.irAActualizar);

        irADescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity, ClientesDescarga.class);
                startActivity(intent);
            }
        });
        irAActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity, ActualizarCliente.class);
                startActivity(intent);
            }
        });
    }

}
