package com.xss.sweeky.simulation.task;

import com.xss.sweeky.simulation.car.abs.AbstractCar;
import com.xss.sweeky.simulation.enums.Direction;
import com.xss.sweeky.simulation.graphics.MainFrame;
import com.xss.sweeky.simulation.station.abs.AbstractStation;
import com.xss.sweeky.simulation.time.HourMinuteTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationTask implements Runnable {
    private final List<AbstractStation> stations;
    private final HourMinuteTime time;
    private final MainFrame mainFrame;

    public SimulationTask(List<AbstractStation> stations, HourMinuteTime time, MainFrame mainFrame) {
        this.stations = stations;
        this.time = time;
        this.mainFrame = mainFrame;
        this.mainFrame.initFrame(time, stations);
    }

    @Override
    public void run() {
        updateTime();
        arrivalStation();
        exitStation();
        updateCoordinate();
        generatePassengers();
        refresh();
    }

    private void updateTime() {
        time.updateTime();
    }

    private void arrivalStation() {
        Map<Direction, List<AbstractCar>> arrivalCarMap = new HashMap<>(stations.size());
        for (AbstractStation station : stations) {
            List<AbstractCar> arrivalCars = station.getAndRemoveArrivalCars();
            if (!arrivalCars.isEmpty()) {
                Direction direction = Direction.XI_AN_TO_BAO_JI.equals(station.getDirection()) ?
                        Direction.BAO_JI_TO_XI_AN : Direction.XI_AN_TO_BAO_JI;
                arrivalCarMap.computeIfAbsent(direction, k -> new ArrayList<>()).addAll(arrivalCars);
            }
        }

        if (!arrivalCarMap.isEmpty()) {
            for (AbstractStation station : stations) {
                if (arrivalCarMap.containsKey(station.getDirection())) {
                    station.addArrivalCars(arrivalCarMap.get(station.getDirection()), time);
                }
            }
        }
    }

    private void exitStation() {
        for (AbstractStation station : stations) {
            station.carExitStation(time);
        }
    }

    private void updateCoordinate() {
        for (AbstractStation station : stations) {
            station.updateCoordinate(time);
        }
    }

    private void generatePassengers() {
        for (AbstractStation station : stations) {
            station.generateWaitPassengers(time);
        }
    }

    private void refresh() {
        this.mainFrame.refresh();
    }
}
