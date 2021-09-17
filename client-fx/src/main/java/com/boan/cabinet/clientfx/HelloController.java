package com.boan.cabinet.clientfx;

import com.boan.cabinet.clientfx.models.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private TextField initialDir;
    @FXML
    private Label filesPicked;


    private List<String> fileList;
    @FXML
    private Request request;

    @FXML
    public void initialize() {
        initialDir.setText("C:\\");

        fileList = new ArrayList<>();
        fileList.add("Hello");

        request = new Request( FXCollections.observableArrayList(fileList));

        filesPicked.textProperty().bind(request.filesPaths.sizeProperty().asString());
    }

    @FXML
    protected void pickFilesToSend(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        var selectedFiles = ShowMultipleFilePicker(stage);


        System.out.println(selectedFiles.size());

        var filePaths = selectedFiles.stream().map(File::getAbsolutePath).toList();
        request.filesPaths.addAll(filePaths);
//        filesPicked.setText("Welcome to JavaFX Application!");
    }

    public List<File> ShowMultipleFilePicker(Stage stage) {


        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pdf Files", "*.pdf")
        );

        List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);
        return selectedFile;
    }
}
