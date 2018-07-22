package com.applus.vistas.operario.clientes;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.ClientesController;
import com.applus.modelos.Cliente;
import com.applus.modelos.TipoCliente;
import com.applus.vistas.operario.censo.DialogNic;

import java.util.ArrayList;

public class ActualizarCliente extends AppCompatActivity implements  DialogNic.NicListener{

    Button ac_buscar_codigo, ac_buscar_nic, ac_guardar_cliente;
    EditText ac_codigo, ac_nombre, ac_direccion, ac_nic;
    Spinner ac_tipoCliente, ac_ReporteDesocupado;
    Intent intent = null;
    Activity activity = null;
    TipoCliente tipoClienteElegido = null;
    TipoCliente tipoNovedadElegido = null;
    ActualizarCliente listener = null;
    ClientesController cont = new ClientesController();
    Cliente clienteEncontrado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_cliente);
        activity = this;
        listener = this;

        ac_codigo = (EditText) findViewById(R.id.ac_codigo);
        ac_nombre = (EditText) findViewById(R.id.ac_nombre);
        ac_direccion = (EditText) findViewById(R.id.ac_direccion);
        ac_nic = (EditText) findViewById(R.id.ac_nic);
        ac_buscar_codigo = (Button) findViewById(R.id.ac_buscar_codigo);
        ac_buscar_nic = (Button) findViewById(R.id.ac_buscar_nic);
        ac_guardar_cliente = (Button) findViewById(R.id.ac_guardar_cliente);
        ac_tipoCliente = (Spinner) findViewById(R.id.ac_tipoCliente);
        ac_ReporteDesocupado = (Spinner) findViewById(R.id.ac_ReporteDesocupado);
        ac_tipoCliente.setEnabled(false);
        ac_ReporteDesocupado.setEnabled(false);

        ArrayList<TipoCliente> tipos = new ArrayList<TipoCliente>();
        tipos.add(new TipoCliente("","SIN TIPO"));
        tipos.add(new TipoCliente("C","COMERCIAL"));
        tipos.add(new TipoCliente("R","RESIDENCIAL"));
        tipos.add(new TipoCliente("I","INDUSTRIAL"));
        ArrayAdapter<TipoCliente> tiposAdapter = new ArrayAdapter<TipoCliente>(this,
                android.R.layout.simple_spinner_item, tipos);
        tiposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ac_tipoCliente.setAdapter(tiposAdapter);
        ac_tipoCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tipoClienteElegido = (TipoCliente) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                tipoClienteElegido = null;
            }
        });

        ArrayList<TipoCliente> tiposNovedad = new ArrayList<TipoCliente>();
        tiposNovedad.add(new TipoCliente("SINNOVEDAD","SIN NOVEDAD"));
        tiposNovedad.add(new TipoCliente("DESOCUPADO","DESOCUPADO"));
        tiposNovedad.add(new TipoCliente("HABITADO","HABITADO"));
        ArrayAdapter<TipoCliente> tiposNovedadAdapter = new ArrayAdapter<TipoCliente>(this,
                android.R.layout.simple_spinner_item, tiposNovedad);
        tiposNovedadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ac_ReporteDesocupado.setAdapter(tiposNovedadAdapter);
        ac_ReporteDesocupado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tipoNovedadElegido = (TipoCliente) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                tipoNovedadElegido = null;
            }
        });

        ac_buscar_codigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ac_codigo.getText().toString().equals("")) {
                    clienteEncontrado = cont.getClienteCodigo(Long.parseLong(ac_codigo.getText().toString()), activity);
                    if (clienteEncontrado != null) {
                        ac_nombre.setText(clienteEncontrado.getNombre());
                        ac_direccion.setText(clienteEncontrado.getDireccion());
                        ac_nic.setText("" + clienteEncontrado.getNic());
                        if(clienteEncontrado.getTipo().equals("C")){
                            ac_tipoCliente.setSelection(1);
                        } else if(clienteEncontrado.getTipo().equals("R")){
                            ac_tipoCliente.setSelection(2);
                        } else if(clienteEncontrado.getTipo().equals("I")){
                            ac_tipoCliente.setSelection(3);
                        } else if(clienteEncontrado.getTipo().equals("")){
                            ac_tipoCliente.setSelection(0);
                        }

                        if(clienteEncontrado.getReporte().equals("SINNOVEDAD")
                            || clienteEncontrado.getReporte().equals("")){
                            ac_ReporteDesocupado.setSelection(0);
                        } else if(clienteEncontrado.getReporte().equals("DESOCUPADO")){
                            ac_ReporteDesocupado.setSelection(1);
                        } else if(clienteEncontrado.getReporte().equals("HABITADO")){
                            ac_ReporteDesocupado.setSelection(2);
                        }

                        ac_nombre.setEnabled(true);
                        ac_direccion.setEnabled(true);
                        ac_tipoCliente.setEnabled(true);
                        ac_ReporteDesocupado.setEnabled(true);
                        ac_guardar_cliente.setEnabled(true);
                    }
                }else{
                    Toast.makeText(activity, "Por favor, digite el codigo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ac_buscar_nic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df=new DialogNic(listener);
                df.show(getFragmentManager(), "nic");
            }
        });
        ac_guardar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente cliente = new Cliente();
                cliente.setCodigo(clienteEncontrado.getCodigo());
                cliente.setNombre(ac_nombre.getText().toString());
                cliente.setDireccion(ac_direccion.getText().toString());
                cliente.setNic(clienteEncontrado.getNic());
                cliente.setTipo_cliente(tipoClienteElegido.getTag());
                cliente.setReporte(tipoNovedadElegido.getTag());
                cont.insertarClienteActualizar(cliente, activity);
                limpiar();
                Toast.makeText(activity, "Cliente Actualizado. Sincronize para enviar.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void limpiar(){
        ac_nombre.setEnabled(false);
        ac_nombre.setText("");
        ac_direccion.setEnabled(false);
        ac_direccion.setText("");
        ac_nic.setText("");
        ac_tipoCliente.setEnabled(false);
        ac_ReporteDesocupado.setEnabled(false);
        ac_tipoCliente.setSelection(0);
        ac_ReporteDesocupado.setSelection(0);
        ac_guardar_cliente.setEnabled(false);
        clienteEncontrado = null;
    }

    @Override
    public void onAddNic(Cliente cliente) {
        ac_nic.setText("" + cliente.getNic());
    }
}
