package com.example.actividad2_jsmv.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.actividad2_jsmv.interfaces.ConstantesApp;
import com.example.actividad2_jsmv.modelo.dto.Proyecto;
import com.example.actividad2_jsmv.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    private SQLiteDatabase db;

    public ProyectoDAO(Context context) {
        Log.i("ProductDAO","Instanciando BD");
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION)
                .getWritableDatabase();
    }

    public String insertar(Proyecto p) {
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("codigo_proyecto", p.getCodigoProyecto());
        registro.put("codigo_actividad", p.getCodigoActividad());
        registro.put("estado", p.getEstado());
        registro.put("observacion", p.getObservacion());
        registro.put("fecha", p.getFecha());
        try {
            db.insertOrThrow(ConstantesApp.TABLA_PROYECTO, null, registro);
            Log.i("ProductDAO","INSERTANDO");
        } catch (SQLException ex) {
            resp = ex.getMessage();
        }
        return resp;
    }

    public String eliminar(int id) {
        String resp = "";
        String[] parametros = {String.valueOf(id)};
        try {
            db.delete(ConstantesApp.TABLA_PROYECTO, "codigo_proyecto=?", parametros);
            Log.i("ProductDAO","ELIMINANDO");
        } catch (SQLException ex) {
            resp = ex.getMessage();
        }
        return resp;

    }

    public List<Proyecto> getList() {
        List<Proyecto> lista = new ArrayList<>();
        String cadSQL = "SELECT codigo_proyecto, codigo_actividad, estado, observacion, fecha FROM " + ConstantesApp.TABLA_PROYECTO + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Proyecto p = new Proyecto();
                    p.setCodigoProyecto(c.getInt(c.getColumnIndexOrThrow("codigo_proyecto")));
                    p.setCodigoActividad(c.getInt(c.getColumnIndexOrThrow("codigo_actividad")));
                    p.setEstado(c.getString(c.getColumnIndexOrThrow("estado")));
                    p.setObservacion(c.getString(c.getColumnIndexOrThrow("observacion")));
                    p.setFecha(c.getString(c.getColumnIndexOrThrow("fecha")));
                    lista.add(p);
                } while (c.moveToNext());
            }
            Log.i("ProductDAO","OBTENIENDO LISTA");
            c.close();
        }
       return lista;
    }

    public ArrayList<String> obtenerEstados() {
        ArrayList<String> estados = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT nombre FROM estado", null);

        if (cursor.moveToFirst()) {
            do {
                estados.add(cursor.getString(0)); // Agregar el nombre del estado a la lista
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.i("ProductDAO","MOSTRAR ESTADO");
        return estados; // Retornar la lista de estados
    }

    public String update(Proyecto p) {
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("codigo_actividad", p.getCodigoActividad());
        registro.put("estado", p.getEstado());
        registro.put("observacion", p.getObservacion());
        registro.put("fecha", p.getFecha());

        try {
            String[] parametros = {"" + p.getCodigoProyecto()};
            int filasAfectadas = db.update(ConstantesApp.TABLA_PROYECTO, registro, "codigo_proyecto=?", parametros);

            // Verificar si se actualizó algún registro
            if (filasAfectadas == 0) {
                resp = "No se encontró el Codigo de Proyecto para actualizar.";
            }
        } catch (SQLException ex) {
            resp = ex.getMessage(); // Capturar el mensaje del SQLException
        }
        return resp;
    }

    public Proyecto buscar(int p) {
        Log.i("ProyectoDAO","Buscar: "+p);

        String resp ="";
        Proyecto pr = null;
        String cadenaSQL = "SELECT codigo_proyecto, codigo_actividad, estado, observacion, fecha FROM " + ConstantesApp.TABLA_PROYECTO + " WHERE codigo_proyecto=?";
        Cursor c = null;

        try {
             c = db.rawQuery(cadenaSQL, new String[]{String.valueOf(p)});

            if (c != null && c.moveToFirst()) {
                pr = new Proyecto();
                pr.setCodigoProyecto(c.getInt(c.getColumnIndexOrThrow("codigo_proyecto")));
                pr.setCodigoActividad(c.getInt(c.getColumnIndexOrThrow("codigo_actividad")));
                pr.setEstado(c.getString(c.getColumnIndexOrThrow("estado")));
                pr.setObservacion(c.getString(c.getColumnIndexOrThrow("observacion")));
                pr.setFecha(c.getString(c.getColumnIndexOrThrow("fecha")));
            }
        } catch (SQLException e) {
            Log.e("ProductDAO", "Error buscando proyecto: " + e.getMessage());
        }
        c.close();
        return pr;
    }



}
