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

    public String getHorarioFuncion() {
        return (horario != null) ? horario.toString() : "";
    }

    public String getFechaFuncion() {
        return (fecha != null) ? fecha.toString() : "";
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

    /**
     * Registra la fecha ingresada y lo valida en formato dd - mm - aaaa
     *
     * @param leer Tipo Scanner para almacenar lo ingresado
     * @return Devuelve la fecha de la funcion ingresada de tipo LocalDate
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
     * Registra el horario ingresado y lo valida en formato hh : mm
     *
     * @param leer Tipo Scanner para almacenar lo ingresado
     * @return devuelve la hora ingresada validada en formato LocalTime
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
     * Muestra la lista de salas y pide que ingreses un id, la busca en la lista
     * que llega por parámetro y devuelve la sala buscada.
     *
     * @param leer Tipo scanner, herramienta de lectura de ingreso.
     * @param listaSalas arraylist de salas a buscar
     * @return devuelve una sala buscada por id.
     */
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

    /**
     * Pide el ingreso de una sala, una pelicula, fecha y hora de funcion y crea
     * una nueva funcion.
     *
     * @param leer Tipo scanner, herramienta de lectura de ingreso
     * @param listaSalas Tipo arrayList de Sala donde se seleccionara una sala
     * valida
     * @param listPelicula Tipo arrayList de Pelicula donde se seleccionara una
     * pelicula activa
     * @param listaDeFunciones Tipo arrayList de Funcion donde se validara si ID
     * existe
     *
     * @return Devuelve un objeto de tipo Funcion con los parametros cargados y
     * validados.
     */
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

    /**
     * Muestra todos los parametros de una funcion de manera organizada.
     *
     * @param funcion objeto tipo Funcion a mostrar.
     */
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

    /**
     * Busca una funcion por id de la funcion en una lista de funciones. De
     * encontrarlo devuelve la id. De no encontrarlo, o si la lista es null o
     * vacia, devuelve -1
     *
     * @param listaDeFunciones lista de funcion donde se buscara por ID.
     * @param idFuncion Tipo int, id de funcion a buscar
     * @return devuelve un entero De encontrar la id, devuelve la misma. De no
     * encontrarlo devuelve -1
     */
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

    /**
     * Busca una funcion por ID. De encontrarlo devuelve la funcion, sino
     * devuelve null
     *
     * @param listaDeFunciones Lista de tipo Funcion. Lista de funciones donde
     * se buscara por ID
     * @param idDeFuncion tipo int, numero entero que se buscara en la lista de
     * funciones
     * @return Devuelve un objeto de tipo Funcion si se encuentra la ID, null si
     * no lo encuentra o si hay un error
     */
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

}
