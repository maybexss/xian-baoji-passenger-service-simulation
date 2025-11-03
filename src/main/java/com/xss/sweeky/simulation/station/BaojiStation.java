package com.xss.sweeky.simulation.station;

import com.xss.sweeky.simulation.enums.Direction;
import com.xss.sweeky.simulation.station.abs.AbstractStation;

public class BaojiStation extends AbstractStation {
    /**
     * 汽车站名称
     */
    public static final String STATION_NAME = "宝鸡汽车站";

    /**
     * 初始化沃尔沃横坐标
     */
    private static final Double INIT_VOLVO_ABSCISSA = 200D;

    /**
     * 沃尔沃增量横坐标
     */
    private static final Double INCREMENT_VOLVO_ABSCISSA = -40D;

    /**
     * 初始化沃尔沃纵坐标
     */
    private static final Double INIT_VOLVO_ORDINATE = 120D;

    /**
     * 初始化依维柯横坐标
     */
    private static final Double INIT_IVECO_ABSCISSA = 200D;

    /**
     * 依维柯增量横坐标
     */
    private static final Double INCREMENT_IVECO_ABSCISSA = -40D;

    /**
     * 初始化依维柯纵坐标
     */
    private static final Double INIT_IVECO_ORDINATE = 190D;

    /**
     * 依维柯增量纵坐标
     */
    private static final Double INCREMENT_IVECO_ORDINATE = 40D;

    private static AbstractStation baojiStation;

    private BaojiStation(int volvoNumber, int ivecoNumber) {
        super(STATION_NAME, Direction.BAO_JI_TO_XI_AN, volvoNumber, ivecoNumber,
                INIT_VOLVO_ABSCISSA, INCREMENT_VOLVO_ABSCISSA, INIT_VOLVO_ORDINATE,
                INIT_IVECO_ABSCISSA, INCREMENT_IVECO_ABSCISSA, INIT_IVECO_ORDINATE, INCREMENT_IVECO_ORDINATE);
    }

    public static synchronized AbstractStation getInstance(int volvoNumber, int ivecoNumber) {
        if (baojiStation == null) {
            baojiStation = new BaojiStation(volvoNumber, ivecoNumber);
        }

        return baojiStation;
    }

}
