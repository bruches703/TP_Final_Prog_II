/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.util.Date;

/**
 *
 * @author Bruches
 */
public class Pelicula {
    private String titulo;
    private Date duracion;
    private double estaActiva;
    
    // Getters y Setters ------- Inicio

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDuracion() {
        return duracion;
    }

    public void setDuracion(Date duracion) {
        this.duracion = duracion;
    }

    public double getEstaActiva() {
        return estaActiva;
    }

    public void setEstaActiva(double estaActiva) {
        this.estaActiva = estaActiva;
    }
    
    // Getters y Setters ------- Fin
}
