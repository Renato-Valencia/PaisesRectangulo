package com.example.paises;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdaptadorPais extends ArrayAdapter<Paises> {
    public AdaptadorPais(Context context, ArrayList<Paises> datos) {
        super(context, R.layout.ly_pais, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ly_pais, null);

        TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
        lblTitulo.setText(getItem(position).getTitulo());

        TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
        lblSubtitulo.setText(getItem(position).getSubtitulo());

        ImageView imageView = (ImageView)item.findViewById(R.id.imgPais);
        Glide.with(this.getContext())
                .load(getItem(position).getUrl())
                //.error(R.drawable.imgnotfound)
                .into(imageView);

        imageView.setTag(getItem(position).getUrl());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = v.getTag().toString();

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("PDF Paper");
                request.setTitle("Imagen");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Descarga.png");
                DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                try {
                    manager.enqueue(request);
                    Toast.makeText(getContext(), "Descarga completa!!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
        return(item);
    }
}
