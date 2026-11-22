package persistencia;

import cineModelos.Pelicula;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class AlmacenarPeliculas {

    private static final String SEPARADOR = ";";
    private static final String NOMBRE_ARCHIVO = "peliculas.csv";
    private final Path directorio;

    public AlmacenarPeliculas(Path directorio) {
        this.directorio = directorio;
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
    public void almacenarListaDePeliculas(List<Pelicula> listaPeliculas){
        for (Pelicula pelicula : listaPeliculas) {
            almacenarPelicula(pelicula);
        }
    }
}