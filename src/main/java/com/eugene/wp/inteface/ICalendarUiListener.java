package com.eugene.wp.inteface;

import com.eugene.wp.data.CalendarDay;
import javafx.scene.control.Button;

public interface ICalendarUiListener {
    void closeStage();
    void initWeekBtn(CalendarDay day);
    void addCurrentDayHighlight(int day);
}
