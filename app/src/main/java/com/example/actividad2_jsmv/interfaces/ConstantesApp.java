package com.example.actividad2_jsmv.interfaces;

public interface ConstantesApp {

    String BDD = "proyectos.db";
    int VERSION = 1;
    String TABLA_ESTADOS = "estado";
    String TABLA_DDL_ESTADOS = "CREATE TABLE estado (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "    nombre VARCHAR(60) NOT NULL\n" +
            ");";

    String TABLA_PROYECTO = "proyecto";
    String TABLA_DDL_PROYECTO = "CREATE TABLE proyecto (\n" +
            "    codigo_proyecto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "    codigo_actividad INTEGER NOT NULL,\n" +
            "    estado INTEGER NOT NULL,\n" +
            "    observacion TEXT,\n" +
            "    fecha TEXT DEFAULT (CURRENT_DATE),\n" +
            "    FOREIGN KEY (estado) REFERENCES estado(id) ON DELETE CASCADE\n" +
            ");";

}
