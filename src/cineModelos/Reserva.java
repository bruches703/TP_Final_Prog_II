/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import utilidadesHerramientas.Validaciones;

/**
 *
 * @author Bruches
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean estadoDeReserva;
    private double importe;
    private String codigo;
    private Sala sala;
    private List<Butaca> butacasReservadas;

    public Reserva(boolean estadoDeReserva, double importe, String codigo) {
        this.estadoDeReserva = estadoDeReserva;
        this.importe = importe;
        this.codigo = codigo;
        this.butacasReservadas = new ArrayList<>();
    }

    // Getters y Setters ------- Inicio
    public boolean isEstadoDeReserva() {
        return estadoDeReserva;
    }

    public void setEstadoDeReserva(boolean estadoDeReserva) {
        this.estadoDeReserva = estadoDeReserva;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<Butaca> getButacasReservadas() {
        return butacasReservadas;
    }

    public void setButacasReservadas(List<Butaca> butacasReservadas) {
        this.butacasReservadas = (butacasReservadas != null) ? butacasReservadas : new ArrayList<>();
    }
    // Getters y Setters ------- Fin

    @Override
    public String toString() {
        return "Reserva{" + "estadoDeReserva=" + estadoDeReserva + ", importe=" + importe + ", codigo=" + codigo + '}';
    }

    /**
     * Realiza nueva reserva pidiendo al usuario los ingresos de los parametros.
     * Los mismos se validan y se guardan. Si no hay funciones, no se puede realizar
     * ninguna reserva.
     * @param listaDeFunciones Lista de funciones que se presentan para realizar
     * la nueva reserva.
     * @param leer herramienta para leer lo ingresado
     * @return Devuelve un objeto tipo Reserva con parametros ingresados por
     * el usuario.
     */
    public static Reserva ingresarNuevaResarva(List<Funcion> listaDeFunciones, Scanner leer) {
        if (listaDeFunciones == null || listaDeFunciones.isEmpty()) {
            System.out.println("No hay funciones registradas para hacer una reserva.");
            return null;
        }

        Funcion funcionEscogida = null;
        while (funcionEscogida == null) {
            Funcion.mostrarFunciones(listaDeFunciones); 
            
            // Ingreso de Id
            System.out.println("Ingrese el ID de la funcion:");
            String entrada = leer.nextLine().trim();

            int idDeFuncion;
            try {
                idDeFuncion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero entero.");
                continue;
            }

            funcionEscogida = Funcion.traerFuncionPorId(listaDeFunciones, idDeFuncion);
            if (funcionEscogida == null) {
                System.out.println("No se encontro una funcion con ID " + idDeFuncion + ". Intente nuevamente.");
            }
        }

        Sala salaDeFuncion = funcionEscogida.getSala();
        double precio = funcionEscogida.getPrecio();
        double precioTotal = 0;
        List<Butaca> butacasSeleccionadas = new ArrayList<>();

        Funcion.motrarUnaFuncion(funcionEscogida);
        Sala.mostrarButacasDeSala(salaDeFuncion);

        boolean seguirAgregando = true;
        while (seguirAgregando) {
            int fila = Validaciones.validarFilas(leer, salaDeFuncion);
            int numeroButaca = Validaciones.validarCantidadAsientos(leer, salaDeFuncion);

            if (!puedeReservarButaca(salaDeFuncion, butacasSeleccionadas, fila, numeroButaca)) {
                continue;
            }

            butacasSeleccionadas.add(new Butaca(fila, numeroButaca));
            precioTotal += precio;

            String respuesta;
            do {
                System.out.println("Desea agregar otra butaca? (S/N):");
                respuesta = leer.nextLine().trim().toLowerCase();
            } while (!respuesta.equals("s") && !respuesta.equals("si")
                    && !respuesta.equals("n") && !respuesta.equals("no"));

            if (respuesta.equals("n") || respuesta.equals("no")) {
                seguirAgregando = false;
            }
        }

        if (butacasSeleccionadas.isEmpty()) {
            System.out.println("Debe seleccionar al menos una butaca para realizar la reserva.");
            return null;
        }

        String confirmar;
        do {
            System.out.println("Confirma reserva? Total: $" + precioTotal + " (S/N):");
            confirmar = leer.nextLine().trim().toLowerCase();
        } while (!confirmar.equals("s") && !confirmar.equals("si")
                && !confirmar.equals("n") && !confirmar.equals("no"));

        if (confirmar.equals("n") || confirmar.equals("no")) {
            System.out.println("Reserva cancelada.");
            return null;
        }

        if (salaDeFuncion != null) {
            salaDeFuncion.reservarButacaDeSala(salaDeFuncion.getListaButacas(), butacasSeleccionadas);
        }

        System.out.println("Reserva confirmada.");
        Reserva reservaConfirmada = new Reserva(true, precioTotal, genererarCodigoReserva());
        reservaConfirmada.setSala(salaDeFuncion);
        reservaConfirmada.setButacasReservadas(butacasSeleccionadas);
        return reservaConfirmada;
    }

    /**
     * 
     * @return 
     */
    public static String genererarCodigoReserva() {
        int numero = ThreadLocalRandom.current().nextInt(100_000_000);
        return String.format("%08d", numero);
    }

    /**
     *
     * @param sala
     * @param listaButacasReservadas
     * @param fila
     * @param numButaca
     * @return
     */
    public static boolean puedeReservarButaca(Sala sala, List<Butaca> listaButacasReservadas, int fila, int numButaca) {
        if (sala == null || sala.getListaButacas() == null) {
            System.out.println("La sala no tiene butacas disponibles.");
            return false;
        }

        Butaca butacaEnSala = null;
        for (Butaca butaca : sala.getListaButacas()) {
            if (butaca != null && butaca.getFila() == fila && butaca.getAsiento() == numButaca) {
                butacaEnSala = butaca;
                break;
            }
        }

        if (butacaEnSala == null || !butacaEnSala.isEstaDisponible()) {
            System.out.println("La butaca no esta disponible. Elige otra.");
            return false;
        }

        if (listaButacasReservadas != null) {
            for (Butaca butaca : listaButacasReservadas) {
                if (butaca != null && butaca.getFila() == fila && butaca.getAsiento() == numButaca) {
                    System.out.println("Ya seleccionaste esa butaca en esta reserva. Elige otra.");
                    return false;
                }
            }
        }

        return true;
    }
}