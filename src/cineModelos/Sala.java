/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

import java.util.List;

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

    @Override
    public String toString() {
        return "Sala{" + "idSala=" + idSala + ", listaButacas=" + listaButacas + ", estaDisponible=" + estaDisponible + '}';
    }
    
}
