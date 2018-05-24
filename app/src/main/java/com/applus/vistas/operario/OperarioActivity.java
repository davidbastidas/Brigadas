package com.applus.vistas.operario;

import com.applus.controladores.UsuarioController;
import com.applus.R;
import com.applus.modelos.BrigadaParcelable;
import com.applus.modelos.Calendario;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.operario.brigada.BrigadaListado;
import com.applus.vistas.operario.novedad.NovedadListado;
import com.applus.vistas.operario.totalizadores.TotalizadorListado;
import com.applus.vistas.slidermenu.adapter.NavDrawerListAdapter;
import com.applus.vistas.slidermenu.model.NavDrawerItem;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class OperarioActivity extends AppCompatActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	FragmentManager fragmentManager;
	
	BrigadaParcelable material;
	ArrayList<BrigadaParcelable> materialRetirado = new ArrayList<BrigadaParcelable>();
	
	Calendario cal=new Calendario();
	
	public boolean guardadoActivo=true;
	public int ESTADO_SERVICE = 0;
	
	AlertDialog.Builder dialog;
	AlertDialog.Builder dialogoServicios;
	Message msg = null;
	
	SesionSingleton sesion;
	
	@SuppressLint("ResourceType")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidermenu);
		sesion=SesionSingleton.getInstance();
		
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_operario);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons_operario);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.lista_deslizante);
		
		navDrawerItems = new ArrayList<NavDrawerItem>();

		
		if(sesion.getTipo_usuario()==5){
			// adding nav drawer items to array
			// Inicio
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
			// Brigada
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
			// Totalizador
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
			// Novedad
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
			// Salir
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		}else if(sesion.getTipo_usuario()==4){
			// Inicio
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
			// Totalizador
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
			// Novedad
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
			// Salir
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		}else if(sesion.getTipo_usuario()==3){
			// Inicio
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
			// Novedad
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
			// Salir
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		}
		

		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.mipmap.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.operario, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.operario:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.operario).setVisible(false);
		
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	Intent intentar=null;
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			if(sesion.getTipo_usuario()==5){
				intentar = new Intent(this, BrigadaListado.class);
				startActivity(intentar);
			}else if(sesion.getTipo_usuario()==4){
				intentar = new Intent(this, TotalizadorListado.class);
				startActivity(intentar);
			}else if(sesion.getTipo_usuario()==3){
				intentar = new Intent(this, NovedadListado.class);
				startActivity(intentar);
			}
			break;
		case 2:
			if(sesion.getTipo_usuario()==5){
				intentar = new Intent(this, TotalizadorListado.class);
				startActivity(intentar);
			}else if(sesion.getTipo_usuario()==4){
				intentar = new Intent(this, NovedadListado.class);
				startActivity(intentar);
			}else if(sesion.getTipo_usuario()==3){
				confirmDialogEliminarServicios();
			}
			break;
		case 3:
			if(sesion.getTipo_usuario()==5){
				intentar = new Intent(this, NovedadListado.class);
				startActivity(intentar);
			}else if(sesion.getTipo_usuario()==4){
				confirmDialogEliminarServicios();
			}
			break;
		case 4:
			confirmDialogEliminarServicios();
			break;

		default:
			break;
		}
		if (fragment != null) {
			iniciarFragment(fragment);
		} else {
			// error in creating fragment
			System.out.println("OperarioActivity Error in creating fragment");
		}
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	public void iniciarFragment(Fragment fragment){
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
	}

	public void dialogoDinamico(String titulo, final String mensaje) {
		new Handler() {
			public void handleMessage(Message msg) {
				String v = (String) msg.obj;
				if ((v.trim()).equals("ok")) {
					dialog.setIcon(R.drawable.confirmacion_icon);
					dialog.setMessage(mensaje);
					try {
						dialog.show();
					} catch (Exception e) {
					}
				} else {
					dialog.setIcon(R.drawable.info_icon);
					dialog.setMessage(v);
					try {
						dialog.show();
					} catch (Exception e) {
					}
				}
			}
		};
		dialog = new AlertDialog.Builder(this);
		dialog.setTitle(titulo);
		dialog.setPositiveButton("Cerrar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						displayView(1);
					}
				});
		dialog.create();
	}
	
    @Override
    public void onBackPressed() {
    	confirmDialogEliminarServicios();
        /*if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }*/
    }
    AlertDialog.Builder dialogoSINO;
	public void confirmDialogEliminarServicios(){
		if(dialogoSINO==null){
			dialogoSINO = new AlertDialog.Builder(this);  
			dialogoSINO.setTitle("Importante");
			dialogoSINO.setMessage("�Desea Salir?");         
			dialogoSINO.setCancelable(false);
			dialogoSINO.setIcon(R.mipmap.faq);
			dialogoSINO.setPositiveButton("SI", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogo1, int id) {
					finish();
	            }  
	        });
			dialogoSINO.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogo1, int id) {
	            	dialogo1.dismiss();
	            }
	        });
		}
		dialogoSINO.show();
	}
}
