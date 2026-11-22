/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineAplication;
import cineModelos.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import persistencia.AlmacenamientoSalas;
import persistencia.AlmacenarPeliculas;
import java.util.Scanner;
/**
 *
 * @author Bruches
 */
public class app {
    public static void main(String[] args) {
        
        Scanner leer = new Scanner(System.in);
        // --- 1. CREACIÓN DE 4 PELÍCULAS ---
        
        List<Pelicula> listaPeliculas = new ArrayList<>();
        listaPeliculas.add(new Pelicula("Lord Of The Rings", Duration.ofMinutes(120), true));
        listaPeliculas.add(new Pelicula("Interstellar", Duration.ofMinutes(169), true));
        listaPeliculas.add(new Pelicula("Inception", Duration.ofMinutes(148), true));
        listaPeliculas.add(new Pelicula("The Matrix", Duration.ofMinutes(136), true));
        

        // --- 2. CREACIÓN DE 4 LISTAS DE BUTACAS ---
        List<Butaca> listaButacas1 = Butaca.nuevaListaButacas(3, 4); // Sala pequeña: 3 filas, 4 butacas
        List<Butaca> listaButacas2 = Butaca.nuevaListaButacas(4, 5); // Sala mediana: 4 filas, 5 butacas
        List<Butaca> listaButacas3 = Butaca.nuevaListaButacas(5, 6); // Sala grande: 5 filas, 6 butacas
        List<Butaca> listaButacas4 = Butaca.nuevaListaButacas(4, 4); // Sala estándar: 4 filas, 4 butacas

        // --- 3. CREACIÓN DE 4 SALAS ---
        List<Sala> listaSalas = new ArrayList<>();
        listaSalas.add(new Sala(1, listaButacas1, true));
        listaSalas.add(new Sala(2, listaButacas2, true));
        listaSalas.add(new Sala(3, listaButacas3, true));
        listaSalas.add(new Sala(4, listaButacas4, true));
        
        // --- 4. Funciones vacias ---
        List<Funcion> listaFunciones = new ArrayList<>();
        // --- 5. Reservas vacias ---
        List<Reserva> listaReservas = new ArrayList<>();
        
        SucursalCine cineBera = new SucursalCine("Berazategui", listaPeliculas, listaSalas , listaFunciones, listaReservas);
            
        //cineBera.mostrarPeliculas();
        
        cineBera.mostrarFunciones();
        //Sala sala1 = 
        Funcion nuevaFuncion = new Funcion(LocalDate.of(2026,11,25), LocalTime.of(18, 30), listaSalas.get(0), listaPeliculas.get(0),  125);
        System.out.println(nuevaFuncion.toString());
  
        
        AlmacenarPeliculas almacenListaPeliculas = new AlmacenarPeliculas(Path.of("datasave"));
        AlmacenamientoSalas almacenarListaSalas = new AlmacenamientoSalas(Path.of("datasave"));
        
        
        almacenListaPeliculas.almacenarListaDePeliculas(listaPeliculas);
        almacenarListaSalas.almacenarListaDeSalas(listaSalas);
        
    }
}
