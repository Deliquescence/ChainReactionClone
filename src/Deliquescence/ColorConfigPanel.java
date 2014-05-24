/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;

/**
 *
 * @author Josh
 */
public class ColorConfigPanel extends javax.swing.JPanel implements Refreshable {

    /**
     * Creates new form ColorConfigPanel
     */
    public ColorConfigPanel() {
        refreshConfig();
        Config.refreshables.add(this);
    }

    @Override
    public void refreshConfig() {
        this.removeAll();
        setLayout(new java.awt.GridLayout(Config.getInt("MAX_PLAYERS"), 2, 5, 5));

        for (int i = 1; i <= Config.getInt("MAX_PLAYERS"); i++) {
            final JButton button = new JButton();
            button.setName(Integer.toString(i));
            button.setText(Deliquescence.Config.getDefaultPlayerName(i));
            button.setMaximumSize(new Dimension(400, 20));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    colorConfigButtonActionPreformed(e);
                }
            });

            final JLabel label = new JLabel(new ImageIcon(Config.imagesByPlayerID.get(i)[1]));

            this.add(button);
            this.add(label);
        }
    }

    private void colorConfigButtonActionPreformed(java.awt.event.ActionEvent evt) {
        Object source = evt.getSource();
        if (source instanceof JButton) {
            JButton button = (JButton) source;
            String configName = "Color_" + button.getName();

            int[] defaultRGB = Config.getRGBFromPlayerID(Integer.valueOf(button.getName()));
            //JColorChooser chooser = new JColorChooser();
            Color newColor = JColorChooser.showDialog(this, "Player " + button.getName() + " color", new Color(defaultRGB[0], defaultRGB[1], defaultRGB[2]));
            if (newColor != null) {
                Config.setString(configName, newColor.getRed() + "," + newColor.getGreen() + "," + newColor.getBlue());
            }
            Config.refresh();
            refreshConfig();
        }
    }
}
