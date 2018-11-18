package com.applus.vistas.operario.clientes;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.ClientesController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.NicsController;
import com.applus.modelos.Cliente;
import com.applus.modelos.Departamento;
import com.applus.modelos.Nics;
import com.applus.modelos.SesionSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MenuClientes extends AppCompatActivity implements DialogProgress.NoticeDialogListener{

    ClientesController clieController = new ClientesController();
    NicsController nicsController = new NicsController();
    DepartamentoController dep=new DepartamentoController();
    ArrayList<Departamento> departamento = null;

    Button irADescargar, irAActualizar, irADescargarCensos, irADescargarNics;
    Intent intent = null;
    Activity activity = null;
    DialogFragment dialogo = new DialogProgress();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clientes);
        activity = this;
        irADescargar = (Button) findViewById(R.id.irADescargar);
        irAActualizar = (Button) findViewById(R.id.irAActualizar);
        irADescargarCensos = (Button) findViewById(R.id.irADescargarCensos);
        irADescargarNics = (Button) findViewById(R.id.irADescargarNics);

        irADescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent = new Intent(activity, CensosDescarga.class);
                //startActivity(intent);
                departamento = dep.consultar(0, 0, "id=" + SesionSingleton.getInstance().getFkDistrito(), activity);
                if (departamento.get(0).getVersion().equals(SesionSingleton.getInstance().getVersionClientes())){
                    Toast.makeText(activity, "Ya tienen descargado el listado de clientes actual.", Toast.LENGTH_SHORT).show();
                }else{
                    dialogo.show(getFragmentManager(), "Descargando Clientes...");
                    new DownloadFilesTask().execute();
                }
            }
        });
        irAActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity, ActualizarCliente.class);
                startActivity(intent);
            }
        });
        irADescargarCensos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity, CensosDescarga.class);
                startActivity(intent);
            }
        });

        irADescargarNics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent = new Intent(activity, CensosDescarga.class);
                //startActivity(intent);
                dialogo.show(getFragmentManager(), "Bajando NICs...");
                departamento = dep.consultar(0, 0, "id=" + SesionSingleton.getInstance().getFkDistrito(), activity);
                new DownloadNicsTask().execute();
            }
        });
    }

    private void leerArchivo(File inputFile){
        try {
            FileInputStream stream = new FileInputStream(inputFile);
            String jsonStr = null;
            try {
                InputStream inputStream = new FileInputStream(inputFile);

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    jsonStr = stringBuilder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            JSONObject json_data = new JSONObject(jsonStr);
            JSONArray clientes = json_data.getJSONArray("clientes");
            Cliente c = null;
            int size = clientes.length();
            for (int i = 0; i < size; ++i) {
                JSONObject cli = clientes.getJSONObject(i);
                c = new Cliente();
                c.setId(cli.getInt("id"));
                c.setNombre(cli.getString("nombre"));
                c.setDireccion(cli.getString("direccion"));
                c.setNic(cli.getLong("nic"));
                c.setTipo(cli.getString("tipo"));
                c.setCenso((int) Float.parseFloat(cli.getString("censo")));
                c.setTipo_cliente(cli.getString("tipo_cliente"));
                c.setLongitud(cli.getString("longitud"));
                c.setLatitud(cli.getString("latitud"));
                c.setOrden_reparto("" + cli.getLong("orden_reparto"));
                c.setItinerario(cli.getString("itinerario"));
                c.setFk_barrio(cli.getInt("Fk_barrio"));
                c.setCodigo(cli.getLong("codigo"));
                c.setCensos(cli.getString("censos"));
                c.setCenso_fes(cli.getString("censo_fes"));
                if (cli.has("reporte")) {
                    c.setReporte(cli.getString("reporte"));
                } else {
                    c.setReporte("");
                }
                clieController.insertar(c, activity);
                System.err.println("Insertado " + cli.getLong("nic"));
            }
        }catch (Exception e){
            System.err.println("Error " + e);
        }finally {

        }
    }

    private void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(Exception e) {
            System.err.println("Error Archivo: " + e);
        }
    }

    private File downloadZip(String urlD){
        int count;
        File file = null;
        try {
            URL url = new URL(urlD);
            URLConnection connection = url.openConnection();
            connection.connect();

            int lenghtOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            String filename = URLUtil.guessFileName(urlD, null, null);
            file = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
            FileOutputStream output = new FileOutputStream(file); //context.openFileOutput("content.zip", Context.MODE_PRIVATE);

            FileDescriptor fd = output.getFD();

            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            System.err.println("Error " + e);
        }

        return file;
    }

    public void unzip(File _zipFile) {
        try  {
            InputStream fin = null;
            if(fin == null) {
                fin = new FileInputStream(_zipFile);
            }
            String fileName = _zipFile.getName();
            if (fileName.indexOf(".") > 0){
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            _dirChecker(Environment.getExternalStorageDirectory() + File.separator + fileName);

            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if(ze.isDirectory()) {
                    _dirChecker(Environment.getExternalStorageDirectory() + File.separator + ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), ze.getName()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;

                    // reading and writing
                    while((count = zin.read(buffer)) != -1)
                    {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                    zin.closeEntry();
                }
            }
            zin.close();
        } catch(Exception e) {
            System.err.println("Error " + e);
        }
    }

    private void _dirChecker(String dir) {
        File f = new File(dir);

        if(dir.length() >= 0 && !f.isDirectory() ) {
            f.mkdirs();
        }
    }

    private void leerArchivoNics(File inputFile){
        try {
            FileInputStream stream = new FileInputStream(inputFile);
            String jsonStr = null;
            try {
                InputStream inputStream = new FileInputStream(inputFile);

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    jsonStr = stringBuilder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            JSONObject json_data = new JSONObject(jsonStr);
            JSONArray clientes = json_data.getJSONArray("nics");
            Nics c = null;
            int size = clientes.length();
            for (int i = 0; i < size; ++i) {
                JSONObject cli = clientes.getJSONObject(i);
                c = new Nics();
                c.setNic(Long.parseLong(cli.getString("nic")));
                c.setFkBarrio(Long.parseLong(cli.getString("fk_barrio")));
                nicsController.insertar(c, activity);
                System.err.println("Insertado " + "Nic: " + c.getNic() + " - FK: " + c.getFkBarrio());
            }
        }catch (Exception e){
            System.err.println("Error Fichero Nics " + e);
        }finally {

        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private class DownloadFilesTask extends AsyncTask<Void, Integer, Integer> {
        protected Integer doInBackground(Void... voids) {
            try {
                String version = departamento.get(0).getVersion();

                String url = departamento.get(0).getUrl();
                File file1 = downloadZip(url);
                unzip(file1);
                clieController.eliminarTodo(activity);
                String filename = URLUtil.guessFileName(url, null, null);
                if (filename.indexOf(".") > 0){
                    filename = filename.substring(0, filename.lastIndexOf("."));
                }

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
                File[] files = f.listFiles();
                for (File inFile : files) {
                    if (!inFile.isDirectory()) {
                        leerArchivo(inFile);
                    }
                }
                SesionSingleton.getInstance().setVersionClientes(version);
                SharedPreferences preferencias = getSharedPreferences("configuracion",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("versionclientes", version);
                editor.commit();
                dialogo.dismiss();
            } catch (Exception e) {
                System.err.println("Error " + e);
                dialogo.dismiss();
            } finally {
                dialogo.dismiss();
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Integer result) {
            dialogo.dismiss();
        }
    }

    private class DownloadNicsTask extends AsyncTask<Void, Integer, Integer> {
        protected Integer doInBackground(Void... voids) {
            try {
                String version = departamento.get(0).getVersionNics();

                String url = departamento.get(0).getUrlNics();
                File file1 = downloadZip(url);
                unzip(file1);
                nicsController.eliminarTodo(activity);
                String filename = URLUtil.guessFileName(url, null, null);
                if (filename.indexOf(".") > 0){
                    filename = filename.substring(0, filename.lastIndexOf("."));
                }

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
                File[] files = f.listFiles();
                for (File inFile : files) {
                    if (!inFile.isDirectory()) {
                        leerArchivoNics(inFile);
                    }
                }
                SesionSingleton.getInstance().setVersionNics(version);
                SharedPreferences preferencias = getSharedPreferences("configuracion",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("versionnics", version);
                editor.commit();
                dialogo.dismiss();
            } catch (Exception e) {
                System.err.println("Error " + e);
                dialogo.dismiss();
            } finally {
                dialogo.dismiss();
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Integer result) {
            dialogo.dismiss();
        }
    }
}
