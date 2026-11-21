/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import utilidadesHerramientas.Validaciones;
import persistencia.AlmacenarPeliculas;

/**
 *
 * @author Bruches
 */
public class Pelicula implements Serializable{
    
    private static final long serialVersionUID = 1001;
    
    private String titulo;
    private Duration duracion;
    private boolean estaActiva;

    //Constructor
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
     * @param leer Tipo scanner para leer los datos ingresados
     * @return Devuelve una pelicula nueva
     */
    public Pelicula nuevaPelicula(Scanner leer){
        //Guarda el titulo
        System.out.println("Ingrese titulo de pelicula");
        String nombre = leer.nextLine();
        
        //Guarda la duracion
        System.out.println("Ingrese duracion: ");
        long minutos = 0;
        try {
            minutos = Long.parseLong(leer.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida al ingresar la duracion.");
        }
        
        // Convierte la variable duracion de Long a Duration y activa la pelicula        
        Duration duracion = Duration.ofMinutes(minutos);
        boolean estaActiva = true;
        
        return new Pelicula(nombre, duracion, estaActiva);                
    }
    
    /**
     * Almacena una pelicula nueva en la lista. Si la pelicula ya existe en el archivo
     * no se alacena. Para que exista debe coincidir todos los atributos de las
     * peliculas, no solo el titulo.
     * @param peliculaNueva De tipo pelicula, Representa a la nueva pelicula a guardar
     * @param listaPeliculas Lista de peliculas donde se almacena la nueva pelicula
     */
    public static void almacenarPelicula(Pelicula peliculaNueva, List<Pelicula> listaPeliculas){
        // Si la pelicula es nula..
        if (peliculaNueva == null) {
            System.out.println("No se puede guardar pelicula nula.");
            return;            
        }
        
        //Si la pelicula existe...
        if (listaPeliculas.contains(peliculaNueva)) {
            System.out.println("Pelicula "+ peliculaNueva.getTitulo()+" ya existe");
            return;
        }
        
        //Datos para persistencia
        Path directorio = Path.of("datasave");
        String separador = ";";
        AlmacenarPeliculas guardador = new AlmacenarPeliculas(directorio);
        guardador.almacenarPelicula(peliculaNueva, separador);
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
        return titulo != null && titulo.equalsIgnoreCase(pelicula.titulo);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(titulo == null ? null : titulo.toLowerCase());
    }
    

}

