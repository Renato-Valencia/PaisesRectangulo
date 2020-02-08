package com.example.paises;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("http://www.geognos.com/api/en/countries/info/all.json", datos,MainActivity.this, (Asynchtask) MainActivity.this);
        ws.execute("");
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        ListView LS=(ListView)findViewById(R.id.lpais);
        LS.setOnItemClickListener(this);
    }

    @Override
    public void processFinish(String result) throws JSONException {
        ArrayList<Paises> lpais= new ArrayList<Paises>();
        JSONObject jsonObject = new JSONObject(result);
        JSONObject jresults = jsonObject.getJSONObject("Results");
        Iterator<?> iterator = jresults.keys();
        while (iterator.hasNext()){
            String key =(String)iterator.next();
            JSONObject jpais = jresults.getJSONObject(key);
            Paises pais = new Paises();

            pais.setTitulo(jpais.getString("Name"));
            JSONObject georectangulo = jpais.getJSONObject("GeoRectangle");

            pais.setWest(georectangulo.getString("West"));
            pais.setEast(georectangulo.getString("East"));
            pais.setNorth(georectangulo.getString("North"));
            pais.setSouth(georectangulo.getString("South"));


            JSONArray jsonGeoCenter = jpais.getJSONArray("GeoPt");
            pais.setLat(jsonGeoCenter.getDouble(0));
            pais.setLongi(jsonGeoCenter.getDouble(1));

            JSONObject jCountryCodes = jpais.getJSONObject("CountryCodes");
            pais.setUrl(jCountryCodes.getString("iso2"));
            lpais.add(pais);
        }
        AdaptadorPais adaptadorPais = new AdaptadorPais(this, lpais);
        ListView lstOpciones = (ListView)findViewById(R.id.lpais);
        lstOpciones.setAdapter(adaptadorPais);
    }



    private void getPermission(String permiso) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(permiso) == PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{permiso}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            Toast.makeText(this.getApplicationContext(),"OK", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this.getApplicationContext(),((Paises)parent.getItemAtPosition(position)).getUrlPdf(),Toast.LENGTH_LONG).show();
       /* DownloadManager.Request request = new DownloadManager.Request(Uri.parse(((Paises)parent.getItemAtPosition(position)).getUrl()));
        request.setDescription("PDF	Paper");
        request.setTitle("Informacion Imagen");
        if (Build.VERSION.SDK_INT >=	Build.VERSION_CODES.HONEYCOMB)	{
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,	"pais.png");
        DownloadManager manager	=	(DownloadManager)this.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        try	{
            manager.enqueue(request);
        }	catch	(Exception e)	{
            Toast.makeText(this.getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }*/
        Intent intent= new Intent(MainActivity.this, MapaImagen.class);
       // Bundle b = new Bundle();
       // b.putString("latlong",((Paises)parent.getItemAtPosition(position)).getLatlong());
       // b.putString("norte",((Paises)parent.getItemAtPosition(position)).getNorth());
       // b.putString("sur",((Paises)parent.getItemAtPosition(position)).getSouth());
       // b.putString("oeste",((Paises)parent.getItemAtPosition(position)).getWest());
       // b.putString("este",((Paises)parent.getItemAtPosition(position)).getEast());
      //  b.putString("lat",((Paises)parent.getItemAtPosition(position)).getLat());



        intent.putExtra("norte",((Paises)parent.getItemAtPosition(position)).getNorth());
        intent.putExtra("sur",((Paises)parent.getItemAtPosition(position)).getSouth());
        intent.putExtra("oeste",((Paises)parent.getItemAtPosition(position)).getWest());
        intent.putExtra("este",((Paises)parent.getItemAtPosition(position)).getEast());
        intent.putExtra("latitud",((Paises)parent.getItemAtPosition(position)).getLat());
        intent.putExtra("longitud",((Paises)parent.getItemAtPosition(position)).getLongi());
        intent.putExtra("url",((Paises)parent.getItemAtPosition(position)).getUrl());
        startActivity(intent);
    }
}
