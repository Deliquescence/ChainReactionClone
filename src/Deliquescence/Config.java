/* 
 * Copyright (c) 2014, Deliquescence <Deliquescence1@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors
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
package Deliquescence;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 * This allows various settings be be stored and retrieved.
 *
 * @author Josh
 */
public class Config {

    protected static Properties props;
    protected static Properties defaultProps;
    protected static boolean fileInitialized;

    private static Map<Integer, Image[]> imagesByPlayerID;
    private static FileOutputStream configOutputStream;

    public static ArrayList<Refreshable> refreshables = new ArrayList<>();
    public static File configFile;

    public static void init() {

        defaultProps = new Properties();
        defaultProps = setDefaultProperties(defaultProps);

        try {
            props = new Properties(defaultProps);

            String filePath = System.getProperty("user.home") + "\\ChainReactionClone\\config.txt";
            configFile = new File(filePath);
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
            FileInputStream fileInput = new FileInputStream(configFile);

            props.load(fileInput);

            configOutputStream = new FileOutputStream(configFile);

            fileInitialized = true;

            saveFile();//Write defaults
        } catch (IOException ex) {
            System.err.println("Error loading config! Only default values will be used and no new values saved!");
            System.err.println(ex);
            fileInitialized = false;
        }

        initImages();
    }

    private static Properties setDefaultProperties(Properties p) {
        p.setProperty("FRAME_WIDTH", "800");
        p.setProperty("FRAME_HEIGHT", "800");
        p.setProperty("MAX_PLAYERS", "8");//Maybe changeable someday? Need colors
        p.setProperty("CELL_SIZE", "50");//Size of board tiles
        p.setProperty("MAX_COLUMNS", "15");
        p.setProperty("MAX_ROWS", "10");
        p.setProperty("TITLE", "Chain Reaction Clone");
        p.setProperty("Color_1", "255,0,0");
        p.setProperty("Color_2", "0,255,0");
        p.setProperty("Color_3", "0,0,255");
        p.setProperty("Color_4", "255,255,0");
        p.setProperty("Color_5", "255,0,255");
        p.setProperty("Color_6", "0,255,255");
        p.setProperty("Color_7", "255,128,0");
        p.setProperty("Color_8", "255,255,255");
        p.setProperty("NETWORK_PORT", "22222");

        return p;
    }

    public static void initImages() {
        Image[] images;
        imagesByPlayerID = new HashMap<>();
        images = new Image[5]; //Dont forget about 0

        for (int i = 0; i < 5; i++) {
            java.net.URL imgURL = Main.class.getResource("/TileImages/" + i + ".png");
            images[i] = (new ImageIcon(imgURL)).getImage();
        }

        imagesByPlayerID.put(0, images);
        for (int player = 1; player <= Config.getInt("MAX_PLAYERS"); player++) {
            Image[] colorizedImages = new Image[5];
            for (int i = 0; i < 5; i++) {
                colorizedImages[i] = Colorizer.colorize(images[i], player);
            }
            imagesByPlayerID.put(player, colorizedImages);
        }
    }

    /**
     * Refresh the player colors from the config.
     */
    public static void refreshColors() {
        initImages();
    }

    /**
     * Gets the standard name of the player, "Player _"
     *
     * @param playerInt The player ID of the player.
     *
     * @return The default name of the player.
     */
    public static String getDefaultPlayerName(int playerInt) {
        return "Player " + playerInt;
    }

    /**
     * Gets a config value as an integer.
     *
     * @param key The key of the property
     *
     * @return The value of the property
     */
    public static int getInt(String key) {
        return Integer.valueOf(props.getProperty(key));
    }

    /**
     * Gets a config value.
     *
     * @param key The key of the property
     *
     * @return The value of the property
     */
    public static String getString(String key) {
        return props.getProperty(key);
    }

    /**
     * Sets a property value in the config.
     *
     * @param key The key of the property
     *
     * @param The value of the property
     */
    public static void setString(String key, String value) {
        props.setProperty(key, value);
        saveFile();
    }

    /**
     * Sets an integer property value in the config.
     *
     * @param key The key of the property
     *
     * @param The value of the property
     */
    public static void setInt(String key, int value) {
        props.setProperty(key, Integer.toString(value));
        saveFile();
    }

    /**
     * Gets a Set of all the keys in the config.
     *
     * @return Set of all the keys.
     */
    public static Set<String> getKeys() {
        return props.stringPropertyNames();
    }

    /**
     * Gets the default value of a property
     *
     * @param key The key of the property
     *
     * @return The value of the property
     */
    public static String getDefaultString(String key) {
        return defaultProps.getProperty(key);
    }

    /**
     * Refresh all the registered refreshables so they show the updated config.
     */
    public static void refresh() {
        refreshColors();
        for (Refreshable refresh : refreshables) {
            refresh.refreshConfig();
        }
    }

    /**
     * Saves the configuration to a file.
     */
    public static void saveFile() {
        if (fileInitialized) {
            try {
                props.store(configOutputStream, configFile.getPath());
            } catch (IOException ex) {
                System.err.println("Could not save config file!");
                System.err.println(ex);
            }
        }
    }

    public static Image getImageByPlayerID(int player, int num) {
        return imagesByPlayerID.get(player)[num];
    }

    /**
     * Gets the configured RGB triplet of the player.
     *
     * @param p The player ID for the RGB
     *
     * @return Array of the RGB triplet
     */
    public static int[] getRGBFromPlayerID(int p) {
        String[] stringInts;
        switch (p) {
            case 1:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 2:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 3:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 4:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 5:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 6:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 7:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            case 8:
                stringInts = props.getProperty("Color_" + p).split(",");
                break;
            default:
                stringInts = props.getProperty("Color_" + p, "0,0,0").split(",");
                break;
        }
        int[] RGB = new int[3];
        RGB[0] = Integer.valueOf(stringInts[0]);
        RGB[1] = Integer.valueOf(stringInts[1]);
        RGB[2] = Integer.valueOf(stringInts[2]);
        return RGB;
    }
}
