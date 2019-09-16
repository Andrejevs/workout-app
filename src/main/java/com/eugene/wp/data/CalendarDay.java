package com.eugene.wp.data;

import com.eugene.wp.logic.CalendarLogic.WeekDay;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Data;

@Data
public class CalendarDay {
    private final WeekDay name;
    private final SimpleBooleanProperty isWorkDay;

    public CalendarDay(WeekDay name, boolean isWorkDay) {
        this.name = name;
        this.isWorkDay = new SimpleBooleanProperty(isWorkDay);
    }

    public void setIsWorkDay(boolean isWorkDay) {
        this.isWorkDay.set(isWorkDay);
    }
}
