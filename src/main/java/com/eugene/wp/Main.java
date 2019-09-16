package com.eugene.wp;

import com.eugene.wp.windows.AddTrainingWindow;
import com.eugene.wp.windows.CalendarWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    public static final String ADD_TRAINING_TITLE_NAME = "ADD_TRAINING";
    public static final String CALENDAR_TITLE_NAME = "CALENDAR";

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String CALENDAR_UI_PATH = "/calendarUI.fxml";
    private static final String ADD_TRAINING_UI_PATH = "/addTrainingUi.fxml";
    private double offsetX = 0;
    private double offsetY = 0;

    public Main() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> log.error("Error: {}", e.getMessage(), e));

        Storage storage = new Storage();

        CalendarWindow calendarWindow =
                new CalendarWindow(CALENDAR_UI_PATH, CALENDAR_TITLE_NAME, primaryStage, storage);

        //TODO should use db for ids
        AddTrainingWindow addTrainingWindow =
                new AddTrainingWindow(ADD_TRAINING_UI_PATH, ADD_TRAINING_TITLE_NAME, new Stage(), storage,
                        0, 0);

        storage.addWindow(calendarWindow);
        storage.addWindow(addTrainingWindow);

        calendarWindow.show();
    }
}
