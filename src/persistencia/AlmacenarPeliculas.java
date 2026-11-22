/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import cineModelos.Pelicula;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;



/**
 *
 * @author Bruches
 */
public class AlmacenarPeliculas implements Serializable {

    private static final long serialVersionUID = 1001;
    private final Path directorio;
    
    public AlmacenarPeliculas(Path directorio) {
        this.directorio = directorio;
    }

    public void agregarNuevaPelicula(){
        String separador = ";";
        Scanner leer = new Scanner(System.in);
        Pelicula nuevaPelicula = Pelicula.nuevaPelicula(leer);
        almacenarPelicula(nuevaPelicula, separador);
    }
    
    public void almacenarPelicula(Pelicula pelicula, String separador) {
        if (pelicula == null) {
            System.out.println("No se puede guardar esta informacion");
            return;
        }

        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve("peliculas.csv"); 
            
            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                String linea = pelicula.getTitulo() + separador
                        + pelicula.getDuracion().toMinutes() + separador
                        + pelicula.getEstaActiva();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar esta informacion" );
        }
    }
}
