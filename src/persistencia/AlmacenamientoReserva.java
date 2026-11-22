/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import cineModelos.Butaca;
import cineModelos.Reserva;
import cineModelos.Sala;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author bruch
 */
public class AlmacenamientoReserva implements Serializable {

    private static final long serialVersionUID = 4001L;
    private static final String ARCHIVO = "reservas.csv";
    private static final String SEPARADOR = ";";

    private final Path directorio;

    public AlmacenamientoReserva() {
        this.directorio = Path.of("datasave");
    }

    public AlmacenamientoReserva(Path directorio) {
        if (directorio != null) {
            this.directorio = directorio;
        } else {
            this.directorio = Path.of("datasave");
        }
    }

    public void almacenarReserva(Reserva reserva) {
        almacenarReserva(reserva, null, null);
    }

    public void almacenarReserva(Reserva reserva, Sala sala, List<Butaca> butacasReservadas) {
        if (reserva == null) {
            System.out.println("No se puede guardar una reserva nula.");
            return;
        }

        if (!reserva.isEstadoDeReserva()) {
            System.out.println("La reserva no esta confirmada y no se guardara.");
            return;
        }

        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(ARCHIVO);

            String codigo = (reserva.getCodigo() != null) ? reserva.getCodigo() : "";
            if (!codigo.isEmpty() && existeCodigoEnArchivo(archivo, codigo, SEPARADOR)) {
                System.out.println("La reserva con codigo " + codigo + " ya existe en " + ARCHIVO + ".");
                return;
            }

            int idSala = (sala != null) ? sala.getIdSala() : -1;
            String butacasTexto = formatearButacas(butacasReservadas);

            String linea = codigo + SEPARADOR
                    + reserva.isEstadoDeReserva() + SEPARADOR
                    + idSala + SEPARADOR
                    + butacasTexto + SEPARADOR
                    + reserva.getImporte();

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la reserva en el archivo: " + e.getMessage());
        }
    }

    public List<Reserva> cargarReservas() {
        List<Reserva> reservasCargadas = new ArrayList<>();
        Path archivo = this.directorio.resolve(ARCHIVO);

        if (!Files.exists(archivo)) {
            return reservasCargadas;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(Pattern.quote(SEPARADOR));
                if (partes.length < 5) {
                    System.out.println("Linea con formato invalido en " + ARCHIVO + ": " + linea);
                    continue;
                }

                try {
                    String codigo = partes[0].trim();
                    boolean estado = Boolean.parseBoolean(partes[1].trim());
                    double importe = Double.parseDouble(partes[4].trim());

                    if (codigo.isEmpty() || existeCodigoEnLista(reservasCargadas, codigo)) {
                        continue;
                    }

                    reservasCargadas.add(new Reserva(estado, importe, codigo));
                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear importe en " + ARCHIVO + ": " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }

        return reservasCargadas;
    }

    private boolean existeCodigoEnLista(List<Reserva> lista, String codigoBuscado) {
        for (Reserva r : lista) {
            if (r != null && r.getCodigo() != null && r.getCodigo().equals(codigoBuscado)) {
                return true;
            }
        }
        return false;
    }

    private String formatearButacas(List<Butaca> butacasReservadas) {
        if (butacasReservadas == null || butacasReservadas.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < butacasReservadas.size(); i++) {
            Butaca b = butacasReservadas.get(i);
            if (b == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("F").append(b.getFila()).append("-A").append(b.getAsiento());
        }
        return sb.toString();
    }

    private boolean existeCodigoEnArchivo(Path archivo, String codigoBuscado, String sep) {
        if (!Files.exists(archivo)) {
            return false;
        }

        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(Pattern.quote(sep));
                if (partes.length > 0 && partes[0].trim().equals(codigoBuscado)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de reservas: " + e.getMessage());
        }

        return false;
    }
}