/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineAplication;

import cineController.ControladorPrincipal;
import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Reserva;
import cineModelos.Sala;
import cineModelos.SucursalCine;
import cinePersistencia.AlmacenamientoFunciones;
import cinePersistencia.AlmacenamientoReserva;
import cinePersistencia.AlmacenamientoSalas;
import cinePersistencia.AlmacenarPeliculas;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Bruches
 */
public class app extends Application {

    private static final Path CARPETA_DATASAVE = Paths.get("datasave");

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cineVistas/principal.fxml"));
        Parent root = loader.load();

        ControladorPrincipal controladorPrincipal = loader.getController();
        inicializarAplicacion(controladorPrincipal);

        Scene scene = new Scene(root);
        var css = getClass().getResource("/cineVistas/estilos.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("Cine UTN");
        stage.setScene(scene);
        stage.show();
    }

    private void inicializarAplicacion(ControladorPrincipal controladorPrincipal) throws Exception {
        Files.createDirectories(CARPETA_DATASAVE);

        List<Pelicula> peliculas = new ArrayList<>();
        List<Sala> salas = new ArrayList<>();
        List<Funcion> funciones = new ArrayList<>();
        List<Reserva> reservas = new ArrayList<>();

        String estado = cargarRepositoriosCsv(peliculas, salas, funciones, reservas);
        SucursalCine cine = new SucursalCine("Cine UTN", peliculas, salas, funciones, reservas);

        if (controladorPrincipal != null) {
            controladorPrincipal.setDatosIniciales(cine, peliculas, salas, funciones, reservas, estado);
        }
    }

    private String cargarRepositoriosCsv(
            List<Pelicula> peliculas,
            List<Sala> salas,
            List<Funcion> funciones,
            List<Reserva> reservas
    ) {
        peliculas.addAll(new AlmacenarPeliculas(CARPETA_DATASAVE).cargarPeliculas());
        salas.addAll(new AlmacenamientoSalas(CARPETA_DATASAVE).cargarSalas());
        funciones.addAll(new AlmacenamientoFunciones(CARPETA_DATASAVE).cargarFunciones(salas, peliculas));
        reservas.addAll(new AlmacenamientoReserva(CARPETA_DATASAVE).cargarReservas());
        return "Datos cargados desde " + CARPETA_DATASAVE.toAbsolutePath();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
