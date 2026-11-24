/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineAplication;

import cineController.ControladorPrincipal;
import cineModelos.SucursalCine;
import cinePersistencia.AlmacenamientoEstadoCine;
import cinePersistencia.PersistenciaException;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author Bruches
 */
public class app extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("/cineVistas/principal.fxml");
        if (fxmlUrl == null) {
            throw new IOException("No se encontró el recurso FXML: /cineVistas/principal.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        ControladorPrincipal controladorPrincipal = loader.getController();
        AlmacenamientoEstadoCine almacenEstado = new AlmacenamientoEstadoCine();

        SucursalCine sucursal;
        String mensajeEstado;
        try {
            sucursal = almacenEstado.cargarSucursal();
            mensajeEstado = "Estado serializado cargado desde " + almacenEstado.getRutaArchivoEstado();
        } catch (PersistenciaException ex) {
            sucursal = almacenEstado.crearSucursalInicial();
            mensajeEstado = "Error de persistencia al cargar estado";
            mostrarAlertaError(primaryStage, "Error de persistencia", ex.getMessage());
        }

        controladorPrincipal.setAlmacenEstado(almacenEstado);
        controladorPrincipal.setDatosIniciales(
                sucursal,
                sucursal.getPeliculas(),
                sucursal.getSalas(),
                sucursal.getFunciones(),
                sucursal.getReservas(),
                mensajeEstado
        );

        Scene scene = new Scene(root, 1180, 760);

        URL cssUrl = getClass().getResource("/cineVistas/estilos.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("Sistema de Reservas de Cine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarAlertaError(Stage owner, String titulo, String detalle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(detalle);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}