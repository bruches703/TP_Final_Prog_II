/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Butaca;
import cineModelos.Sala;
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
public class ControladorSalas {

    @FXML
    private TableView<Sala> tblSalas;

    @FXML
    private TableColumn<Sala, Integer> colIdSala;

    @FXML
    private TableColumn<Sala, Integer> colButacas;

    @FXML
    private TableColumn<Sala, String> colDisponible;

    @FXML
    private Button btnVolver;

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
    private Label lblMensaje;

    private ControladorPrincipal controladorPrincipal;
    private List<Sala> listaSalasReferencia = new ArrayList<>();
    private final ObservableList<Sala> salasObservables = FXCollections.observableArrayList();

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public void setSalas(List<Sala> salas) {
        if (salas != null) {
            this.listaSalasReferencia = salas;
        }
        salasObservables.setAll(this.listaSalasReferencia);
    }

    @FXML
    public void initialize() {
        tblSalas.setItems(salasObservables);
        if (chkDisponible != null) {
            chkDisponible.setSelected(true);
        }
        configurarColumnas();

        tblSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdSala.setText(String.valueOf(newSel.getIdSala()));
                int maxFila = 0;
                int maxAsiento = 0;
                if (newSel.getListaButacas() != null) {
                    for (Butaca b : newSel.getListaButacas()) {
                        if (b != null) {
                            maxFila = Math.max(maxFila, b.getFila());
                            maxAsiento = Math.max(maxAsiento, b.getAsiento());
                        }
                    }
                }
                txtFilas.setText(String.valueOf(maxFila));
                txtAsientosPorFila.setText(String.valueOf(maxAsiento));
                if (chkDisponible != null) {
                    chkDisponible.setSelected(newSel.isEstaDisponible());
                }
            }
        });
    }

    private void configurarColumnas() {
        colIdSala.setCellValueFactory(cell -> {
            Sala s = cell.getValue();
            return new SimpleObjectProperty<>(s != null ? s.getIdSala() : 0);
        });

        colButacas.setCellValueFactory(cell -> {
            Sala s = cell.getValue();
            int total = (s != null && s.getListaButacas() != null) ? s.getListaButacas().size() : 0;
            return new SimpleObjectProperty<>(total);
        });

        colDisponible.setCellValueFactory(cell -> {
            Sala s = cell.getValue();
            boolean disp = s != null && s.isEstaDisponible();
            return new SimpleStringProperty(disp ? "Disponible" : "No disponible");
        });
    }

    @FXML
    private void agregarSala() {
        String idTexto = (txtIdSala != null && txtIdSala.getText() != null)
                ? txtIdSala.getText().trim()
                : "";
        String filasTexto = (txtFilas != null && txtFilas.getText() != null)
                ? txtFilas.getText().trim()
                : "";
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
        } catch (NumberFormatException e) {
            lblMensaje.setText("ID, filas y asientos por fila deben ser números enteros.");
            return;
        }

        if (idSala <= 0 || filas <= 0 || asientosPorFila <= 0) {
            lblMensaje.setText("Todos los valores numéricos deben ser mayores a 0.");
            return;
        }

        if (Sala.buscarSalaPorId(listaSalasReferencia, idSala) != null) {
            lblMensaje.setText("Ya existe una sala con el ID " + idSala + ".");
            return;
        }

        List<Butaca> butacas = generarButacas(filas, asientosPorFila);
        Sala nuevaSala = new Sala(idSala, butacas, disponible);

        listaSalasReferencia.add(nuevaSala);
        salasObservables.setAll(listaSalasReferencia);

        if (controladorPrincipal != null && controladorPrincipal.getAlmacenSalas() != null) {
            controladorPrincipal.getAlmacenSalas().almacenarSala(nuevaSala, ";");
        }

        limpiarFormulario();
        lblMensaje.setText("Sala " + idSala + " (" + butacas.size() + " butacas) agregada y guardada en salas.csv.");
    }

    @FXML
    private void modificarSala() {
        Sala seleccionada = tblSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensaje.setText("Seleccioná una sala de la tabla para modificar.");
            return;
        }

        String idTexto = (txtIdSala != null && txtIdSala.getText() != null)
                ? txtIdSala.getText().trim()
                : "";
        String filasTexto = (txtFilas != null && txtFilas.getText() != null)
                ? txtFilas.getText().trim()
                : "";
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
        } catch (NumberFormatException e) {
            lblMensaje.setText("ID, filas y asientos por fila deben ser números enteros.");
            return;
        }

        if (idSala <= 0 || filas <= 0 || asientosPorFila <= 0) {
            lblMensaje.setText("Todos los valores numéricos deben ser mayores a 0.");
            return;
        }

        for (Sala s : listaSalasReferencia) {
            if (s != null && s != seleccionada && s.getIdSala() == idSala) {
                lblMensaje.setText("Ya existe otra sala con el ID " + idSala + ".");
                return;
            }
        }

        List<Butaca> butacas = generarButacas(filas, asientosPorFila);
        Sala actualizada = new Sala(idSala, butacas, disponible);

        int idx = listaSalasReferencia.indexOf(seleccionada);
        if (idx >= 0) {
            listaSalasReferencia.set(idx, actualizada);
        }

        salasObservables.setAll(listaSalasReferencia);
        tblSalas.getSelectionModel().select(actualizada);

        if (controladorPrincipal != null && controladorPrincipal.getAlmacenSalas() != null) {
            controladorPrincipal.getAlmacenSalas().sobrescribirSalas(listaSalasReferencia);
        }

        lblMensaje.setText("Sala " + idSala + " modificada y guardada en salas.csv.");
    }

    private List<Butaca> generarButacas(int filas, int asientosPorFila) {
        List<Butaca> butacas = new ArrayList<>();
        for (int f = 1; f <= filas; f++) {
            for (int a = 1; a <= asientosPorFila; a++) {
                butacas.add(new Butaca(f, a));
            }
        }
        return butacas;
    }

    private void limpiarFormulario() {
        txtIdSala.clear();
        txtFilas.clear();
        txtAsientosPorFila.clear();
        if (chkDisponible != null) {
            chkDisponible.setSelected(true);
        }
    }

    @FXML
    private void volver() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("inicio");
        }
    }

}