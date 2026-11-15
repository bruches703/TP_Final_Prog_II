/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;
import cineModelos.Pelicula;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 *
 * @author Bruches
 */
public class Funcion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDate fecha;
    private LocalTime horario;
    private Sala sala;
    private Pelicula pelicula;
    private double precio;

    //Constructor

    public Funcion(LocalDate fecha, LocalTime horario, Sala sala, Pelicula pelicula, double precio) {
        this.fecha = fecha;
        this.horario = horario;
        this.sala = sala;
        this.pelicula = pelicula;
        this.precio = precio;
    }
    
    
    //Getters y Setters------- INICIO

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }
    
    // Getters y Setters------------- Fin
    
    public LocalDateTime getFinalizacionPelicula(){
        LocalDateTime inicio = LocalDateTime.of(this.fecha, this.horario);
        
        return inicio.plus(pelicula.getDuracion());
    }
    
}
