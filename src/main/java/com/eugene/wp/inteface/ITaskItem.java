package com.eugene.wp.inteface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public interface ITaskItem {
    String getELEMENT_PATH();
    ITaskController getController();
    FXMLLoader getFxmlLoader();
    Parent getRoot();
    String getName();
    int getIndex();
    void setIndex(int index);
}
