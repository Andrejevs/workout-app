package com.eugene.wp.windows;

import com.eugene.wp.Storage;
import com.eugene.wp.controllers.CalendarTaskController;
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
public class CalendarWindow implements IWindows {
    private static final Logger log = LoggerFactory.getLogger(CalendarWindow.class);
    private final String CALENDAR_UI_PATH;
    private final String TITLE_NAME;
    private final Stage stage;
    private final FXMLLoader fxmlLoader;
    private final CalendarTaskController calendarController;
    private Scene scene;
    private double offsetX = 0;
    private double offsetY = 0;

    public CalendarWindow(String CALENDAR_UI_PATH, String TITLE_NAME, Stage stage, Storage storage)
            throws IOException {
        this.CALENDAR_UI_PATH = CALENDAR_UI_PATH;
        this.TITLE_NAME = TITLE_NAME;
        this.stage = stage;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(CALENDAR_UI_PATH));
        this.calendarController = new CalendarTaskController(this, storage);

        initWindow();
    }

    private void initWindow() throws IOException {
        fxmlLoader.setController(calendarController);

        scene = new Scene(fxmlLoader.load());

        handlersRegistration(stage, scene.getRoot());
        showStageWithSettings(stage, scene);

        log.debug("init Calendar window");
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void showStageWithSettings(Stage primaryStage, Scene scene) {
        scene.getStylesheets().add(STYLE_PATH);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE_NAME);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.show();
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
}
