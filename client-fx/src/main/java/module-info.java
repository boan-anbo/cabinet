module com.boan.cabinet.clientfx.clientfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires lombok;
    requires openapi.java.client.v0;

    opens com.boan.cabinet.clientfx to javafx.fxml;
    exports com.boan.cabinet.clientfx;
    exports com.boan.cabinet.clientfx.models;
    opens com.boan.cabinet.clientfx.models to javafx.fxml;
}
