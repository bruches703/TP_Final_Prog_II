/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

/**
 *
 * @author Bruches
 */
public class Reserva {
    private boolean estadoDeReserva;
    private double importe;
    private String codigo;
    
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
    
    // Getters y Setters ------- Fin

    @Override
    public String toString() {
        return "Reserva{" + "estadoDeReserva=" + estadoDeReserva + ", importe=" + importe + ", codigo=" + codigo + '}';
    }
    
    
}
