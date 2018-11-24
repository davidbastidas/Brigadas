package com.applus.vistas.operario.clientes;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.BarrioController;
import com.applus.controladores.CensoClienteController;
import com.applus.controladores.CensoController;
import com.applus.controladores.ClientesController;
import com.applus.controladores.ConexionController;
import com.applus.controladores.DepartamentoController;
import com.applus.controladores.MunicipioController;
import com.applus.controladores.interfaces.ClientesInterface;
import com.applus.modelos.Barrio;
import com.applus.modelos.CensoCliente;
import com.applus.modelos.Cliente;
import com.applus.modelos.Departamento;
import com.applus.modelos.Municipio;
import com.applus.modelos.SesionSingleton;
import com.applus.modelos.StateVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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


public class CensosDescarga extends AppCompatActivity implements ClientesInterface, DialogProgress.NoticeDialogListener{

	CensoController ServCont=new CensoController();
	Activity Activity;
	Intent intentar;
	Context context;

	Spinner departamento, municipio, barrio;
    Button descargarBarrio;
    TextView t_barrios_descargados;

	Departamento departamentoElegido = null;
	Municipio municipioElegido = null;
	Barrio barrioElegido = null;
	AdapterSpinnerCheck myAdapterBarrio = null;

	ConexionController conexion = new ConexionController();

	ClientesController clieController = new ClientesController();

	CensoClienteController censoClienteController = new CensoClienteController();

	String busquedaPor = "";
	long totalPaginas = 0;
	long contadorPaginas = 0;
	long registrosPorPagina = 100;
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
        descargarBarrio = (Button) findViewById(R.id.descargarBarrio);
		t_barrios_descargados = findViewById(R.id.t_barrios_descargados);
		SharedPreferences preferencias = getSharedPreferences("configuracion",
				Context.MODE_PRIVATE);
		t_barrios_descargados.setText(preferencias.getString("barrioselegidos", ""));

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

				ArrayList<StateVO> listVOs = new ArrayList<>();
				StateVO stateVO = new StateVO();
				stateVO.setId(0);
				stateVO.setTitle("SELECCIONE LOS BARRIOS");
				stateVO.setSelected(false);
				listVOs.add(stateVO);
				for (int i = 0; i < barrios.size(); i++) {
					stateVO = new StateVO();
					stateVO.setId(barrios.get(i).getId());
					stateVO.setTitle(barrios.get(i).getNombre());
					stateVO.setSelected(false);
					listVOs.add(stateVO);
				}
				myAdapterBarrio = new AdapterSpinnerCheck(Activity, 0, listVOs);

				barrio.setAdapter(myAdapterBarrio);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				municipioElegido = null;
			}
		});

        descargarBarrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				ArrayList<StateVO> barriosCheck = myAdapterBarrio.getListState();
				ArrayList<StateVO> barriosElegidos = null;
				String barriosString = "";
				if(barriosCheck.size() > 0){
					barriosElegidos = new ArrayList<>();
					for (int i = 0; i < barriosCheck.size(); i++){
						if(barriosCheck.get(i).isSelected()){
							barriosElegidos.add(barriosCheck.get(i));
							barriosString += barriosCheck.get(i).getTitle() + "\n";
						}
					}
					if(barriosElegidos.size() > 0){
						Gson gson = new Gson();
						String json = gson.toJson(barriosElegidos);

						dialogo.show(getFragmentManager(), "Bajando Censos Realizados...");

						SharedPreferences preferencias = getSharedPreferences("configuracion",
								Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferencias.edit();
						editor.putString("barrioselegidos", barriosString);
						editor.commit();
						t_barrios_descargados.setText(barriosString);
						System.out.println("JsOn Array: " + json);
						new AsyncTask<Void, Integer, Boolean>(){

							@Override
							protected Boolean doInBackground(Void... voids) {
								conexion.getUrlArchivoCensos(
										""+ SesionSingleton.getInstance().getFk_id_operario(),
										json
								);
								return true;
							}
						}.execute();
					} else {
						Toast.makeText(Activity, "Elija al menos un barrio.", Toast.LENGTH_SHORT).show();
					}
                }else{
                    Toast.makeText(Activity, "No hay barrios", Toast.LENGTH_SHORT).show();
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
			System.out.println(pagina+" , "+ registrosPorPagina);
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
			Toast.makeText(Activity, "Ocurrio un problema leyendo los censos", Toast.LENGTH_LONG).show();
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
				if(cli.has("reporte")){
					c.setReporte(cli.getString("reporte"));
				}else{
					c.setReporte("");
				}
				if(c.getCodigo() == 1313760){
					System.out.println("OJOOOOOOO: 1313760");
				}
				clieController.insertar(c, Activity);
			}
		} catch (Exception e) {
			System.out.println("error guardando cliente" + e);
		} finally {
			//actualizar estado de la barra

			//calcular si quedan mas clientes
			contadorPaginas++;

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
				System.out.println(pagina+" , "+ registrosPorPagina);
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

	@Override
	public void onDescargarUrlCensos(String result) {
		System.out.println("onDescargarUrlCensos: "+result);
		try {
			String urlZip = result.trim();
			if(URLUtil.isValidUrl(urlZip)){
				new AsyncTask<Void, Integer, Boolean>(){

					@Override
					protected Boolean doInBackground(Void... voids) {
						File file1 = downloadZip(urlZip);
						unzip(file1);
						censoClienteController.eliminarTodo(Activity);

						String filename = URLUtil.guessFileName(urlZip, null, null);
						if (filename.indexOf(".") > 0){
							filename = filename.substring(0, filename.lastIndexOf("."));
						}
						try{
							File f = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
							File[] files = f.listFiles();
							for (File inFile : files) {
								if (!inFile.isDirectory()) {
									leerArchivo(inFile);
								}
							}
						} catch (Exception e){
							System.err.println(e);
						}finally {
							dialogo.dismiss();
						}

						return true;
					}
				}.execute();
			} else {
				Toast.makeText(Activity, result, Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			dialogo.dismiss();
			Toast.makeText(Activity, "Ocurrio un problema leyendo los censos", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} finally {
			dialogo.dismiss();
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
					_dirChecker(Environment.getExternalStorageDirectory() + File.separator + fileName + File.separator + ze.getName());
				} else {
					FileOutputStream fout = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+ File.separator + fileName, ze.getName()));
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
			JSONArray censos = json_data.getJSONArray("censos");
			CensoCliente c = null;
			ContentValues values = null;
			int size = censos.length();
			for (int i = 0; i < size; ++i) {
				JSONObject cli = censos.getJSONObject(i);
				c = new CensoCliente();
				c.setClienteId(cli.getLong("cliente_id"));
				c.setFecha(cli.getString("fecha"));
				c.setEstado(cli.getString("estado"));
				c.setUsuario(cli.getString("usuario"));
				c.setAprobado(cli.getInt("aprobado"));

				censoClienteController.insertar(c, Activity);
				System.err.println("Insertado " + cli.getLong("cliente_id"));
			}
		}catch (Exception e){
			System.err.println("Error " + e);
		}finally {

		}
	}
}
