/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Sala;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author bruch
 */
public class AlmacenamientoFunciones implements Serializable {

    private static final long serialVersionUID = 3001L;
    private static final String ARCHIVO = "funciones.csv";
    private static final String SEPARADOR = ";";

    private final Path directorio;

    public AlmacenamientoFunciones() {
        this.directorio = Path.of("datasave");
    }

    public AlmacenamientoFunciones(Path directorio) {
        if (directorio != null) {
            this.directorio = directorio;
        } else {
            this.directorio = Path.of("datasave");
        }
    }

    public void agregarNuevaFuncion(Scanner leer, List<Sala> listaSalas, List<Pelicula> listaPeliculas, List<Funcion> listaDeFunciones) {
        Funcion nuevaFuncion = Funcion.nuevaFuncion(leer, listaSalas, listaPeliculas, listaDeFunciones);
        if (nuevaFuncion == null) {
            System.out.println("No se pudo crear la funcion.");
            return;
        }

        Sala salaSeleccionada = nuevaFuncion.getSala();
        if (salaSeleccionada == null) {
            System.out.println("La funcion no tiene una sala asignada.");
            return;
        }

        Sala salaExistente;
        if (listaSalas != null) {
            salaExistente = Sala.buscarSalaPorId(listaSalas, salaSeleccionada.getIdSala());
        } else {
            salaExistente = null;
        }

        if (salaExistente == null) {
            System.out.println("No existe una sala con el ID " + salaSeleccionada.getIdSala() + ".");
            return;
        }

        if (!salaExistente.isEstaDisponible()) {
            System.out.println("La sala ID " + salaExistente.getIdSala() + " no esta disponible.");
            return;
        }

        if (listaDeFunciones != null) {
            for (Funcion f : listaDeFunciones) {
                if (f != null
                        && f.getSala() != null
                        && f.getSala().getIdSala() == salaExistente.getIdSala()
                        && f.getFecha() != null && f.getFecha().equals(nuevaFuncion.getFecha())
                        && f.getHorario() != null && f.getHorario().equals(nuevaFuncion.getHorario())) {
                    System.out.println("Ya existe una funcion en esa sala para la misma fecha y horario.");
                    return;
                }
            }
            listaDeFunciones.add(nuevaFuncion);
        }

        almacenarFuncion(nuevaFuncion);
    }

    public void almacenarFuncion(Funcion funcion) {
        if (funcion == null) {
            System.out.println("No se puede guardar una funcion nula.");
            return;
        }
        if (funcion.getSala() == null) {
            System.out.println("No se puede guardar una funcion sin sala asignada.");
            return;
        }
        if (funcion.getPelicula() == null) {
            System.out.println("No se puede guardar una funcion sin pelicula asignada.");
            return;
        }

        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(ARCHIVO);

            String linea = funcion.getFechaFuncion() + SEPARADOR
                    + funcion.getHorarioFuncion() + SEPARADOR
                    + funcion.getSala().getIdSala() + SEPARADOR
                    + funcion.getPelicula().getTitulo() + SEPARADOR
                    + funcion.getPrecio();

            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la funcion en el archivo: " + e.getMessage());
        }
    }

    public List<Funcion> cargarFunciones(List<Sala> listaSalas, List<Pelicula> listaPeliculas) {
        List<Funcion> funcionesCargadas = new ArrayList<>();
        Path archivo = this.directorio.resolve(ARCHIVO);

        if (!Files.exists(archivo)) {
            return funcionesCargadas;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(SEPARADOR);
                if (partes.length < 5) {
                    System.out.println("Linea invalida en " + ARCHIVO + ": " + linea);
                    continue;
                }

                try {
                    LocalDate fecha = LocalDate.parse(partes[0].trim());
                    LocalTime horario = LocalTime.parse(partes[1].trim());
                    int idSala = Integer.parseInt(partes[2].trim());
                    String tituloPelicula = partes[3].trim();
                    double precio = Double.parseDouble(partes[4].trim());

                    Sala salaEncontrada = (listaSalas != null) ? Sala.buscarSalaPorId(listaSalas, idSala) : null;
                    Pelicula peliculaEncontrada = buscarPeliculaPorTitulo(listaPeliculas, tituloPelicula);

                    if (salaEncontrada == null || peliculaEncontrada == null) {
                        System.out.println("No se pudo reconstruir la funcion (sala o pelicula no encontrada): " + linea);
                        continue;
                    }

                    int idFuncion = funcionesCargadas.size() + 1;
                    Funcion funcion = new Funcion(idFuncion, fecha, horario, salaEncontrada, peliculaEncontrada, precio);
                    funcionesCargadas.add(funcion);
                } catch (DateTimeParseException | NumberFormatException e) {
                    System.out.println("Error de formato al parsear linea en " + ARCHIVO + ": " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO + ": " + e.getMessage());
        }

        return funcionesCargadas;
    }

    private Pelicula buscarPeliculaPorTitulo(List<Pelicula> listaPeliculas, String titulo) {
        if (listaPeliculas == null || titulo == null || titulo.isEmpty()) {
            return null;
        }
        for (Pelicula pelicula : listaPeliculas) {
            if (pelicula != null && pelicula.getTitulo() != null
                    && titulo.equalsIgnoreCase(pelicula.getTitulo().trim())) {
                return pelicula;
            }
        }
        return null;
    }
}