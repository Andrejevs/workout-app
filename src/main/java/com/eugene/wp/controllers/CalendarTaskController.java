package com.eugene.wp.controllers;

import com.eugene.wp.Storage;
import com.eugene.wp.data.CalendarDay;
import com.eugene.wp.inteface.ICalendarUiListener;
import com.eugene.wp.inteface.ITaskController;
import com.eugene.wp.inteface.IWindows;
import com.eugene.wp.logic.CalendarLogic;
import com.eugene.wp.logic.CalendarLogic.WeekDay;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import static com.eugene.wp.logic.AddTrainingLogic.WorkOutType.REST;
import static com.eugene.wp.logic.AddTrainingLogic.WorkOutType.WORK;

public class CalendarTaskController implements ICalendarUiListener {
    private static final Logger log = LoggerFactory.getLogger(CalendarTaskController.class);
    private final String TODAY_STYLE_BTN_CLASS = "calendar-button-today";
    private final String TODAY_STYLE_TEXT_CLASS = "calendar-text-today";
    private final CalendarLogic logic;
    private final IWindows calendarWindow;

    @FXML
    private Button btnClose;
    @FXML
    private Button btnMon;
    @FXML
    private Button btnTue;
    @FXML
    private Button btnWed;
    @FXML
    private Button btnThu;
    @FXML
    private Button btnFri;
    @FXML
    private Button btnSat;
    @FXML
    private Button btnSun;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnPlay;

    @FXML
    private Label textMon;
    @FXML
    private Label textTue;
    @FXML
    private Label textWed;
    @FXML
    private Label textThu;
    @FXML
    private Label textFri;
    @FXML
    private Label textSat;
    @FXML
    private Label textSun;

    @FXML
    public void initialize() {
        btnClose.setOnAction(event -> logic.exitApp());
        btnAdd.setOnAction(event -> logic.initAddWindow());

        logic.initCurrentDay();
        logic.initWeekDays();

        initActionBtns();
    }

    public CalendarTaskController(IWindows calendarWindow, Storage storage) {
        this.logic = new CalendarLogic(this, storage);
        this.calendarWindow = calendarWindow;
    }

    @Override
    public void closeStage() {
        Platform.runLater(calendarWindow::close);
    }

    @Override
    public void initWeekBtn(CalendarDay day) {
        WeekDay weekDay = day.getName();

        Platform.runLater(() -> {
            switch (weekDay) {
                case MON:
                    initSpecificDay(day, textMon, btnMon);

                    break;
                case TUE:
                    initSpecificDay(day, textTue, btnTue);

                    break;
                case WED:
                    initSpecificDay(day, textWed, btnWed);

                    break;
                case THU:
                    initSpecificDay(day, textThu, btnThu);

                    break;
                case FRI:
                    initSpecificDay(day, textFri, btnFri);

                    break;
                case SAT:
                    initSpecificDay(day, textSat, btnSat);

                    break;
                case SUN:
                    initSpecificDay(day, textSun, btnSun);

                    break;
                default:
                    log.error("Unknown day of week: {}", weekDay);
            }
        });
    }

    @Override
    public void addCurrentDayHighlight(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                setCurrentDayStyle(btnSun, textSun);

                break;
            case Calendar.MONDAY:
                setCurrentDayStyle(btnMon, textMon);

                break;
            case Calendar.TUESDAY:
                setCurrentDayStyle(btnTue, textTue);

                break;
            case Calendar.WEDNESDAY:
                setCurrentDayStyle(btnWed, textWed);

                break;
            case Calendar.THURSDAY:
                setCurrentDayStyle(btnThu, textThu);

                break;
            case Calendar.FRIDAY:
                setCurrentDayStyle(btnFri, textFri);

                break;
            case Calendar.SATURDAY:
                setCurrentDayStyle(btnSat, textSat);

                break;
            default:
                log.error("Unknown day of week: " + day);
        }
    }

    private void setCurrentDayStyle(Button btn, Label text) {
        btn.getStyleClass().clear();
        btn.getStyleClass().add(TODAY_STYLE_BTN_CLASS);

        text.getStyleClass().clear();
        text.getStyleClass().add(TODAY_STYLE_TEXT_CLASS);
    }

    private void initSpecificDay(CalendarDay day, Label weekText, Button btn) {
        EventHandler<ActionEvent> eventHandler = event -> {
            day.setIsWorkDay(!day.getIsWorkDay().get());
            logic.updateCalendarDay(day);
        };

        setWeekBtnText(weekText, day.getIsWorkDay().get());
        setListenerForIsWorkDay(day, weekText, btn);
        btn.setOnAction(eventHandler);
        setPictureSetter(day, btn);
    }

    private void setPictureSetter(CalendarDay calDay, Button btn) {
        boolean isWorkDay = calDay.getIsWorkDay().get();
        String picName = logic.getPicName(isWorkDay);

        btn.setStyle("-fx-background-image: url(" + picName + ");");
    }

    private void setListenerForIsWorkDay(CalendarDay day, Label text, Button btn) {
        day.getIsWorkDay().addListener((observable, oldValue, newValue) -> {
            setWeekBtnText(text, newValue);
            setPictureSetter(day, btn);
        });
    }

    private void setWeekBtnText(Label text, Boolean newValue) {
        if (newValue) {
            text.setText(WORK.toString());
        } else {
            text.setText(REST.toString());
        }
    }

    private void initActionBtns() {
        Platform.runLater(() -> {
            btnAdd.setStyle("-fx-background-image: url(\"pic/newBtn.png\");");
            btnEdit.setStyle("-fx-background-image: url(\"pic/editBtn.png\");");
            btnPlay.setStyle("-fx-background-image: url(\"pic/playBtn.png\");");
        });
    }
}
