/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1001L;

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

    /**
     * Crea una pelicula nueva, pidiendo que ingrese los datos.
     *
     * @param leer Tipo scanner para leer los datos ingresados
     * @return Devuelve una pelicula nueva
     */
    public static Pelicula nuevaPelicula(Scanner leer) {
        System.out.println("Ingrese titulo de pelicula");
        String nombre = leer.nextLine().trim();

        System.out.println("Ingrese duracion: ");
        long minutos = 0;
        try {
            minutos = Long.parseLong(leer.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida al ingresar la duracion.");
        }

        Duration duracion = Duration.ofMinutes(minutos);
        boolean estaActiva = true;

        return new Pelicula(nombre, duracion, estaActiva);
    }

    /**
     *
     * @param listaPelicula
     * @param tituloBuscado
     * @return
     */
    public static Pelicula buscarPeliculaPorTitulo(List<Pelicula> listaPelicula, String tituloBuscado) {
        if (listaPelicula == null || tituloBuscado == null) {
            return null;
        }
        String buscado = tituloBuscado.trim();
        for (Pelicula pelicula : listaPelicula) {
            if (pelicula != null && pelicula.getTitulo() != null
                    && pelicula.getTitulo().trim().equalsIgnoreCase(buscado)) {
                return pelicula;
            }
        }
        return null;
    }

    /**
     * 
     * @param listaDePeliculas 
     */
    public static void mostrarListaDePelicula(List<Pelicula> listaDePeliculas) {
        if (listaDePeliculas == null || listaDePeliculas.isEmpty()) {
            System.out.println("No hay peliculas registradas.");
            return;
        }
        System.out.println("Lista de peliculas: ");
        for (Pelicula pelicula : listaDePeliculas) {
            if (pelicula == null) {
                continue;
            }
            System.out.println("Titulo: " + pelicula.getTitulo());
            System.out.println("Duracion: " + pelicula.getDuracionFormateada());
            System.out.println("Estado: " + (pelicula.getEstaActiva() ? "Activa" : "Inactiva"));
            System.out.println("-----------");
        }
    }

    /**
     * 
     * @param leer
     * @param listaPeliculas
     * @return 
     */
    public static Pelicula seleccionarPelicula(Scanner leer, List<Pelicula> listaPeliculas) {
        if (listaPeliculas == null || listaPeliculas.isEmpty()) {
            System.out.println("No hay peliculas disponibles");
            return null;
        }

        Pelicula.mostrarListaDePelicula(listaPeliculas);
        Pelicula peliculaEscogida = null;

        while (peliculaEscogida == null) {
            System.out.println("Ingresa el titulo de la pelicula");
            String titulo = leer.nextLine().trim();
            if (titulo.isEmpty()) {
                System.out.println("El nombre de la pelicula no puede estar en blanco.");
                continue;
            }
            peliculaEscogida = Pelicula.buscarPeliculaPorTitulo(listaPeliculas, titulo);
            if (peliculaEscogida == null) {
                System.out.println("El titulo ingresado no esta en la lista.");
            }
        }
        return peliculaEscogida;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Pelicula)) {
            return false;
        }
        Pelicula pelicula = (Pelicula) objeto;
        return titulo != null && pelicula.titulo != null
                && titulo.trim().equalsIgnoreCase(pelicula.titulo.trim());
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo == null ? null : titulo.trim().toLowerCase());
    }
}