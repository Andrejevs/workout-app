package com.eugene.wp.data;

import com.eugene.wp.controllers.AddTrainingWorkoutTaskController;
import com.eugene.wp.inteface.ITaskController;
import com.eugene.wp.inteface.ITaskItem;
import com.eugene.wp.logic.AddTrainingLogic;
import com.eugene.wp.logic.AddTrainingLogic.WorkOutType;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkOut implements ITaskItem {
    private final int id;
    private List<ITaskItem> elementList;
    private String name;
    private int loop;
    private WorkOutType type;
    private int restSec;
    //need for complex workouts
    private boolean separate = true;

    private String ELEMENT_PATH = "/addTrainComponentUi.fxml";
    private ITaskController controller;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int index;

    public WorkOut(int id, AddTrainingLogic logic, int index, ObservableList<Integer> indexList) throws IOException {
        this.id = id;
        this.index = index;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(ELEMENT_PATH));
        this.controller = new AddTrainingWorkoutTaskController(this, logic, indexList);
        fxmlLoader.setController(controller);
        this.root = fxmlLoader.load();
        this.elementList = new ArrayList<>();
    }

    public WorkOut(int id, List<ITaskItem> elementList, String name, int loop, WorkOutType type) {
        this.elementList = elementList;
        this.id = id;
        this.name = name;
        this.loop = loop;
        this.type = type;
    }
}
