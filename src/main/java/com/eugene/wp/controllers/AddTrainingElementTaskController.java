package com.eugene.wp.controllers;

import com.eugene.wp.data.TaskElement;
import com.eugene.wp.inteface.ITaskController;
import com.eugene.wp.logic.AddTrainingLogic;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import lombok.Data;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class AddTrainingElementTaskController implements ITaskController {
    private final TaskElement element;
    private final AddTrainingLogic logic;
    private ObservableList<Integer> indexList;

    @FXML
    private Button btnCloseEl;

    @FXML
    private ComboBox<Integer> comboIndex;
    @FXML
    private ComboBox<Integer> comboTime;


    public AddTrainingElementTaskController(TaskElement element, AddTrainingLogic logic,
                                            ObservableList<Integer> indexList) {
        this.element = element;
        this.logic = logic;
        this.indexList = indexList;
    }

    public void initialize() {
        btnCloseEl.setOnAction(event -> logic.removeEl(element));

        comboIndex.getItems().addAll(indexList);
        comboIndex.setOnAction(event -> onChangeComboIndex());

        comboTime.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 30).boxed().collect(Collectors.toList())));

        indexList.addListener(getIndexLustListener());
    }

    public void changeComboIndexValue(int index) {
        Platform.runLater(() -> comboIndex.getSelectionModel().select(index));
    }

    @Override
    public ComboBox<Integer> getComboIndex() {
        return comboIndex;
    }

    private void onChangeComboIndex() {
        Integer newIndex = comboIndex.getSelectionModel().getSelectedItem();
        logic.onChangeComboIndex(newIndex, element);
    }

    private InvalidationListener getIndexLustListener() {
        return observable -> {
                comboIndex.getItems().clear();
                comboIndex.getItems().addAll(indexList);

                comboIndex.getSelectionModel().select(element.getIndex());
            };
    }
}
