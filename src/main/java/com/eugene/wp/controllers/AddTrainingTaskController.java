package com.eugene.wp.controllers;

import com.eugene.wp.Storage;
import com.eugene.wp.data.TaskElement;
import com.eugene.wp.inteface.IAddTrainingController;
import com.eugene.wp.inteface.ITaskController;
import com.eugene.wp.inteface.ITaskItem;
import com.eugene.wp.inteface.IWindows;
import com.eugene.wp.logic.AddTrainingLogic;
import com.eugene.wp.logic.AddTrainingLogic.AddElementAction;
import com.eugene.wp.logic.AddTrainingLogic.WorkOutType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddTrainingTaskController implements IAddTrainingController {
    private final IWindows addTrainingWindow;
    private final AddTrainingLogic logic;

    @FXML
    private Button btnClose;
    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<WorkOutType> comboType;
    @FXML
    private ComboBox<Integer> comboLoop;
    @FXML
    private ComboBox<AddElementAction> comboAdd;

    @FXML
    private VBox vboxAddTrain;

    public AddTrainingTaskController(IWindows addTrainingWindow, Storage storage, int workoutId, int elementId) {
        this.logic = new AddTrainingLogic(this, storage, workoutId, elementId);
        this.addTrainingWindow = addTrainingWindow;
    }

    @FXML
    public void initialize() {
        btnClose.setOnAction(event -> logic.exitApp());
        btnSave.setStyle("-fx-background-image: url(\"pic/saveBtn.png\");");

        initComboBoxes();
    }

    @Override
    public void removeElFromBox(ITaskItem root) {
        Platform.runLater(() -> vboxAddTrain.getChildren().remove(root.getRoot()));
    }

    @Override
    public int getVboxLastIndex() {
        return vboxAddTrain.getChildren().size();
    }

    @Override
    public void addElToBox(ITaskItem root, int index) {
        Platform.runLater(() -> vboxAddTrain.getChildren().add(index, root.getRoot()));
    }

    @Override
    public void closeStage() {
        Platform.runLater(addTrainingWindow::close);
    }

    @Override
    public void cleanScene() {

    };

    @Override
    public void addNewElement(ITaskItem element) {
        Platform.runLater(() -> {
            vboxAddTrain.getChildren().add(element.getIndex(), element.getRoot());

            element.getController().getComboIndex().getSelectionModel().select(element.getIndex());
        });

    }

    @Override
    public void clearAddComboSelection() {
        Platform.runLater(() -> comboAdd.getSelectionModel().clearSelection());
    }

    private void initComboBoxes() {
        comboType.setItems(FXCollections.observableArrayList(WorkOutType.values()));

        comboLoop.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(0,6).boxed().collect(Collectors.toList())));

        comboAdd.setItems(FXCollections.observableArrayList(AddElementAction.values()));
        comboAdd.setOnAction(event -> logic.onAddBtnAction(comboAdd.getSelectionModel().getSelectedItem()));
    }
}
