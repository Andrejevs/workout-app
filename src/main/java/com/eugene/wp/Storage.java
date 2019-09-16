package com.eugene.wp;

import com.eugene.wp.data.CalendarDay;
import com.eugene.wp.inteface.ITaskItem;
import com.eugene.wp.inteface.IWindows;
import com.eugene.wp.logic.CalendarLogic.WeekDay;
import javafx.scene.Parent;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class Storage {
    private final ConcurrentHashMap<WeekDay, CalendarDay> weekDays = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, IWindows> windowsList = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Parent, ITaskItem> tempItemList = new ConcurrentHashMap<>();

    public void addWeekDay(CalendarDay day) {
        weekDays.put(day.getName(), day);
    }

    public void addWindow(IWindows windows) {
        windowsList.put(windows.getTITLE_NAME(), windows);
    }

    public void addItemToTempList(ITaskItem element) {
        tempItemList.put(element.getRoot(), element);
    }
}


