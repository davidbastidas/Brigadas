package com.applus.vistas.operario.totalizadores;
import com.applus.controladores.TotalizadorController;
import com.applus.R;
import com.applus.modelos.Totalizadores;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class TotalizadorListado extends ListActivity {

	protected static final int PAGESIZE = 10;

	protected TextView textViewDisplaying;

	protected View footerView;

	protected boolean loading = false;

	private int offset = 0;
	
	private ProgressBar progressBar;
	
	private ImageButton first;

	private ImageButton last;

	private ImageButton prev;

	private ImageButton next;
	ArrayList<Totalizadores> servicios;
	
	TotalizadorController ServCont=new TotalizadorController();
	Activity Activity;
	Intent intentar;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_vista_total);
		Activity=this;
		context=this;
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		first = (ImageButton) findViewById(R.id.buttonfirst);
		prev = (ImageButton) findViewById(R.id.buttonprev);
		next = (ImageButton) findViewById(R.id.buttonnext);
		last = (ImageButton) findViewById(R.id.buttonlast);
		//la consulta se carga en background con la ejecucion del hilo asinktask
		mostrarBarraProgreso();
		consultarServicios();
		actualizarVistas();
		//(new LoadNextPage()).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_totalizador_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.mnuevatotalizador:
			intentar = new Intent(this, TotalizadoresActivity.class);
			startActivity(intentar);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==3){
			finish();
		}
    }
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		try {
			Totalizadores o = (Totalizadores) getListAdapter().getItem(position);
			vistaDetalle(o);
			/*Toast.makeText(
					this,
					"Selecci�n: " + Integer.toString(position) + " - "
							+ o.getId() + ", " + o.getCliente(),
					Toast.LENGTH_LONG).show();*/
		} catch (Exception ex) {
			Toast.makeText(this, "Selecci�n: " + ex, Toast.LENGTH_SHORT).show();
		}

	}
	protected void onResume(){
		super.onResume();
	}
	protected void onRestart(){
		mostrarBarraProgreso();
		consultarServicios();
		actualizarVistas();
		//(new LoadNextPage()).execute();
		super.onRestart();
	}
	public void mostrarBarraProgreso(){
		progressBar.setVisibility(View.VISIBLE);
		loading = true;
	}
	public void consultarServicios(){
		servicios=ServCont.consultar(offset,PAGESIZE,"",this);
	}
	public void actualizarVistas(){
		try{
			setListAdapter(new TotalizadorAdaptadorLista(Activity, servicios));
			//System.out.println("Se metio ");
		}catch(Exception ex){
			System.out.println("Exception: "+ex);
		}
		updateDisplayingTextView();
		loading = false;
	}
	/*protected class LoadNextPage extends AsyncTask<ServiciosParcelable, Void, ServiciosParcelable> {

		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			loading = true;
			super.onPreExecute();
		}

		@Override
		protected ServiciosParcelable doInBackground(ServiciosParcelable... params) {
			ServCont.setActivity(Activity);
			servicios=ServCont.consultar(offset,PAGESIZE);
			return null;
		}
		
		protected void onPostExecute(ServiciosParcelable parm) {
			try{
				setListAdapter(new MyAdaptadorServicios1(Activity, servicios));
				//System.out.println("Se metio ");
			}catch(Exception ex){
				System.out.println("Exception: "+ex);
			}
			updateDisplayingTextView();
			loading = false;
		}
	}*/
	@SuppressLint("StringFormatMatches")
	protected void updateDisplayingTextView(){
		String text = getString(R.string.displayshort);
		text = String.format(text, Math.min(ServCont.getTamanoConsulta(), offset + 1), Math.min(offset + PAGESIZE, ServCont.getTamanoConsulta()), ServCont.getTamanoConsulta());
		textViewDisplaying.setText(text);
		updateButtons();
		progressBar.setVisibility(View.INVISIBLE);
	}
	private void updateButtons(){
		if (getCurrentPage() > 1){
			first.setEnabled(true);
			prev.setEnabled(true);
			setImageButtonEnabled(context, true, first, R.mipmap.primero);
			setImageButtonEnabled(context, true, prev, R.mipmap.anterior);
		}else{
			first.setEnabled(false);
			prev.setEnabled(false);
			setImageButtonEnabled(context, false, first, R.mipmap.primero1);
			setImageButtonEnabled(context, false, prev, R.mipmap.anterior1);
		}
		if (getCurrentPage() < getLastPage()){
			next.setEnabled(true);
			last.setEnabled(true);
			setImageButtonEnabled(context, true, next, R.mipmap.siguiente);
			setImageButtonEnabled(context, true, last, R.mipmap.ultimo);
		}else{
			next.setEnabled(false);
			last.setEnabled(false);
			setImageButtonEnabled(context, false, next, R.mipmap.siguiente1);
			setImageButtonEnabled(context, false, last, R.mipmap.ultimo1);
		}
	}
	private int getLastPage(){
		return (int) (Math.ceil((float) ServCont.getTamanoConsulta() / PAGESIZE));
	}

	private int getCurrentPage(){
		return (int) (Math.ceil((float) (offset + 1) / PAGESIZE));
	}

	/******************** ACCIONES DE BUTTONS *************************/
	public void first(View v){
		if (!loading){
			offset = 0;
			mostrarBarraProgreso();
			consultarServicios();
			actualizarVistas();
			//(new LoadNextPage()).execute();
		}
	}

	public void next(View v){
		if (!loading){
			offset = getCurrentPage() * PAGESIZE;
			mostrarBarraProgreso();
			consultarServicios();
			actualizarVistas();
			//(new LoadNextPage()).execute();
		}
	}

	public void previous(View v){
		if (!loading){
			offset = (getCurrentPage() - 2) * PAGESIZE;
			mostrarBarraProgreso();
			consultarServicios();
			actualizarVistas();
			//(new LoadNextPage()).execute();
		}
	}

	public void last(View v){
		if (!loading){
			offset = (getLastPage() - 1) * PAGESIZE;
			mostrarBarraProgreso();
			consultarServicios();
			actualizarVistas();
			//(new LoadNextPage()).execute();
		}
	}
	public void vistaDetalle(Totalizadores servicio){
		/*intentar = new Intent(this, Detalle.class );
		intentar.putExtra("servicio", servicio);
		intentar.putExtra("llamado", "itinerario");
        startActivityForResult(intentar, 0);*/
	}
	public static void setImageButtonEnabled(Context ctxt, boolean enabled, 
	        ImageButton item, int iconResId) {

	    item.setEnabled(enabled);
	    Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
	    Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
	    item.setImageDrawable(icon);
	}
	public static Drawable convertDrawableToGrayScale(Drawable drawable) {
	    if (drawable == null) 
	        return null;

	    Drawable res = drawable.mutate();
	    res.setColorFilter(Color.GRAY, Mode.SRC_IN);
	    return res;
	}
}
