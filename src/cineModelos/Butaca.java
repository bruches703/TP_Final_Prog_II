/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.util.ArrayList;
import java.util.List;
import utilidadesHerramientas.*;

/**
 *
 * @author Bruches
 */
public class Butaca {
    private int fila;
    private int asiento;
    private boolean estaDisponible;

    //Getters and Setters-------------- inicio
    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }

    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }
    
    //Getters and Setters-------------- fin

    public Butaca(int fila, int asiento) {
        this.fila = fila;
        this.asiento = asiento;
        this.estaDisponible = true;
    }

    /**
     * Crea una lista de butacas con la cantidad de asientos, almacenando 
     * la cantidad de filas y de asientos por fila.
     * 
     * @param filas primer valor entero que representa la cantidad de filas
     * @param numeroButaca segundo valor entero que representa la cantidad de 
     * butacas por fila
     * 
     * @return Devuelve una lista de tipo Butacas. 
     */
    public static List<Butaca> nuevaListaButacas(int filas, int numeroButaca){
        if(!Validaciones.esMayorA(filas,0) ){
            System.out.println("El numero de filas no puede ser 0 o negativo");
            return new ArrayList<>();
        } else if (!Validaciones.esMayorA(numeroButaca, 0)){
            System.out.println("El numero de butacas no puede ser 0 o negativo");
            return new ArrayList<>();
        }
        
        List<Butaca> listaButacas = new ArrayList<>();
        for (int i = 1; i <= filas; i++) {
            for (int j = 1; j <= numeroButaca; j++) {
                listaButacas.add(new Butaca(i,j));
            }
        }
         return listaButacas;
    }
    
    


    @Override
    public String toString() {
        return "Butaca{" + "fila=" + fila + ", asiento=" + asiento + ", estaDisponible=" + estaDisponible + '}';
    }
    
    
    
}
