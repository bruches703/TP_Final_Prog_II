/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

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
    
    
    
}
