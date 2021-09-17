module com.boan.cabinet.clientfx.clientfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires lombok;

    opens com.boan.cabinet.clientfx to javafx.fxml;
    exports com.boan.cabinet.clientfx;
    exports com.boan.cabinet.clientfx.models;
    opens com.boan.cabinet.clientfx.models to javafx.fxml;
}
