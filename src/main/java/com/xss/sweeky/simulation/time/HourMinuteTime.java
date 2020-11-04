package com.xss.sweeky.simulation.time;

import com.xss.sweeky.simulation.CarRunningSimulation;

public class HourMinuteTime {
    private static final int START_TIME_HOUR = 8;
    private static final int MAX_TIME_HOUR = 24;
    private static final int MAX_TIME_MINUTE = 60;
    private static final int LAST_TIME_HOUR = 17;
    private static final int LAST_TIME_MINUTE = 59;
    private static final long COUNT_MINUS = 1000 / CarRunningSimulation.DELAY_TIME;

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
        this.hour = START_TIME_HOUR - 1;
        this.minute = LAST_TIME_MINUTE - 5;
    }

    public static synchronized HourMinuteTime getInstance() {
        if (time == null) {
            time = new HourMinuteTime();
        }

        return time;
    }

    /**
     * 当日是否处在非发车时间段内
     *
     * @return 判断结果
     */
    public boolean illegalDepartTime() {
        boolean upperResult = (hour > LAST_TIME_HOUR + 1) || (hour == (LAST_TIME_HOUR + 1) && minute > 0);
        boolean lowerResult = hour < START_TIME_HOUR;

        boolean illegalTime = count % COUNT_MINUS != 0;

        return upperResult || lowerResult || illegalTime;
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
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        StringBuilder sb = new StringBuilder();
        if (hourStr.length() <= 1) {
            sb.append("0");
        }
        sb.append(hourStr).append(":");
        if (minuteStr.length() <= 1) {
            sb.append("0");
        }
        sb.append(minuteStr);

        return sb.toString();
    }

    public void updateTime() {
        count++;
        if (count % COUNT_MINUS == 0) {
            minute += 1;
            hour += minute / MAX_TIME_MINUTE;
            hour %= MAX_TIME_HOUR;
            minute %= MAX_TIME_MINUTE;
            count %= COUNT_MINUS;
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
