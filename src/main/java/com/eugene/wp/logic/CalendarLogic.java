package com.eugene.wp.logic;

import com.eugene.wp.Main;
import com.eugene.wp.Storage;
import com.eugene.wp.data.CalendarDay;
import com.eugene.wp.db.DbRequest;
import com.eugene.wp.inteface.ICalendarUiListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CalendarLogic {
    private static final Logger log = LoggerFactory.getLogger(CalendarLogic.class);
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private final int WORK_PIC_COUNT = 4;
    private final int REST_PIC_COUNT = 1;
    private final String PIC_WORK_PATH = "pic/work_";
    private final String PIC_REST_PATH = "pic/rest_";
    private final String PIC_FORMAT = ".jpg";
    private final ICalendarUiListener listener;
    private final Storage storage;
    private final DbRequest db;

    public enum WeekDay {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    public CalendarLogic(ICalendarUiListener listener, Storage storage) {
        this.listener = listener;
        this.storage = storage;
        this.db = initDb();
    }

    public void exitApp() {
        log.debug("Close APP btn was pressed");

        service.execute(() -> {
            try {
                db.disconnect();
            } catch (Exception e) {
                log.error("Error to close DB connection. Error: {}", e.getMessage(), e);
            } finally {
                service.shutdown();
            }

            service.shutdown();
        });

        storage.getWindowsList().get(Main.ADD_TRAINING_TITLE_NAME).close();
        listener.closeStage();
    }

    public void initAddWindow() {
        storage.getWindowsList().get(Main.ADD_TRAINING_TITLE_NAME).show();
    }

    public void initWeekDays() {
        log.debug("initialize week days");

        try {
            HashMap<WeekDay, CalendarDay> dayMap = db.getCalendarDayMap();

            for (WeekDay day : WeekDay.values()) {
                CalendarDay calDay = dayMap.get(day);

                storage.addWeekDay(calDay);

                listener.initWeekBtn(calDay);
            }
        } catch (Exception e) {
            log.error("Error to get calendar map from db. Error: {}", e.getMessage(), e);
        }
    }

    public String getPicName(boolean isWorkDay) {
        if (isWorkDay) {
            int randomInt = new Random().nextInt(WORK_PIC_COUNT) + 1;

            return PIC_WORK_PATH + randomInt + PIC_FORMAT;
        }

        int randomInt = new Random().nextInt(REST_PIC_COUNT) + 1;

        return PIC_REST_PATH + randomInt + PIC_FORMAT;
    }

    public void initCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        listener.addCurrentDayHighlight(day);
    }

    public void updateCalendarDay(CalendarDay day) {
        service.execute(() -> {
            String name = day.getName().toString();

            try {
                db.updateCalendarDay(day);
                log.debug("The {} was updated to {}.", name, day.getIsWorkDay().get());
            } catch (Exception e) {
                log.error("Error to update Calendar day: {}. Error: {}", name, e.getMessage(), e);
            }
        });
    }

    private DbRequest initDb() {
        DbRequest db = null;

        try {
            db = new DbRequest();
            log.debug("Connect to DB");
        } catch (SQLException e) {
            log.error("Can't connect db. Error: {}", e.getMessage(), e);
            exitApp();
        }

        return db;
    }
}
