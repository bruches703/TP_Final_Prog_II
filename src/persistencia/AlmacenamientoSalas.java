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
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class AlmacenamientoSalas implements Serializable {
private static final String archivo = "salas.csv";
    private static final String separador = ";";

    private final Path directorio;
    
    
    public AlmacenamientoSalas(Path directorio) {
        this.directorio = directorio;
    }
    
    
    /**
     * 
     */
    public void agregarNuevaSala(){
        String separador = ";";
        Scanner leer = new Scanner(System.in);
        Sala nuevaSala = Sala.nuevaSala(leer);
        almacenarSala(nuevaSala, separador);
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

        almacenarSala(nuevaSala, separador);
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
    
    public void almacenarListaDeSalas(List<Sala> listaDeSalas){
        for (Sala sala : listaDeSalas) {
            almacenarSala(sala, separador);
        }
    }
}
