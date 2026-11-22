/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Sala;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author bruch
 */
public class AlmacenamientoFunciones implements Serializable {

    private static final long serialVersionUID = 3001L;
    private static final String archivoGuardado = "funciones.csv";
    private static final String separador = ";";

    private Path directorio;

    public AlmacenamientoFunciones() {
        this.directorio = Path.of("datasave");
    }

    public AlmacenamientoFunciones(Path directorio) {
        establecerDirectorio(directorio);
    }

    public void establecerDirectorio(Path directorio) {
        this.directorio = Objects.requireNonNullElse(directorio, Path.of("datasave"));
    }

    public void agregarNuevaFuncion(Scanner leer, List<Sala> listaSalas, List<Pelicula> listaPeliculas) {
        Funcion nuevaFuncion = Funcion.nuevaFuncion(leer, listaSalas, listaPeliculas);
        if (nuevaFuncion == null) {
            System.out.println("No se pudo crear la funcion.");
            return;
        }
        almacenarFuncion(nuevaFuncion, separador);
    }

    public void almacenarFuncion(Funcion funcion, String separador) {
        if (funcion == null) {
            System.out.println("No se puede guardar una funcion nula.");
            return;
        }


        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve(archivoGuardado);

            String linea = funcion.getFechaFuncion() + separador
                    + funcion.getHorarioFuncion() + separador
                    + funcion.getSala().getIdSala() + separador
                    + funcion.getPelicula().getTitulo() + separador
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
}