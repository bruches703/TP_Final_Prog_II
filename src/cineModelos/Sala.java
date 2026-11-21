/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class Sala {
    private int idSala;
    private List<Butaca> listaButacas;
    private boolean estaDisponible;
    
    // Getters y Setters ------- Inicio

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
    }

        
    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }

    
    // Getters y Setters ------- Fin

    public Sala(int idSala, List<Butaca> listaButacas, boolean estaDisponible) {
        this.idSala = idSala;
        this.listaButacas = listaButacas;
        this.estaDisponible = estaDisponible;
    }

    public Sala nuevaSala(Scanner leer){
        System.out.println("Ingrese numero de sala: ");
        int idNuevaSala = leer.nextInt();
        
        System.out.println("Ingrese cantidad de filas: ");
        int filas = leer.nextInt();
        
        System.out.println("Ingrese cantidad de butacas por fila: ");
        int numeroButacas = leer.nextInt();

        return new Sala(idNuevaSala,Butaca.nuevaListaButacas(filas, numeroButacas),true);
    }
    /**
     * Muestra la lista de butacas de la sala como una matriz. Muestra por filas y el
     * numero de butaca. Si la butaca no esta disponible se muestra con una X.
     * @param listaButacas Lista de butacas a mostrar
     */
    public static void mostrarButacasDeSala(Sala sala) {
        //Si la sala es null
        if (sala == null) {
            System.out.println("Sala no disponible");
            return;
        }

        //Muestra si la sala esta disponible
        if (sala.isEstaDisponible()) {
            System.out.println("Estado de sala: Disponible\n");
        } else {
            System.out.println("Estado de sala: No disponible\n");
        }
        //Muestra las butacas en forma de matriz
        int filaActual = -1;
        for (Butaca butaca : sala.listaButacas) {
            if (butaca.getFila() != filaActual) {
                if (filaActual != -1) {
                    System.out.println();
                }
                filaActual = butaca.getFila();
                System.out.print("Fila " + filaActual + ": ");
            }
            //Si la butaca esta disponible muestra el numero, sino X.
            if (butaca.isEstaDisponible()) {
                System.out.print("[" + butaca.getAsiento() + "] ");
            } else {
                System.out.print("[X] ");
            }
        }
        System.out.println();
    }
        
    /**
     * To string
     *
     * @return Devuelve un String con la informacion de la sala
     */
    @Override
    public String toString() {
        return "Sala{" + "idSala=" + idSala + ", listaButacas=" + listaButacas + ", estaDisponible=" + estaDisponible + '}';
    }
    
}
