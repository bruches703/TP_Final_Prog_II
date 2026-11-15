/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

/**
 *
 * @author Bruches
 */
public abstract class Entrada {
    private double precio;
    
    // Getter y setter------ inicio

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    // Getter y setter --- Fin
    
        public Entrada(double precio) {
        this.precio = precio;
    }
}
