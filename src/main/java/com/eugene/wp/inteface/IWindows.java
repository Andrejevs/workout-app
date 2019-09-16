package com.eugene.wp.inteface;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public interface IWindows {
    static final String STYLE_PATH = "style.css";

    String getTITLE_NAME();
    void showStageWithSettings(Stage primaryStage, Scene scene);
    void handlersRegistration(Stage primaryStage, Parent root);
    void show();
    void close();
}
