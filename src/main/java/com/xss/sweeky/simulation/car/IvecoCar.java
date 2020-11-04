package com.xss.sweeky.simulation.car;

import com.xss.sweeky.simulation.car.abs.AbstractCar;
import com.xss.sweeky.simulation.enums.CarParameter;
import com.xss.sweeky.simulation.enums.Direction;

public class IvecoCar extends AbstractCar {
    public IvecoCar(int number, Double abscissa, Double oridnate, Direction direction) {
        super(CarParameter.IVECO, number, abscissa, oridnate, direction);
    }
}
