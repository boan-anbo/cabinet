package com.boan.cabinet.clientfx;

import com.boan.cabinet.clientfx.models.Request;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DrawerController {

    @FXML
    public ListView<File> listView;
    @FXML
    private TextField initialDir;
    @FXML
    private Label numberOfFilesPicked;


    private final SimpleListProperty<File> fileList = new SimpleListProperty<File>(
            FXCollections.observableArrayList(
                    new ArrayList<File>()
            ));
    @FXML
    private Request request;

    @FXML
    public void initialize() {
        initialDir.setText("C:\\");

        listView.setItems(fileList);
        numberOfFilesPicked.textProperty().bind(fileList.sizeProperty().asString());
    }

    @FXML
    protected void pickFilesToSend(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var selectedFiles = ShowMultipleFilePicker(stage);

        if (selectedFiles != null) {
            fileList.addAll(selectedFiles);
        }

    }


    public List<File> ShowMultipleFilePicker(Stage stage) {


        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
        );

        List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);

        return selectedFile;
    }
}
