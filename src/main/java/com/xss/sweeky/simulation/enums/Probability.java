package com.xss.sweeky.simulation.enums;

public enum Probability {
    /**
     * 西安->宝鸡
     */
    XI_AN_TO_BAO_JI(65, 99, Destination.BAO_JI),

    /**
     * 西安->虢镇
     */
    XI_AN_TO_GUO_ZHEN(25, 40, Destination.GUO_ZHEN),

    /**
     * 西安->蔡家坡
     */
    XI_AN_TO_CAI_JIA_PO(15, 24, Destination.CAI_JIA_PO),

    /**
     * 西安->武功
     */
    XI_AN_TO_WU_GONG(7, 14, Destination.WU_GONG),

    /**
     * 西安->兴平
     */
    XI_AN_TO_XING_PING(0, 6, Destination.XING_PING),

    /**
     * 西安->咸阳
     */
    XI_AN_TO_XIAN_YANG(41, 64, Destination.XIAN_YANG),

    /**
     * 宝鸡->西安
     */
    BAO_JI_TO_XI_AN(60, 99, Destination.XI_AN),

    /**
     * 宝鸡->咸阳
     */
    BAO_JI_TO_XIAN_YANG(35, 59, Destination.XIAN_YANG),

    /**
     * 宝鸡->兴平
     */
    BAO_JI_TO_XING_PING(20, 34, Destination.XING_PING),

    /**
     * 宝鸡->武功
     */
    BAO_JI_TO_WU_GONG(9, 19, Destination.WU_GONG),

    /**
     * 宝鸡->蔡家坡
     */
    BAO_JI_TO_CAI_JIA_PO(5, 8, Destination.CAI_JIA_PO),

    /**
     * 宝鸡->虢镇
     */
    BAO_JI_TO_GUO_ZHEN(0, 4, Destination.GUO_ZHEN);

    /**
     * 概率起始值
     */
    private final int from;

    /**
     * 概率结束值
     */
    private final int to;

    /**
     * 目的地
     */
    private final Destination destination;

    Probability(int from, int to, Destination destination) {
        this.from = from;
        this.to = to;
        this.destination = destination;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Destination getDestination() {
        return destination;
    }
}
