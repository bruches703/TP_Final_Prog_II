/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Butaca;
import cineModelos.Sala;
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
public class ControladorSalas {

    @FXML
    private TableView<Sala> tblSalas;

    @FXML
    private TableColumn<Sala, String> colIdSala;

    @FXML
    private TableColumn<Sala, String> colCapacidad;

    @FXML
    private TableColumn<Sala, String> colDisponible;

    @FXML
    private TextField txtIdSala;

    @FXML
    private TextField txtFilas;

    @FXML
    private TextField txtAsientosPorFila;

    @FXML
    private CheckBox chkDisponible;

    @FXML
    private Button btnAgregarSala;

    @FXML
    private Button btnModificarSala;

    @FXML
    private Button btnVolver;

    @FXML
    private Label lblMensaje;

    private ControladorPrincipal controladorPrincipal;
    private List<Sala> listaSalasReferencia = new ArrayList<>();
    private final ObservableList<Sala> salasObservables = FXCollections.observableArrayList();

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public void setDatosSalas(List<Sala> salas) {
        this.listaSalasReferencia = (salas != null) ? salas : new ArrayList<>();
        salasObservables.setAll(this.listaSalasReferencia);
        if (tblSalas != null) {
            tblSalas.setItems(salasObservables);
        }
    }

    @FXML
    public void initialize() {
        if (tblSalas != null) {
            tblSalas.setItems(salasObservables);
            configurarColumnas();
            tblSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    if (txtIdSala != null) {
                        txtIdSala.setText(String.valueOf(newSel.getIdSala()));
                    }
                    if (chkDisponible != null) {
                        chkDisponible.setSelected(newSel.isEstaDisponible());
                    }
                }
            });
        }
        if (chkDisponible != null) {
            chkDisponible.setSelected(true);
        }
    }

    private List<Butaca> generarButacas(int filas, int asientosPorFila) {
        return Butaca.nuevaListaButacas(filas, asientosPorFila);
    }

    private int contarButacas(Sala sala) {
        if (sala == null) {
            return 0;
        }
        if (sala.getListaButacas() != null) {
            return sala.getListaButacas().size();
        }
        return sala.getButacasTotales();
    }

    private void configurarColumnas() {
        if (colIdSala != null) {
            colIdSala.setCellValueFactory(cell -> {
                Sala s = cell.getValue();
                return new SimpleStringProperty(s != null ? "Sala " + s.getIdSala() : "");
            });
        }
        if (colCapacidad != null) {
            colCapacidad.setCellValueFactory(cell -> {
                Sala s = cell.getValue();
                int total = contarButacas(s);
                return new SimpleStringProperty(total + " butacas");
            });
        }
        if (colDisponible != null) {
            colDisponible.setCellValueFactory(cell -> {
                Sala s = cell.getValue();
                boolean disp = s != null && s.isEstaDisponible();
                return new SimpleStringProperty(disp ? "Disponible" : "No disponible");
            });
        }
    }

    @FXML
    private void agregarSala() {
        DatosNuevaSala datos = validarNuevaSala();
        if (datos == null) {
            return;
        }

        for (Sala s : listaSalasReferencia) {
            if (s != null && s.getIdSala() == datos.idSala) {
                mostrarMensaje("Ya existe una sala con el número " + datos.idSala + ".");
                return;
            }
        }

        List<Butaca> butacas = generarButacas(datos.filas, datos.asientosPorFila);
        Sala nuevaSala = new Sala(datos.idSala, butacas, datos.disponible);

        listaSalasReferencia.add(nuevaSala);
        salasObservables.setAll(listaSalasReferencia);

        if (controladorPrincipal != null && !controladorPrincipal.persistirEstadoActual(lblMensaje)) {
            return;
        }

        limpiarFormulario();
        mostrarMensaje("Sala " + nuevaSala.getIdSala() + " (" + butacas.size()
                + " butacas) guardada en el estado del cine.");
    }

    @FXML
    private void modificarSala() {
        if (tblSalas == null) {
            return;
        }
        Sala seleccionada = tblSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccioná una sala de la tabla para modificar.");
            return;
        }

        boolean disponible = (chkDisponible == null) || chkDisponible.isSelected();
        seleccionada.setEstaDisponible(disponible);

        salasObservables.setAll(listaSalasReferencia);
        tblSalas.refresh();

        if (controladorPrincipal != null && !controladorPrincipal.persistirEstadoActual(lblMensaje)) {
            return;
        }

        mostrarMensaje("Sala " + seleccionada.getIdSala() + " actualizada en el estado del cine.");
    }

    private DatosNuevaSala validarNuevaSala() {
        String idTexto = (txtIdSala != null && txtIdSala.getText() != null) ? txtIdSala.getText().trim() : "";
        String filasTexto = (txtFilas != null && txtFilas.getText() != null) ? txtFilas.getText().trim() : "";
        String asientosTexto = (txtAsientosPorFila != null && txtAsientosPorFila.getText() != null)
                ? txtAsientosPorFila.getText().trim()
                : "";
        boolean disponible = (chkDisponible == null) || chkDisponible.isSelected();

        int idSala;
        int filas;
        int asientosPorFila;

        try {
            idSala = Integer.parseInt(idTexto);
            filas = Integer.parseInt(filasTexto);
            asientosPorFila = Integer.parseInt(asientosTexto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingresá números enteros válidos para ID de sala, filas y asientos.");
            return null;
        }

        if (idSala <= 0 || filas <= 0 || asientosPorFila <= 0) {
            mostrarMensaje("ID de sala, filas y asientos por fila deben ser mayores a 0.");
            return null;
        }

        return new DatosNuevaSala(idSala, filas, asientosPorFila, disponible);
    }

    private void limpiarFormulario() {
        if (txtIdSala != null) {
            txtIdSala.clear();
        }
        if (txtFilas != null) {
            txtFilas.clear();
        }
        if (txtAsientosPorFila != null) {
            txtAsientosPorFila.clear();
        }
        if (chkDisponible != null) {
            chkDisponible.setSelected(true);
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

    private record DatosNuevaSala(int idSala, int filas, int asientosPorFila, boolean disponible) {
    }
}