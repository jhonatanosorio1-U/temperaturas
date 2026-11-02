package graficas;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.util.Map;

public abstract class GraficaFactory {

    protected abstract JFreeChart crearGrafica(Map<String, Double> datos, String titulo);

    public ChartPanel crearPanel(Map<String, Double> datos, String titulo) {
        return new ChartPanel(crearGrafica(datos, titulo));
    }
}
