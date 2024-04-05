package com.example.aplicativognsstcc.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.core.app.ActivityCompat;

import com.example.aplicativognsstcc.Data.DatabaseHelper;

public class GNSSLoggingService extends Service {

    private static final String TAG = "GNSSLoggingService";
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

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
        try {
            databaseHelper = new DatabaseHelper(this);
            db = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar o banco de dados", e);
            stopSelf(); // Encerrar o serviço se não for possível inicializar o banco de dados
            return START_NOT_STICKY;
        }

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
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                // Verificar se o provedor de localização GPS está disponível
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // Verificar permissões de localização
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        // Se as permissões não forem concedidas, solicitar permissões
                        ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    } else {
                        // Permissões concedidas, iniciar a coleta de dados GNSS
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
                        Log.d(TAG, "Provedor de localização GPS iniciado com sucesso.");
                    }
                } else {
                    Log.e(TAG, "O provedor de localização GPS não está disponível.");
                }
            } else {
                Log.e(TAG, "LocationManager não pôde ser inicializado.");
                stopSelf(); // Encerrar o serviço se o LocationManager não puder ser inicializado
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao iniciar a coleta de dados GNSS", e);
            stopSelf(); // Encerrar o serviço se ocorrer um erro ao iniciar a coleta de dados GNSS
        }
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
        try {
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
        } catch (Exception e) {
            Log.e(TAG, "Erro ao armazenar dados GNSS", e);
        }
    }

    private void pararColetaGNSS() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao parar a coleta de dados GNSS", e);
        }
    }
}
