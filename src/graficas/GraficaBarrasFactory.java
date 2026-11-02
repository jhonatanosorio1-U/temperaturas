package graficas;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.Map;

public class GraficaBarrasFactory extends GraficaFactory {

    @Override
    protected JFreeChart crearGrafica(Map<String, Double> datos, String titulo) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        datos.forEach((ciudad, promedio) -> dataset.addValue(promedio, "Promedio", ciudad));

        return ChartFactory.createBarChart(
                titulo,
                "Ciudad",
                "Temperatura promedio (°C)",
                dataset,
                PlotOrientation.VERTICAL,
                false,  // leyenda
                true,   // tooltips
                false); // urls
    }
}