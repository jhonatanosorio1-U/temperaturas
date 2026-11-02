package servicios;

import modelos.RegistroTemperatura;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.TreeMap;

public class TemperaturasServicio {

    // Punto 1: cargar los datos del CSV (paradigma funcional: Stream + map + collect)
    public static List<RegistroTemperatura> cargarDatos(String rutaArchivo) {
        var formatoFecha = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            Stream<String> lineasArchivo = Files.lines(Paths.get(rutaArchivo));
            return lineasArchivo.skip(1) // se salta la cabecera Ciudad,Fecha,Temperatura
                    .filter(linea -> !linea.isBlank())
                    .map(linea -> linea.split(","))
                    .map(textos -> new RegistroTemperatura(
                            textos[0].trim(),
                            LocalDate.parse(textos[1].trim(), formatoFecha),
                            Double.parseDouble(textos[2].trim())
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    // Utilidad ligada a la carga: lista de ciudades distintas presentes en los datos.
    // La van a necesitar los compañeros 2 y 3 para llenar combos/promedios, así que
    // la dejo aquí para que no tengan que repetir la lógica de "sacar ciudades únicas".
    public static List<String> getCiudades(List<RegistroTemperatura> registros) {
        return registros.stream()
                .map(RegistroTemperatura::getCiudad)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

   public static String ciudadMasCalurosa(List<RegistroTemperatura> registros, LocalDate fecha) {
        return registros.stream()
                .filter(r -> r.getFecha().equals(fecha))
                .max(Comparator.comparingDouble(RegistroTemperatura::getTemperatura))
                .map(RegistroTemperatura::getCiudad)
                .orElse("No hay datos para esta fecha");
    }

    public static String ciudadMenosCalurosa(List<RegistroTemperatura> registros, LocalDate fecha) {
        return registros.stream()
                .filter(r -> r.getFecha().equals(fecha))
                .min(Comparator.comparingDouble(RegistroTemperatura::getTemperatura))
                .map(RegistroTemperatura::getCiudad)
                .orElse("No hay datos para esta fecha");
    }

    public static Map<String, Double> promedioPorCiudadEnRango(List<RegistroTemperatura> registros, LocalDate desde, LocalDate hasta) {
        return registros.stream()
        .filter(r -> !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta))
        .collect(Collectors.groupingBy(
                RegistroTemperatura::getCiudad,
                TreeMap::new, // ciudades ordenadas alfabéticamente
                Collectors.averagingDouble(RegistroTemperatura::getTemperatura)));
    }
}
