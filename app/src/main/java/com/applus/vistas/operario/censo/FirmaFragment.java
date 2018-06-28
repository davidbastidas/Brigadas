package com.applus.vistas.operario.censo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applus.R;
import com.applus.controladores.CensoController;
import com.applus.controladores.ConexionController;
import com.applus.modelos.Censo;
import com.applus.modelos.CensoForm;
import com.applus.modelos.Cliente;
import com.applus.modelos.SesionSingleton;
import com.applus.vistas.operario.DialogoGPS;
import com.applus.vistas.operario.DialogoGPS.OnGPSIntent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("ValidFragment")
public class FirmaFragment extends Fragment{

	private NuevoCensoFormFragment.FirmaListener mListener;

	public FirmaFragment(NuevoCensoFormFragment.FirmaListener mListener){
		try{
			this.mListener = mListener;
		}catch (ClassCastException e){
			throw new ClassCastException(mListener.toString() + "implementa el listener");
		}
	}
	SignatureView firma;
	Button borrarFirma, guardarFirma;

	String firmaString = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_firma, container, false);
		getActivity().setTitle("Firma del Cliente");

		firma = (SignatureView) rootView.findViewById(R.id.signature);
		firma.setSigBackgroundColor(Color.WHITE);
		firma.setSigColor(Color.BLACK);
		borrarFirma = (Button) rootView.findViewById(R.id.borrar_firma);
		borrarFirma.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				firma.clearSignature();
			}
		});
		guardarFirma = (Button) rootView.findViewById(R.id.guardarFirma);
		guardarFirma.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap mySignature = firma.getImage();
				firmaString = BitMapToString(mySignature);
				mListener.onRecibirFirma(firmaString);
			}
		});

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_nuevo_censo, menu);
		menu.findItem(R.id.mnuevocenso).setVisible(false);
		menu.findItem(R.id.m_electrodomesticos).setVisible(false);
		menu.findItem(R.id.m_guardar_censo).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	public String BitMapToString(Bitmap bitmap){
		ByteArrayOutputStream baos=new  ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG,30, baos);
		byte [] b=baos.toByteArray();
		String temp= Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	/**
	 * @param encodedString
	 * @return bitmap (from given string)
	 */
	public Bitmap StringToBitMap(String encodedString){
		try {
			byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
			Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			return bitmap;
		} catch(Exception e) {
			e.getMessage();
			return null;
		}
	}
}
