package com.example.aplicativognsstcc.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome do banco de dados
    private static final String DATABASE_NAME = "seu_banco_de_dados.db";

    // Versão do banco de dados
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela e coluna
    public static final String TABLE_PONTOS = "pontos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOME = "nome";

    // Comando SQL para criar a tabela
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_PONTOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NOME + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executa o comando SQL para criar a tabela
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualiza o esquema do banco de dados, se necessário
        // Aqui você pode adicionar a lógica para migrar os dados, se necessário
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PONTOS);
        onCreate(db);
    }
}
