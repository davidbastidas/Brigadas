package com.applus.vistas.operario.clientes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.applus.R;
import com.applus.vistas.operario.censo.SignatureView;

import java.io.ByteArrayOutputStream;

@SuppressLint("ValidFragment")
public class Firma extends AppCompatActivity {

	SignatureView firma;
	Button borrarFirma, guardarFirma;

	String firmaString = "";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_firma);

		firma = findViewById(R.id.signature);
		firma.setSigBackgroundColor(Color.WHITE);
		firma.setSigColor(Color.BLACK);
		firma.modified = false;
		borrarFirma = findViewById(R.id.borrar_firma);
		borrarFirma.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				firma.clearSignature();
			}
		});
		guardarFirma = findViewById(R.id.guardarFirma);
		guardarFirma.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firma.hasChanged()){
					Bitmap mySignature = firma.getImage();
					firmaString = BitMapToString(mySignature);
					Intent returnIntent = new Intent();
					returnIntent.putExtra("firma",firmaString);
					setResult(ActualizarCliente.RESULT_OK,returnIntent);
					finish();
				}else{
					Toast.makeText(Firma.this, "Por favor, FIRMAR.", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

	public String BitMapToString(Bitmap bitmap){
		ByteArrayOutputStream baos=new  ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG,30, baos);
		byte [] b=baos.toByteArray();
		String temp= Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
}
