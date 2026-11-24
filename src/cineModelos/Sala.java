/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class Sala implements Serializable {

    private static final long serialVersionUID = 1002L;
    private int idSala;
    private List<Butaca> listaButacas;
    private int butacasTotales;
    private boolean estaDisponible;

    public Sala(int idSala, List<Butaca> listaButacas, boolean estaDisponible) {
        this.idSala = idSala;
        this.listaButacas = listaButacas;
        this.estaDisponible = estaDisponible;
        this.butacasTotales = (listaButacas != null) ? listaButacas.size() : 0;
    }

    // Getters y Setters ------- Inicio
    public int getButacasTotales() {
        return butacasTotales;
    }

    public void setButacasTotales(int butacasTotales) {
        this.butacasTotales = butacasTotales;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public List<Butaca> getListaButacas() {
        return listaButacas;
    }

    public void setListaButacas(List<Butaca> listaButacas) {
        this.listaButacas = listaButacas;
        this.butacasTotales = (listaButacas != null) ? listaButacas.size() : 0;
    }

    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }
    // Getters y Setters ------- Fin

    public static Sala nuevaSala(Scanner leer) {
        System.out.println("Ingrese numero de sala: ");
        int idNuevaSala = Integer.parseInt(leer.nextLine().trim());

        System.out.println("Ingrese cantidad de filas: ");
        int filas = Integer.parseInt(leer.nextLine().trim());

        System.out.println("Ingrese cantidad de butacas por fila: ");
        int numeroButacas = Integer.parseInt(leer.nextLine().trim());

        return new Sala(idNuevaSala, Butaca.nuevaListaButacas(filas, numeroButacas), true);
    }

    /**
     * Muestra la lista de butacas de la sala como una matriz. Muestra por filas y el
     * numero de butaca. Si la butaca no esta disponible se muestra con una X.
     *
     * @param sala Sala cuyas butacas se van a mostrar
     */
    public static void mostrarButacasDeSala(Sala sala) {
        if (sala == null) {
            System.out.println("Sala no disponible");
            return;
        }

        if (sala.isEstaDisponible()) {
            System.out.println("Estado de sala: Disponible\n");
        } else {
            System.out.println("Estado de sala: No disponible\n");
        }

        if (sala.listaButacas == null || sala.listaButacas.isEmpty()) {
            System.out.println("No hay butacas cargadas en esta sala.");
            return;
        }

        int filaActual = -1;
        for (Butaca butaca : sala.listaButacas) {
            if (butaca == null) {
                continue;
            }
            if (butaca.getFila() != filaActual) {
                if (filaActual != -1) {
                    System.out.println();
                }
                filaActual = butaca.getFila();
                System.out.print("Fila " + filaActual + ": ");
            }
            if (butaca.isEstaDisponible()) {
                System.out.print("[" + butaca.getAsiento() + "] ");
            } else {
                System.out.print("[X] ");
            }
        }
        System.out.println();
    }
/**
 * Cuenta la cantidad de filas totales de la sala
 * @param sala Sala a la cual se le contara las filas
 * @return  devuelve la cantidad de filas en numero entero
 */
    public int filasTotales(Sala sala) {
        if (sala == null || sala.getListaButacas() == null) {
            return -1;
        }
        int filasTotales = 0;

        for (Butaca butaca : sala.getListaButacas()) {
            if (butaca != null && butaca.getFila() > filasTotales) {
                filasTotales = butaca.getFila();
            }
        }

        return filasTotales;
    }
/**
 * Cuenta la cantidad de asientos totales por fila de la
 * sala
 * @param sala Sala a la que se le contaran los asientos por fila
 * @return Devuelve la cantidad de asientos por fila de la sala
 */
    public int asientosTotales(Sala sala) {
        if (sala == null || sala.getListaButacas() == null) {
            return -1;
        }
        int asientosTotales = 0;

        for (Butaca butaca : sala.getListaButacas()) {
            if (butaca != null && butaca.getAsiento() > asientosTotales) {
                asientosTotales = butaca.getAsiento();
            }
        }

        return asientosTotales;
    }

    /**
     * Busca la sala por Id 
     * @param listaSalas Tipo lista de Sala. Lista de salas a buscar la ID
     * @param idBuscada Tipo entero. Id buscado en la lista
     * @return
     *      Si lo encuentra, devuelve la sala.
     *      Si no lo encuentra, devuelve null.
     */     
    public static Sala buscarSalaPorId(List<Sala> listaSalas, int idBuscada) {
        if (listaSalas == null) {
            return null;
        }
        for (Sala sala : listaSalas) {
            if (sala != null && sala.getIdSala() == idBuscada) {
                return sala;
            }
        }
        return null;
    }

    /**
     * 
     * @param listaDeButaca
     * @param butacasReservadas 
     */
    public void reservarButacaDeSala(List<Butaca> listaDeButaca, List<Butaca> butacasReservadas) {
        if (listaDeButaca == null || butacasReservadas == null) {
            System.out.println("No hay butacas que reservar");
            return;
        }
        for (Butaca reservada : butacasReservadas) {
            if (reservada == null) {
                continue;
            }
            for (Butaca butacaSala : listaDeButaca) {
                if (butacaSala != null
                        && butacaSala.getFila() == reservada.getFila()
                        && butacaSala.getAsiento() == reservada.getAsiento()) {
                    butacaSala.setEstaDisponible(false);
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Sala{" + "idSala=" + idSala + ", listaButacas=" + listaButacas + ", estaDisponible=" + estaDisponible + '}';
    }
}
