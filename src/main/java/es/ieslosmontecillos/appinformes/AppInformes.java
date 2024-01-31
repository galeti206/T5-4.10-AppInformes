package es.ieslosmontecillos.appinformes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author profesora
 */
public class AppInformes extends Application {
    public final String LISTADO_FACTURAS = "Informes/Listado_Facturas.jasper";
    public final String VENTAS_TOTALES = "Informes/Ventas_Totales.jasper";
    public final String FACTURAS_POR_CLIENTE = "Informes/Facturas_por_Cliente.jasper";
    public final String SUBINFORME_LISTADO_FACTURAS = "Informes/Subinforme_Listado_Facturas.jasper";
    public final String SUBINFORME = "Informes/Subinforme4.10.jasper";

    public static Connection conexion = null;
    @FXML
    private TextField txtIdCliente;

    @Override
    public void start(Stage primaryStage) {
        // Establecemos la conexion con la BD
        conectaBD();

        // Creamos la escena
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appInformesView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);

            primaryStage.setTitle("Aplicacion de informes");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void stop() throws Exception {
        try {
            DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/test;shutdown=true");
        } catch (Exception ex) {
            System.out.println("No se pudo cerrar la conexion a la BD");
            ex.printStackTrace();
        }
    }

    public void conectaBD(){
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost/test";
        String usuario = "SA";
        String clave = "";
        try{
            //Class.forName("org.hsqldb.jdbcDriver").newInstance();
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            conexion = DriverManager.getConnection(baseDatos,usuario,clave);
        }
        catch (ClassNotFoundException cnfe){
            System.err.println("Fallo al cargar JDBC");
            cnfe.printStackTrace();
            System.exit(1);
        }
        catch (SQLException sqle){
            System.err.println("No se pudo conectar a BD");
            sqle.printStackTrace();
            System.exit(1);
        }
        catch (Exception ex){
            System.err.println("Imposible Conectar");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    public void generaFacturas1y2(String nombre_Informe) {
        try {
            JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(nombre_Informe));

            //Map de parámetros
            Map parametros = new HashMap();

            // Ya tenemos los datos para instanciar un objeto JasperPrint que permite ver,
            // Imprimir o exportar a otros formatos
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaFacturasporCliente(String nombre_Informe) {
        try {
            JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(nombre_Informe));

            //Map de parámetros
            Map parametros = new HashMap();

            // Coje el texto del cuadro, lo parsea a Entero y lo usa como parámetro para generar el informe
            String txtField = txtIdCliente.getText();
            if (txtField == ""){
                System.err.println("No se ha introducido ningún número");
            }
            else{
                int nCliente = Integer.valueOf(txtField);
                parametros.put("NUMERO_CLIENTE", nCliente);
            }

            // Ya tenemos los datos para instanciar un objeto JasperPrint que permite ver,
            // Imprimir o exportar a otros formatos
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaSubinforme(){
        try{
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource(SUBINFORME_LISTADO_FACTURAS));
            JasperReport jsr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource(SUBINFORME));

            //Map de parámetros
            Map parametros = new HashMap();
            parametros.put("subReportParameter", jsr);

            //Ya tenemos los datos para instanciar un objeto JasperPrint que permite ver, //imprimir o exportar a otros formatos
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);

        }catch (JRException ex){
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaListado_Facturas(){
        try{
            generaFacturas1y2(LISTADO_FACTURAS);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public void generaVentas_Totales(){
        try{
            generaFacturas1y2(VENTAS_TOTALES);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public void generaFacturas_por_Cliente(){
        try{
            generaFacturasporCliente(FACTURAS_POR_CLIENTE);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public void ventanaAceptar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("introduceIdView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(AppInformes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
