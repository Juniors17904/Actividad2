package com.example.actividad2_jsmv.modelo.dto;

import androidx.annotation.NonNull;

public class Proyecto {
    private int codigoProyecto;
    private int codigoActividad;
    private String estado;
    private String observacion;
    private String fecha;

    public Proyecto() {
    }

    public Proyecto(int codigoProyecto, int codigoActividad, String estado, String observacion, String fecha) {
        this.codigoProyecto = codigoProyecto;
        this.codigoActividad = codigoActividad;
        this.estado = estado;
        this.observacion = observacion;
        this.fecha = fecha;
    }

    public int getCodigoProyecto() {
        return codigoProyecto;
    }

    public void setCodigoProyecto(int codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }

    public int getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(int codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    @NonNull
    @Override
    public String toString() {
        return
                "Codigo Proyecto: " + codigoProyecto +"\n"+
                "Codigo Actividad: " + codigoActividad +"\n"+
                "Estado: " + estado +"\n"+
                "Observacion: " + observacion +"\n"+
                "Fecha: " + fecha ;
    }
}
