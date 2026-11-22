/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import utilidadesHerramientas.Validaciones;

/**
 *
 * @author Bruches
 */
public class Funcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idFuncion;
    private LocalDate fecha;
    private LocalTime horario;
    private Sala sala;
    private Pelicula pelicula;
    private double precio;

    public Funcion(int idFuncion, LocalDate fecha, LocalTime horario, Sala sala, Pelicula pelicula, double precio) {
        this.idFuncion = idFuncion;
        this.fecha = fecha;
        this.horario = horario;
        this.sala = sala;
        this.pelicula = pelicula;
        this.precio = precio;
    }

    // Getters y Setters ------- INICIO
    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

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
    // Getters y Setters ------------- Fin

    public LocalDateTime getFinalizacionPelicula() {
        LocalDateTime inicio = LocalDateTime.of(this.fecha, this.horario);
        return inicio.plus(pelicula.getDuracion());
    }

    @Override
    public String toString() {
        return "Funcion{" + "idFuncion=" + idFuncion + ", fecha=" + fecha + ", horario=" + horario
                + ", sala=" + sala + ", pelicula=" + pelicula + ", precio=" + precio + '}';
    }

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

    public static Sala seleccionarSala(Scanner leer, List<Sala> listaSalas) {
        if (listaSalas == null || listaSalas.isEmpty()) {
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

    public static Funcion nuevaFuncion(Scanner leer, List<Sala> listaSalas, List<Pelicula> listPelicula, List<Funcion> listaDeFunciones) {
        int idFuncion = Validaciones.validarIdFuncion(leer, listaDeFunciones);
        LocalDate fechaFuncion = ingresarFecha(leer);
        LocalTime horarioFuncion = ingresarHorario(leer);
        Sala salaParaFuncion = seleccionarSala(leer, listaSalas);
        Pelicula peliculaParaFuncion = Pelicula.seleccionarPelicula(leer, listPelicula);

        return new Funcion(idFuncion, fechaFuncion, horarioFuncion, salaParaFuncion, peliculaParaFuncion, 5000);
    }

    public static void mostrarFunciones(List<Funcion> listaDeFunciones) {
        if (listaDeFunciones == null || listaDeFunciones.isEmpty()) {
            System.out.println("No hay funciones previstas");
            return;
        }

        for (Funcion funcion : listaDeFunciones) {
            if (funcion != null) {
                motrarUnaFuncion(funcion);
            }
        }
    }

    public static void motrarUnaFuncion(Funcion funcion) {
        if (funcion != null && funcion.getPelicula() != null && funcion.getPelicula().getEstaActiva()) {
            System.out.println("ID Funcion: " + funcion.getIdFuncion());
            System.out.println("Pelicula: " + funcion.getPelicula().getTitulo());
            System.out.println("Fecha: " + funcion.getFechaFuncion());
            System.out.println("Horario: " + funcion.getHorarioFuncion());
            int idSala = (funcion.getSala() != null) ? funcion.getSala().getIdSala() : -1;
            System.out.println("Id de sala: " + idSala);
        }
    }

    public static int buscarFuncionPorId(List<Funcion> listaDeFunciones, int idFuncion) {
        if (listaDeFunciones == null || listaDeFunciones.isEmpty()) {
            return -1;
        }
        for (Funcion funcion : listaDeFunciones) {
            if (funcion != null && funcion.getIdFuncion() == idFuncion) {
                return idFuncion;
            }
        }
        return -1;
    }

    public static Funcion traerFuncionPorId(List<Funcion> listaDeFunciones, int idDeFuncion) {
        if (listaDeFunciones == null || listaDeFunciones.isEmpty()) {
            return null;
        }
        for (Funcion funcion : listaDeFunciones) {
            if (funcion != null && funcion.getIdFuncion() == idDeFuncion) {
                return funcion;
            }
        }
        return null;
    }

    public String getHorarioFuncion() {
        return (horario != null) ? horario.toString() : "";
    }

    public String getFechaFuncion() {
        return (fecha != null) ? fecha.toString() : "";
    }
}