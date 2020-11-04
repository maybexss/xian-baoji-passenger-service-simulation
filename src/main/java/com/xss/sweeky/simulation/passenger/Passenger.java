package com.xss.sweeky.simulation.passenger;

import com.xss.sweeky.simulation.enums.Destination;
import com.xss.sweeky.simulation.enums.Direction;
import com.xss.sweeky.simulation.enums.Probability;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.xss.sweeky.simulation.constants.NameConstant.LAST_NAME;
import static com.xss.sweeky.simulation.constants.NameConstant.SURNAME;

public class Passenger {
    private static final int MAX_NAME_GENERATE_TIMES = 100;
    private static final Random random = new Random();
    private static Set<String> nameSet = new HashSet<>();
    private String name;
    private Destination destination;

    public Passenger(int probability, Direction direction) {
        this.name = generateName();
        switch (direction) {
            case XI_AN_TO_BAO_JI:
                if (probability >= Probability.XI_AN_TO_XIAN_YANG.getFrom() && probability <= Probability.XI_AN_TO_XIAN_YANG.getTo()) {
                    this.destination = Probability.XI_AN_TO_XIAN_YANG.getDestination();
                } else if (probability >= Probability.XI_AN_TO_XING_PING.getFrom() && probability <= Probability.XI_AN_TO_XING_PING.getTo()) {
                    this.destination = Probability.XI_AN_TO_XING_PING.getDestination();
                } else if (probability >= Probability.XI_AN_TO_WU_GONG.getFrom() && probability <= Probability.XI_AN_TO_WU_GONG.getTo()) {
                    this.destination = Probability.XI_AN_TO_WU_GONG.getDestination();
                } else if (probability >= Probability.XI_AN_TO_CAI_JIA_PO.getFrom() && probability <= Probability.XI_AN_TO_CAI_JIA_PO.getTo()) {
                    this.destination = Probability.XI_AN_TO_CAI_JIA_PO.getDestination();
                } else if (probability >= Probability.XI_AN_TO_GUO_ZHEN.getFrom() && probability <= Probability.XI_AN_TO_GUO_ZHEN.getTo()) {
                    this.destination = Probability.XI_AN_TO_GUO_ZHEN.getDestination();
                } else {
                    this.destination = Probability.XI_AN_TO_BAO_JI.getDestination();
                }
                break;
            case BAO_JI_TO_XI_AN:
            default:
                if (probability >= Probability.BAO_JI_TO_GUO_ZHEN.getFrom() && probability <= Probability.BAO_JI_TO_GUO_ZHEN.getTo()) {
                    this.destination = Probability.BAO_JI_TO_GUO_ZHEN.getDestination();
                } else if (probability >= Probability.BAO_JI_TO_CAI_JIA_PO.getFrom() && probability <= Probability.BAO_JI_TO_CAI_JIA_PO.getTo()) {
                    this.destination = Probability.BAO_JI_TO_CAI_JIA_PO.getDestination();
                } else if (probability >= Probability.BAO_JI_TO_WU_GONG.getFrom() && probability <= Probability.BAO_JI_TO_WU_GONG.getTo()) {
                    this.destination = Probability.BAO_JI_TO_WU_GONG.getDestination();
                } else if (probability >= Probability.BAO_JI_TO_XING_PING.getFrom() && probability <= Probability.BAO_JI_TO_XING_PING.getTo()) {
                    this.destination = Probability.BAO_JI_TO_XING_PING.getDestination();
                } else if (probability >= Probability.BAO_JI_TO_XIAN_YANG.getFrom() && probability <= Probability.BAO_JI_TO_XIAN_YANG.getTo()) {
                    this.destination = Probability.BAO_JI_TO_XIAN_YANG.getDestination();
                } else {
                    this.destination = Probability.BAO_JI_TO_XI_AN.getDestination();
                }
                break;
        }
    }

    private String generateName() {
        int surnameIndex = random.nextInt(SURNAME.length);
        int lastNameIndex = random.nextInt(LAST_NAME.length);
        int times = 0;
        String name = "";
        while (times < MAX_NAME_GENERATE_TIMES) {
            name = SURNAME[surnameIndex] + LAST_NAME[lastNameIndex];
            if (!nameSet.contains(name)) {
                nameSet.add(name);
                break;
            }
            times++;
        }

        return name;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
