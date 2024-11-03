package com.example.actividad2_jsmv.servicios;

import static com.example.actividad2_jsmv.interfaces.ConstantesApp.TABLA_ESTADOS;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.actividad2_jsmv.interfaces.ConstantesApp;
public class ConectaDB extends SQLiteOpenHelper {

    public ConectaDB(
            @Nullable Context context,
            @Nullable String name,
            @Nullable SQLiteDatabase.CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantesApp.TABLA_DDL_ESTADOS);
        Log.i("ConectaDB", "Tabla Estados creada");
        db.execSQL(ConstantesApp.TABLA_DDL_PROYECTO);
        Log.i("ConectaDB", "Tabla Proyectos creada");

        insertarEstados(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertarEstados(SQLiteDatabase db) {
        String[] estados = {"No Iniciado", "Retrasado", "Ejecutando", "Terminado"};

        for (String estado : estados) {
            ContentValues values = new ContentValues();
            values.put("nombre", estado);
            db.insert(TABLA_ESTADOS, null, values);
        }
        Log.i("ConectaDB", "Estados insertados");
    }





}
