package com.xss.sweeky.simulation.car.abs;

import com.xss.sweeky.simulation.enums.CarParameter;
import com.xss.sweeky.simulation.enums.CarStatus;
import com.xss.sweeky.simulation.enums.Destination;
import com.xss.sweeky.simulation.enums.Direction;
import com.xss.sweeky.simulation.passenger.Passenger;
import com.xss.sweeky.simulation.time.HourMinuteTime;

import java.util.*;

public abstract class AbstractCar {
    /**
     * 每次更新坐标乘以的倍数
     */
    private static final double COORDINATE_UPDATE_TIMES = 0.15D;

    /**
     * 车辆参数
     */
    private final CarParameter parameter;

    /**
     * 车辆编号
     */
    private final int number;

    /**
     * 横坐标
     */
    private Double abscissa;

    /**
     * 纵坐标
     */
    private Double ordinate;

    /**
     * 车辆行驶方向
     */
    private Direction direction;

    /**
     * 乘客
     */
    private final Map<String, List<Passenger>> passengers = new HashMap<>(16);

    /**
     * 目的地列表
     */
    private final List<Destination> destinations = new ArrayList<>(Destination.values().length);

    /**
     * 车辆当前状态
     */
    private CarStatus status = CarStatus.AT_STATION;

    public AbstractCar(CarParameter parameter, int number, Double abscissa, Double ordinate, Direction direction) {
        this.parameter = parameter;
        this.number = number;
        this.abscissa = abscissa;
        this.ordinate = ordinate;
        this.direction = direction;
        initDestinations();
    }

    /**
     * 车辆到达本站
     *
     * @param direction 车辆下次行驶方向
     */
    public void carArrival(Direction direction) {
        setStatus(CarStatus.AT_STATION);
        setDirection(direction);
        initDestinations();
    }

    /**
     * 乘客上车
     *
     * @param passenger 乘客
     */
    public void getOn(Passenger passenger) {
        if (remainPassengersCount() < getParameter().getSeats()) {
            getPassengers().computeIfAbsent(passenger.getDestination().getStation(), k -> new ArrayList<>()).add(passenger);
        }
    }

    /**
     * @return 车上剩余乘客数
     */
    public int remainPassengersCount() {
        int count = 0;
        for (Map.Entry<String, List<Passenger>> entry : passengers.entrySet()) {
            count += entry.getValue().size();
        }

        return count;
    }

    /**
     * 更新车辆当前坐标(实际横坐标更新, 纵坐标不变)
     *
     * @param time 当前时间
     */
    public void updateCoordinate(HourMinuteTime time) {
        int status = getStatus().getStatus();
        if (status == CarStatus.STOP_ONE_MINUTE.getStatus()) {
            if (time.legalTime()) {
                // 乘客下车
                Destination destination = destinations.removeFirst();
                int offPassengers = getOff(destination);
                System.out.println("车辆(" + parameter.getPlate() + "-" + number + ") 到达 <" + destination.getDescription() +
                        ">, 有乘客下车, 下车人数: " + offPassengers + ", 当前时间: " + time.currentTime());
                setStatus(CarStatus.WAIT_TO_RUN);
            }
        } else if (status == CarStatus.WAIT_TO_RUN.getStatus()) {
            if (time.legalTime()) {
                if (destinations.isEmpty()) {
                    // 车辆到达终点站
                    System.out.println("车辆(" + parameter.getPlate() + "-" + number + ") 到达 终点站, 当前时间: " + time.currentTime());
                    setStatus(CarStatus.ARRIVAL);
                } else {
                    setStatus(CarStatus.RUNNING);
                }
            }
        } else if (status == CarStatus.RUNNING.getStatus()) {
            switch (direction) {
                case XI_AN_TO_BAO_JI:
                    if (!destinations.isEmpty()) {
                        Destination destination = destinations.getFirst();
                        if (abscissa.compareTo(destination.getAbscissa()) <= 0) {
                            arrivedStation(destination, time);
                        }
                    }
                    setAbscissa(abscissa - parameter.getSpeed() * COORDINATE_UPDATE_TIMES);
                    break;
                case BAO_JI_TO_XI_AN:
                default:
                    if (!destinations.isEmpty()) {
                        Destination destination = destinations.getFirst();
                        if (abscissa.compareTo(destination.getAbscissa()) >= 0) {
                            arrivedStation(destination, time);
                        }
                    }
                    setAbscissa(abscissa + parameter.getSpeed() * COORDINATE_UPDATE_TIMES);
                    break;
            }
        }
    }

    /**
     * 设置车辆出站
     *
     * @param ordinate 车辆纵坐标
     */
    public void exitStation(Double ordinate) {
        setStatus(CarStatus.RUNNING);
        switch (this.direction) {
            case XI_AN_TO_BAO_JI:
                setAbscissa(Destination.XI_AN.getAbscissa());
                setOrdinate(ordinate);
                break;
            case BAO_JI_TO_XI_AN:
            default:
                setAbscissa(Destination.BAO_JI.getAbscissa());
                setOrdinate(ordinate);
                break;
        }
    }

    public CarParameter getParameter() {
        return parameter;
    }

    public int getNumber() {
        return number;
    }

    public Double getAbscissa() {
        return abscissa;
    }

    public void setAbscissa(Double abscissa) {
        this.abscissa = abscissa;
    }

    public Double getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(Double ordinate) {
        this.ordinate = ordinate;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Map<String, List<Passenger>> getPassengers() {
        return passengers;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    /**
     * 乘客下车
     *
     * @param destination 当前目的地
     * @return 该站点下车人数
     */
    private int getOff(Destination destination) {
        if (remainPassengersCount() > 0) {
            List<Passenger> offPassengers = getPassengers().remove(destination.getStation());
            if (offPassengers == null) {
                return 0;
            }

            return offPassengers.size();
        }

        return 0;
    }

    /**
     * 初始化目的地列表
     */
    private void initDestinations() {
        this.destinations.addAll(Arrays.asList(Destination.values()));
        if (Direction.XI_AN_TO_BAO_JI.equals(this.direction)) {
            this.destinations.sort((o1, o2) -> o2.getOrder().compareTo(o1.getOrder()));
        } else {
            this.destinations.sort(Comparator.comparing(Destination::getOrder));
        }

        this.destinations.removeFirst();
    }

    /**
     * 是否有乘客已到达目的地
     *
     * @param destination 当前目的地
     * @return 是否有乘客的目的地为当前目的地
     */
    private boolean hasPassengerArrived(Destination destination) {
        return passengers.containsKey(destination.getStation()) && !passengers.get(destination.getStation()).isEmpty();
    }

    /**
     * 车辆到站
     *
     * @param destination 当前站
     * @param time        当前时间
     */
    private void arrivedStation(Destination destination, HourMinuteTime time) {
        if (hasPassengerArrived(destination)) {
            // 有乘客到达目的地, 则停车, 否则直接行驶不停
            setStatus(CarStatus.STOP_ONE_MINUTE);
        } else {
            destinations.removeFirst();
            if (destinations.isEmpty()) {
                setStatus(CarStatus.WAIT_TO_RUN);
            }
            System.out.println("车辆(" + parameter.getPlate() + "-" + number + ") 到达 <" + destination.getDescription() +
                    ">, 无乘客下车, 当前时间: " + time.currentTime());
        }
    }
}