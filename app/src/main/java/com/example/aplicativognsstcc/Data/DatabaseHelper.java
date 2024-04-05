package com.example.aplicativognsstcc.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome do banco de dados
    private static final String DATABASE_NAME = "tcc.db";

    // Versão do banco de dados
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela e colunas
    public static final String TABLE_GNSS_DATA = "gnss_data";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ALTITUDE = "altitude";
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_BEARING = "bearing";
    public static final String COLUMN_PROVIDER = "provider";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    // Coluna para armazenar os dados GNSS em formato JSON
    public static final String COLUMN_GNSS_DATA_JSON = "gnss_data_json";

    // Comando SQL para criar a tabela de dados GNSS
    private static final String SQL_CREATE_TABLE_GNSS_DATA =
            "CREATE TABLE " + TABLE_GNSS_DATA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LATITUDE + " REAL," +
                    COLUMN_LONGITUDE + " REAL," +
                    COLUMN_ALTITUDE + " REAL," +
                    COLUMN_ACCURACY + " REAL," +
                    COLUMN_SPEED + " REAL," +
                    COLUMN_BEARING + " REAL," +
                    COLUMN_PROVIDER + " TEXT," +
                    COLUMN_TIMESTAMP + " INTEGER," +
                    // Coluna para armazenar os dados GNSS em formato JSON
                    COLUMN_GNSS_DATA_JSON + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executa o comando SQL para criar a tabela de dados GNSS
        db.execSQL(SQL_CREATE_TABLE_GNSS_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualiza o esquema do banco de dados, se necessário
        // Aqui você pode adicionar a lógica para migrar os dados, se necessário
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GNSS_DATA);
        onCreate(db);
    }
}
