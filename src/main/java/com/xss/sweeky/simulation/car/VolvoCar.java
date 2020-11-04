package com.xss.sweeky.simulation.car;

import com.xss.sweeky.simulation.car.abs.AbstractCar;
import com.xss.sweeky.simulation.enums.CarParameter;
import com.xss.sweeky.simulation.enums.Direction;

public class VolvoCar extends AbstractCar {
    public VolvoCar(int number, Double abscissa, Double ordinate, Direction direction) {
        super(CarParameter.VOLVO, number, abscissa, ordinate, direction);
    }
}
