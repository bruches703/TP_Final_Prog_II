/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Sala;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
/**
 *
 * @author Bruches
 */
public class ControladorFunciones {

    @FXML
    private TableView<Funcion> tblFunciones;

    @FXML
    private TableColumn<Funcion, String> colPelicula;

    @FXML
    private TableColumn<Funcion, String> colSala;

    @FXML
    private TableColumn<Funcion, String> colFecha;

    @FXML
    private TableColumn<Funcion, String> colHorario;

    @FXML
    private TableColumn<Funcion, String> colPrecio;

    @FXML
    private Button btnVolver;

    @FXML
    private ComboBox<Pelicula> cmbPelicula;

    @FXML
    private ComboBox<Sala> cmbSala;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtHorario;

    @FXML
    private TextField txtPrecio;

    @FXML
    private Button btnAgregarFuncion;

    @FXML
    private Button btnModificarFuncion;

    @FXML
    private Label lblMensaje;

    private ControladorPrincipal controladorPrincipal;
    private List<Funcion> listaFuncionesReferencia = new ArrayList<>();
    private List<Pelicula> listaPeliculasReferencia = new ArrayList<>();
    private List<Sala> listaSalasReferencia = new ArrayList<>();

    private final ObservableList<Funcion> funcionesObservables = FXCollections.observableArrayList();
    private final ObservableList<Pelicula> peliculasObservables = FXCollections.observableArrayList();
    private final ObservableList<Sala> salasObservables = FXCollections.observableArrayList();

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public void setDatosFunciones(List<Funcion> funciones, List<Pelicula> peliculas, List<Sala> salas) {
        if (funciones != null) {
            this.listaFuncionesReferencia = funciones;
        }
        if (peliculas != null) {
            this.listaPeliculasReferencia = peliculas;
        }
        if (salas != null) {
            this.listaSalasReferencia = salas;
        }

        funcionesObservables.setAll(this.listaFuncionesReferencia);
        peliculasObservables.setAll(this.listaPeliculasReferencia);
        salasObservables.setAll(this.listaSalasReferencia);

        if (!peliculasObservables.isEmpty() && cmbPelicula.getValue() == null) {
            cmbPelicula.setValue(peliculasObservables.get(0));
        }
        if (!salasObservables.isEmpty() && cmbSala.getValue() == null) {
            cmbSala.setValue(salasObservables.get(0));
        }
    }

    @FXML
    public void initialize() {
        tblFunciones.setItems(funcionesObservables);
        cmbPelicula.setItems(peliculasObservables);
        cmbSala.setItems(salasObservables);

        if (dpFecha != null && dpFecha.getValue() == null) {
            dpFecha.setValue(LocalDate.now());
        }

        configurarCombos();
        configurarColumnas();

        tblFunciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                if (newSel.getPelicula() != null) {
                    cmbPelicula.setValue(newSel.getPelicula());
                }
                if (newSel.getSala() != null) {
                    cmbSala.setValue(newSel.getSala());
                }
                if (newSel.getFecha() != null) {
                    dpFecha.setValue(newSel.getFecha());
                }
                txtHorario.setText(newSel.getHorario() != null ? newSel.getHorario().toString() : "");
                txtPrecio.setText(String.valueOf(newSel.getPrecio()));
            }
        });
    }

    private void configurarCombos() {
        cmbPelicula.setConverter(new StringConverter<Pelicula>() {
            @Override
            public String toString(Pelicula p) {
                if (p == null) {
                    return "";
                }
                String titulo = p.getTitulo() != null ? p.getTitulo() : "Sin título";
                long min = p.getDuracion() != null ? p.getDuracion().toMinutes() : 0L;
                return titulo + " (" + min + " min)";
            }

            @Override
            public Pelicula fromString(String string) {
                return null;
            }
        });

        cmbSala.setConverter(new StringConverter<Sala>() {
            @Override
            public String toString(Sala s) {
                if (s == null) {
                    return "";
                }
                String estado = s.isEstaDisponible() ? "Disponible" : "No disponible";
                return "Sala " + s.getIdSala() + " (" + estado + ")";
            }

            @Override
            public Sala fromString(String string) {
                return null;
            }
        });
    }

    private void configurarColumnas() {
        colPelicula.setCellValueFactory(cell -> {
            Funcion f = cell.getValue();
            Pelicula p = (f != null) ? f.getPelicula() : null;
            return new SimpleStringProperty(p != null && p.getTitulo() != null ? p.getTitulo() : "");
        });

        colSala.setCellValueFactory(cell -> {
            Funcion f = cell.getValue();
            Sala s = (f != null) ? f.getSala() : null;
            return new SimpleStringProperty(s != null ? "Sala " + s.getIdSala() : "");
        });

        colFecha.setCellValueFactory(cell -> {
            Funcion f = cell.getValue();
            return new SimpleStringProperty(f != null && f.getFecha() != null ? f.getFecha().toString() : "");
        });

        colHorario.setCellValueFactory(cell -> {
            Funcion f = cell.getValue();
            return new SimpleStringProperty(f != null && f.getHorario() != null ? f.getHorario().toString() : "");
        });

        colPrecio.setCellValueFactory(cell -> {
            Funcion f = cell.getValue();
            double precio = (f != null) ? f.getPrecio() : 0.0;
            return new SimpleStringProperty(String.format("$ %.2f", precio));
        });
    }

    @FXML
    private void agregarFuncion() {
        Pelicula pelicula = (cmbPelicula != null) ? cmbPelicula.getValue() : null;
        Sala sala = (cmbSala != null) ? cmbSala.getValue() : null;
        LocalDate fecha = (dpFecha != null) ? dpFecha.getValue() : null;
        String horarioTexto = (txtHorario != null && txtHorario.getText() != null)
                ? txtHorario.getText().trim()
                : "";
        String precioTexto = (txtPrecio != null && txtPrecio.getText() != null)
                ? txtPrecio.getText().trim().replace(",", ".")
                : "";

        if (pelicula == null) {
            lblMensaje.setText("Seleccioná una película.");
            return;
        }
        if (sala == null) {
            lblMensaje.setText("Seleccioná una sala.");
            return;
        }
        if (!sala.isEstaDisponible()) {
            lblMensaje.setText("La Sala " + sala.getIdSala() + " no está disponible.");
            return;
        }
        if (fecha == null) {
            lblMensaje.setText("Seleccioná una fecha.");
            return;
        }

        LocalTime horario;
        try {
            horario = LocalTime.parse(horarioTexto, DateTimeFormatter.ofPattern("H:mm"));
        } catch (DateTimeParseException e) {
            lblMensaje.setText("Ingresá un horario válido (ej: 20:30).");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingresá un precio numérico válido.");
            return;
        }

        if (precio <= 0) {
            lblMensaje.setText("El precio debe ser mayor a 0.");
            return;
        }

        for (Funcion f : listaFuncionesReferencia) {
            if (f != null && f.getSala() != null
                    && f.getSala().getIdSala() == sala.getIdSala()
                    && fecha.equals(f.getFecha())
                    && horario.equals(f.getHorario())) {
                lblMensaje.setText("Ya existe una función en Sala " + sala.getIdSala()
                        + " para esa fecha y horario.");
                return;
            }
        }

        int nuevoId = listaFuncionesReferencia.size() + 1;
        Funcion nuevaFuncion = new Funcion(nuevoId, fecha, horario, sala, pelicula, precio);

        listaFuncionesReferencia.add(nuevaFuncion);
        funcionesObservables.setAll(listaFuncionesReferencia);

        if (controladorPrincipal != null && controladorPrincipal.getAlmacenFunciones() != null) {
            controladorPrincipal.getAlmacenFunciones().almacenarFuncion(nuevaFuncion);
        }

        txtHorario.clear();
        txtPrecio.clear();
        lblMensaje.setText("Función agregada y guardada en funciones.csv.");
    }

    @FXML
    private void modificarFuncion() {
        Funcion seleccionada = tblFunciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensaje.setText("Seleccioná una función de la tabla para modificar.");
            return;
        }

        Pelicula pelicula = (cmbPelicula != null) ? cmbPelicula.getValue() : null;
        Sala sala = (cmbSala != null) ? cmbSala.getValue() : null;
        LocalDate fecha = (dpFecha != null) ? dpFecha.getValue() : null;
        String horarioTexto = (txtHorario != null && txtHorario.getText() != null)
                ? txtHorario.getText().trim()
                : "";
        String precioTexto = (txtPrecio != null && txtPrecio.getText() != null)
                ? txtPrecio.getText().trim().replace(",", ".")
                : "";

        if (pelicula == null) {
            lblMensaje.setText("Seleccioná una película.");
            return;
        }
        if (sala == null) {
            lblMensaje.setText("Seleccioná una sala.");
            return;
        }
        if (!sala.isEstaDisponible()) {
            lblMensaje.setText("La Sala " + sala.getIdSala() + " no está disponible.");
            return;
        }
        if (fecha == null) {
            lblMensaje.setText("Seleccioná una fecha.");
            return;
        }

        LocalTime horario;
        try {
            horario = LocalTime.parse(horarioTexto, DateTimeFormatter.ofPattern("H:mm"));
        } catch (DateTimeParseException e) {
            lblMensaje.setText("Ingresá un horario válido (ej: 20:30).");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            lblMensaje.setText("Ingresá un precio numérico válido.");
            return;
        }

        if (precio <= 0) {
            lblMensaje.setText("El precio debe ser mayor a 0.");
            return;
        }

        for (Funcion f : listaFuncionesReferencia) {
            if (f != null && f != seleccionada && f.getSala() != null
                    && f.getSala().getIdSala() == sala.getIdSala()
                    && fecha.equals(f.getFecha())
                    && horario.equals(f.getHorario())) {
                lblMensaje.setText("Ya existe otra función en Sala " + sala.getIdSala()
                        + " para esa fecha y horario.");
                return;
            }
        }

        int idConservado = seleccionada.getIdFuncion();
        Funcion actualizada = new Funcion(idConservado, fecha, horario, sala, pelicula, precio);

        int idx = listaFuncionesReferencia.indexOf(seleccionada);
        if (idx >= 0) {
            listaFuncionesReferencia.set(idx, actualizada);
        }

        funcionesObservables.setAll(listaFuncionesReferencia);
        tblFunciones.getSelectionModel().select(actualizada);

        if (controladorPrincipal != null && controladorPrincipal.getAlmacenFunciones() != null) {
            controladorPrincipal.getAlmacenFunciones().sobrescribirFunciones(listaFuncionesReferencia);
        }

        lblMensaje.setText("Función modificada y guardada en funciones.csv.");
    }

    @FXML
    private void volver() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("inicio");
        }
    }
}