package com.xss.sweeky.simulation.graphics;

import com.xss.sweeky.simulation.car.abs.AbstractCar;
import com.xss.sweeky.simulation.enums.CarParameter;
import com.xss.sweeky.simulation.enums.Destination;
import com.xss.sweeky.simulation.station.BaojiStation;
import com.xss.sweeky.simulation.station.XianStation;
import com.xss.sweeky.simulation.station.abs.AbstractStation;
import com.xss.sweeky.simulation.time.HourMinuteTime;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DrawShape {
    private static final int MAX_PASSENGER_INFO_LINE = 8;
    private HourMinuteTime time;
    private java.util.List<AbstractStation> stations;

    public DrawShape(HourMinuteTime time, java.util.List<AbstractStation> stations) {
        this.time = time;
        this.stations = stations;
    }

    public void paintRoad(Graphics g) {
        g.setColor(Color.ORANGE);
        g.drawRect(Destination.BAO_JI.getAbscissa().intValue(), Destination.BAO_JI.getOrdinateBaoji().intValue(), 174 * 5 + 40, 20);
        g.drawRect(Destination.BAO_JI.getAbscissa().intValue(), 287, 174 * 5 + 40, 17);
        for (int i = 0; i < (174 * 5 + 40) / 60; i++) {
            g.setColor(Color.GREEN);
            g.fillArc(Destination.BAO_JI.getAbscissa().intValue() + 30 + 60 * i, 289, 15, 15, 0, 360);
        }
        g.setColor(Color.ORANGE);
        g.drawRect(Destination.BAO_JI.getAbscissa().intValue(), Destination.XI_AN.getOrdinateXian().intValue(), 174 * 5 + 40, 20);
    }

    public void paintStation(Graphics g) {
        g.setColor(Color.MAGENTA);

        // 绘制两个终点站台
        g.drawString(BaojiStation.STATION_NAME, 10, 25);
        g.drawRect(10, 10, 60, 20);

        g.drawString(XianStation.STATION_NAME, 1190, 25);
        g.drawRect(1190, 10, 60, 20);

        for (Destination destination : Destination.values()) {
            g.setColor(Color.MAGENTA);
            g.drawString(destination.getDescription(), destination.getAbscissa().intValue(), destination.getOrdinateBaoji().intValue() + 15);
            g.drawRect(destination.getAbscissa().intValue(), destination.getOrdinateBaoji().intValue(),
                    destination.getWidth().intValue(), destination.getHeight().intValue());
            g.setColor(Color.RED);
            g.fillRect(destination.getAbscissa().intValue(), destination.getOrdinateBaoji().intValue() + destination.getHeight().intValue(),
                    destination.getWidth().intValue(), 8);

            g.setColor(Color.MAGENTA);
            g.drawString(destination.getDescription(), destination.getAbscissa().intValue(), destination.getOrdinateXian().intValue() + 15);
            g.drawRect(destination.getAbscissa().intValue(), destination.getOrdinateXian().intValue(),
                    destination.getWidth().intValue(), destination.getHeight().intValue());
            g.setColor(Color.RED);
            g.fillRect(destination.getAbscissa().intValue(), destination.getOrdinateXian().intValue() - 8,
                    destination.getWidth().intValue(), 8);
        }
    }

    public void paintAuthor(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("XSS ♥ WFM", 520, 35);
        g.drawString("3Rd Edition", 75, 560);
    }

    public void paintTime(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(685, 20, 50, 20);
        g.drawString(this.time.currentTime(), 695, 35);
    }

    public void paintWaitingPassengers(Graphics g) {
        g.setColor(Color.BLUE);
        for (AbstractStation station : stations) {
            String waitPassengers = "候车: " + station.getWaitPassengers().size() + " / " + station.getTotalPassengers();
            String totalIncome = "收入：¥" + station.getTotalIncome();
            List<String> waitPassengerInfos = station.getWaitPassengerInfos();
            Map<Destination, AtomicInteger> waitPassengerDestinationMap = station.getWaitPassengerDestinationMap();
            Map<Destination, AtomicInteger> totalPassengerDestinationMap = station.getTotalPassengerDestinationMap();
            int showPassengerInfoSize = waitPassengerInfos.size() >= MAX_PASSENGER_INFO_LINE ? MAX_PASSENGER_INFO_LINE : waitPassengerInfos.size();
            int waitPassengerInfoCount = 0, currentPassengerInfoCount = 0;
            switch (station.getDirection()) {
                case XI_AN_TO_BAO_JI:
                    g.setColor(Color.RED);
                    g.drawString(waitPassengers, 1260, 25);

                    g.setColor(Color.BLUE);
                    g.drawString(totalIncome, 1360, 25);

                    g.setColor(Color.DARK_GRAY);
                    for (Destination destination : Destination.values()) {
                        if (Destination.XI_AN.equals(destination)) {
                            continue;
                        }
                        String currentWaitPassengerInfo = destination.getDescription() + ": " +
                                waitPassengerDestinationMap.computeIfAbsent(destination, k -> new AtomicInteger(0)).get() + " / " +
                                totalPassengerDestinationMap.computeIfAbsent(destination, k -> new AtomicInteger(0)).get();
                        g.drawString(currentWaitPassengerInfo,
                                1190 + 100 * (currentPassengerInfoCount % 2),
                                50 + 20 * (currentPassengerInfoCount / 2));
                        currentPassengerInfoCount++;
                    }

                    g.setColor(Color.RED);
                    for (int i = 0; i < showPassengerInfoSize; i++) {
                        g.drawString(waitPassengerInfos.get(i), 1190, 120 + 20 * waitPassengerInfoCount);
                        waitPassengerInfoCount++;
                    }
                    if (time.shouldRemovePassengerInfo() && waitPassengerInfos.size() > 0) {
                        waitPassengerInfos.remove(0);
                    }
                    break;
                case BAO_JI_TO_XI_AN:
                default:
                    g.setColor(Color.RED);
                    g.drawString(waitPassengers, 80, 25);

                    g.setColor(Color.BLUE);
                    g.drawString(totalIncome, 165, 25);

                    g.setColor(Color.DARK_GRAY);
                    for (Destination destination : Destination.values()) {
                        if (Destination.BAO_JI.equals(destination)) {
                            continue;
                        }
                        String currentWaitPassengerInfo = destination.getDescription() + ": " +
                                waitPassengerDestinationMap.computeIfAbsent(destination, k -> new AtomicInteger(0)).get() + " / " +
                                totalPassengerDestinationMap.computeIfAbsent(destination, k -> new AtomicInteger(0)).get();
                        g.drawString(currentWaitPassengerInfo,
                                10 + 100 * (currentPassengerInfoCount % 2),
                                50 + 20 * (currentPassengerInfoCount / 2));
                        currentPassengerInfoCount++;
                    }

                    g.setColor(Color.RED);
                    for (int i = 0; i < showPassengerInfoSize; i++) {
                        g.drawString(waitPassengerInfos.get(i), 10, 350 + 20 * waitPassengerInfoCount);
                        waitPassengerInfoCount++;
                    }
                    if (time.shouldRemovePassengerInfo() && waitPassengerInfos.size() > 0) {
                        waitPassengerInfos.remove(0);
                    }
                    break;
            }
        }
    }

    public void paintWaitingCars(Graphics g) {
        for (AbstractStation station : stations) {
            drawWaitingVolvoCars(g, station);
            drawWaitingIvecoCars(g, station);
        }
    }

    public void paintRunningCars(Graphics g) {
        for (AbstractStation station : stations) {
            List<AbstractCar> volvoCars = station.getRunningCars()
                    .stream()
                    .filter(car -> CarParameter.VOLVO.equals(car.getParameter()))
                    .collect(Collectors.toList());

            for (AbstractCar volvoCar : volvoCars) {
                g.setColor(Color.ORANGE);
                int x = volvoCar.getAbscissa().intValue();
                int y = volvoCar.getOrdinate().intValue();
                g.drawRect(x, y, 50, 30);
                g.drawArc(x + 8, y + 30, 8, 8, 0, 360);
                g.drawArc(x + 32, y + 30, 8, 8, 0, 360);
                g.setColor(Color.RED);
                g.drawString("车牌:" + CarParameter.VOLVO.getPlate() + "-" + volvoCar.getNumber(), x, y + 10);
                g.setColor(Color.MAGENTA);
                g.drawString("乘客:" + volvoCar.remainPassengersCount(), x, y + 25);
            }

            List<AbstractCar> ivecoCars = station.getRunningCars()
                    .stream()
                    .filter(car -> CarParameter.IVECO.equals(car.getParameter()))
                    .collect(Collectors.toList());
            for (AbstractCar ivecoCar : ivecoCars) {
                g.setColor(Color.ORANGE);
                int x = ivecoCar.getAbscissa().intValue();
                int y = ivecoCar.getOrdinate().intValue();
                g.drawRect(x, y, 50, 30);
                g.drawArc(x + 8, y + 30, 8, 8, 0, 360);
                g.drawArc(x + 32, y + 30, 8, 8, 0, 360);
                g.setColor(Color.RED);
                g.drawString("车牌:" + CarParameter.IVECO.getPlate() + "-" + ivecoCar.getNumber(), x, y + 10);
                g.setColor(Color.MAGENTA);
                g.drawString("乘客:" + ivecoCar.remainPassengersCount(), x, y + 25);
            }
        }
    }

    private void drawWaitingVolvoCars(Graphics g, AbstractStation station) {
        List<AbstractCar> volvoCars = station.getWaitCars().computeIfAbsent(CarParameter.VOLVO.getType(), k -> new ArrayList<>());
        int times = volvoCars.size() / AbstractStation.MAX_VOLVO_PER_LINE;
        int count = 0;
        switch (station.getDirection()) {
            case XI_AN_TO_BAO_JI:
                g.setColor(Color.BLUE);
                g.drawString(CarParameter.VOLVO.getDescription(), 1190, XianStation.INIT_VOLVO_ORDINATE.intValue() - 10);
                g.setColor(Color.ORANGE);
                for (int i = 0; i < times; i++) {
                    for (int j = 0; j < AbstractStation.MAX_VOLVO_PER_LINE; j++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(1190 + 40 * j, XianStation.INIT_VOLVO_ORDINATE.intValue() + 40 * i, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.VOLVO.getPlate() + "-" + volvoCars.get(count++).getNumber(),
                                1200 + 40 * j,
                                XianStation.INIT_VOLVO_ORDINATE.intValue() + 20 + 40 * i);
                    }
                }

                if (volvoCars.size() % AbstractStation.MAX_VOLVO_PER_LINE != 0) {
                    for (int i = 0; i < volvoCars.size() % AbstractStation.MAX_VOLVO_PER_LINE; i++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(1190 + 40 * i, XianStation.INIT_VOLVO_ORDINATE.intValue() + 40 * times, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.VOLVO.getPlate() + "-" + volvoCars.get(count++).getNumber(),
                                1200 + 40 * i,
                                XianStation.INIT_VOLVO_ORDINATE.intValue() + 20 + 40 * times);
                    }
                }
                break;
            case BAO_JI_TO_XI_AN:
            default:
                g.setColor(Color.BLUE);
                g.drawString(CarParameter.VOLVO.getDescription(), 180, 110);
                for (int i = 0; i < times; i++) {
                    for (int j = 0; j < AbstractStation.MAX_VOLVO_PER_LINE; j++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(175 - 40 * j, 120 + 40 * i, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.VOLVO.getPlate() + "-" + volvoCars.get(count++).getNumber(), 185 - 40 * j, 140 + 40 * i);
                    }
                }

                if (volvoCars.size() % AbstractStation.MAX_VOLVO_PER_LINE != 0) {
                    for (int i = 0; i < volvoCars.size() % AbstractStation.MAX_VOLVO_PER_LINE; i++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(175 - 40 * i, 120 + 40 * times, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.VOLVO.getPlate() + "-" + volvoCars.get(count++).getNumber(), 185 - 40 * i, 140 + 40 * times);
                    }
                }
                break;
        }
    }

    private void drawWaitingIvecoCars(Graphics g, AbstractStation station) {
        List<AbstractCar> volvoCars = station.getWaitCars().computeIfAbsent(CarParameter.VOLVO.getType(), k -> new ArrayList<>());
        int volvoTimes = volvoCars.size() / AbstractStation.MAX_VOLVO_PER_LINE - (volvoCars.size() % AbstractStation.MAX_VOLVO_PER_LINE == 0 ? 1 : 0);
        List<AbstractCar> ivecoCars = station.getWaitCars().computeIfAbsent(CarParameter.IVECO.getType(), k -> new ArrayList<>());
        int times = ivecoCars.size() / AbstractStation.MAX_IVECO_PER_LINE;
        int count = 0;
        switch (station.getDirection()) {
            case XI_AN_TO_BAO_JI:
                g.setColor(Color.BLUE);
                g.drawString(CarParameter.IVECO.getDescription(), 1190, XianStation.INIT_IVECO_ORDINATE.intValue() - 10 + 40 * volvoTimes);
                for (int i = 0; i < times; i++) {
                    for (int j = 0; j < AbstractStation.MAX_IVECO_PER_LINE; j++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(1190 + 40 * j, XianStation.INIT_IVECO_ORDINATE.intValue() + 40 * volvoTimes + 40 * i, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.IVECO.getPlate() + "-" + ivecoCars.get(count++).getNumber(),
                                1200 + 40 * j,
                                XianStation.INIT_IVECO_ORDINATE.intValue() + 20 + 40 * volvoTimes + 40 * i);
                    }
                }

                if (ivecoCars.size() % AbstractStation.MAX_IVECO_PER_LINE != 0) {
                    for (int i = 0; i < ivecoCars.size() % AbstractStation.MAX_IVECO_PER_LINE; i++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(1190 + 40 * i, XianStation.INIT_IVECO_ORDINATE.intValue() + 40 * volvoTimes + 40 * times, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.IVECO.getPlate() + "-" + ivecoCars.get(count++).getNumber(),
                                1200 + 40 * i,
                                XianStation.INIT_IVECO_ORDINATE.intValue() + 20 + 40 * volvoTimes + 40 * times);
                    }
                }
                break;
            case BAO_JI_TO_XI_AN:
            default:
                g.setColor(Color.BLUE);
                g.drawString(CarParameter.IVECO.getDescription(), 180, 170 + 40 * volvoTimes);
                for (int i = 0; i < times; i++) {
                    for (int j = 0; j < AbstractStation.MAX_IVECO_PER_LINE; j++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(175 - 40 * j, 180 + 40 * volvoTimes + 40 * i, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.IVECO.getPlate() + "-" + ivecoCars.get(count++).getNumber(),
                                185 - 40 * j,
                                200 + 40 * volvoTimes + 40 * i);
                    }
                }

                if (ivecoCars.size() % AbstractStation.MAX_IVECO_PER_LINE != 0) {
                    for (int i = 0; i < ivecoCars.size() % AbstractStation.MAX_IVECO_PER_LINE; i++) {
                        g.setColor(Color.ORANGE);
                        g.drawRect(175 - 40 * i, 180 + 40 * volvoTimes + 40 * times, 40, 30);
                        g.setColor(Color.RED);
                        g.drawString(CarParameter.IVECO.getPlate() + "-" + ivecoCars.get(count++).getNumber(),
                                185 - 40 * i,
                                200 + 40 * volvoTimes + 40 * times);
                    }
                }
                break;
        }
    }
}
