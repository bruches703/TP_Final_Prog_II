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
    private Butaca butaca;
    private TipoEntrada tipoEntrada;

    public Entrada(double precio) {
        this.precio = precio;
        this.tipoEntrada = TipoEntrada.GENERAL;
    }

    public Entrada(Butaca butaca, TipoEntrada tipoEntrada, double precioBase) {
        this.butaca = butaca;
        this.tipoEntrada = tipoEntrada != null ? tipoEntrada : TipoEntrada.GENERAL;
        this.precio = this.tipoEntrada.calcularPrecioUnitario(precioBase);
    }

    // Getters y Setters ---------- Inicio
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Butaca getButaca() {
        return butaca;
    }

    public void setButaca(Butaca butaca) {
        this.butaca = butaca;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    // Getters y Setters ---------- Fin
    @Override
    public String toString() {
        String butacaStr = butaca != null
                ? ("F" + butaca.getFila() + "-A" + butaca.getAsiento())
                : "Sin butaca";
        String tipoStr = tipoEntrada != null ? tipoEntrada.getEtiqueta() : "General";
        return "Entrada{" + butacaStr + ", tipo=" + tipoStr + ", precio=" + precio + "}";
    }
}
