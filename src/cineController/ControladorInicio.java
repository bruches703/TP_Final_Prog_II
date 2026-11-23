/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

/**
 *
 * @author Bruches
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControladorInicio {

    @FXML
    private Label lblSubtituloSucursal;

    @FXML
    private Label lblTotalFunciones;

    @FXML
    private Label lblTotalReservas;

    @FXML
    private Label lblTotalSalas;

    @FXML
    private Label lblTotalPeliculas;

    @FXML
    private Label lblEstadoPersistencia;

    @FXML
    private Button btnIrCartelera;

    @FXML
    private Button btnIrReservas;

    @FXML
    private Button btnIrFunciones;

    private ControladorPrincipal controladorPrincipal;

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public void actualizarResumen(int funciones, int reservas, int salas, int peliculas, String estadoTexto) {
        if (lblTotalFunciones != null) {
            lblTotalFunciones.setText(String.valueOf(funciones));
        }
        if (lblTotalReservas != null) {
            lblTotalReservas.setText(String.valueOf(reservas));
        }
        if (lblTotalSalas != null) {
            lblTotalSalas.setText(String.valueOf(salas));
        }
        if (lblTotalPeliculas != null) {
            lblTotalPeliculas.setText(String.valueOf(peliculas));
        }
        if (lblEstadoPersistencia != null && estadoTexto != null && !estadoTexto.isBlank()) {
            lblEstadoPersistencia.setText(estadoTexto);
        }
    }

    @FXML
    private void irACartelera() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("cartelera");
        }
    }

    @FXML
    private void irAReservas() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("reservas");
        }
    }

    @FXML
    private void irAFunciones() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("funciones");
        }
    }
}