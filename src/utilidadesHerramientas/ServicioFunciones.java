/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesHerramientas;
import cineModelos.Funcion;
import cineModelos.Pelicula;
import cineModelos.Sala;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/**
 *
 * @author Bruches
 */
public class ServicioFunciones {

    private static final int DURACION_POR_DEFECTO_MINUTOS = 120;

    public static Funcion buscarFuncionSuperpuesta(
            List<Funcion> funcionesExistentes,
            Sala sala,
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            Funcion funcionAExcluir
    ) {
        if (funcionesExistentes == null || sala == null || fecha == null || horaInicio == null) {
            return null;
        }

        int duracionNueva = (duracionMinutos > 0) ? duracionMinutos : DURACION_POR_DEFECTO_MINUTOS;
        LocalDateTime inicioNueva = LocalDateTime.of(fecha, horaInicio);
        LocalDateTime finNueva = inicioNueva.plusMinutes(duracionNueva);

        for (Funcion existente : funcionesExistentes) {
            if (existente == null || existente == funcionAExcluir) {
                continue;
            }
            if (existente.getSala() == null || existente.getFecha() == null || existente.getHorario() == null) {
                continue;
            }

            boolean mismaSala = existente.getSala().getIdSala() == sala.getIdSala();
            boolean mismaFecha = existente.getFecha().equals(fecha);
            if (!mismaSala || !mismaFecha) {
                continue;
            }

            int duracionExistente = obtenerDuracionMinutos(existente.getPelicula());
            LocalDateTime inicioExistente = LocalDateTime.of(existente.getFecha(), existente.getHorario());
            LocalDateTime finExistente = inicioExistente.plusMinutes(duracionExistente);

            boolean seSuperponen = inicioNueva.isBefore(finExistente) && finNueva.isAfter(inicioExistente);
            if (seSuperponen) {
                return existente;
            }
        }

        return null;
    }

    public static int obtenerDuracionMinutos(Pelicula pelicula) {
        if (pelicula == null) {
            return DURACION_POR_DEFECTO_MINUTOS;
        }
        Duration duracion = pelicula.getDuracion();
        if (duracion == null || duracion.isZero() || duracion.isNegative()) {
            return DURACION_POR_DEFECTO_MINUTOS;
        }
        long minutos = duracion.toMinutes();
        return (minutos > 0 && minutos <= Integer.MAX_VALUE)
                ? (int) minutos
                : DURACION_POR_DEFECTO_MINUTOS;
    }
}