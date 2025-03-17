package com.members.membersService.model;

public class ResumenEntrenamiento {
    private Integer totalDuracion;
    private Integer totalCalorias;

    public void actualizar(DatosEntrenamiento datos) {
        this.totalDuracion += datos.getDuracion();
        this.totalCalorias += datos.getCaloriasQuemadas();
    }

}