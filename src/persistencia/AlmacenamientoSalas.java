/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import cineModelos.Sala;
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
public class AlmacenamientoSalas implements Serializable {
    private static final long serialVersionUID = 2001;
    private final Path directorio;

    
    public AlmacenamientoSalas(Path directorio) {
        this.directorio = directorio;
    }
    

    public void agregarNuevaPelicula(){
        Scanner leer = new Scanner(System.in);
        
    }
    
    /**
     * 
     * @param sala
     * @param separador 
     */
    public void almacenarSala(Sala sala, String separador) {
        if (sala == null) {
            System.out.println("No se puede guardar esta sala");
            return;
        }

        try {
            Files.createDirectories(this.directorio);
            Path archivo = this.directorio.resolve("sala.csv"); 
            
            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                String linea = sala.getIdSala()+ separador
                        + sala.getListaButacas() + separador;
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar esta sala nueva" );
        }
    }    
}
