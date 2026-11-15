/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.time.Duration;

/**
 *
 * @author Bruches
 */
public class Pelicula implements Serializable{
    private String titulo;
    private Duration duracion;
    private double estaActiva;

    public Pelicula(String titulo, Duration duracion, double estaActiva) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.estaActiva = estaActiva;
    }
    
    
    // Getters y Setters ------- Inicio

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
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

