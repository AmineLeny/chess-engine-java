package com.chess.gui;

import javax.swing.*;
import java.awt.*;

public class Table {
    private static final Dimension OUTER_FRAM_DIMENSION = new Dimension(600,600) ;
    private final JFrame gameFrame;

    public Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setSize(OUTER_FRAM_DIMENSION);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
