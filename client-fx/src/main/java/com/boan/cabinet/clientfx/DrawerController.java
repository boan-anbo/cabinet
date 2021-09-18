package com.boan.cabinet.clientfx;
// Import classes:
import org.openapitools.client.*;
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
import org.openapitools.client.api.ExtractPdfControllerApi;
import org.openapitools.client.model.ExtractPdfRequest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DrawerController {

    @FXML
    public ListView<File> listView;

    @FXML
    private TextField initialDir;

    @FXML
    private Label numberOfFilesPicked;

    @FXML
    private Label numberofFilesProcessed;

    @FXML
    private Label numberofCardsProcessed;

    @FXML
    private Label lastSubmittedTime;


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

    public void SendRequestToApi(List<File> files) {
        var request = new ExtractPdfRequest();

        request.setFilePaths(files.stream().map(File::getAbsolutePath).toList());

        var api = new ExtractPdfControllerApi();
        try {
            var result = api.extractAndStorePdf(request);
            this.numberofCardsProcessed.setText(Objects.requireNonNull(result.getSavedCards()).toString());
            this.numberofFilesProcessed.setText(Objects.requireNonNull(result.getSavedFiles()).toString());
            var now = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
            this.lastSubmittedTime.setText(now);
            System.out.println(result.getSavedCards());
        } catch (ApiException e) {
            System.out.println(e);
        }
    }

    public List<File> ShowMultipleFilePicker(Stage stage) {


        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pdf files", "*.pdf")
        );

        List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);

        return selectedFile;
    }

    public void sendRequest(ActionEvent actionEvent) {
        var filesSelected = fileList.stream().toList();
        this.SendRequestToApi(filesSelected);
    }
}
