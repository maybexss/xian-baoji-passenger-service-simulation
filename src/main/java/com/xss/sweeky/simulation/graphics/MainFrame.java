package com.xss.sweeky.simulation.graphics;

import com.xss.sweeky.simulation.station.abs.AbstractStation;
import com.xss.sweeky.simulation.time.HourMinuteTime;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class MainFrame extends JFrame {
    @Serial
    private static final long serialVersionUID = -2325914349498593839L;
    private static final int X = 300, Y = 250, WIDTH = 1480, HEIGHT = 650;

    public void initFrame(HourMinuteTime time, java.util.List<AbstractStation> stations) {
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(X, Y, WIDTH, HEIGHT);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(0, 0));

        setContentPane(jPanel);

        MainPanel mainPanel = new MainPanel(time, stations);
        mainPanel.setBounds(0, 0, 0, 0);

        jPanel.add(mainPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        repaint();
    }
}
