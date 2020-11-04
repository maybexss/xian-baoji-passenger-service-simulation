package com.xss.sweeky.simulation.enums;

public enum Destination {

    /**
     * 宝鸡站
     */
    BAO_JI("BJ", "宝鸡站", 1, 250D, 70D, 550D, 50D, 20D, 62.5D, 0D),


    /**
     * 虢镇站
     */
    GUO_ZHEN("GZ", "虢镇站", 2, 370D, 70D, 550D, 50D, 20D, 58.5D, 12.5D),


    /**
     * 蔡家坡站
     */
    CAI_JIA_PO("CP", "蔡家坡站", 3, 475D, 70D, 550D, 50D, 20D, 55.5D, 25.5D),


    /**
     * 武功站
     */
    WU_GONG("WG", "武功站", 4, 715D, 70D, 550D, 50D, 20D, 42.5D, 43.5D),


    /**
     * 兴平站
     */
    XING_PING("XP", "兴平站", 5, 890D, 70D, 550D, 50D, 20D, 25.5D, 52.5D),

    /**
     * 咸阳站
     */
    XIAN_YANG("XY", "咸阳站", 6, 1010D, 70D, 550D, 50D, 20D, 13.5D, 58.5D),

    /**
     * 西安站
     */
    XI_AN("XA", "西安站", 7, 1110D, 70D, 550D, 50D, 20D, 0D, 62.5D);

    /**
     * 站点
     */
    private String station;

    /**
     * 站点名称描述
     */
    private String description;

    /**
     * 排序号
     */
    private Integer order;

    /**
     * 站横坐标
     */
    private Double abscissa;

    /**
     * 宝鸡出发纵坐标
     */
    private Double ordinateBaoji;

    /**
     * 西安出发纵坐标
     */
    private Double ordinateXian;

    /**
     * 站台宽度
     */
    private Double width;

    /**
     * 站台高度
     */
    private Double height;

    /**
     * 从西安出发的票价
     */
    private Double priceFromXian;

    /**
     * 从宝鸡出发的票价
     */
    private Double priceFromBaoji;

    Destination(String station, String description, Integer order, Double abscissa,
                Double ordinateBaoji, Double ordinateXian, Double width, Double height,
                Double priceFromXian, Double priceFromBaoji) {
        this.station = station;
        this.description = description;
        this.order = order;
        this.abscissa = abscissa;
        this.ordinateBaoji = ordinateBaoji;
        this.ordinateXian = ordinateXian;
        this.width = width;
        this.height = height;
        this.priceFromXian = priceFromXian;
        this.priceFromBaoji = priceFromBaoji;
    }

    /**
     * 根据车辆行驶方向返回票价
     *
     * @param direction 车辆行驶方向
     * @return 票价
     */
    public Double getPrice(Direction direction) {
        Double price = priceFromBaoji;
        if (Direction.XI_AN_TO_BAO_JI.equals(direction)) {
            price = priceFromXian;
        }

        return price;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Double getAbscissa() {
        return abscissa;
    }

    public void setAbscissa(Double abscissa) {
        this.abscissa = abscissa;
    }

    public Double getOrdinateBaoji() {
        return ordinateBaoji;
    }

    public void setOrdinateBaoji(Double ordinateBaoji) {
        this.ordinateBaoji = ordinateBaoji;
    }

    public Double getOrdinateXian() {
        return ordinateXian;
    }

    public void setOrdinateXian(Double ordinateXian) {
        this.ordinateXian = ordinateXian;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getPriceFromXian() {
        return priceFromXian;
    }

    public void setPriceFromXian(Double priceFromXian) {
        this.priceFromXian = priceFromXian;
    }

    public Double getPriceFromBaoji() {
        return priceFromBaoji;
    }

    public void setPriceFromBaoji(Double priceFromBaoji) {
        this.priceFromBaoji = priceFromBaoji;
    }
}
