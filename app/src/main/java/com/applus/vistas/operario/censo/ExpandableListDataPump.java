package com.applus.vistas.operario.censo;

import com.applus.modelos.CensoForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static LinkedHashMap<String, List<CensoForm>> getData(String cadena) {
        LinkedHashMap<String, List<CensoForm>> expandableListDetail = new LinkedHashMap<String, List<CensoForm>>();

        JSONObject myjson = null;
        try {
            CensoForm cat1, cat2;
            myjson = new JSONObject(cadena);
            //obtengo un array de los objetos
            JSONArray the_json_array = myjson.getJSONArray("items");

            List<CensoForm> items = new ArrayList<CensoForm>();
            int size = the_json_array.length();
            for (int i = 0; i < size; i++) {
                //encabezados del acordeon
                cat1 = new CensoForm();
                cat1.setTipo(the_json_array.getJSONObject(i).getString("tipo"));
                cat1.setNombre(the_json_array.getJSONObject(i).getString("nombre"));
                if(cat1.getTipo().equals("acordeon")){
                    cat1.setItems(the_json_array.getJSONObject(i).getString("items"));

                    //items del acordeon
                    JSONArray json_items = new JSONObject("{\"items\":"+cat1.getItems()+"}").getJSONArray("items");
                    int size_items = json_items.length();
                    items = new ArrayList<CensoForm>();
                    for (int y = 0; y < size_items; y++){
                        cat2 = new CensoForm();
                        cat2.setTipo(json_items.getJSONObject(y).getString("tipo"));
                        cat2.setNombre(json_items.getJSONObject(y).getString("nombre"));
                        if(cat2.getTipo().equals("acordeon")){
                            cat2.setItems(json_items.getJSONObject(y).getString("items"));
                        }else{
                            cat2.setId(json_items.getJSONObject(y).getInt("id"));
                            cat2.setWatts(json_items.getJSONObject(y).getString("watts"));
                        }
                        items.add(cat2);
                    }
                    System.out.println("nombre cat: "+cat1.getNombre()+", tamaño: "+size);
                    //seteamos el array final
                    expandableListDetail.put(cat1.getNombre(), items);
                    items = null;
                } else if(cat1.getTipo().equals("item")){
                    cat1.setId(the_json_array.getJSONObject(i).getInt("id"));
                    cat1.setWatts(the_json_array.getJSONObject(i).getString("watts"));

                    items.add(cat1);
                }
            }
            if(items != null){
                expandableListDetail.put("Electrodomesticos", items);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return expandableListDetail;
    }
}