/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineModelos;

/**
 *
 * @author Bruches
 */
public class Entrada {

    private double precio;
    private Butaca butaca;
    private TipoEntrada tipoEntrada;

    /**
     * Crea una entrada con tarifa general y un importe fijo.
     *
     * @param precio importe asignado a la entrada.
     */
    public Entrada(double precio) {
        this.precio = precio;
        this.tipoEntrada = TipoEntrada.GENERAL;
    }

    /**
     * Crea una entrada asociada a una butaca y un tipo de entrada, calculando
     * su precio unitario a partir del precio base de la función.
     *
     * @param butaca butaca asignada a la entrada.
     * @param tipoEntrada tipo o tarifa de entrada aplicada.
     * @param precioBase precio base de la función.
     */
    public Entrada(Butaca butaca, TipoEntrada tipoEntrada, double precioBase) {
        this.butaca = butaca;
        this.tipoEntrada = tipoEntrada != null ? tipoEntrada : TipoEntrada.GENERAL;
        this.precio = this.tipoEntrada.calcularPrecioUnitario(precioBase);
    }

    // Getters y Setters ---------- Inicio

    /**
     * Obtiene el precio final calculado de la entrada.
     *
     * @return importe de la entrada.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio final de la entrada.
     *
     * @param precio nuevo importe de la entrada.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la butaca asociada a la entrada.
     *
     * @return butaca asignada, o {@code null} si no tiene una asociada.
     */
    public Butaca getButaca() {
        return butaca;
    }

    /**
     * Asigna la butaca correspondiente a la entrada.
     *
     * @param butaca butaca a asociar.
     */
    public void setButaca(Butaca butaca) {
        this.butaca = butaca;
    }

    /**
     * Obtiene el tipo de entrada o tarifa aplicada.
     *
     * @return tipo de entrada.
     */
    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    /**
     * Establece el tipo de entrada o tarifa aplicada.
     *
     * @param tipoEntrada nuevo tipo de entrada.
     */
    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }
    // Getters y Setters ---------- Fin

    /**
     * Devuelve una representación en texto de la entrada con su ubicación de
     * butaca, tipo y precio.
     *
     * @return cadena descriptiva de la entrada.
     */
    @Override
    public String toString() {
        String butacaStr = butaca != null
                ? ("F" + butaca.getFila() + "-A" + butaca.getAsiento())
                : "Sin butaca";
        String tipoStr = tipoEntrada != null ? tipoEntrada.getEtiqueta() : "General";
        return "Entrada{" + butacaStr + ", tipo=" + tipoStr + ", precio=" + precio + "}";
    }
}