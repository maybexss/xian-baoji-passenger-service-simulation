package com.xss.sweeky.simulation.enums;

public enum CarStatus {
    /**
     * 站内
     */
    AT_STATION(-1),

    /**
     * 行驶中
     */
    RUNNING(0),

    /**
     * 停车1分钟
     */
    STOP_ONE_MINUTE(1),

    /**
     * 等待行驶
     */
    WAIT_TO_RUN(2),

    /**
     * 车辆到达终点站
     */
    ARRIVAL(3);

    private final int status;

    CarStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
