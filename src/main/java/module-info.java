module es.ieslosmontecillos.appinformes {
    requires javafx.controls;
    requires javafx.fxml;
    requires jasperreports;
    requires java.desktop;
    requires java.logging;
    requires java.sql;
    requires org.hsqldb;


    opens es.ieslosmontecillos.appinformes to javafx.fxml;
    exports es.ieslosmontecillos.appinformes;
}