/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineAplication;

import cineModelos.Butaca;
import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Reserva;
import cineModelos.Sala;
import cineModelos.SucursalCine;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import persistencia.AlmacenamientoFunciones;
import persistencia.AlmacenamientoReserva;
import persistencia.AlmacenamientoSalas;
import persistencia.AlmacenarPeliculas;

/**
 *
 * @author Bruches
 */
public class app {

    public static void main(String[] args) {
        // --- 1. PELICULAS ---
        List<Pelicula> listaPeliculas = new ArrayList<>();
        Pelicula lotr = new Pelicula("Lord Of The Rings", Duration.ofMinutes(120), true);
        Pelicula interstellar = new Pelicula("Interstellar", Duration.ofMinutes(169), true);
        Pelicula inception = new Pelicula("Inception", Duration.ofMinutes(148), true);
        Pelicula matrix = new Pelicula("The Matrix", Duration.ofMinutes(136), true);

        listaPeliculas.add(lotr);
        listaPeliculas.add(interstellar);
        listaPeliculas.add(inception);
        listaPeliculas.add(matrix);

        // --- 2. TRES SALAS ---
        Sala sala1 = new Sala(1, Butaca.nuevaListaButacas(3, 4), true);
        Sala sala2 = new Sala(2, Butaca.nuevaListaButacas(4, 5), true);
        Sala sala3 = new Sala(3, Butaca.nuevaListaButacas(5, 6), true);

        List<Sala> listaSalas = new ArrayList<>();
        listaSalas.add(sala1);
        listaSalas.add(sala2);
        listaSalas.add(sala3);

        // --- 3. VARIAS FUNCIONES EN DISTINTAS SALAS Y HORARIOS ---
        Funcion funcion1 = new Funcion(1, LocalDate.of(2026, 11, 25), LocalTime.of(16, 0), sala1, lotr, 5000.0);
        Funcion funcion2 = new Funcion(2, LocalDate.of(2026, 11, 25), LocalTime.of(19, 30), sala2, interstellar, 5500.0);
        Funcion funcion3 = new Funcion(3, LocalDate.of(2026, 11, 25), LocalTime.of(22, 0), sala3, inception, 5500.0);
        Funcion funcion4 = new Funcion(4, LocalDate.of(2026, 11, 26), LocalTime.of(18, 15), sala1, matrix, 5000.0);
        Funcion funcion5 = new Funcion(5, LocalDate.of(2026, 11, 26), LocalTime.of(21, 15), sala2, lotr, 5200.0);

        List<Funcion> listaFunciones = new ArrayList<>();
        listaFunciones.add(funcion1);
        listaFunciones.add(funcion2);
        listaFunciones.add(funcion3);
        listaFunciones.add(funcion4);
        listaFunciones.add(funcion5);

        // --- 4. TRES RESERVAS HARCODEADAS ---
        List<Reserva> listaReservas = new ArrayList<>();

        // Reserva 1: 2 butacas en Sala 1 (Funcion 1)
        List<Butaca> butacasReserva1 = new ArrayList<>();
        butacasReserva1.add(new Butaca(1, 1));
        butacasReserva1.add(new Butaca(1, 2));
        sala1.reservarButacaDeSala(sala1.getListaButacas(), butacasReserva1);
        Reserva reserva1 = new Reserva(true, funcion1.getPrecio() * butacasReserva1.size(), "10000001");
        reserva1.setSala(sala1);
        reserva1.setButacasReservadas(butacasReserva1);
        listaReservas.add(reserva1);

        // Reserva 2: 3 butacas en Sala 2 (Funcion 2)
        List<Butaca> butacasReserva2 = new ArrayList<>();
        butacasReserva2.add(new Butaca(2, 1));
        butacasReserva2.add(new Butaca(2, 2));
        butacasReserva2.add(new Butaca(2, 3));
        sala2.reservarButacaDeSala(sala2.getListaButacas(), butacasReserva2);
        Reserva reserva2 = new Reserva(true, funcion2.getPrecio() * butacasReserva2.size(), "10000002");
        reserva2.setSala(sala2);
        reserva2.setButacasReservadas(butacasReserva2);
        listaReservas.add(reserva2);

        // Reserva 3: 1 butaca en Sala 3 (Funcion 3)
        List<Butaca> butacasReserva3 = new ArrayList<>();
        butacasReserva3.add(new Butaca(3, 4));
        sala3.reservarButacaDeSala(sala3.getListaButacas(), butacasReserva3);
        Reserva reserva3 = new Reserva(true, funcion3.getPrecio() * butacasReserva3.size(), "10000003");
        reserva3.setSala(sala3);
        reserva3.setButacasReservadas(butacasReserva3);
        listaReservas.add(reserva3);

        // --- 5. SUCURSAL Y PERSISTENCIA INICIAL ---
        SucursalCine cineBera = new SucursalCine("Berazategui", listaPeliculas, listaSalas, listaFunciones, listaReservas);

        AlmacenarPeliculas almacenPeliculas = new AlmacenarPeliculas();
        AlmacenamientoSalas almacenSalas = new AlmacenamientoSalas();
        AlmacenamientoFunciones almacenFunciones = new AlmacenamientoFunciones();
        AlmacenamientoReserva almacenReservas = new AlmacenamientoReserva();

        almacenPeliculas.almacenarListaDePeliculas(cineBera.getPeliculas());
        almacenSalas.almacenarListaDeSalas(cineBera.getSalas());

        for (Funcion f : cineBera.getFunciones()) {
            almacenFunciones.almacenarFuncion(f);
        }

        for (Reserva r : cineBera.getReservas()) {
            almacenReservas.almacenarReserva(r, r.getSala(), r.getButacasReservadas());
        }

        System.out.println("Archivos CSV generados correctamente en datasave/.");
    }
}
