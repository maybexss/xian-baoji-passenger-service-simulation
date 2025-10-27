package com.xss.sweeky.simulation.enums;

public enum CarParameter {

    /**
     * 沃尔沃参数
     */
    VOLVO("VOLVO", "V", "沃尔沃", 48, 1.9),

    /**
     * 依维柯参数
     */
    IVECO("IVECO", "I", "依维柯", 21, 1.4);

    /**
     * 类型
     */
    private final String type;

    /**
     * 车牌
     */
    private final String plate;

    /**
     * 车辆类型描述
     */
    private final String description;

    /**
     * 准坐人数
     */
    private final int seats;

    /**
     * 车辆速度(公里/分钟)
     */
    private final double speed;

    CarParameter(String type, String plate, String description, int seats, double speed) {
        this.type = type;
        this.plate = plate;
        this.description = description;
        this.seats = seats;
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public String getPlate() {
        return plate;
    }

    public String getDescription() {
        return description;
    }

    public int getSeats() {
        return seats;
    }

    public double getSpeed() {
        return speed;
    }
}
