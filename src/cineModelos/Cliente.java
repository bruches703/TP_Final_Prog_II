/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

/**
 *
 * @author Bruches
 */
public class Cliente {
    private int dni;
    private String nombre;
    private boolean esAdmin;

    // Getters and Setters ---------- Inicio
    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }
    
    
    // Getters and Setters ---------- Fin

    public Cliente(int dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
        this.esAdmin = false;
    }

    @Override
    public String toString() {
        return "Cliente{" + "dni=" + dni + ", nombre=" + nombre + ", esAdmin=" + esAdmin + '}';
    }

    
    
}
