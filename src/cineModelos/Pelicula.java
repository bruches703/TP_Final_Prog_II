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
    private boolean estaActiva;

    public Pelicula(String titulo, Duration duracion, boolean estaActiva) {
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

    public boolean getEstaActiva() {
        return estaActiva;
    }

    public void setEstaActiva(boolean estaActiva) {
        this.estaActiva = estaActiva;
    }
    // Getters y Setters ------- Fin

    
    public String getDuracionFormateada() {
        if (this.duracion == null) {
            return "Duración no especificada";
        }
        return this.duracion.toMinutes() + " minutos";
    }
    
    @Override
    public String toString() {
        return "Pelicula{" + "titulo=" + titulo + ", duracion=" + getDuracionFormateada()+ ", estaActiva=" + estaActiva + '}';
    }
    
    
    
    
}

