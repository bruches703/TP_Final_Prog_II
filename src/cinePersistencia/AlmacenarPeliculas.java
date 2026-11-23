/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinePersistencia;

import cineModelos.Pelicula;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class AlmacenarPeliculas {

    private static final String SEPARADOR = ";";
    private static final String NOMBRE_ARCHIVO = "peliculas.csv";
    private final Path directorio;

    public AlmacenarPeliculas() {
        this.directorio = Path.of("datasave");
    }

    public AlmacenarPeliculas(Path directorio) {
        if (directorio != null) {
            this.directorio = directorio;
        } else {
            this.directorio = Path.of("datasave");
        }
    }

    public void agregarNuevaPelicula(Scanner leer, List<Pelicula> listaPeliculas) {
        Pelicula nuevaPelicula = Pelicula.nuevaPelicula(leer);
        almacenarNuevaPelicula(nuevaPelicula, listaPeliculas);
    }

    public void almacenarNuevaPelicula(Pelicula peliculaNueva, List<Pelicula> listaPeliculas) {
        if (peliculaNueva == null) {
            System.out.println("No se puede guardar pelicula nula.");
            return;
        }

        if (listaPeliculas != null && listaPeliculas.contains(peliculaNueva)) {
            System.out.println("Pelicula " + peliculaNueva.getTitulo() + " ya existe");
            return;
        }

        if (listaPeliculas != null) {
            listaPeliculas.add(peliculaNueva);
        }

        almacenarPelicula(peliculaNueva);
    }

    public void almacenarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            System.out.println("No se puede guardar esta informacion");
            return;
        }

        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(NOMBRE_ARCHIVO);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                String linea = pelicula.getTitulo() + SEPARADOR
                        + pelicula.getDuracion().toMinutes() + SEPARADOR
                        + pelicula.getEstaActiva();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar esta informacion");
        }
    }

    public void almacenarListaDePeliculas(List<Pelicula> listaPeliculas) {
        if (listaPeliculas == null) {
            return;
        }
        for (Pelicula pelicula : listaPeliculas) {
            almacenarPelicula(pelicula);
        }
    }

    public List<Pelicula> cargarPeliculas() {
        List<Pelicula> peliculasCargadas = new ArrayList<>();
        Path archivo = this.directorio.resolve(NOMBRE_ARCHIVO);

        if (!Files.exists(archivo)) {
            return peliculasCargadas;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(SEPARADOR);
                if (partes.length < 3) {
                    System.out.println("Linea con formato invalido en " + NOMBRE_ARCHIVO + ": " + linea);
                    continue;
                }

                try {
                    String titulo = partes[0].trim();
                    long minutos = Long.parseLong(partes[1].trim());
                    boolean estaActiva = Boolean.parseBoolean(partes[2].trim());

                    if (titulo.isEmpty() || minutos <= 0) {
                        System.out.println("Datos invalidos en " + NOMBRE_ARCHIVO + ": " + linea);
                        continue;
                    }

                    Pelicula pelicula = new Pelicula(titulo, Duration.ofMinutes(minutos), estaActiva);
                    if (!existePeliculaPorTitulo(peliculasCargadas, titulo)) {
                        peliculasCargadas.add(pelicula);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear duracion en " + NOMBRE_ARCHIVO + ": " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + NOMBRE_ARCHIVO + ": " + e.getMessage());
        }

        return peliculasCargadas;
    }

    private boolean existePeliculaPorTitulo(List<Pelicula> lista, String tituloBuscado) {
        for (Pelicula p : lista) {
            if (p != null && p.getTitulo() != null
                    && p.getTitulo().trim().equalsIgnoreCase(tituloBuscado)) {
                return true;
            }
        }
        return false;
    }

    public void sobrescribirPeliculas(List<Pelicula> listaPeliculas) {
        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(NOMBRE_ARCHIVO);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                if (listaPeliculas == null) {
                    return;
                }
                for (Pelicula pelicula:  listaPeliculas) {
                    if (pelicula == null || pelicula.getTitulo() == null || pelicula.getDuracion() == null) {
                        continue;
                    }
                    String linea = pelicula.getTitulo() + SEPARADOR
                            + pelicula.getDuracion().toMinutes() + SEPARADOR
                            + pelicula.getEstaActiva();
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al sobrescribir " + NOMBRE_ARCHIVO + ": " + e.getMessage());
        }
    }
}
