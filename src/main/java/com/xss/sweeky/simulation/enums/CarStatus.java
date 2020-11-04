package com.xss.sweeky.simulation.enums;

public enum CarStatus {
    /**
     * 站内
     */
    AT_STATION(-1, "站内"),

    /**
     * 行驶中
     */
    RUNNING(0, "行驶中"),

    /**
     * 停车1分钟
     */
    STOP_ONE_MINUTE(1, "停车一分钟"),

    /**
     * 等待行驶
     */
    WAIT_TO_RUN(2, "等待行驶"),

    /**
     * 车辆到达终点站
     */
    ARRIVAL(3, "到达终点站");

    private int status;

    private String description;

    CarStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
