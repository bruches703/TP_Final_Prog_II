/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author bruch
 */
public class SucursalCine implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String sucursal;
    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<Reserva> reservas;
    
    //getters y setters ------------- INICIO
    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<Funcion> funciones) {
        this.funciones = funciones;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    //getters y setters ------------- FIN

    public SucursalCine(String sucursal, List<Pelicula> peliculas, List<Sala> salas, List<Funcion> funciones, List<Reserva> reservas) {
        this.sucursal = sucursal;
        this.peliculas = peliculas;
        this.salas = salas;
        this.funciones = funciones;
        this.reservas = reservas;
    }
    
    public void agregarPelicula(Pelicula pelicula){
        this.peliculas.add(pelicula);
    } 
    
    public void agregarSala(Sala sala){
        this.salas.add(sala);
    }
    
    public void agregarFuncion(Funcion funcion){
        this.funciones.add(funcion);
    }
    
    public void agregarReserva(Reserva reserva){
        this.reservas.add(reserva);
    }
    
}
