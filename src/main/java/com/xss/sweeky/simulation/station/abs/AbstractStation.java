package com.xss.sweeky.simulation.station.abs;

import com.xss.sweeky.simulation.car.IvecoCar;
import com.xss.sweeky.simulation.car.VolvoCar;
import com.xss.sweeky.simulation.car.abs.AbstractCar;
import com.xss.sweeky.simulation.enums.CarParameter;
import com.xss.sweeky.simulation.enums.CarStatus;
import com.xss.sweeky.simulation.enums.Destination;
import com.xss.sweeky.simulation.enums.Direction;
import com.xss.sweeky.simulation.passenger.Passenger;
import com.xss.sweeky.simulation.time.HourMinuteTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractStation {
    /**
     * 每行展示的沃尔沃最大车辆数
     */
    public static final int MAX_VOLVO_PER_LINE = 5;

    /**
     * 每行展示的依维柯最大车辆数
     */
    public static final int MAX_IVECO_PER_LINE = 5;

    /**
     * 初始化等待乘客数
     */
    private static final int INIT_WAITING_PASSENGERS = CarParameter.VOLVO.getSeats() + CarParameter.IVECO.getSeats();

    /**
     * 每分钟最大新候车乘客数
     */
    private static final int MAX_WAIT_PASSENGERS_GENERATE_NUMBER = 5;

    /**
     * 站内候车乘客临界值, 大于这个值, 则每分钟生成最大候车乘客数应该更少
     */
    private static final int MAX_WAIT_PASSENGERS_NUMBER = CarParameter.VOLVO.getSeats() + CarParameter.IVECO.getSeats();

    /**
     * 最大概率100%
     */
    private static final int MAX_PROBABILITY = 100;

    /**
     * 沃尔沃出站时间
     */
    private static final Set<Integer> VOLVO_EXIT_STATION_TIME_IN_MINUTE = new HashSet<>(Collections.singletonList(30));

    /**
     * 依维柯出站时间
     */
    private static final Set<Integer> IVECO_EXIT_STATION_TIME_IN_MINUTE = new HashSet<>(Arrays.asList(0, 20, 40));

    /**
     * 沃尔沃车辆编号
     */
    private static int volvoCarNumber = 1;

    /**
     * 依维柯车辆编号
     */
    private static int ivecoCarNumber = 1;

    /**
     * 车辆行驶方向
     */
    private Direction direction;

    /**
     * 初始站内沃尔沃车辆数
     */
    private final int INITIAL_VOLVO_NUMBER;

    /**
     * 初始站内依维柯车辆数
     */
    private final int INITIAL_IVECO_NUMBER;

    /**
     * 汽车站名称
     */
    private String name;

    /**
     * 站内车辆队列
     */
    private Map<String, List<AbstractCar>> waitCars = new HashMap<>(4);

    /**
     * 候车乘客队列
     */
    private List<Passenger> waitPassengers;

    /**
     * 行驶中的车辆队列
     */
    private List<AbstractCar> runningCars = new ArrayList<>();

    /**
     * 已到终点站的车辆队列
     */
    private List<AbstractCar> arrivalCars = new ArrayList<>();

    /**
     * 等待乘客信息
     */
    private List<String> waitPassengerInfos = new ArrayList<>();

    /**
     * 总收入
     */
    private Double totalIncome = 0.0D;

    /**
     * 总乘客数
     */
    private int totalPassengers = 0;

    /**
     * 当前以各个站点为目的地的乘客统计信息
     */
    private Map<Destination, AtomicInteger> waitPassengerDestinationMap = new HashMap<>(Destination.values().length);

    /**
     * 以各个站点为目的地的乘客总的统计信息
     */
    private Map<Destination, AtomicInteger> totalPassengerDestinationMap = new HashMap<>(Destination.values().length);

    /**
     * 随机数(用于生成乘客)
     */
    private Random random = new Random();

    /**
     * 初始化的车辆横纵坐标
     */
    private Double initVolvoAbscissa, initVolvoOrdinate, initIvecoAbscissa, initIvecoOrdinate;

    public AbstractStation(String name, Direction direction, int volvoNumber, int ivecoNumber,
                           Double initVolvoAbscissa, Double incrementVolvoAbscissa, Double initVolvoOrdinate,
                           Double initIvecoAbscissa, Double incrementIvecoAbscissa, Double initIvecoOrdinate, Double incrementIvecoOrdinate) {
        this.name = name;
        this.direction = direction;
        this.INITIAL_VOLVO_NUMBER = volvoNumber;
        this.INITIAL_IVECO_NUMBER = ivecoNumber;
        this.initVolvoAbscissa = initVolvoAbscissa;
        this.initVolvoOrdinate = initVolvoOrdinate;
        this.initIvecoAbscissa = initIvecoAbscissa;
        this.initIvecoOrdinate = initIvecoOrdinate;
        initVolvoCars(initVolvoAbscissa, incrementVolvoAbscissa, initVolvoOrdinate);
        initIvecoCars(initIvecoAbscissa, incrementIvecoAbscissa, initIvecoOrdinate, incrementIvecoOrdinate);
        initWaitPassengers();
    }

    /**
     * 获取并删除已到终点站的车辆
     *
     * @return 已到终点站的车辆
     */
    public List<AbstractCar> getAndRemoveArrivalCars() {
        List<AbstractCar> arrivalCarList = new ArrayList<>();
        arrivalCarList.addAll(arrivalCars);
        this.arrivalCars.clear();

        return arrivalCarList;
    }

    /**
     * 将到达本站的车辆加入到车辆队列
     *
     * @param arrivalCars 所有到达本站的车辆
     * @param time        当前时间
     */
    public void addArrivalCars(List<AbstractCar> arrivalCars, HourMinuteTime time) {
        for (AbstractCar car : arrivalCars) {
            car.carArrival(this.direction);
            waitCars.computeIfAbsent(car.getParameter().getType(), k -> new ArrayList<>()).add(car);
            System.out.println("车辆(" + car.getParameter().getPlate() + "-" + car.getNumber() +
                    ")到达 <" + name + ">, 到站时间: " + time.currentTime());
        }
    }


    /**
     * 车辆出站
     *
     * @param time 当前时间
     */
    public void carExitStation(HourMinuteTime time) {
        List<AbstractCar> carList = new ArrayList<>();
        int waitPassengerSize = this.waitPassengers.size();
        if (!time.illegalDepartTime()) {
            int minute = time.getMinute();
            if (VOLVO_EXIT_STATION_TIME_IN_MINUTE.contains(minute) && waitPassengerSize > 0) {
                List<AbstractCar> volvoCarsList = waitCars.computeIfAbsent(CarParameter.VOLVO.getType(), k -> new ArrayList<>());
                if (volvoCarsList.size() > 0) {
                    AbstractCar car = volvoCarsList.remove(0);
                    car.exitStation(this.initVolvoOrdinate);
                    carList.add(car);
                    waitPassengerSize -= car.getParameter().getSeats();
                }
            }

            if (IVECO_EXIT_STATION_TIME_IN_MINUTE.contains(minute) && waitPassengerSize > 0) {
                List<AbstractCar> ivecoCarsList = waitCars.computeIfAbsent(CarParameter.IVECO.getType(), k -> new ArrayList<>());
                if (ivecoCarsList.size() > 0) {
                    AbstractCar car = ivecoCarsList.remove(0);
                    car.exitStation(this.initIvecoOrdinate);
                    carList.add(car);
                }
            }
        }

        for (AbstractCar car : carList) {
            // 乘客上车
            int passengers = waitPassengers.size() >= car.getParameter().getSeats() ? car.getParameter().getSeats() : waitPassengers.size();
            for (int i = 0; i < passengers; i++) {
                Passenger passenger = waitPassengers.remove(0);
                car.getOn(passenger);
                waitPassengerDestinationMap.computeIfAbsent(passenger.getDestination(), k -> new AtomicInteger(0)).decrementAndGet();
            }
            runningCars.add(car);
            System.out.println("车辆(" + car.getParameter().getPlate() + "-" + car.getNumber() +
                    ") 离开 <" + name + ">, 离站时间: " + time.currentTime());
        }
    }

    /**
     * 更新行驶中的车辆坐标
     *
     * @param time 当前时间
     */
    public void updateCoordinate(HourMinuteTime time) {
        Iterator<AbstractCar> carIterator = runningCars.iterator();
        while (carIterator.hasNext()) {
            AbstractCar car = carIterator.next();
            car.updateCoordinate(time);
            if (CarStatus.ARRIVAL.equals(car.getStatus())) {
                carIterator.remove();
                arrivalCars.add(car);
            }
        }
    }

    /**
     * 生成每分钟随机数目随即目的地候车乘客
     *
     * @param time 当前时间
     */
    public void generateWaitPassengers(HourMinuteTime time) {
        if (!time.illegalDepartTime()) {
            int passengers;
            if (this.waitPassengers.size() >= MAX_WAIT_PASSENGERS_NUMBER) {
                passengers = random.nextInt(MAX_WAIT_PASSENGERS_GENERATE_NUMBER - 2);
            } else {
                passengers = random.nextInt(MAX_WAIT_PASSENGERS_GENERATE_NUMBER);
            }

            this.addWaitPassengers(passengers);
            System.out.println(name + " 新增 候车乘客 " + passengers + " 人, 当前时间: " + time.currentTime());
        }
    }

    private void initVolvoCars(Double initAbscissa, Double incrementAbscissa, Double initOrdinate) {
        for (int i = 0; i < this.INITIAL_VOLVO_NUMBER; i++) {
            AbstractCar car = new VolvoCar(volvoCarNumber++, initAbscissa + incrementAbscissa * i, initOrdinate, this.direction);
            waitCars.computeIfAbsent(car.getParameter().getType(), k -> new ArrayList<>()).add(car);
        }
    }

    private void initIvecoCars(Double initAbscissa, Double incrementAbscissa, Double initOrdinate, Double incrementOrdinate) {
        int times = this.INITIAL_IVECO_NUMBER / MAX_IVECO_PER_LINE;
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < MAX_IVECO_PER_LINE; j++) {
                IvecoCar car = new IvecoCar(ivecoCarNumber++,
                        initAbscissa + incrementAbscissa * j,
                        initOrdinate + incrementOrdinate * i,
                        this.direction);
                waitCars.computeIfAbsent(car.getParameter().getType(), k -> new ArrayList<>()).add(car);
            }
        }

        if (this.INITIAL_IVECO_NUMBER % MAX_IVECO_PER_LINE != 0) {
            for (int i = 0; i < this.INITIAL_IVECO_NUMBER % MAX_IVECO_PER_LINE; i++) {
                IvecoCar car = new IvecoCar(ivecoCarNumber++,
                        initAbscissa + incrementAbscissa * i,
                        initOrdinate + incrementOrdinate,
                        this.direction);
                waitCars.computeIfAbsent(car.getParameter().getType(), k -> new ArrayList<>()).add(car);
            }
        }
    }

    private void initWaitPassengers() {
        //  初始化候车乘客
        int passengers = this.random.nextInt(INIT_WAITING_PASSENGERS / 2) + 1;
        this.waitPassengers = new ArrayList<>(passengers);
        this.addWaitPassengers(passengers);
    }

    private void addWaitPassengers(int passengers) {
        for (int i = 0; i < passengers; i++) {
            Passenger passenger = new Passenger(this.random.nextInt(MAX_PROBABILITY), this.direction);
            this.waitPassengers.add(passenger);
            String waitPassengerInfo = "乘客: " + passenger.getName() + ", 目的地: " + passenger.getDestination().getDescription() +
                    ", 票价: ¥" + passenger.getDestination().getPrice(this.direction);
            totalPassengers++;
            totalIncome += passenger.getDestination().getPrice(this.direction);
            this.waitPassengerInfos.add(waitPassengerInfo);
            this.waitPassengerDestinationMap.computeIfAbsent(passenger.getDestination(), k -> new AtomicInteger(0)).incrementAndGet();
            this.totalPassengerDestinationMap.computeIfAbsent(passenger.getDestination(), k -> new AtomicInteger(0)).incrementAndGet();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<AbstractCar>> getWaitCars() {
        return waitCars;
    }

    public void setWaitCars(Map<String, List<AbstractCar>> waitCars) {
        this.waitCars = waitCars;
    }

    public List<Passenger> getWaitPassengers() {
        return waitPassengers;
    }

    public void setWaitPassengers(List<Passenger> waitPassengers) {
        this.waitPassengers = waitPassengers;
    }

    public List<AbstractCar> getRunningCars() {
        return runningCars;
    }

    public void setRunningCars(List<AbstractCar> runningCars) {
        this.runningCars = runningCars;
    }

    public List<AbstractCar> getArrivalCars() {
        return arrivalCars;
    }

    public void setArrivalCars(List<AbstractCar> arrivalCars) {
        this.arrivalCars = arrivalCars;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Double getInitVolvoAbscissa() {
        return initVolvoAbscissa;
    }

    public void setInitVolvoAbscissa(Double initVolvoAbscissa) {
        this.initVolvoAbscissa = initVolvoAbscissa;
    }

    public Double getInitVolvoOrdinate() {
        return initVolvoOrdinate;
    }

    public void setInitVolvoOrdinate(Double initVolvoOrdinate) {
        this.initVolvoOrdinate = initVolvoOrdinate;
    }

    public Double getInitIvecoAbscissa() {
        return initIvecoAbscissa;
    }

    public void setInitIvecoAbscissa(Double initIvecoAbscissa) {
        this.initIvecoAbscissa = initIvecoAbscissa;
    }

    public Double getInitIvecoOrdinate() {
        return initIvecoOrdinate;
    }

    public void setInitIvecoOrdinate(Double initIvecoOrdinate) {
        this.initIvecoOrdinate = initIvecoOrdinate;
    }

    public List<String> getWaitPassengerInfos() {
        return waitPassengerInfos;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public void setWaitPassengerInfos(List<String> waitPassengerInfos) {
        this.waitPassengerInfos = waitPassengerInfos;
    }

    public int getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(int totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public Map<Destination, AtomicInteger> getWaitPassengerDestinationMap() {
        return waitPassengerDestinationMap;
    }

    public void setWaitPassengerDestinationMap(Map<Destination, AtomicInteger> waitPassengerDestinationMap) {
        this.waitPassengerDestinationMap = waitPassengerDestinationMap;
    }

    public Map<Destination, AtomicInteger> getTotalPassengerDestinationMap() {
        return totalPassengerDestinationMap;
    }

    public void setTotalPassengerDestinationMap(Map<Destination, AtomicInteger> totalPassengerDestinationMap) {
        this.totalPassengerDestinationMap = totalPassengerDestinationMap;
    }
}
