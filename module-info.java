module com.example.smartwastesegregator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // If using javafx.application
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;
    requires javafx.web;
    // You don't need to declare javax.mail here as it isn't modularized
    opens com.example.smartwastesegregator to javafx.fxml;
    exports com.example.smartwastesegregator;
}
