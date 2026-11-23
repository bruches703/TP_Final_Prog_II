/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Sala;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Bruches
 */
public class ControladorCartelera {

    @FXML
    private ComboBox<String> cmbPelicula;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Funcion> tblFunciones;

    @FXML
    private TableColumn<Funcion, String> colPelicula;

    @FXML
    private TableColumn<Funcion, String> colSala;

    @FXML
    private TableColumn<Funcion, String> colHorario;

    @FXML
    private TableColumn<Funcion, String> colPrecio;

    @FXML
    private TableColumn<Funcion, Void> colAccion;

    @FXML
    private Label lblSinResultados;

    private ControladorPrincipal controladorPrincipal;
    private final List<Funcion> todasLasFunciones = new ArrayList<>();
    private final ObservableList<Funcion> funcionesVisibles = FXCollections.observableArrayList();

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    @FXML
    public void initialize() {
        tblFunciones.setItems(funcionesVisibles);
        configurarColumnas();
        cargarComboPeliculas(List.of());
    }

    public void setDatosCartelera(List<Funcion> funciones, List<Pelicula> peliculas) {
        todasLasFunciones.clear();
        if (funciones != null) {
            todasLasFunciones.addAll(funciones);
        }
        cargarComboPeliculas(peliculas);
        aplicarFiltros("Todas las películas", null);
    }

    private void cargarComboPeliculas(List<Pelicula> peliculas) {
        ObservableList<String> opciones = FXCollections.observableArrayList();
        opciones.add("Todas las películas");
        if (peliculas != null) {
            for (Pelicula p : peliculas) {
                if (p != null && p.getEstaActiva() && p.getTitulo() != null
                        && !opciones.contains(p.getTitulo())) {
                    opciones.add(p.getTitulo());
                }
            }
        }
        cmbPelicula.setItems(opciones);
        cmbPelicula.getSelectionModel().selectFirst();
    }

    private void configurarColumnas() {
        colPelicula.setCellValueFactory(cell -> {
            Pelicula p = cell.getValue().getPelicula();
            return new SimpleStringProperty(p != null && p.getTitulo() != null ? p.getTitulo() : "");
        });

        colSala.setCellValueFactory(cell -> {
            Sala s = cell.getValue().getSala();
            return new SimpleStringProperty(s != null ? "Sala " + s.getIdSala() : "");
        });

        colHorario.setCellValueFactory(cell -> {
            Funcion f = cell.getValue();
            String hora = f.getHorarioFuncion();
            return new SimpleStringProperty(hora != null ? hora : "");
        });

        colPrecio.setCellValueFactory(cell ->
                new SimpleStringProperty("$ " + String.format("%.0f", cell.getValue().getPrecio())));

        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button btnReservar = new Button("Reservar →");

            {
                btnReservar.getStyleClass().add("btn-secondary");
                btnReservar.setOnAction(event -> {
                    Funcion funcionSeleccionada = getTableView().getItems().get(getIndex());
                    if (controladorPrincipal != null && funcionSeleccionada != null) {
                        controladorPrincipal.navegarA("reservas");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnReservar);
            }
        });
    }

    @FXML
    private void buscarFunciones() {
        String peliculaSeleccionada = cmbPelicula.getValue();
        LocalDate fechaSeleccionada = dpFecha.getValue();
        aplicarFiltros(peliculaSeleccionada, fechaSeleccionada);
    }

    @FXML
    private void limpiarFiltros() {
        cmbPelicula.getSelectionModel().selectFirst();
        dpFecha.setValue(null);
        aplicarFiltros("Todas las películas", null);
    }

    private void aplicarFiltros(String tituloPelicula, LocalDate fecha) {
        List<Funcion> filtradas = new ArrayList<>();
        for (Funcion f : todasLasFunciones) {
            if (f == null) {
                continue;
            }
            Pelicula p = f.getPelicula();
            if (p == null || !p.getEstaActiva()) {
                continue;
            }

            boolean coincidePelicula = tituloPelicula == null
                    || tituloPelicula.isBlank()
                    || "Todas las películas".equals(tituloPelicula)
                    || (p.getTitulo() != null && tituloPelicula.equalsIgnoreCase(p.getTitulo().trim()));

            boolean coincideFecha = fecha == null || fecha.equals(f.getFecha());

            if (coincidePelicula && coincideFecha) {
                filtradas.add(f);
            }
        }

        funcionesVisibles.setAll(filtradas);
        boolean sinCoincidencias = filtradas.isEmpty();
        lblSinResultados.setVisible(sinCoincidencias);
        lblSinResultados.setManaged(sinCoincidencias);
    }
}
