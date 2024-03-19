package com.example.aplicativognsstcc.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.aplicativognsstcc.Data.DatabaseHelper;


public class GNSSLoggingService extends Service {

    private static final String TAG = "GNSSLoggingService";

    private boolean isRunning = false;

    // Banco de dados
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    // Location Manager
    private LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;

        // Inicializar o banco de dados
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        // Iniciar o processo de coleta de dados GNSS
        iniciarColetaGNSS();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;

        // Parar o processo de coleta de dados GNSS
        pararColetaGNSS();

        // Fechar o banco de dados
        if (db != null) {
            db.close();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    private void iniciarColetaGNSS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (isRunning) {
                if (location != null) {
                    armazenarDadosGNSS(location);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    private void armazenarDadosGNSS(Location location) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LATITUDE, location.getLatitude());
        values.put(DatabaseHelper.COLUMN_LONGITUDE, location.getLongitude());
        values.put(DatabaseHelper.COLUMN_ALTITUDE, location.getAltitude());
        values.put(DatabaseHelper.COLUMN_ACCURACY, location.getAccuracy());
        values.put(DatabaseHelper.COLUMN_SPEED, location.getSpeed());
        values.put(DatabaseHelper.COLUMN_BEARING, location.getBearing());
        values.put(DatabaseHelper.COLUMN_PROVIDER, location.getProvider());
        values.put(DatabaseHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());

        long newRowId = db.insert(DatabaseHelper.TABLE_GNSS_DATA, null, values);

        if (newRowId != -1) {
            Log.d(TAG, "Dados GNSS armazenados com sucesso: " + newRowId);
        } else {
            Log.e(TAG, "Erro ao armazenar dados GNSS");
        }
    }

    private void pararColetaGNSS() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
