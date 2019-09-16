package com.eugene.wp.db;

import com.eugene.wp.data.CalendarDay;
import com.eugene.wp.logic.CalendarLogic;
import com.eugene.wp.logic.CalendarLogic.WeekDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DbRequest {
    private static final Logger log = LoggerFactory.getLogger(DbRequest.class);
    private final String DB_URL = "jdbc:sqlite:wpDb1.db";
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public HashMap<WeekDay, CalendarDay> getCalendarDayMap() throws Exception {
        String sql = "SELECT weekName,isWorkDay FROM calendarDay";

        pstmt = conn.prepareStatement(sql);

        rs = pstmt.executeQuery();

        HashMap<WeekDay, CalendarDay> dayList = new HashMap<>();

        while (rs.next()) {
            String name = rs.getString("weekName");
            boolean isWorkDay = rs.getBoolean("isWorkDay");

            WeekDay day = WeekDay.valueOf(name);

            CalendarDay calendarDay = new CalendarDay(day, isWorkDay);

            dayList.put(day, calendarDay);
        }

        return dayList;
    }

    public void updateCalendarDay(CalendarDay day) throws Exception {
        String sql = "UPDATE calendarDay SET isWorkDay=? WHERE weekName=?";

        pstmt = conn.prepareStatement(sql);

        pstmt.setBoolean(1, day.getIsWorkDay().get());
        pstmt.setString(2, day.getName().toString());

        pstmt.executeUpdate();
    }

    public DbRequest() throws SQLException {
        connect();
    }

    public void disconnect() throws Exception {
        conn.close();
    }

    private void connect() throws SQLException {
        conn = DriverManager.getConnection(DB_URL);
    }
}
