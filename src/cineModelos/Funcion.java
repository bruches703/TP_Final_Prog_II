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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

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

    @Override
    public String toString() {
        return "Funcion{" + "fecha=" + fecha + ", horario=" + horario + ", sala=" + sala + ", pelicula=" + pelicula + ", precio=" + precio + '}';
    }
    
    /**
     * 
     * @param leer
     * @return 
     */
    public static LocalDate ingresarFecha(Scanner leer) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate fechaFuncion = null;
        while (fechaFuncion == null) {
            System.out.println("Ingrese fecha de la funcion");
            String auxiliar = leer.nextLine().trim();
            try {
                fechaFuncion = LocalDate.parse(auxiliar, formatoFecha);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida. Use el formato dd/MM/yyyy.");
            }
        }
        return fechaFuncion;
    }
    
    /**
     * 
     * @param leer
     * @return 
     */
    public static LocalTime ingresarHorario(Scanner leer) {
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horarioFuncion = null;
        while (horarioFuncion == null) {
            System.out.println("Ingrese horario de la funcion (HH:mm): ");
            String textoHora = leer.nextLine().trim();
            try {
                horarioFuncion = LocalTime.parse(textoHora, formatoHora);
            } catch (DateTimeParseException e) {
                System.out.println("Horario invalido. Use el formato HH:mm.");
            }
        }
        return horarioFuncion;
    }
    
    /**
     * 
     * @param leer
     * @param listaSalas
     * @return 
     */
    public static Sala seleccionarSala(Scanner leer, List<Sala> listaSalas) {
        if (listaSalas.isEmpty()) {
            System.out.println("No hay salas registradas");
            return null;
        }

        System.out.println("Lista de salas: ");
        for (Sala sala : listaSalas) {
            System.out.println("Id de sala: " + sala.getIdSala());
            System.out.println("Cantidad de butacas: " + sala.getButacasTotales());
            System.out.println("-----------");
        }

        Sala salaParaFuncion = null;
        while (salaParaFuncion == null) {
            System.out.println("Ingresa la id de la sala: ");
            String auxiliar = leer.nextLine().trim();
            try {
                int idBuscado = Integer.parseInt(auxiliar);
                salaParaFuncion = Sala.buscarSalaPorId(listaSalas, idBuscado);

                if (salaParaFuncion == null) {
                    System.out.println("No se encontro una sala con ID " + idBuscado + ". Reingrese:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero entero para el ID:");
            }
        }
        return salaParaFuncion;
    }

    public static Pelicula seleccionarPelicula(Scanner leer, List<Pelicula> listaPeliculas) {
        if (listaPeliculas == null) {
            System.out.println("No hay peliculas disponibles");
            return null;
        }

        System.out.println("Lista de peliculas: ");
        for (Pelicula pelicula : listaPeliculas) {
            System.out.println(pelicula.getTitulo());
            System.out.println("Duracion: " + pelicula.getTitulo());

            if (pelicula.getEstaActiva()) {
                System.out.println("Estado: Activa");
            } else {
                System.out.println("Estado: Inactiva");
            }
        }
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
    public static Funcion nuevaFuncion(Scanner leer, List<Sala> listaSalas, List<Pelicula> listPelicula){       
        LocalDate fechaFuncion = ingresarFecha(leer);
        
        LocalTime horarioFuncion = ingresarHorario(leer);
        
        Sala salaParaFuncion = seleccionarSala(leer, listaSalas);
        
        Pelicula peliculaParaFuncion = seleccionarPelicula(leer, listPelicula);
        
        return new Funcion(fechaFuncion, horarioFuncion, salaParaFuncion, peliculaParaFuncion, 50000);
    }
}
