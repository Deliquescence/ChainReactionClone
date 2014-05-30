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
package Deliquescence;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

/**
 * Contains various utilities involving colors.
 *
 * @author Josh
 */
public class Colorizer {

    /**
     * Turns a grayscale image into a colorized one using a {@link Player}'s color.
     *
     * @param grayImage A gray image to be converted into color.
     * @param player The player whose color will be used to colorize.
     *
     * @return The colorized image.
     */
    public static Image colorize(Image grayImage, int player) {
        BufferedImageOp colorizeFilter = createColorizeOp(Config.getRGBFromPlayerID(player));
        BufferedImage targetImage = colorizeFilter.filter(toBufferedImage(grayImage), null);
        return targetImage;
    }

    /**
     * Returns a players color, given the numeric ID of the player.
     *
     * @param p The ID of the player.
     *
     * @return The color of the provided player.
     */
    public static Color getPlayerColor(int p) {
        int[] rgb = Config.getRGBFromPlayerID(p);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    //http://stackoverflow.com/questions/23763/colorizing-images-in-java
    //http://stackoverflow.com/questions/7621774/whats-the-appropriate-way-to-colorize-a-grayscale-image-with-transparency-in-ja
    /**
     * @see <a href="http://stackoverflow.com/questions/23763/colorizing-images-in-java">Colorizing images in Java</a>
     * @see <a href="http://stackoverflow.com/questions/7621774/whats-the-appropriate-way-to-colorize-a-grayscale-image-with-transparency-in-ja">What's the appropriate way to colorize a grayscale image with transparency in Java?</a>
     *
     * @param colors An array, {R, G, B}
     *
     * @return
     */
    protected static LookupOp createColorizeOp(int[] colors) {//short R1, short G1, short B1
        short[] alpha = new short[256];
        short[] red = new short[256];
        short[] green = new short[256];
        short[] blue = new short[256];

//int Y = 0.3*R + 0.59*G + 0.11*B
        for (short i = 255; i < 256; i++) {//keep white white
            alpha[i] = i;
            red[i] = i;
            green[i] = i;
            blue[i] = i;
        }
//        red[255] = (short) 0;
//        green[255] = (short) 0;
//        blue[255] = (short) 0;
        for (short i = 0; i < 255; i++) {
            alpha[i] = i;
            red[i] = (short) (colors[0] * (float) i / 255.0);
            green[i] = (short) (colors[1] * (float) i / 255.0);
            blue[i] = (short) (colors[2] * (float) i / 255.0);
        }

        short[][] data = new short[][]{
            red, green, blue, alpha
        };

        LookupTable lookupTable = new ShortLookupTable(0, data);
        return new LookupOp(lookupTable, null);
    }

    //http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     *
     * @return The converted BufferedImage
     *
     * @see <a href="http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage">Stack Overflow</a>
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

}
