package graficas;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.util.Map;

public abstract class GraficaFactory {

    // Factory Method: cada subclase decide qué tipo de gráfica construye
    protected abstract JFreeChart crearGrafica(Map<String, Double> datos, String titulo);

    // Este método usa el factory method sin saber qué gráfica se creó (polimorfismo)
    public ChartPanel crearPanel(Map<String, Double> datos, String titulo) {
        return new ChartPanel(crearGrafica(datos, titulo));
    }
}
