package vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.*;

public class TemperaturasVista extends JFrame {

    // --- Punto 1 (carga de archivo) ---
    private JButton btnCargarArchivo;
    private JLabel lblEstadoArchivo;

    // --- Punto 2 (gráfica de barras por rango de fechas) ---
    private JSpinner spnDesde, spnHasta;
    private JButton btnGraficar;
    private JPanel pnlGrafica; // panel vacío: aquí se debe insertar el ChartPanel/JPanel con la gráfica de barras

    // --- Punto 3 (ciudad más y menos calurosa en una fecha) ---
    private JSpinner spnFechaConsulta;
    private JButton btnBuscarExtremos;
    private JLabel lblCiudadMasCalurosa;
    private JLabel lblCiudadMenosCalurosa;

    public TemperaturasVista() {
        setTitle("Temperaturas por ciudad");
        setSize(650, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(construirPanelCarga(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Promedio por rango de fechas", construirPanelRango());
        tabs.addTab("Ciudad más/menos calurosa", construirPanelFechaUnica());
        add(tabs, BorderLayout.CENTER);
    }

    // Punto 1: panel para cargar el CSV
    private JPanel construirPanelCarga() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnCargarArchivo = new JButton("Cargar archivo CSV");
        lblEstadoArchivo = new JLabel("Ningún archivo cargado");
        panel.add(btnCargarArchivo);
        panel.add(lblEstadoArchivo);
        return panel;
    }

    // Punto 2: lo arma el compañero que calcule promedios y dibuje la gráfica de barras
    private JPanel construirPanelRango() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel pnlControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlControles.add(new JLabel("Desde:"));
        spnDesde = crearSpinnerFecha();
        pnlControles.add(spnDesde);

        pnlControles.add(new JLabel("Hasta:"));
        spnHasta = crearSpinnerFecha();
        pnlControles.add(spnHasta);

        btnGraficar = new JButton("Graficar promedio por ciudad");
        pnlControles.add(btnGraficar);

        pnlGrafica = new JPanel(new BorderLayout());
        pnlGrafica.setBorder(BorderFactory.createTitledBorder("Gráfica"));

        panel.add(pnlControles, BorderLayout.NORTH);
        panel.add(pnlGrafica, BorderLayout.CENTER);
        return panel;
    }

    // Punto 3: lo arma el compañero que busque los extremos por fecha
    private JPanel construirPanelFechaUnica() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel pnlControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlControles.add(new JLabel("Fecha:"));
        spnFechaConsulta = crearSpinnerFecha();
        pnlControles.add(spnFechaConsulta);

        btnBuscarExtremos = new JButton("Buscar ciudad más y menos calurosa");
        pnlControles.add(btnBuscarExtremos);

        JPanel pnlResultado = new JPanel(new GridLayout(2, 1));
        lblCiudadMasCalurosa = new JLabel("Ciudad más calurosa: -");
        lblCiudadMenosCalurosa = new JLabel("Ciudad menos calurosa: -");
        pnlResultado.add(lblCiudadMasCalurosa);
        pnlResultado.add(lblCiudadMenosCalurosa);

        panel.add(pnlControles, BorderLayout.NORTH);
        panel.add(pnlResultado, BorderLayout.CENTER);
        return panel;
    }

    private JSpinner crearSpinnerFecha() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        return spinner;
    }

    // ---------- getters / setters que usará el controlador ----------

    public void setCargarArchivoClick(ActionListener evento) {
        btnCargarArchivo.addActionListener(evento);
    }

    public void setGraficarClick(ActionListener evento) {
        btnGraficar.addActionListener(evento);
    }

    public void setBuscarExtremosClick(ActionListener evento) {
        btnBuscarExtremos.addActionListener(evento);
    }

    public void mostrarEstadoArchivo(String texto) {
        lblEstadoArchivo.setText(texto);
    }

    public LocalDate getFechaDesde() {
        return convertirAFecha((Date) spnDesde.getValue());
    }

    public LocalDate getFechaHasta() {
        return convertirAFecha((Date) spnHasta.getValue());
    }

    public LocalDate getFechaConsulta() {
        return convertirAFecha((Date) spnFechaConsulta.getValue());
    }

    // Punto 2: el compañero mete aquí su JPanel/ChartPanel con la gráfica de barras
    public JPanel getPanelGrafica() {
        return pnlGrafica;
    }

    // Punto 3: el compañero solo llama a estos dos setters con el resultado
    public void mostrarCiudadMasCalurosa(String ciudad) {
        lblCiudadMasCalurosa.setText("Ciudad más calurosa: " + ciudad);
    }

    public void mostrarCiudadMenosCalurosa(String ciudad) {
        lblCiudadMenosCalurosa.setText("Ciudad menos calurosa: " + ciudad);
    }

    private LocalDate convertirAFecha(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
