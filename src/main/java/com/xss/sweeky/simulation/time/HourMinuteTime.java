package com.xss.sweeky.simulation.time;

import com.xss.sweeky.simulation.CarRunningSimulation;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

public class HourMinuteTime {
    private static final int START_TIME_HOUR = 8;
    private static final int MAX_TIME_HOUR = 24;
    private static final int MAX_TIME_MINUTE = 60;
    private static final int LAST_TIME_HOUR = 17;
    private static final int LAST_TIME_MINUTE = 55;
    private static final long COUNT_MINUS = 1000 / CarRunningSimulation.DELAY_TIME;

    /**
     * 时间实例
     */
    private final Calendar calendar;

    /**
     * 时
     */
    private int hour;

    /**
     * 分
     */
    private int minute;

    /**
     * 计数器, 为1后分针加1, 再归零
     */
    private int count = 0;

    private static HourMinuteTime time;

    private HourMinuteTime() {
        this.calendar = Calendar.getInstance();
        this.hour = START_TIME_HOUR - 1;
        this.minute = LAST_TIME_MINUTE;
    }

    public static synchronized HourMinuteTime getInstance() {
        if (time == null) {
            time = new HourMinuteTime();
        }

        return time;
    }

    /**
     * 当日是否处在发车时间段内
     *
     * @return 判断结果
     */
    public boolean legalDepartTime() {
        boolean upperResult = (hour > LAST_TIME_HOUR + 1) || (hour == (LAST_TIME_HOUR + 1) && minute > 0);
        boolean lowerResult = hour < START_TIME_HOUR;

        boolean illegalTime = count % COUNT_MINUS != 0;

        return !upperResult && !lowerResult && !illegalTime;
    }

    /**
     * 是否再生成乘客时间段
     *
     * @return 判断结果
     */
    public boolean legalGeneratePassengerTime() {
        boolean upperResult = (hour == LAST_TIME_HOUR && minute > 35) || (hour > LAST_TIME_HOUR);
        boolean lowerResult = (hour < START_TIME_HOUR - 1) || (hour == START_TIME_HOUR - 1 && minute < LAST_TIME_MINUTE - 10);

        boolean illegalTime = count % COUNT_MINUS != 0;

        return !upperResult && !lowerResult && !illegalTime;
    }

    /**
     * 是否是最后一个发车批次
     *
     * @return 判断结果
     */
    public boolean lastDepartTime() {
        return hour == LAST_TIME_HOUR && minute >= 20;
    }

    /**
     * 当前时间片是否合法
     *
     * @return 是否合法时间片
     */
    public boolean legalTime() {
        return count % COUNT_MINUS == 0;
    }

    /**
     * 应该删除乘客信息
     *
     * @return 判断结果
     */
    public boolean shouldRemovePassengerInfo() {
        return count % (COUNT_MINUS / 2) == 0;
    }

    /**
     * 获取当前时间, 用于展示
     *
     * @return 当前时间 xx:xx
     */
    public String currentTime() {
        return this.calendar.get(Calendar.YEAR) + "/" +
                StringUtils.leftPad(String.valueOf(this.calendar.get(Calendar.MONTH) + 1), 2, "0") + "/" +
                StringUtils.leftPad(String.valueOf(this.calendar.get(Calendar.DAY_OF_MONTH)), 2, "0") +
                " " +
                StringUtils.leftPad(String.valueOf(this.hour), 2, "0") + ":" +
                StringUtils.leftPad(String.valueOf(this.minute), 2, "0") + ":" +
                "00";
    }

    public void updateTime() {
        count++;
        if (count % COUNT_MINUS == 0) {
            minute += 1;
            hour += minute / MAX_TIME_MINUTE;
            int dayDelta = hour / MAX_TIME_HOUR;
            calendar.add(Calendar.DAY_OF_MONTH, dayDelta);
            hour %= MAX_TIME_HOUR;
            minute %= MAX_TIME_MINUTE;
            count %= (int) COUNT_MINUS;
        }
    }

    public int getMinute() {
        return minute;
    }
}
