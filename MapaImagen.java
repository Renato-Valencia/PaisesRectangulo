package com.example.paises;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapaImagen extends AppCompatActivity implements OnMapReadyCallback{
    private String norte="", sur="", este="", oeste="", url="";
    private Double lat=0.0, longi=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_imagen);
        norte=getIntent().getExtras().getString("norte");
        sur=getIntent().getExtras().getString("sur");
        este=getIntent().getExtras().getString("este");
        oeste=getIntent().getExtras().getString("oeste");
        lat=getIntent().getExtras().getDouble("latitud", 0.0);
        longi=getIntent().getExtras().getDouble("longitud", 0.0);
        url=getIntent().getExtras().getString("url");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        Double nort=Double.valueOf(norte);
        Double su=Double.valueOf(sur);
        Double est=Double.valueOf(este);
        Double oest=Double.valueOf(oeste);

        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 6);
        googleMap.moveCamera(camUpd1);
        PolylineOptions lineas = new PolylineOptions().add(new LatLng(nort, oest)).add(new LatLng(nort, est)).add(new LatLng(su, est)).add(new LatLng(su, oest)).add(new LatLng(nort, oest));
        lineas.width(10);
        lineas.color(Color.RED);

        googleMap.addPolyline(lineas);

        //Imagen cargar
        ImageView imageView = (ImageView)findViewById(R.id.imgBandera);
        Glide.with(getApplicationContext())
                .load(url)
                //.error(R.drawable.imgnotfound)
                .into(imageView);
    }
}
