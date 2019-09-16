package com.eugene.wp.windows;

import com.eugene.wp.Storage;
import com.eugene.wp.controllers.AddTrainingTaskController;
import com.eugene.wp.inteface.IAddTrainingController;
import com.eugene.wp.inteface.IWindows;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Data
public class AddTrainingWindow implements IWindows {
    private static final Logger log = LoggerFactory.getLogger(AddTrainingWindow.class);
    private final String ADD_TRAINING_UI_PATH;
    private final String TITLE_NAME;
    private Stage stage;
    private final FXMLLoader fxmlLoader;
    private final IAddTrainingController addTrainingController;
    private Scene scene;
    private double offsetX = 0;
    private double offsetY = 0;

    public AddTrainingWindow(String ADD_TRAINING_UI_PATH, String TITLE_NAME, Stage stage, Storage storage,
                             int workoutId, int elementId)
            throws IOException {
        this.ADD_TRAINING_UI_PATH = ADD_TRAINING_UI_PATH;
        this.TITLE_NAME = TITLE_NAME;
        this.stage = stage;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(ADD_TRAINING_UI_PATH));
        this.addTrainingController = new AddTrainingTaskController(this, storage, workoutId, elementId);

        initWindow();
    }

    private void initWindow() throws IOException {

        fxmlLoader.setController(addTrainingController);

        scene = new Scene(fxmlLoader.load());

        handlersRegistration(stage, scene.getRoot());
        showStageWithSettings(stage, scene);

        log.debug("init Add training window");
    }

    @Override
    public void showStageWithSettings(Stage primaryStage, Scene scene) {
        scene.getStylesheets().add(STYLE_PATH);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE_NAME);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
    }

    @Override
    public void handlersRegistration(Stage primaryStage, Parent root) {
        root.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - offsetX);
            primaryStage.setY(event.getScreenY() - offsetY);
        });
    }

    @Override
    public void show() {
        addTrainingController.cleanScene();

        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }
}
