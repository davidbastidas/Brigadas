package com.applus.vistas.operario.censo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.applus.R;
import com.applus.controladores.CensoController;
import com.applus.modelos.Censo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityDetalleCenso extends AppCompatActivity {

    private ListView lista_detalle, lista_electrodomesticos;
    private Censo censo = null;
    private CensoController ServCont=new CensoController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_censo);

        lista_detalle = findViewById(R.id.lista_detalle);
        lista_electrodomesticos = findViewById(R.id.lista_electrodomesticos);

        long servicioId;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            servicioId= 0;
        } else {
            servicioId = extras.getLong("servicio_id");
            ArrayList<Censo> consultar = ServCont.consultar(0, 0, "id = " + servicioId, this);
            censo = consultar.get(0);
        }
        if(censo != null){
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

            ArrayList<HashMap<String, String>> dataElectrodomestico = new ArrayList<HashMap<String, String>>();
            JSONObject json_data = null;
            try {
                json_data = new JSONObject("{\"electrodomesticos\":" + censo.getFormulario() + "}");
                JSONArray array = json_data.getJSONArray("electrodomesticos");
                int size = array.length();
                for (int i = 0; i < size; ++i) {
                    JSONObject electr = array.getJSONObject(i);
                    datum = new HashMap<String, String>();
                    datum.put("titulo", electr.getString("nombre"));
                    datum.put("subtitulo", "Cantidad = " + electr.getInt("cantidad") + ". Consumo = " + electr.getString("watts"));
                    dataElectrodomestico.add(datum);
                }
                SimpleAdapter adapterElectrodomestico = new SimpleAdapter(this, dataElectrodomestico, android.R.layout.simple_list_item_2, new String[] {"titulo", "subtitulo"}, new int[] {android.R.id.text1, android.R.id.text2});
                lista_electrodomesticos.setAdapter(adapterElectrodomestico);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {

            }
        }
    }
}
