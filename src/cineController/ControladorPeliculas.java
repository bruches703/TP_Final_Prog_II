/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Pelicula;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
    private TableColumn<Pelicula, String> colDuracion;

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

    public void setDatosPeliculas(List<Pelicula> peliculas) {
        this.listaPeliculasReferencia = (peliculas != null) ? peliculas : new ArrayList<>();
        peliculasObservables.setAll(this.listaPeliculasReferencia);
        if (tblPeliculas != null) {
            tblPeliculas.setItems(peliculasObservables);
        }
    }

    @FXML
    public void initialize() {
        if (tblPeliculas != null) {
            tblPeliculas.setItems(peliculasObservables);
            configurarColumnas();
            tblPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    if (txtTitulo != null) {
                        txtTitulo.setText(newSel.getTitulo() != null ? newSel.getTitulo() : "");
                    }
                    if (txtDuracion != null) {
                        long min = (newSel.getDuracion() != null) ? newSel.getDuracion().toMinutes() : 0L;
                        txtDuracion.setText(min > 0 ? String.valueOf(min) : "");
                    }
                    if (chkActiva != null) {
                        chkActiva.setSelected(newSel.getEstaActiva());
                    }
                }
            });
        }
        if (chkActiva != null) {
            chkActiva.setSelected(true);
        }
    }

    private void configurarColumnas() {
        if (colTitulo != null) {
            colTitulo.setCellValueFactory(cell -> {
                Pelicula p = cell.getValue();
                return new SimpleStringProperty(p != null && p.getTitulo() != null ? p.getTitulo() : "");
            });
        }
        if (colDuracion != null) {
            colDuracion.setCellValueFactory(cell -> {
                Pelicula p = cell.getValue();
                if (p == null || p.getDuracion() == null) {
                    return new SimpleStringProperty("");
                }
                return new SimpleStringProperty(String.valueOf(p.getDuracion().toMinutes()));
            });
        }
        if (colActiva != null) {
            colActiva.setCellValueFactory(cell -> {
                Pelicula p = cell.getValue();
                boolean activa = p != null && p.getEstaActiva();
                return new SimpleStringProperty(activa ? "Sí" : "No");
            });
        }
    }

    @FXML
    private void agregarPelicula() {
        DatosPelicula datos = validarYLeerFormulario();
        if (datos == null) {
            return;
        }

        Pelicula nueva = new Pelicula(datos.titulo, Duration.ofMinutes(datos.minutos), datos.activa);
        listaPeliculasReferencia.add(nueva);
        peliculasObservables.setAll(listaPeliculasReferencia);

        if (controladorPrincipal != null && !controladorPrincipal.persistirEstadoActual(lblMensaje)) {
            return;
        }

        limpiarFormulario();
        mostrarMensaje("Película agregada y guardada en el estado del cine.");
    }

    @FXML
    private void modificarPelicula() {
        if (tblPeliculas == null) {
            return;
        }
        Pelicula seleccionada = tblPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccioná una película de la tabla para modificar.");
            return;
        }

        DatosPelicula datos = validarYLeerFormulario();
        if (datos == null) {
            return;
        }

        seleccionada.setTitulo(datos.titulo);
        seleccionada.setDuracion(Duration.ofMinutes(datos.minutos));
        seleccionada.setEstaActiva(datos.activa);

        peliculasObservables.setAll(listaPeliculasReferencia);
        tblPeliculas.refresh();

        if (controladorPrincipal != null && !controladorPrincipal.persistirEstadoActual(lblMensaje)) {
            return;
        }

        mostrarMensaje("Película modificada y guardada en el estado del cine.");
    }

    private DatosPelicula validarYLeerFormulario() {
        String titulo = (txtTitulo != null && txtTitulo.getText() != null)
                ? txtTitulo.getText().trim()
                : "";
        String duracionTexto = (txtDuracion != null && txtDuracion.getText() != null)
                ? txtDuracion.getText().trim()
                : "";
        boolean activa = (chkActiva == null) || chkActiva.isSelected();

        if (titulo.isEmpty()) {
            mostrarMensaje("Ingresá el título de la película.");
            return null;
        }

        int minutos;
        try {
            minutos = Integer.parseInt(duracionTexto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingresá una duración válida en minutos (número entero).");
            return null;
        }

        if (minutos <= 0) {
            mostrarMensaje("La duración debe ser mayor a 0 minutos.");
            return null;
        }

        return new DatosPelicula(titulo, minutos, activa);
    }

    private void limpiarFormulario() {
        if (txtTitulo != null) {
            txtTitulo.clear();
        }
        if (txtDuracion != null) {
            txtDuracion.clear();
        }
        if (chkActiva != null) {
            chkActiva.setSelected(true);
        }
    }

    private void mostrarMensaje(String texto) {
        if (lblMensaje != null) {
            lblMensaje.setText(texto);
        }
    }

    @FXML
    private void volver() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("inicio");
        }
    }

    private record DatosPelicula(String titulo, int minutos, boolean activa) {
    }
}