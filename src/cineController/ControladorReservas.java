/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineController;

import cineModelos.Butaca;
import cineModelos.Entrada;
import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Reserva;
import cineModelos.Sala;
import cineModelos.TipoEntrada;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;
/**
 *
 * @author Bruches
 */
public class ControladorReservas {

    @FXML
    private ComboBox<Funcion> cmbFuncion;

    @FXML
    private ComboBox<TipoEntrada> cmbTipoEntrada;

    @FXML
    private Label lblEncabezadoFuncion;

    @FXML
    private Label lblPantalla;

    @FXML
    private GridPane gridButacas;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnLimpiarSeleccion;

    @FXML
    private Label lblButacasSeleccionadas;

    @FXML
    private Label lblCantidadButacas;

    @FXML
    private Label lblPrecioTotal;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Button btnConfirmarReserva;

    @FXML
    private Label lblMensajeReserva;

    @FXML
    private TableView<Reserva> tblReservas;

    @FXML
    private TableColumn<Reserva, String> colCodigo;

    @FXML
    private TableColumn<Reserva, String> colEstado;

    @FXML
    private TableColumn<Reserva, String> colImporte;

    @FXML
    private TextField txtCodigoCancelar;

    @FXML
    private Button btnCancelarReserva;

    private ControladorPrincipal controladorPrincipal;
    private List<Reserva> listaReservasReferencia = new ArrayList<>();
    private List<Funcion> listaFuncionesReferencia = new ArrayList<>();

    private final Map<String, List<Butaca>> butacasPorCodigoReserva = new HashMap<>();
    private final ObservableList<Reserva> reservasObservables = FXCollections.observableArrayList();
    private final ObservableList<Funcion> funcionesObservables = FXCollections.observableArrayList();
    private final Map<Butaca, TipoEntrada> seleccionConTarifa = new LinkedHashMap<>();

    public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public void setDatosReservas(List<Reserva> reservas, List<Funcion> funciones) {
        if (reservas != null) {
            this.listaReservasReferencia = reservas;
        }
        if (funciones != null) {
            this.listaFuncionesReferencia = funciones;
        }

        reservasObservables.setAll(this.listaReservasReferencia);
        funcionesObservables.setAll(this.listaFuncionesReferencia);

        if (!funcionesObservables.isEmpty() && cmbFuncion.getValue() == null) {
            cmbFuncion.setValue(funcionesObservables.get(0));
        }
        construirGrillaButacas();
        actualizarResumen();
    }

    @FXML
    public void initialize() {
        tblReservas.setItems(reservasObservables);
        cmbFuncion.setItems(funcionesObservables);
        cmbTipoEntrada.setItems(FXCollections.observableArrayList(TipoEntrada.values()));
        cmbTipoEntrada.setValue(TipoEntrada.GENERAL);

        configurarComboFuncion();
        configurarColumnas();

        cmbFuncion.valueProperty().addListener((obs, oldVal, newVal) -> {
            seleccionConTarifa.clear();
            lblMensajeReserva.setText("");
            construirGrillaButacas();
            actualizarResumen();
        });

        tblReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null && newSel.getCodigo() != null && txtCodigoCancelar != null) {
                txtCodigoCancelar.setText(newSel.getCodigo());
            }
        });
    }

    private void configurarComboFuncion() {
        cmbFuncion.setConverter(new StringConverter<Funcion>() {
            @Override
            public String toString(Funcion f) {
                if (f == null) {
                    return "";
                }
                Pelicula p = f.getPelicula();
                Sala s = f.getSala();
                String titulo = (p != null && p.getTitulo() != null) ? p.getTitulo() : "Sin título";
                String salaStr = (s != null) ? "Sala " + s.getIdSala() : "Sin sala";
                String horaStr = (f.getHorario() != null) ? f.getHorario().toString() : "";
                return "#" + f.getIdFuncion() + " - " + titulo + " (" + salaStr + " " + horaStr + ")";
            }

            @Override
            public Funcion fromString(String string) {
                return null;
            }
        });
    }

    private void configurarColumnas() {
        colCodigo.setCellValueFactory(cell -> {
            Reserva r = cell.getValue();
            return new SimpleStringProperty(r != null && r.getCodigo() != null ? r.getCodigo() : "");
        });

        colEstado.setCellValueFactory(cell -> {
            Reserva r = cell.getValue();
            return new SimpleStringProperty(r != null && r.isEstadoDeReserva() ? "Confirmada" : "Pendiente");
        });

        colImporte.setCellValueFactory(cell -> {
            Reserva r = cell.getValue();
            double imp = (r != null) ? r.getImporte() : 0.0;
            return new SimpleStringProperty(String.format("$ %.2f", imp));
        });
    }

    private void construirGrillaButacas() {
        gridButacas.getChildren().clear();

        Funcion funcion = cmbFuncion.getValue();
        if (funcion == null) {
            lblEncabezadoFuncion.setText("No hay funciones cargadas");
            return;
        }

        Pelicula p = funcion.getPelicula();
        Sala sala = funcion.getSala();
        String titulo = (p != null && p.getTitulo() != null) ? p.getTitulo() : "Función";
        String salaTexto = (sala != null) ? "Sala " + sala.getIdSala() : "Sin sala";
        String fechaTexto = (funcion.getFecha() != null) ? funcion.getFecha().toString() : "";
        String horaTexto = (funcion.getHorario() != null) ? funcion.getHorario().toString() : "";
        lblEncabezadoFuncion.setText(titulo + " · " + salaTexto + " · " + fechaTexto + " · " + horaTexto);

        if (sala == null || sala.getListaButacas() == null || sala.getListaButacas().isEmpty()) {
            lblMensajeReserva.setText("La sala de esta función no tiene butacas configuradas.");
            return;
        }

        for (Butaca butaca : sala.getListaButacas()) {
            if (butaca == null) {
                continue;
            }
            int colIndex = Math.max(0, butaca.getAsiento() - 1);
            int rowIndex = Math.max(0, butaca.getFila() - 1);

            StackPane celda = crearCeldaButaca(butaca, sala.isEstaDisponible());
            gridButacas.add(celda, colIndex, rowIndex);
        }
    }

    private StackPane crearCeldaButaca(Butaca butaca, boolean salaDisponible) {
        StackPane stack = new StackPane();
        stack.setPrefSize(56.0, 42.0);
        stack.setAlignment(Pos.CENTER);

        Button btnButaca = new Button(formatearEtiquetaButaca(butaca));
        btnButaca.setPrefSize(56.0, 42.0);

        boolean ocupada = !butaca.isEstaDisponible() || !salaDisponible;
        boolean seleccionada = seleccionConTarifa.containsKey(butaca);

        if (ocupada) {
            btnButaca.setDisable(true);
            btnButaca.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #64748b; "
                    + "-fx-border-color: #94a3b8; -fx-border-radius: 4; -fx-background-radius: 4; -fx-opacity: 0.9;");

            Line tachado = new Line(4.0, 38.0, 52.0, 4.0);
            tachado.setStroke(Color.web("#475569"));
            tachado.setStrokeWidth(1.8);
            tachado.setMouseTransparent(true);

            stack.getChildren().addAll(btnButaca, tachado);
        } else if (seleccionada) {
            btnButaca.setStyle("-fx-background-color: #1e293b; -fx-text-fill: #ffffff; "
                    + "-fx-border-color: #0f172a; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-weight: bold;");
            btnButaca.setOnAction(e -> alternarSeleccion(butaca));
            stack.getChildren().add(btnButaca);
        } else {
            btnButaca.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #0f172a; "
                    + "-fx-border-color: #94a3b8; -fx-border-radius: 4; -fx-background-radius: 4;");
            btnButaca.setOnAction(e -> alternarSeleccion(butaca));
            stack.getChildren().add(btnButaca);
        }

        return stack;
    }

    private void alternarSeleccion(Butaca butaca) {
        TipoEntrada elegida = (cmbTipoEntrada != null && cmbTipoEntrada.getValue() != null)
                ? cmbTipoEntrada.getValue()
                : TipoEntrada.GENERAL;

        TipoEntrada actual = seleccionConTarifa.get(butaca);
        if (actual != null && actual == elegida) {
            seleccionConTarifa.remove(butaca);
        } else {
            seleccionConTarifa.put(butaca, elegida);
        }

        construirGrillaButacas();
        actualizarResumen();
    }

    private String formatearEtiquetaButaca(Butaca butaca) {
        char letraFila = (char) ('A' + Math.max(0, butaca.getFila() - 1));
        return letraFila + String.valueOf(butaca.getAsiento());
    }

    private double calcularTotalSeleccion(double precioBase) {
        double total = 0.0;
        for (TipoEntrada tipo : seleccionConTarifa.values()) {
            total += tipo.calcularPrecioUnitario(precioBase);
        }
        return total;
    }

    private void actualizarResumen() {
        Funcion funcion = cmbFuncion.getValue();
        double precioBase = (funcion != null) ? funcion.getPrecio() : 0.0;

        if (seleccionConTarifa.isEmpty()) {
            lblButacasSeleccionadas.setText("Butacas: -");
            lblCantidadButacas.setText("Cantidad: 0");
            lblPrecioTotal.setText("Total: $ 0.00");
            btnConfirmarReserva.setDisable(true);
            return;
        }

        List<String> detalles = new ArrayList<>();
        for (Map.Entry<Butaca, TipoEntrada> entry : seleccionConTarifa.entrySet()) {
            String etq = formatearEtiquetaButaca(entry.getKey());
            double unitario = entry.getValue().calcularPrecioUnitario(precioBase);
            detalles.add(String.format("%s (%s: $ %.0f)", etq, entry.getValue().getEtiqueta(), unitario));
        }

        int cantidad = seleccionConTarifa.size();
        double total = calcularTotalSeleccion(precioBase);

        lblButacasSeleccionadas.setText("Butacas: " + String.join(", ", detalles));
        lblCantidadButacas.setText("Cantidad: " + cantidad);
        lblPrecioTotal.setText(String.format("Total: $ %.2f", total));
        btnConfirmarReserva.setDisable(false);
    }

    @FXML
    private void limpiarSeleccion() {
        seleccionConTarifa.clear();
        lblMensajeReserva.setText("");
        construirGrillaButacas();
        actualizarResumen();
    }

    @FXML
    private void volver() {
        if (controladorPrincipal != null) {
            controladorPrincipal.navegarA("cartelera");
        }
    }

    @FXML
    private void confirmarReserva() {
        Funcion funcion = cmbFuncion.getValue();
        if (funcion == null) {
            lblMensajeReserva.setText("Seleccioná una función.");
            return;
        }

        Sala sala = funcion.getSala();
        if (sala == null || !sala.isEstaDisponible()) {
            lblMensajeReserva.setText("La sala no está disponible.");
            return;
        }

        if (seleccionConTarifa.isEmpty()) {
            lblMensajeReserva.setText("Seleccioná al menos una butaca disponible.");
            return;
        }

        for (Butaca b : seleccionConTarifa.keySet()) {
            if (!b.isEstaDisponible()) {
                lblMensajeReserva.setText("Alguna butaca seleccionada ya no está disponible.");
                seleccionConTarifa.clear();
                construirGrillaButacas();
                actualizarResumen();
                return;
            }
        }

        String codigoIngresado = txtCodigo.getText() != null ? txtCodigo.getText().trim() : "";
        String codigoFinal = codigoIngresado.isEmpty()
                ? "RES-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
                : codigoIngresado;

        for (Reserva r : listaReservasReferencia) {
            if (r != null && codigoFinal.equalsIgnoreCase(r.getCodigo())) {
                lblMensajeReserva.setText("Ya existe una reserva con el código " + codigoFinal + ".");
                return;
            }
        }

        List<Butaca> reservadasAhora = new ArrayList<>();
        List<Entrada> entradasEmitidas = new ArrayList<>();
        StringBuilder detalleEntradas = new StringBuilder();
        double precioBase = funcion.getPrecio();
        double importeTotal = 0.0;

        for (Map.Entry<Butaca, TipoEntrada> entry : seleccionConTarifa.entrySet()) {
            Butaca b = entry.getKey();
            TipoEntrada tipo = entry.getValue();

            Entrada entrada = new Entrada(b, tipo, precioBase) {};
            b.setEstaDisponible(false);
            reservadasAhora.add(b);
            entradasEmitidas.add(entrada);
            importeTotal += entrada.getPrecio();

            detalleEntradas.append(String.format(
                    "• %s — %s: $ %.2f%n",
                    formatearEtiquetaButaca(b),
                    tipo.getEtiqueta(),
                    entrada.getPrecio()
            ));
        }

        int cantidad = reservadasAhora.size();
        Reserva nuevaReserva = new Reserva(true, importeTotal, codigoFinal);

        butacasPorCodigoReserva.put(codigoFinal.toUpperCase(), reservadasAhora);
        listaReservasReferencia.add(nuevaReserva);
        reservasObservables.setAll(listaReservasReferencia);

        if (controladorPrincipal != null && !controladorPrincipal.persistirEstadoActual(lblMensajeReserva)) {
            return;
        }

        seleccionConTarifa.clear();
        txtCodigo.clear();
        construirGrillaButacas();
        actualizarResumen();

        lblMensajeReserva.setText(String.format(
                "Reserva %s confirmada (%d butacas — $ %.2f) y guardada en el estado del cine.",
                codigoFinal, cantidad, importeTotal
        ));

        mostrarVentanaConfirmacion(codigoFinal, funcion, detalleEntradas.toString(), cantidad, importeTotal);
    }

    private void mostrarVentanaConfirmacion(
            String codigo,
            Funcion funcion,
            String detalleEntradas,
            int cantidad,
            double total
    ) {
        Pelicula p = funcion.getPelicula();
        Sala s = funcion.getSala();
        String titulo = (p != null && p.getTitulo() != null) ? p.getTitulo() : "Función";
        String salaStr = (s != null) ? "Sala " + s.getIdSala() : "Sin sala";
        String fechaStr = (funcion.getFecha() != null) ? funcion.getFecha().toString() : "";
        String horaStr = (funcion.getHorario() != null) ? funcion.getHorario().toString() : "";

        String contenido = String.format(
                "Código de reserva: %s%n"
                + "Película: %s%n"
                + "Sala: %s  |  Fecha: %s  |  Horario: %s%n"
                + "Precio base función: $ %.2f%n%n"
                + "Detalle de butacas (%d):%n%s%n"
                + "TOTAL A PAGAR: $ %.2f",
                codigo, titulo, salaStr, fechaStr, horaStr, funcion.getPrecio(), cantidad, detalleEntradas, total
        );

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Reserva confirmada");
        alerta.setHeaderText("Resumen de reserva — " + codigo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    @FXML
    private void cancelarReserva() {
        String codigoBuscado = (txtCodigoCancelar != null && txtCodigoCancelar.getText() != null)
                ? txtCodigoCancelar.getText().trim()
                : "";

        if (codigoBuscado.isEmpty()) {
            Reserva seleccionada = tblReservas.getSelectionModel().getSelectedItem();
            if (seleccionada != null && seleccionada.getCodigo() != null) {
                codigoBuscado = seleccionada.getCodigo().trim();
            }
        }

        if (codigoBuscado.isEmpty()) {
            lblMensajeReserva.setText("Ingresá o seleccioná un código de reserva para cancelar.");
            return;
        }

        Reserva encontrada = null;
        for (Reserva r : listaReservasReferencia) {
            if (r != null && r.getCodigo() != null
                    && r.getCodigo().trim().equalsIgnoreCase(codigoBuscado)) {
                encontrada = r;
                break;
            }
        }

        if (encontrada == null) {
            lblMensajeReserva.setText("No se encontró ninguna reserva con el código \"" + codigoBuscado + "\".");
            return;
        }

        listaReservasReferencia.remove(encontrada);
        reservasObservables.setAll(listaReservasReferencia);

        List<Butaca> butacasLiberadas = butacasPorCodigoReserva.remove(codigoBuscado.toUpperCase());
        if (butacasLiberadas != null) {
            for (Butaca b : butacasLiberadas) {
                if (b != null) {
                    b.setEstaDisponible(true);
                }
            }
        }

        if (controladorPrincipal != null && !controladorPrincipal.persistirEstadoActual(lblMensajeReserva)) {
            return;
        }

        if (txtCodigoCancelar != null) {
            txtCodigoCancelar.clear();
        }
        construirGrillaButacas();
        actualizarResumen();
        lblMensajeReserva.setText("Reserva \"" + encontrada.getCodigo() + "\" cancelada y guardada en el estado del cine.");
    }
}