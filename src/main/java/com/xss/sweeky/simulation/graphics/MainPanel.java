package com.xss.sweeky.simulation.graphics;

import com.xss.sweeky.simulation.station.abs.AbstractStation;
import com.xss.sweeky.simulation.time.HourMinuteTime;

import javax.swing.JPanel;
import java.awt.Graphics;

public class MainPanel extends JPanel {
    private static final long serialVersionUID = -1376753116221557187L;
    private HourMinuteTime time;
    private java.util.List<AbstractStation> stations;

    public MainPanel(HourMinuteTime time, java.util.List<AbstractStation> stations) {
        this.time = time;
        this.stations = stations;
    }

    @Override
    protected void paintComponent(Graphics g) {
        DrawShape ds = new DrawShape(time, stations);

        ds.paintRoad(g);
        ds.paintStation(g);
        ds.paintAuthor(g);
        ds.paintTime(g);
        ds.paintWaitingPassengers(g);
        ds.paintWaitingCars(g);
        ds.paintRunningCars(g);
    }
}
