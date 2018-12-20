package com.applus.vistas.operario.clientes;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.applus.BuildConfig;
import com.applus.R;
import com.applus.controladores.ClientesController;
import com.applus.modelos.Cliente;
import com.applus.modelos.ClienteAActualizar;
import com.applus.modelos.SesionSingleton;
import com.applus.modelos.TipoCliente;
import com.applus.vistas.operario.censo.DialogNic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActualizarCliente extends AppCompatActivity implements DialogNic.NicListener{

    Button ac_buscar_codigo, ac_buscar_nic, ac_guardar_cliente, pedirFirma;
    EditText ac_codigo, ac_nombre, ac_direccion, ac_nic;
    Spinner ac_tipoCliente, ac_ReporteDesocupado;
    Intent intent = null;
    Activity activity = null;
    TipoCliente tipoClienteElegido = null;
    TipoCliente tipoNovedadElegido = null;
    ActualizarCliente listener = null;
    ClientesController cont = new ClientesController();
    Cliente clienteEncontrado = null;
    private String fotoSoporte = "";
    private String firmaSoporte = "";
    private File photoFile;

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
        pedirFirma = (Button) findViewById(R.id.pedirFirma);
        ac_guardar_cliente = (Button) findViewById(R.id.ac_guardar_cliente);
        ac_tipoCliente = (Spinner) findViewById(R.id.ac_tipoCliente);
        ac_ReporteDesocupado = (Spinner) findViewById(R.id.ac_ReporteDesocupado);
        ac_tipoCliente.setEnabled(false);
        ac_ReporteDesocupado.setEnabled(false);

        ac_codigo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ac_nombre.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ac_direccion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ac_nic.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


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
                try{
                    if(!ac_nic.getText().toString().equals("")){
                        DialogFragment df=new DialogNic(listener, Long.parseLong(ac_nic.getText().toString()));
                        df.show(getFragmentManager(), "nic");
                    }else{
                        Toast.makeText(activity, "Por favor, digite el nic", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(activity, "Por favor, digite el nic", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pedirFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, Firma.class);
                startActivityForResult(i, 1);
            }
        });
        ac_guardar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac_guardar_cliente.setEnabled(false);
                ClienteAActualizar cliente = null;
                String date = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date());
                if (!ac_nombre.getText().toString().trim().equalsIgnoreCase(clienteEncontrado.getNombre())){
                    if(!fotoSoporte.equals("") && !firmaSoporte.equals("")){
                        cliente = new ClienteAActualizar();
                        cliente.setCodigo(clienteEncontrado.getCodigo());
                        cliente.setCampo("nombre");
                        cliente.setDatoAnterior(clienteEncontrado.getNombre());
                        cliente.setDatoActual(ac_nombre.getText().toString());
                        cliente.setFkUsuario(SesionSingleton.getInstance().getFk_id_operario());
                        cliente.setEsAplicado(0);
                        cliente.setFecha(date);
                        cliente.setFoto(fotoSoporte);
                        cliente.setFirma(firmaSoporte);
                        cont.insertarClienteActualizar(cliente, activity);
                    }
                }

                if (!ac_direccion.getText().toString().trim().equalsIgnoreCase(clienteEncontrado.getDireccion())){
                    if(!fotoSoporte.equals("") && !firmaSoporte.equals("")){
                        cliente = new ClienteAActualizar();
                        cliente.setCodigo(clienteEncontrado.getCodigo());
                        cliente.setCampo("direccion");
                        cliente.setDatoAnterior(clienteEncontrado.getDireccion());
                        cliente.setDatoActual(ac_direccion.getText().toString());
                        cliente.setFkUsuario(SesionSingleton.getInstance().getFk_id_operario());
                        cliente.setEsAplicado(0);
                        cliente.setFecha(date);
                        cliente.setFoto(fotoSoporte);
                        cliente.setFirma(firmaSoporte);
                        cont.insertarClienteActualizar(cliente, activity);
                    }
                }

                if (!ac_nic.getText().toString().trim().equalsIgnoreCase(""+clienteEncontrado.getNic())){
                    cliente = new ClienteAActualizar();
                    cliente.setCodigo(clienteEncontrado.getCodigo());
                    cliente.setCampo("nic");
                    cliente.setDatoAnterior(""+clienteEncontrado.getNic());
                    cliente.setDatoActual(ac_nic.getText().toString());
                    cliente.setFkUsuario(SesionSingleton.getInstance().getFk_id_operario());
                    cliente.setEsAplicado(0);
                    cliente.setFecha(date);
                    cliente.setFoto(fotoSoporte);
                    cliente.setFirma(firmaSoporte);
                    cont.insertarClienteActualizar(cliente, activity);
                }

                if (!tipoClienteElegido.getTag().equalsIgnoreCase(clienteEncontrado.getTipo())){
                    cliente = new ClienteAActualizar();
                    cliente.setCodigo(clienteEncontrado.getCodigo());
                    cliente.setCampo("tipo_cliente");
                    cliente.setDatoAnterior(clienteEncontrado.getTipo());
                    cliente.setDatoActual(tipoClienteElegido.getTag());
                    cliente.setFkUsuario(SesionSingleton.getInstance().getFk_id_operario());
                    cliente.setEsAplicado(0);
                    cliente.setFecha(date);
                    cliente.setFoto(fotoSoporte);
                    cliente.setFirma(firmaSoporte);
                    cont.insertarClienteActualizar(cliente, activity);
                }

                if(!tipoNovedadElegido.getTag().equalsIgnoreCase("SINNOVEDAD")){
                    if (!tipoNovedadElegido.getTag().equalsIgnoreCase(clienteEncontrado.getReporte())){
                        cliente = new ClienteAActualizar();
                        cliente.setCodigo(clienteEncontrado.getCodigo());
                        cliente.setCampo("tipo_reporte");
                        cliente.setDatoAnterior(clienteEncontrado.getReporte());
                        cliente.setDatoActual(tipoNovedadElegido.getTag());
                        cliente.setFkUsuario(SesionSingleton.getInstance().getFk_id_operario());
                        cliente.setEsAplicado(0);
                        cliente.setFecha(date);
                        cliente.setFoto(fotoSoporte);
                        cliente.setFirma(firmaSoporte);
                        cont.insertarClienteActualizar(cliente, activity);
                    }
                }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actualizar_cliente, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.mfotoactualizarcliente:
                takePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }
        Uri outputFileUri = null;
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
            outputFileUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
        } else{
            outputFileUri = Uri.fromFile(photoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 1888);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1888:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath);
                        Bitmap out = Bitmap.createScaledBitmap(b, 480, 640, false);
                        FileOutputStream fOut = new FileOutputStream(photoFile);
                        out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        b.recycle();
                        out.recycle();
                        fotoSoporte = getStringFromFile(photoFile);
                    } catch (Exception e) {
                        System.err.println("Error foto: " + e);
                    }

                }
                break;
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    firmaSoporte = data.getStringExtra("firma");
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getStringFromFile(File file) throws Exception {
        InputStream in = new FileInputStream(file);
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try{
            while ((bytesRead = in.read(buffer)) != -1){
                output.write(buffer, 0, bytesRead);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String ret = Base64.encodeToString(bytes, Base64.DEFAULT);
        //Make sure you close all streams.
        in.close();
        return ret;
    }
}
