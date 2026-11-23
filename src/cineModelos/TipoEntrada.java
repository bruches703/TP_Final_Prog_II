/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package cineModelos;

/**
 *
 * @author Bruches
 */
public enum TipoEntrada {
    GENERAL("General", 0.0),
    JUBILADO("Jubilado", 0.15),
    SOCIO("Socio", 0.20),
    ESTUDIANTE("Estudiante", 0.10);

    private final String etiqueta;
    private final double porcentajeDescuento;

    TipoEntrada(String etiqueta, double porcentajeDescuento) {
        this.etiqueta = etiqueta;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public double calcularPrecioUnitario(double precioBase) {
        return precioBase * (1.0 - porcentajeDescuento);
    }

    @Override
    public String toString() {
        int pct = (int) Math.round(porcentajeDescuento * 100);
        return pct > 0 ? etiqueta + " (" + pct + "% dto.)" : etiqueta;
    }
}
