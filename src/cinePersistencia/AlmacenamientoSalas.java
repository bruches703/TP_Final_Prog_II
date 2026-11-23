/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinePersistencia;

import cineModelos.Butaca;
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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bruches
 */
public class AlmacenamientoSalas implements Serializable {

    private static final long serialVersionUID = 2001L;
    private static final String ARCHIVO = "salas.csv";
    private static final String SEPARADOR = ";";

    private final Path directorio;

    public AlmacenamientoSalas(Path directorio) {
        this.directorio = (directorio != null) ? directorio : Path.of("datasave");
    }

    public AlmacenamientoSalas() {
        this.directorio = Path.of("datasave");
    }

    public void agregarNuevaSala() {
        Scanner leer = new Scanner(System.in);
        Sala nuevaSala = Sala.nuevaSala(leer);
        almacenarSala(nuevaSala, SEPARADOR);
    }

    public void agregarNuevaSala(Scanner leer, List<Sala> listaSalas) {
        Sala nuevaSala = Sala.nuevaSala(leer);
        if (nuevaSala == null) {
            System.out.println("No se pudo crear la sala.");
            return;
        }

        if (listaSalas != null && Sala.buscarSalaPorId(listaSalas, nuevaSala.getIdSala()) != null) {
            System.out.println("Ya existe una sala con el ID " + nuevaSala.getIdSala() + ".");
            return;
        }

        if (listaSalas != null) {
            listaSalas.add(nuevaSala);
        }

        almacenarSala(nuevaSala, SEPARADOR);
    }

    public void almacenarSala(Sala sala, String separador) {
        if (sala == null) {
            System.out.println("No se puede guardar esta sala");
            return;
        }

        String sep = (separador != null && !separador.isEmpty()) ? separador : SEPARADOR;

        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(ARCHIVO);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                String linea = sala.getIdSala() + sep
                        + sala.getListaButacas() + sep
                        + sala.isEstaDisponible();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar esta sala nueva");
        }
    }

    public void almacenarListaDeSalas(List<Sala> listaDeSalas) {
        if (listaDeSalas == null) {
            return;
        }
        for (Sala sala : listaDeSalas) {
            almacenarSala(sala, SEPARADOR);
        }
    }

    public List<Sala> cargarSalas() {
        List<Sala> salasCargadas = new ArrayList<>();
        Path archivo = this.directorio.resolve(ARCHIVO);

        if (!Files.exists(archivo)) {
            return salasCargadas;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(Pattern.quote(SEPARADOR));
                if (partes.length < 3) {
                    System.out.println("Linea con formato invalido en " + ARCHIVO + ": " + linea);
                    continue;
                }

                try {
                    int idSala = Integer.parseInt(partes[0].trim());
                    List<Butaca> butacas = parsearButacas(partes[1].trim());
                    boolean estaDisponible = Boolean.parseBoolean(partes[2].trim());

                    if (Sala.buscarSalaPorId(salasCargadas, idSala) != null) {
                        continue;
                    }

                    salasCargadas.add(new Sala(idSala, butacas, estaDisponible));
                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear ID de sala en " + ARCHIVO + ": " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }

        return salasCargadas;
    }

    private List<Butaca> parsearButacas(String textoButacas) {
        List<Butaca> butacas = new ArrayList<>();
        if (textoButacas == null || textoButacas.isEmpty() || textoButacas.equals("null")) {
            return butacas;
        }

        Pattern patronButaca = Pattern.compile("fila=(\\d+).*?asiento=(\\d+)(?:.*?estaDisponible=(true|false))?");
        Matcher matcher = patronButaca.matcher(textoButacas);
        while (matcher.find()) {
            int fila = Integer.parseInt(matcher.group(1));
            int asiento = Integer.parseInt(matcher.group(2));
            Butaca butaca = new Butaca(fila, asiento);
            if (matcher.group(3) != null) {
                butaca.setEstaDisponible(Boolean.parseBoolean(matcher.group(3)));
            }
            butacas.add(butaca);
        }

        return butacas;
    }

    public void sobrescribirSalas(List<Sala> listaSalas) {
        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(ARCHIVO);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                if (listaSalas == null) {
                    return;
                }
                for (Sala sala : listaSalas) {
                    if (sala == null) {
                        continue;
                    }
                    String linea = sala.getIdSala() + SEPARADOR
                            + sala.getListaButacas() + SEPARADOR
                            + sala.isEstaDisponible();
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al sobrescribir " + ARCHIVO + ": " + e.getMessage());
        }
    }
}
