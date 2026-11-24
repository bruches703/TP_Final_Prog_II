/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinePersistencia;

import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Reserva;
import cineModelos.Sala;
import cineModelos.SucursalCine;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
/**
 *
 * @author Bruches
 */
public class AlmacenamientoEstadoCine {

    private static final String NOMBRE_ARCHIVO_ESTADO = "estado_cine.dat";
    private static final String NOMBRE_ARCHIVO_TEMPORAL = "estado_cine.dat.tmp";
    private static final String NOMBRE_SUCURSAL_POR_DEFECTO = "Sucursal Principal";

    private final Path directorioDatos;
    private final Path archivoEstado;

    public AlmacenamientoEstadoCine() {
        this(Path.of("datasave"));
    }

    public AlmacenamientoEstadoCine(Path directorioDatos) {
        this.directorioDatos = (directorioDatos != null) ? directorioDatos : Path.of("datasave");
        this.archivoEstado = this.directorioDatos.resolve(NOMBRE_ARCHIVO_ESTADO);
    }

    public Path getDirectorioDatos() {
        return directorioDatos;
    }

    public Path getRutaArchivoEstado() {
        return archivoEstado;
    }

    public boolean existeEstadoGuardado() {
        return Files.exists(archivoEstado) && Files.isRegularFile(archivoEstado);
    }

    public SucursalCine crearSucursalInicial() {
        return new SucursalCine(
                NOMBRE_SUCURSAL_POR_DEFECTO,
                new ArrayList<Pelicula>(),
                new ArrayList<Sala>(),
                new ArrayList<Funcion>(),
                new ArrayList<Reserva>()
        );
    }

    public SucursalCine cargarSucursal() throws PersistenciaException {
        if (!Files.exists(archivoEstado)) {
            SucursalCine inicial = crearSucursalInicial();
            guardarSucursal(inicial);
            return inicial;
        }

        if (!Files.isRegularFile(archivoEstado)) {
            throw new PersistenciaException(
                    "La ruta de estado no corresponde a un archivo regular: " + archivoEstado
            );
        }

        try {
            if (Files.size(archivoEstado) == 0L) {
                throw new PersistenciaException(
                        "El archivo de estado está vacío: " + archivoEstado
                );
            }

            try (InputStream fis = Files.newInputStream(archivoEstado);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                Object obj = ois.readObject();
                if (!(obj instanceof SucursalCine sucursal)) {
                    throw new PersistenciaException(
                            "El contenido serializado en " + archivoEstado + " no es una SucursalCine válida."
                    );
                }

                normalizarColecciones(sucursal);
                return sucursal;
            }
        } catch (PersistenciaException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PersistenciaException(
                    "Error al cargar el estado desde " + archivoEstado + ". Motivo: " + extraerDetalleError(ex),
                    ex
            );
        }
    }

    public void guardarSucursal(SucursalCine sucursal) throws PersistenciaException {
        if (sucursal == null) {
            throw new PersistenciaException("No se puede guardar una SucursalCine nula.");
        }

        normalizarColecciones(sucursal);
        Path archivoTemporal = directorioDatos.resolve(NOMBRE_ARCHIVO_TEMPORAL);

        try {
            Files.createDirectories(directorioDatos);

            try (OutputStream fos = Files.newOutputStream(archivoTemporal);
                 BufferedOutputStream bos = new BufferedOutputStream(fos);
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {

                oos.writeObject(sucursal);
                oos.flush();
            }

            if (!Files.exists(archivoTemporal) || Files.size(archivoTemporal) <= 0L) {
                throw new PersistenciaException(
                        "No se confirmó la escritura de datos en " + archivoTemporal
                );
            }

            Files.move(archivoTemporal, archivoEstado, StandardCopyOption.REPLACE_EXISTING);

            if (!Files.exists(archivoEstado) || Files.size(archivoEstado) <= 0L) {
                throw new PersistenciaException(
                        "Error al guardar el estado en " + archivoEstado + ". No se confirmó el guardado."
                );
            }
        } catch (PersistenciaException ex) {
            limpiarTemporalSilencioso(archivoTemporal);
            throw ex;
        } catch (Exception ex) {
            limpiarTemporalSilencioso(archivoTemporal);
            throw new PersistenciaException(
                    "Error al guardar el estado en " + archivoEstado + ". No se confirmó el guardado. Motivo: " + extraerDetalleError(ex),
                    ex
            );
        }
    }

    private void normalizarColecciones(SucursalCine sucursal) {
        if (sucursal.getPeliculas() == null) {
            sucursal.setPeliculas(new ArrayList<Pelicula>());
        }
        if (sucursal.getSalas() == null) {
            sucursal.setSalas(new ArrayList<Sala>());
        }
        if (sucursal.getFunciones() == null) {
            sucursal.setFunciones(new ArrayList<Funcion>());
        }
        if (sucursal.getReservas() == null) {
            sucursal.setReservas(new ArrayList<Reserva>());
        }
    }

    private void limpiarTemporalSilencioso(Path archivoTemporal) {
        try {
            Files.deleteIfExists(archivoTemporal);
        } catch (IOException ignored) {
        }
    }

    private String extraerDetalleError(Throwable ex) {
        Throwable causaRaiz = ex;
        while (causaRaiz.getCause() != null) {
            causaRaiz = causaRaiz.getCause();
        }
        String tipo = causaRaiz.getClass().getSimpleName();
        String msg = causaRaiz.getMessage();
        return (msg != null && !msg.isBlank()) ? (tipo + ": " + msg) : tipo;
    }
}