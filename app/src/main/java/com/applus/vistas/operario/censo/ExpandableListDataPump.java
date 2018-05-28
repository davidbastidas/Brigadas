package com.applus.vistas.operario.censo;

import com.applus.modelos.CensoForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<CensoForm>> getData() {
        HashMap<String, List<CensoForm>> expandableListDetail = new HashMap<String, List<CensoForm>>();

        String cadena = "{\"categorias\":[{\"tipo\":\"acordeon\",\"nombre\":\"Alumbrado\",\"items\":[{\"tipo\":\"acordeon\",\"nombre\":\"lampara Comercial\",\"items\":[{\"tipo\":\"acordeon\",\"nombre\":\"lampara Fluorecente\",\"items\":[{\"tipo\":\"item\",\"nombre\":\"de 15 Watts\",\"watts\":\"15\"},{\"tipo\":\"item\",\"nombre\":\"de 2x8 (39 Watts)\",\"watts\":\"39\"}]},{\"tipo\":\"acordeon\",\"nombre\":\"lampara Incandescente interna\",\"items\":[{\"tipo\":\"item\",\"nombre\":\"de 100 Watts\",\"watts\":\"100\"},{\"tipo\":\"item\",\"nombre\":\"de 150 Watts\",\"watts\":\"150\"}]}]},{\"tipo\":\"acordeon\",\"nombre\":\"lampara Residencial\",\"items\":[{\"tipo\":\"acordeon\",\"nombre\":\"lampara Mercurio\",\"items\":[{\"tipo\":\"item\",\"nombre\":\"de 1000 Watts\",\"watts\":\"1000\"},{\"tipo\":\"item\",\"nombre\":\"de 150 Watts\",\"watts\":\"150\"}]},{\"tipo\":\"acordeon\",\"nombre\":\"lampara Mercurio\",\"items\":[{\"tipo\":\"item\",\"nombre\":\"de 1000 Watts\",\"watts\":\"1000\"},{\"tipo\":\"item\",\"nombre\":\"de 150 Watts\",\"watts\":\"150\"}]}]}]},{\"tipo\":\"acordeon\",\"nombre\":\"Climatizacion\",\"items\":[{\"tipo\":\"acordeon\",\"nombre\":\"Aire Acondicionado 12000 BTU\",\"items\":[]}]}]}";
        JSONObject myjson = null;
        try {
            CensoForm cat1, cat2;
            myjson = new JSONObject(cadena);
            //obtengo un array de los objetos
            JSONArray the_json_array = myjson.getJSONArray("categorias");

            List<CensoForm> items;
            int size = the_json_array.length();
            for (int i = 0; i < size; i++) {
                //encabezados del acordeon
                cat1 = new CensoForm();
                cat1.setTipo(the_json_array.getJSONObject(i).getString("tipo"));
                cat1.setNombre(the_json_array.getJSONObject(i).getString("nombre"));
                cat1.setItems(the_json_array.getJSONObject(i).getString("items"));
                //items del acordeon
                JSONArray json_items = new JSONObject("{\"items\":"+cat1.getItems()+"}").getJSONArray("items");
                int size_items = json_items.length();
                items = new ArrayList<CensoForm>();
                for (int y = 0; y < size_items; y++){
                    cat2 = new CensoForm();
                    cat2.setTipo(json_items.getJSONObject(y).getString("tipo"));
                    cat2.setNombre(json_items.getJSONObject(y).getString("nombre"));
                    cat2.setItems(json_items.getJSONObject(y).getString("items"));
                    items.add(cat2);
                }
                System.out.println("nombre cat: "+cat1.getNombre()+", tamaño: "+size);
                //seteamos el array final
                expandableListDetail.put(cat1.getNombre(), items);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return expandableListDetail;
    }
}