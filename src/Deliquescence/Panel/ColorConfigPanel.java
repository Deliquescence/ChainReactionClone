/*
 * Copyright (c) 2014, Deliquescence <Deliquescence1@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package Deliquescence.Panel;

import Deliquescence.Config;
import Deliquescence.Refreshable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;

/**
 * A panel with buttons to change each player's color.
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
        setLayout(new java.awt.GridLayout(Config.getInt("MAX_PLAYERS"), 3, 5, 5));

        for (int i = 1; i <= Config.getInt("MAX_PLAYERS"); i++) {
            final JButton playerButton = new JButton();
            playerButton.setName(Integer.toString(i));
            playerButton.setText(Deliquescence.Config.getDefaultPlayerName(i));
            playerButton.setMaximumSize(new Dimension(400, 20));

            playerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playerButtonActionPreformed(e);
                }
            });

            final JLabel label = new JLabel(new ImageIcon(Config.getImageByPlayerID(i, 1)));

            final JButton defaultButton = new JButton();
            defaultButton.setName(Integer.toString(i));
            defaultButton.setText("Default");
            defaultButton.setMaximumSize(new Dimension(400, 20));

            defaultButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    defaultButtonActionPreformed(e);
                }
            });

            this.add(playerButton);
            this.add(label);
            this.add(defaultButton);
        }
    }

    private void playerButtonActionPreformed(java.awt.event.ActionEvent evt) {
        Object source = evt.getSource();
        if (source instanceof JButton) {
            JButton button = (JButton) source;
            String configName = "Color_" + button.getName();

            int[] defaultRGB = Config.getRGBFromPlayerID(Integer.valueOf(button.getName()));

            Color newColor = JColorChooser.showDialog(this, "Player " + button.getName() + " color", new Color(defaultRGB[0], defaultRGB[1], defaultRGB[2]));
            if (newColor != null) {
                Config.setString(configName, newColor.getRed() + "," + newColor.getGreen() + "," + newColor.getBlue());
            }
            Config.refresh();
            refreshConfig();
        }
    }

    private void defaultButtonActionPreformed(java.awt.event.ActionEvent evt) {
        Object source = evt.getSource();
        if (source instanceof JButton) {
            JButton button = (JButton) source;
            String configName = "Color_" + button.getName();

            Config.setString(configName, Config.getDefaultString(configName));

            Config.refresh();
            refreshConfig();
        }
    }
}
