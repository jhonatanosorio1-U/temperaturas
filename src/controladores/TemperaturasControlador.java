package controladores;

import modelos.RegistroTemperatura;
import servicios.TemperaturasServicio;
import vistas.TemperaturasVista;
import graficas.GraficaFactory;
import graficas.GraficaBarrasFactory;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.awt.BorderLayout;
import java.util.Map;

import org.jfree.chart.ChartPanel;

public class TemperaturasControlador {

    private final TemperaturasVista vista;
    private List<RegistroTemperatura> registros;

    public TemperaturasControlador(TemperaturasVista vista) {
        this.vista = vista;

        this.vista.setCargarArchivoClick(evento -> cargarArchivo());
        this.vista.setGraficarClick(evento -> graficarPromedioPorRango());
        this.vista.setBuscarExtremosClick(evento -> buscarExtremosPorFecha());
    }

    // Punto 1: lo que me correspondió a mí
    private void cargarArchivo() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Selecciona el archivo de temperaturas (CSV)");
        selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV", "csv"));

        int resultado = selector.showOpenDialog(vista);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = selector.getSelectedFile().getAbsolutePath();
            registros = TemperaturasServicio.cargarDatos(ruta);

            if (registros.isEmpty()) {
                vista.mostrarEstadoArchivo("No se pudo leer el archivo o está vacío.");
            } else {
                var ciudades = TemperaturasServicio.getCiudades(registros);
                vista.mostrarEstadoArchivo(registros.size() + " registros cargados de "
                        + ciudades.size() + " ciudades.");
            }
        }
    }

    // Punto 2: PENDIENTE — lo completa el compañero encargado del promedio por rango
    // y la gráfica de barras. Ver explicación aparte.
    // Punto 2: promedio por ciudad en un rango de fechas + gráfica de barras
    private void graficarPromedioPorRango() {
        if (registros == null || registros.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Primero carga el archivo CSV.");
            return;
        }

        LocalDate desde = vista.getFechaDesde();
        LocalDate hasta = vista.getFechaHasta();

        if (desde.isAfter(hasta)) {
            JOptionPane.showMessageDialog(vista, "La fecha \"Desde\" no puede ser posterior a \"Hasta\".");
            return;
        }

        Map<String, Double> promedios =
                TemperaturasServicio.promedioPorCiudadEnRango(registros, desde, hasta);

        if (promedios.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay datos en el rango seleccionado.");
            return;
        }

        // Tipo padre, objeto hijo: si mañana hay otra fábrica, solo cambia esta línea
        GraficaFactory fabrica = new GraficaBarrasFactory();
        ChartPanel panel = fabrica.crearPanel(promedios,
                "Promedio de temperatura por ciudad (" + desde + " a " + hasta + ")");

        JPanel contenedor = vista.getPanelGrafica();
        contenedor.removeAll(); // quita la gráfica anterior
        contenedor.add(panel, BorderLayout.CENTER); // pnlGrafica ya tiene BorderLayout
        contenedor.revalidate();
        contenedor.repaint();
    }

    
    private void buscarExtremosPorFecha() {
        if (registros == null || registros.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Primero carga el archivo CSV.");
            return;
        }

        LocalDate fechaConsulta = vista.getFechaConsulta();

        String masCalurosa = TemperaturasServicio.ciudadMasCalurosa(registros, fechaConsulta);
        String menosCalurosa = TemperaturasServicio.ciudadMenosCalurosa(registros, fechaConsulta);

        vista.mostrarCiudadMasCalurosa(masCalurosa);
        vista.mostrarCiudadMenosCalurosa(menosCalurosa);
    }
    

    // Por si los compañeros necesitan los datos ya cargados desde otro lado
    public List<RegistroTemperatura> getRegistros() {
        return registros;
    }
}
