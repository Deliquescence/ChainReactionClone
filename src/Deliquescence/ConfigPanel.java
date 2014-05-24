/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * A panel with fields for setting configuration values.
 *
 * @author Josh
 */
public class ConfigPanel extends JPanel implements Refreshable {

    private ArrayList<JTextArea> textAreas;
    private HashMap componentMap;

    @SuppressWarnings("unchecked")
    public ConfigPanel() {
        Config.refreshables.add(this);
        textAreas = new ArrayList<>();
        componentMap = new HashMap<>();

        Set<String> configSet = Config.getKeys();
        TreeSet<String> configTreeSet = new TreeSet(configSet);

        add(new JLabel("Config File: " + Config.configFile.getPath()));

        //Info Labels
        JLabel[] infoLabels = new JLabel[3];
        infoLabels[0] = new JLabel("Here you will find various values that can be configured.");
        infoLabels[1] = new JLabel("Note some changes may not be noticable until after a restart.");
        infoLabels[2] = new JLabel("Also, changing certain settings may cause errors.");

        for (JLabel info : infoLabels) {
            info.setHorizontalAlignment(SwingConstants.LEFT);
            add(info);
        }

        JButton updateButton = new JButton("Update All");
        add(updateButton);

        updateButton.setPreferredSize(new Dimension(75, 15));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateButtonActionPreformed(e);
            }
        });

        //Create textArea + label for each config value
        for (String key : configTreeSet) {
            String value = Config.getString(key);

            JLabel label = new JLabel(key);
            JTextArea textArea = new JTextArea(value);
            JButton button = new JButton("Default");

            label.setPreferredSize(new Dimension(100, 15));

            textArea.setPreferredSize(new Dimension(100, 12));
            textArea.setMaximumSize(new Dimension(100, 15));
            textArea.setMinimumSize(new Dimension(100, 10));

            button.setPreferredSize(new Dimension(75, 15));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    defaultButtonActionPreformed(e);
                }
            });

            label.setName("label_" + key);
            textArea.setName("textArea_" + key);
            button.setName("button_" + key);

            textAreas.add(textArea);
            componentMap.put(label.getName(), label);
            componentMap.put(textArea.getName(), textArea);
            componentMap.put(button.getName(), button);

            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setVerticalAlignment(SwingConstants.TOP);

            JPanel subPanel = new JPanel(new BorderLayout(10, 50));
            subPanel.add(label, BorderLayout.LINE_START);
            subPanel.add(textArea, BorderLayout.CENTER);
            subPanel.add(button, BorderLayout.LINE_END);

            subPanel.setPreferredSize(new Dimension(250, 15));
            subPanel.setMinimumSize(new Dimension(300, 15));
            subPanel.setMaximumSize(new Dimension(1500, 20));//<----NB!!!

            this.add(subPanel);
            this.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //createComponentMap();
    }

    private void updateButtonActionPreformed(java.awt.event.ActionEvent evt) {
        Object source = evt.getSource();
        if (source instanceof JButton) {

            for (JTextArea textArea : textAreas) {
                String key = textArea.getName().replace("textArea_", "");
                String value = textArea.getText();
                Config.setString(key, value);
            }
            Config.refresh();
        }
    }

    private void defaultButtonActionPreformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton) {
            JButton button = (JButton) source;
            String key = button.getName().replace("button_", "");

            JTextArea textArea = (JTextArea) getComponentByName("textArea_" + key);
            String value = Config.getDefaultString(key);
            textArea.setText(value);
            Config.setString(key, value);
        }
        Config.refresh();//TODO DRY maybe
    }

    @Override
    public void refreshConfig() {
        for (JTextArea textArea : textAreas) {
            String key = textArea.getName().replace("textArea_", "");
            textArea.setText(Config.getString(key));
        }
    }

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        } else {
            return null;
        }
    }
}
