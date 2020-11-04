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
    private String type;

    /**
     * 车牌
     */
    private String plate;

    /**
     * 车辆类型描述
     */
    private String description;

    /**
     * 准坐人数
     */
    private int seats;

    /**
     * 车辆速度(公里/分钟)
     */
    private double speed;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
