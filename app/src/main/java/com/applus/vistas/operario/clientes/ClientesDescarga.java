package com.applus.vistas.operario.clientes;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.BarrioController;
import com.applus.controladores.CensoController;
import com.applus.controladores.ClientesController;
import com.applus.controladores.ConexionController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.controladores.interfaces.ClientesInterface;
import com.applus.modelos.Barrio;
import com.applus.modelos.Cliente;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;
import com.applus.modelos.SesionSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ClientesDescarga extends AppCompatActivity implements ClientesInterface, DialogProgress.NoticeDialogListener{

	CensoController ServCont=new CensoController();
	Activity Activity;
	Intent intentar;
	Context context;

	Spinner departamento, municipio, barrio;
    Button descargarDepartamento, descargarMunicipio, descargarBarrio;

	Departamento departamentoElegido = null;
	Municipio municipioElegido = null;
	Barrio barrioElegido = null;

	ConexionController conexion = new ConexionController();

	ClientesController clieController = new ClientesController();

	String busquedaPor = "";
	long totalPaginas = 0;
	long contadorPaginas = 0;
	long registrosPorPagina = 1000;
	long pagina = 0;

	//dialogo
	DialogFragment dialogo = new DialogProgress();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_cliente_descarga);
		Activity=this;
		context=this;
        conexion.clienteInterface = this;

        departamento = (Spinner) findViewById(R.id.departamento);
        municipio = (Spinner) findViewById(R.id.municipio);
        barrio = (Spinner) findViewById(R.id.barrio);

        descargarDepartamento = (Button) findViewById(R.id.descargarDepartamento);
        descargarMunicipio = (Button) findViewById(R.id.descargarMunicipio);
        descargarBarrio = (Button) findViewById(R.id.descargarBarrio);

		DepartamentoController dep = new DepartamentoController();
		ArrayList<Departamento> departamentos = dep.consultar(0, 0, "", Activity);
		ArrayAdapter<Departamento> departamentoAdapter = new ArrayAdapter<Departamento>(Activity,
				android.R.layout.simple_spinner_item, departamentos);
		departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		departamento.setAdapter(departamentoAdapter);
		departamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				departamentoElegido = (Departamento) parentView.getItemAtPosition(position);

				//llenando los municipios
				MunicipioController mun = new MunicipioController();
				ArrayList<Municipio> municipios = mun.consultar(0, 0, "fk_departamento=" + departamentoElegido.getId(), Activity);
				ArrayAdapter<Municipio> municipioAdapter = new ArrayAdapter<Municipio>(Activity,
						android.R.layout.simple_spinner_item, municipios);
				municipioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				municipio.setAdapter(municipioAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				departamentoElegido = null;
			}
		});

		municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				municipioElegido = (Municipio) parentView.getItemAtPosition(position);

				//llenando los barrios
				BarrioController barr = new BarrioController();
				ArrayList<Barrio> barrios = barr.consultar(0, 0, "fk_municipio=" + municipioElegido.getId() + " ORDER BY nombre", Activity);
				ArrayAdapter<Barrio> barrioAdapter = new ArrayAdapter<Barrio>(Activity,
						android.R.layout.simple_spinner_item, barrios);
				barrioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				barrio.setAdapter(barrioAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				municipioElegido = null;
			}
		});

		barrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				barrioElegido = (Barrio) parentView.getItemAtPosition(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				barrioElegido = null;
			}
		});

        descargarDepartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(departamentoElegido != null){
					dialogo.show(getFragmentManager(), "Clientes");
                    //descargar por departamento
					busquedaPor = "departamento";
					new AsyncTask<Void, Integer, Boolean>(){

						@Override
						protected Boolean doInBackground(Void... voids) {
							conexion.getCountClientes(
									""+ SesionSingleton.getInstance().getFk_id_operario(),
									busquedaPor,
									departamentoElegido.getId()
							);
							return true;
						}
					}.execute();

                }else{
                    Toast.makeText(Activity, "No hay departamento", Toast.LENGTH_SHORT).show();
                }
            }
        });

        descargarMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(municipioElegido != null){
					dialogo.show(getFragmentManager(), "Clientes");
                    //descargar por municipio
					busquedaPor = "municipio";
					new AsyncTask<Void, Integer, Boolean>(){

						@Override
						protected Boolean doInBackground(Void... voids) {
							conexion.getCountClientes(
									""+ SesionSingleton.getInstance().getFk_id_operario(),
									busquedaPor,
									municipioElegido.getId()
							);
							return true;
						}
					}.execute();

                }else{
                    Toast.makeText(Activity, "No hay municipio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        descargarBarrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barrioElegido != null){
					dialogo.show(getFragmentManager(), "Clientes");
                    //descargar por barrio
					busquedaPor = "barrio";
					System.out.println("Seguimos aqui barrios");
					new AsyncTask<Void, Integer, Boolean>(){

						@Override
						protected Boolean doInBackground(Void... voids) {
							conexion.getCountClientes(
									""+ SesionSingleton.getInstance().getFk_id_operario(),
									busquedaPor,
									barrioElegido.getId()
							);
							return true;
						}
					}.execute();

                }else{
                    Toast.makeText(Activity, "No hay barrio", Toast.LENGTH_SHORT).show();
                }
            }
        });

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==3){
			finish();
		}
    }

	protected void onResume(){
		super.onResume();
	}
	protected void onRestart(){
		super.onRestart();
	}

	@Override
	public void onCountClientes(String result) {
		System.out.println("onCountClientes: "+result);
		try {
			JSONObject obj = new JSONObject(result);
			float paginas = Float.parseFloat(obj.getString("total")) / registrosPorPagina;
			totalPaginas = (long) Math.ceil(paginas);
			System.out.println("total clientes: "+obj.getString("total"));

			//eliminando para insertar lo nuevo
			clieController.eliminarTodo(Activity);
			long foranea = 0;
			if (busquedaPor.equals("departamento")){
				foranea = departamentoElegido.getId();
			}else if (busquedaPor.equals("municipio")){
				foranea = municipioElegido.getId();
			}else if (busquedaPor.equals("barrio")){
				foranea = barrioElegido.getId();
			}
			final long forarenaF = foranea;
			new AsyncTask<Void, Integer, Boolean>(){

				@Override
				protected Boolean doInBackground(Void... voids) {
					conexion.getClientes(
							""+ SesionSingleton.getInstance().getFk_id_operario(),
							pagina,
							registrosPorPagina,
							busquedaPor,
							forarenaF
					);
					return true;
				}
			}.execute();

		} catch (Exception e) {
			dialogo.dismiss();
			Toast.makeText(Activity, "Ocurrio un problema leyendo los clientes", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

    @Override
    public void onDescargarClientes(String output) {
        //procesamos los clientes recibidos
		JSONObject json_data;
		try {
			json_data = new JSONObject(output);
			JSONArray clientes = json_data.getJSONArray("clientes");
			Cliente c = null;
			int size = clientes.length();
			for (int i = 0; i < size; ++i) {
				JSONObject cli = clientes.getJSONObject(i);
				c = new Cliente();
				c.setId(cli.getLong("id"));
				c.setNombre(cli.getString("nombre"));
				c.setDireccion(cli.getString("direccion"));
				c.setNic(cli.getLong("nic"));
				c.setTipo(cli.getString("tipo"));
				c.setCenso(cli.getInt("censo"));
				c.setTipo_cliente(cli.getString("tipo_cliente"));
				c.setLongitud(cli.getString("longitud"));
				c.setLatitud(cli.getString("latitud"));
				c.setOrden_reparto(cli.getString("orden_reparto"));
				c.setItinerario(cli.getString("itinerario"));
				c.setFk_barrio(cli.getInt("fk_barrio"));
				c.setCodigo(cli.getLong("codigo"));
				c.setCensos(cli.getString("censos"));

				clieController.insertar(c, Activity);
			}
		} catch (Exception e) {
			System.out.println("error guardando cliente" + e);
		} finally {
			//actualizar estado de la barra

			//calcular si quedan mas clientes
			contadorPaginas++;
			System.out.println(contadorPaginas+" de "+ totalPaginas);
			if(contadorPaginas < totalPaginas){
				//volvemos a pedir mas clientes
				pagina = pagina + registrosPorPagina;
				long foranea = 0;
				if (busquedaPor.equals("departamento")){
					foranea = departamentoElegido.getId();
				}else if (busquedaPor.equals("municipio")){
					foranea = municipioElegido.getId();
				}else if (busquedaPor.equals("barrio")){
					foranea = barrioElegido.getId();
				}
				final long forarenaF = foranea;
				new AsyncTask<Void, Integer, Boolean>(){

					@Override
					protected Boolean doInBackground(Void... voids) {
						conexion.getClientes(
								""+ SesionSingleton.getInstance().getFk_id_operario(),
								pagina,
								registrosPorPagina,
								busquedaPor,
								forarenaF
						);
						return true;
					}
				}.execute();

			}else{
                //termino tarea
				dialogo.dismiss();
				Toast.makeText(Activity, "Clientes descargados", Toast.LENGTH_SHORT).show();
                contadorPaginas = 0;
                totalPaginas = 0;
                pagina = 0;
			}
		}
    }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {

	}
}
