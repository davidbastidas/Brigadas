package com.applus.vistas.operario.censo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.applus.R;
import com.applus.modelos.Censo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityDetalleCenso extends AppCompatActivity {

    private ListView lista_detalle;
    private Censo censo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_censo);

        lista_detalle = findViewById(R.id.lista_detalle);
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        HashMap<String,String> datum = new HashMap<String, String>();
        datum.put("titulo", "Codigo");
        datum.put("subtitulo", "" + censo.getCodigo());
        data.add(datum);

        datum = new HashMap<String, String>();
        datum.put("titulo", "NIC");
        datum.put("subtitulo", "" + censo.getNic());
        data.add(datum);

        JSONObject json = null;
        String nombre = "", direccion = "";
        try {
            json = new JSONObject(censo.getDatos());
            nombre = json.getString("nombre");
            direccion = json.getString("direccion");
        } catch (JSONException e) {
            System.out.println(e);
        }
        datum = new HashMap<String, String>();
        datum.put("titulo", "Nombre");
        datum.put("subtitulo", "" + nombre);
        data.add(datum);

        datum = new HashMap<String, String>();
        datum.put("titulo", "Direccion");
        datum.put("subtitulo", "" + direccion);
        data.add(datum);

        datum = new HashMap<String, String>();
        datum.put("titulo", "Tipo de cliente");
        datum.put("subtitulo", "" + censo.getTipoCliente());
        data.add(datum);

        datum = new HashMap<String, String>();
        datum.put("titulo", "Observaciones");
        datum.put("subtitulo", "" + censo.getObservaciones());
        data.add(datum);

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"titulo", "subtitulo"}, new int[] {android.R.id.text1, android.R.id.text2});
        lista_detalle.setAdapter(adapter);
    }

}
