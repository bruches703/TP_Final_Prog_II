/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruches
 */

public class EstadoCine implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<Reserva> reservas;

    public EstadoCine() {
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public EstadoCine(
            List<Pelicula> peliculas,
            List<Sala> salas,
            List<Funcion> funciones,
            List<Reserva> reservas
    ) {
        this.peliculas = (peliculas != null) ? peliculas : new ArrayList<>();
        this.salas = (salas != null) ? salas : new ArrayList<>();
        this.funciones = (funciones != null) ? funciones : new ArrayList<>();
        this.reservas = (reservas != null) ? reservas : new ArrayList<>();
    }

    public List<Pelicula> getPeliculas() {
        if (peliculas == null) {
            peliculas = new ArrayList<>();
        }
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = (peliculas != null) ? peliculas : new ArrayList<>();
    }

    public List<Sala> getSalas() {
        if (salas == null) {
            salas = new ArrayList<>();
        }
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = (salas != null) ? salas : new ArrayList<>();
    }

    public List<Funcion> getFunciones() {
        if (funciones == null) {
            funciones = new ArrayList<>();
        }
        return funciones;
    }

    public void setFunciones(List<Funcion> funciones) {
        this.funciones = (funciones != null) ? funciones : new ArrayList<>();
    }

    public List<Reserva> getReservas() {
        if (reservas == null) {
            reservas = new ArrayList<>();
        }
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = (reservas != null) ? reservas : new ArrayList<>();
    }
}