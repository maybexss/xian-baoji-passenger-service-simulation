package com.xss.sweeky.simulation;

import com.xss.sweeky.simulation.graphics.MainFrame;
import com.xss.sweeky.simulation.station.BaojiStation;
import com.xss.sweeky.simulation.station.XianStation;
import com.xss.sweeky.simulation.station.abs.AbstractStation;
import com.xss.sweeky.simulation.task.SimulationTask;
import com.xss.sweeky.simulation.time.HourMinuteTime;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CarRunningSimulation {
    public static final long DELAY_TIME = 50L;
    private static final long INIT_DELAY_TIME = 0L;
    private static final int XI_AN_VOLVO_NUMBER = 5;
    private static final int XI_AN_IVECO_NUMBER = 15;
    private static final int BAO_JI_VOLVO_NUMBER = 5;
    private static final int BAO_JI_IVECO_NUMBER = 16;
    private static AbstractStation xianStation, baojiStation;
    private static HourMinuteTime time;
    private MainFrame mainFrame;
    private ScheduledExecutorService executorService;

    public CarRunningSimulation() {
        xianStation = XianStation.getInstance(XI_AN_VOLVO_NUMBER, XI_AN_IVECO_NUMBER);
        baojiStation = BaojiStation.getInstance(BAO_JI_VOLVO_NUMBER, BAO_JI_IVECO_NUMBER);
        time = HourMinuteTime.getInstance();
        this.mainFrame = new MainFrame();
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("CarRunningSimulation-%d").daemon(Boolean.TRUE).build());
    }

    private void start() {
        executorService.scheduleWithFixedDelay(new SimulationTask(new ArrayList<>(Arrays.asList(xianStation, baojiStation)), time, mainFrame),
                INIT_DELAY_TIME,
                DELAY_TIME,
                TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        CarRunningSimulation crs = new CarRunningSimulation();
        crs.start();
    }
}
