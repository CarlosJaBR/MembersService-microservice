package com.members.membersService.model;

import java.time.LocalDate;

public class DatosEntrenamiento {
    private Long memberId;
    private LocalDate fecha;
    private String tipoEjercicio;
    private Integer duracion; 
    private Integer caloriasQuemadas;

    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getTipoEjercicio() {
        return tipoEjercicio;
    }
    public void setTipoEjercicio(String tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }
    public Integer getDuracion(){
        return duracion;
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getCaloriasQuemadas(){
        return caloriasQuemadas;
    }

    public void setCaloriasQuemadas(Integer caloriasQuemadas) {
        this.caloriasQuemadas = caloriasQuemadas;
    }
    
    

}