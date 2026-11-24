/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinePersistencia;

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
    private static final Pattern PATRON_SEPARADOR = Pattern.compile(Pattern.quote(SEPARADOR));

    private final Path directorio;

    public AlmacenamientoReserva() {
        this.directorio = Path.of("datasave");
    }

    public AlmacenamientoReserva(Path directorio) {
        this.directorio = (directorio != null) ? directorio : Path.of("datasave");
    }

    public void almacenarReserva(Reserva reserva) {
        almacenarReserva(reserva, null, null);
    }

    /**
     * Toma una reserva y la guarda con los campos de sala y una lista de Butacas
     * que pueden ser 1 o mas, un codigo de reserva y un importe total
     *
     * @param reserva
     * @param sala
     * @param butacasReservadas
     */
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

    public void reescribirReservas(List<Reserva> listaReservas) {
        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(ARCHIVO);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                if (listaReservas == null) {
                    return;
                }
                for (Reserva reserva : listaReservas) {
                    if (reserva == null) {
                        continue;
                    }
                    String codigo = (reserva.getCodigo() != null) ? reserva.getCodigo() : "";
                    String linea = codigo + SEPARADOR
                            + reserva.isEstadoDeReserva() + SEPARADOR
                            + "-1" + SEPARADOR
                            + "" + SEPARADOR
                            + reserva.getImporte();
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir " + ARCHIVO + ": " + e.getMessage());
        }
    }

    /**
     * Carga las reservas desde el directorio y las reconstruye en una lista
     *
     * @return Devuelve una lista de reservas cargadas desde el directorio.
     */
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

                String[] partes = PATRON_SEPARADOR.split(linea);
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

    /**
     * Representa si el codigo buscado existe o no en la lista
     *
     * @param listaReserva Lista de reserva donde se busca el codigo
     * @param codigoBuscado codigo de reserva que se busca en la lista
     * @return Devuelve true si existe, la reserva no es null o si los codigos de
     * las reservas no son null. Si no lo encuentra, devuelve false.
     */
    private boolean existeCodigoEnLista(List<Reserva> listaReserva, String codigoBuscado) {
        for (Reserva reserva : listaReserva) {
            if (reserva != null && reserva.getCodigo() != null && reserva.getCodigo().equals(codigoBuscado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea una cadena con la lista de butacas
     *
     * @param butacasReservadas Lista de butacas que se mostraran en pantalla
     * @return
     */
    private String formatearButacas(List<Butaca> butacasReservadas) {
        if (butacasReservadas == null || butacasReservadas.isEmpty()) {
            return "";
        }

        StringBuilder nuevoString = new StringBuilder();
        for (int i = 0; i < butacasReservadas.size(); i++) {
            Butaca butaca = butacasReservadas.get(i);
            if (butaca == null) {
                continue;
            }
            if (nuevoString.length() > 0) {
                nuevoString.append(" | ");
            }
            nuevoString.append("F").append(butaca.getFila()).append("-A").append(butaca.getAsiento());
        }
        return nuevoString.toString();
    }

    private boolean existeCodigoEnArchivo(Path archivo, String codigoBuscado, String sep) {
        if (!Files.exists(archivo)) {
            return false;
        }

        Pattern patron = sep.equals(SEPARADOR) ? PATRON_SEPARADOR : Pattern.compile(Pattern.quote(sep));

        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = patron.split(linea);
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