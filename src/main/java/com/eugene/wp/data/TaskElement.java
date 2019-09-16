package com.eugene.wp.data;

import com.eugene.wp.controllers.AddTrainingElementTaskController;
import com.eugene.wp.inteface.ITaskController;
import com.eugene.wp.inteface.ITaskItem;
import com.eugene.wp.logic.AddTrainingLogic;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Data;

import java.io.IOException;

@Data
public class TaskElement implements ITaskItem {
    private final String ELEMENT_PATH = "/addTrainComponentUi.fxml";
    private final ITaskController controller;
    private final FXMLLoader fxmlLoader;
    private final Parent root;
    private final int id;
    //TODO connect to textField
    private String name = "none";
    private int index;

    public TaskElement(int id, AddTrainingLogic logic, int index, ObservableList<Integer> indexList) throws IOException {
        this.id = id;
        this.index = index;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(ELEMENT_PATH));
        this.controller = new AddTrainingElementTaskController(this, logic, indexList);
        fxmlLoader.setController(controller);
        this.root = fxmlLoader.load();
    }

    public void setIndex(int index) {
        this.index = index;

        controller.getComboIndex().getSelectionModel().select(index);
    }
}
