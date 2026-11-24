/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Reserva;
import cineModelos.Sala;
import cineModelos.SucursalCine;
import cinePersistencia.AlmacenamientoEstadoCine;
import cinePersistencia.AlmacenamientoFunciones;
import cinePersistencia.AlmacenamientoReserva;
import cinePersistencia.AlmacenamientoSalas;
import cinePersistencia.AlmacenarPeliculas;
import cinePersistencia.PersistenciaException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Bruches
 */
/**
 *
 * @author Bruches
 */
public class ControladorPrincipal {

    @FXML
    private BorderPane panelPrincipal;

    @FXML
    private Label lblTitulo;

    @FXML
    private Button btnInicio;

    @FXML
    private Button btnCartelera;

    @FXML
    private Button btnPeliculas;

    @FXML
    private Button btnSalas;

    @FXML
    private Button btnFunciones;

    @FXML
    private Button btnReservas;

    private SucursalCine sucursalCine;
    private List<Pelicula> peliculas = new ArrayList<>();
    private List<Sala> salas = new ArrayList<>();
    private List<Funcion> funciones = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private String estadoPersistencia = "Datos cargados desde datasave/";

    private final AlmacenamientoReserva almacenReservas
            = new AlmacenamientoReserva(Path.of("datasave"));

    private final AlmacenarPeliculas almacenPeliculas
            = new AlmacenarPeliculas(Path.of("datasave"));

    private final AlmacenamientoFunciones almacenFunciones
            = new AlmacenamientoFunciones(Path.of("datasave"));

    private final AlmacenamientoSalas almacenSalas
            = new AlmacenamientoSalas(Path.of("datasave"));

    private AlmacenamientoEstadoCine almacenEstado = new AlmacenamientoEstadoCine();

    public void setAlmacenEstado(AlmacenamientoEstadoCine almacenEstado) {
        if (almacenEstado != null) {
            this.almacenEstado = almacenEstado;
        }
    }

    public AlmacenamientoEstadoCine getAlmacenEstado() {
        return almacenEstado;
    }

    public boolean persistirEstadoActual(Label lblRetroalimentacion) {
        try {
            if (sucursalCine == null) {
                sucursalCine = almacenEstado.crearSucursalInicial();
            }
            sucursalCine.setPeliculas(new ArrayList<>(peliculas));
            sucursalCine.setSalas(new ArrayList<>(salas));
            sucursalCine.setFunciones(new ArrayList<>(funciones));
            sucursalCine.setReservas(new ArrayList<>(reservas));
            almacenEstado.guardarSucursal(sucursalCine);
            return true;
        } catch (PersistenciaException ex) {
            if (lblRetroalimentacion != null) {
                lblRetroalimentacion.setText(ex.getMessage());
            }
            return false;
        }
    }

    public AlmacenamientoReserva getAlmacenReservas() {
        return almacenReservas;
    }

    public AlmacenarPeliculas getAlmacenPeliculas() {
        return almacenPeliculas;
    }

    public AlmacenamientoFunciones getAlmacenFunciones() {
        return almacenFunciones;
    }

    public AlmacenamientoSalas getAlmacenSalas() {
        return almacenSalas;
    }

    public void setDatosIniciales(
            SucursalCine sucursalCine,
            List<Pelicula> peliculas,
            List<Sala> salas,
            List<Funcion> funciones,
            List<Reserva> reservas,
            String estadoPersistencia
    ) {
        this.sucursalCine = sucursalCine;
        this.peliculas = peliculas != null ? peliculas : new ArrayList<>();
        this.salas = salas != null ? salas : new ArrayList<>();
        this.funciones = funciones != null ? funciones : new ArrayList<>();
        this.reservas = reservas != null ? reservas : new ArrayList<>();
        if (estadoPersistencia != null && !estadoPersistencia.isBlank()) {
            this.estadoPersistencia = estadoPersistencia;
        }

        if (panelPrincipal != null) {
            mostrarInicio();
        }
    }

    @FXML
    public void initialize() {
        mostrarInicio();
    }

    @FXML
    private void mostrarInicio() {
        cargarVista("/cineVistas/inicio.fxml", "Inicio");
    }

    @FXML
    private void mostrarCartelera() {
        cargarVista("/cineVistas/cartelera.fxml", "Cartelera");
    }

    @FXML
    private void mostrarPeliculas() {
        cargarVista("/cineVistas/peliculas.fxml", "Administrar películas");
    }

    @FXML
    private void mostrarSalas() {
        cargarVista("/cineVistas/salas.fxml", "Administrar salas");
    }

    @FXML
    private void mostrarFunciones() {
        cargarVista("/cineVistas/funciones.fxml", "Administrar funciones");
    }

    @FXML
    private void mostrarReservas() {
        cargarVista("/cineVistas/reservas.fxml", "Reservas");
    }

    public void navegarA(String destino) {
        if (destino == null) {
            return;
        }
        switch (destino.toLowerCase()) {
            case "cartelera" ->
                mostrarCartelera();
            case "reservas" ->
                mostrarReservas();
            case "peliculas", "películas" ->
                mostrarPeliculas();
            case "salas" ->
                mostrarSalas();
            case "funciones" ->
                mostrarFunciones();
            case "inicio" ->
                mostrarInicio();
            default ->
                mostrarInicio();
        }
    }

    private void cargarVista(String ruta, String titulo) {
        try {
            var recurso = getClass().getResource(ruta);
            if (recurso == null) {
                System.err.println("No se encontró el FXML: " + ruta);
                return;
            }

            FXMLLoader loader = new FXMLLoader(recurso);
            Node vista = loader.load();
            Object controller = loader.getController();

            if (controller instanceof ControladorInicio ctrlInicio) {
                ctrlInicio.setControladorPrincipal(this);
                ctrlInicio.actualizarResumen(
                        funciones.size(),
                        reservas.size(),
                        salas.size(),
                        peliculas.size(),
                        estadoPersistencia
                );
            } else if (controller instanceof ControladorCartelera ctrlCartelera) {
                ctrlCartelera.setControladorPrincipal(this);
                ctrlCartelera.setDatosCartelera(funciones, peliculas);
            } else if (controller instanceof ControladorSalas ctrlSalas) {
                ctrlSalas.setControladorPrincipal(this);
                ctrlSalas.setDatosSalas(salas);
            } else if (controller instanceof ControladorPeliculas ctrlPeliculas) {
                ctrlPeliculas.setControladorPrincipal(this);
                ctrlPeliculas.setDatosPeliculas(peliculas);
            } else if (controller instanceof ControladorFunciones ctrlFunciones) {
                ctrlFunciones.setControladorPrincipal(this);
                ctrlFunciones.setDatosFunciones(funciones, peliculas, salas);
            } else if (controller instanceof ControladorReservas ctrlReservas) {
                ctrlReservas.setControladorPrincipal(this);
                ctrlReservas.setDatosReservas(reservas, funciones);
            }

            if (panelPrincipal != null) {
                panelPrincipal.setCenter(vista);
            }
            if (lblTitulo != null) {
                lblTitulo.setText(titulo);
            }
        } catch (IOException ex) {
            System.err.println("Error al cargar la vista: " + ruta);
            ex.printStackTrace();
        }
    }
}