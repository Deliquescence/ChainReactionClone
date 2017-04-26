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
package deliquescence.Panel;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * A panel to contain both game setup panels and actual {@link GamePanel}s.
 * Tabs are used to accomplish this.
 *
 * @author Josh
 */
public class GameManager extends JPanel {

    ArrayList<GamePanel> currentGames;

    /**
     * Creates a new Game manager. Has tabs with {@link GamePanel}s and setup panels.
     */
    public GameManager() {
        initComponents();
    }

    /**
     * Removes a tab in this manager.
     *
     * @param component The tab to remove.
     */
    public void removeTab(Component component) {
        jTabbedPane1.remove(jTabbedPane1.indexOfComponent(component));
    }

    /**
     * Adds a new, closeable {@link GamePanel} to this manager with default name of "Game" and also switch to it.
     *
     * @param game The GamePanel to add to this manager.
     */
    public void addGameTab(GamePanel game) {
        addTab("Game", game, true, true);
    }

    /**
     * Adds a new tab to the manager.
     *
     * @param name The title of the added tab.
     * @param component The component to add.
     */
    public void addTab(String name, Component component) {
        addTab(name, component, false, false);
    }

    /**
     * Adds a new tab to the manager. It is closeable if specified.
     *
     * @param name The title of the added tab.
     * @param component The component to add.
     * @param closeable True if the tab will have a close button.
     */
    public void addTab(String name, Component component, boolean closeable) {
        addTab(name, component, closeable, false);
    }

    /**
     * Adds a new tab to the manager, switched to and closeable if specified.
     *
     * @param name The title of the added tab.
     * @param component The component to add.
     * @param closeable True if the tab will have a close button.
     * @param Switch True to set focus to the new tab.
     */
    public void addTab(String name, Component component, boolean closeable, boolean Switch) {
        jTabbedPane1.addTab(name, component);

        if (closeable) {
            jTabbedPane1.setTabComponentAt(jTabbedPane1.indexOfComponent(component), new ButtonTabComponent(jTabbedPane1));
        }
        if (Switch) {
            jTabbedPane1.setSelectedComponent(component);
        }
    }

    public void switchToTabByTitle(String title) {
        Component[] components = jTabbedPane1.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (jTabbedPane1.getTitleAt(i).equals(title)) {
                jTabbedPane1.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });
        jTabbedPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jTabbedPane1ComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTabbedPane1ComponentShown(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTabbedPane1ComponentShown
        Component comp = evt.getComponent();
        if (comp instanceof GamePanel) {
            GamePanel game = (GamePanel) comp;
            game.startTimer();
        }
    }//GEN-LAST:event_jTabbedPane1ComponentShown

    private void jTabbedPane1ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTabbedPane1ComponentHidden
        Component comp = evt.getComponent();
        if (comp instanceof GamePanel) {
            GamePanel game = (GamePanel) comp;
            game.stopTimer();
        }
    }//GEN-LAST:event_jTabbedPane1ComponentHidden

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        Component comp = jTabbedPane1.getSelectedComponent();

        if (comp instanceof GamePanel) {

            GamePanel game = (GamePanel) comp;
            game.startTimer();

        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
