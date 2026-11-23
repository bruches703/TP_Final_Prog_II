/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Pelicula;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
/**
 *
 * @author Bruches
 */
public class ControladorPeliculas {

    @FXML
    private TableView<Pelicula> tblPeliculas;

    @FXML
    private TableColumn<Pelicula, String> colTitulo;

    @FXML
    private TableColumn<Pelicula, Long> colDuracion;

    @FXML
    private TableColumn<Pelicula, String> colActiva;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtDuracion;

    @FXML
    private CheckBox chkActiva;

    @FXML
    private Button btnAgregarPelicula;

    @FXML
    private Button btnModificarPelicula;

    @FXML
    private Label lblMensaje;

    private ControladorPrincipal controladorPrincipal;
    private List<Pelicula> listaPeliculasReferencia = new ArrayList<>();
    private final ObservableList<Pelicula> peliculasObservables = FXCollections.observableArrayList();

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        if (peliculas != null) {
            this.listaPeliculasReferencia = peliculas;
        }
        peliculasObservables.setAll(this.listaPeliculasReferencia);
    }

    @FXML
    public void initialize() {
        tblPeliculas.setItems(peliculasObservables);
        if (chkActiva != null) {
            chkActiva.setSelected(true);
        }
        configurarColumnas();

        tblPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtTitulo.setText(newSel.getTitulo() != null ? newSel.getTitulo() : "");
                long min = newSel.getDuracion() != null ? newSel.getDuracion().toMinutes() : 0L;
                txtDuracion.setText(String.valueOf(min));
                if (chkActiva != null) {
                    chkActiva.setSelected(newSel.getEstaActiva());
                }
            }
        });
    }

    private void configurarColumnas() {
        colTitulo.setCellValueFactory(cell -> {
            Pelicula p = cell.getValue();
            return new SimpleStringProperty(p != null && p.getTitulo() != null ? p.getTitulo() : "");
        });

        colDuracion.setCellValueFactory(cell -> {
            Pelicula p = cell.getValue();
            long minutos = (p != null && p.getDuracion() != null) ? p.getDuracion().toMinutes() : 0L;
            return new SimpleObjectProperty<>(minutos);
        });

        colActiva.setCellValueFactory(cell -> {
            Pelicula p = cell.getValue();
            boolean activa = p != null && p.getEstaActiva();
            return new SimpleStringProperty(activa ? "Sí" : "No");
        });
    }

    @FXML
    private void agregarPelicula() {
        String titulo = (txtTitulo != null && txtTitulo.getText() != null)
                ? txtTitulo.getText().trim()
                : "";
        String duracionTexto = (txtDuracion != null && txtDuracion.getText() != null)
                ? txtDuracion.getText().trim()
                : "";
        boolean activa = (chkActiva == null) || chkActiva.isSelected();

        if (titulo.isEmpty()) {
            lblMensaje.setText("Ingresá un título para la película.");
            return;
        }

        long minutos;
        try {
            minutos = Long.parseLong(duracionTexto);
        } catch (NumberFormatException e) {
            lblMensaje.setText("La duración debe ser un número entero en minutos.");
            return;
        }

        if (minutos <= 0) {
            lblMensaje.setText("La duración debe ser mayor a 0 minutos.");
            return;
        }

        for (Pelicula p : listaPeliculasReferencia) {
            if (p != null && p.getTitulo() != null
                    && p.getTitulo().trim().equalsIgnoreCase(titulo)) {
                lblMensaje.setText("Ya existe una película con el título \"" + titulo + "\".");
                return;
            }
        }

        Pelicula nuevaPelicula = new Pelicula(titulo, Duration.ofMinutes(minutos), activa);

        listaPeliculasReferencia.add(nuevaPelicula);
        peliculasObservables.setAll(listaPeliculasReferencia);

        if (controladorPrincipal != null && controladorPrincipal.getAlmacenPeliculas() != null) {
            controladorPrincipal.getAlmacenPeliculas().almacenarPelicula(nuevaPelicula);
        }

        limpiarFormulario();
        lblMensaje.setText("Película \"" + titulo + "\" agregada y guardada en peliculas.csv.");
    }

    @FXML
    private void modificarPelicula() {
        Pelicula seleccionada = tblPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensaje.setText("Seleccioná una película de la tabla para modificar.");
            return;
        }

        String titulo = (txtTitulo != null && txtTitulo.getText() != null)
                ? txtTitulo.getText().trim()
                : "";
        String duracionTexto = (txtDuracion != null && txtDuracion.getText() != null)
                ? txtDuracion.getText().trim()
                : "";
        boolean activa = (chkActiva == null) || chkActiva.isSelected();

        if (titulo.isEmpty()) {
            lblMensaje.setText("Ingresá un título para la película.");
            return;
        }

        long minutos;
        try {
            minutos = Long.parseLong(duracionTexto);
        } catch (NumberFormatException e) {
            lblMensaje.setText("La duración debe ser un número entero en minutos.");
            return;
        }

        if (minutos <= 0) {
            lblMensaje.setText("La duración debe ser mayor a 0 minutos.");
            return;
        }

        for (Pelicula p : listaPeliculasReferencia) {
            if (p != null && p != seleccionada && p.getTitulo() != null
                    && p.getTitulo().trim().equalsIgnoreCase(titulo)) {
                lblMensaje.setText("Ya existe otra película con el título \"" + titulo + "\".");
                return;
            }
        }

        int idx = listaPeliculasReferencia.indexOf(seleccionada);
        Pelicula actualizada = new Pelicula(titulo, Duration.ofMinutes(minutos), activa);
        if (idx >= 0) {
            listaPeliculasReferencia.set(idx, actualizada);
        }

        peliculasObservables.setAll(listaPeliculasReferencia);
        tblPeliculas.getSelectionModel().select(actualizada);

        if (controladorPrincipal != null && controladorPrincipal.getAlmacenPeliculas() != null) {
            controladorPrincipal.getAlmacenPeliculas().sobrescribirPeliculas(listaPeliculasReferencia);
        }

        lblMensaje.setText("Película \"" + titulo + "\" modificada y guardada en peliculas.csv.");
    }

    private void limpiarFormulario() {
        txtTitulo.clear();
        txtDuracion.clear();
        if (chkActiva != null) {
            chkActiva.setSelected(true);
        }
    }

    @FXML
    private void volver() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("inicio");
        }
    }
}