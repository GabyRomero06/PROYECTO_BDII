package com.caesolutions.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.InputStream;

public class ImageUtils {
    
    /**
     * Loads the cae_solutions.jpeg logo, removes the white background by making it transparent,
     * and optionally scales it to the specified dimensions.
     * 
     * @param width Target width, or -1 to keep original width
     * @param height Target height, or -1 to keep original height
     * @return The processed transparent Image
     */
    public static Image getTransparentLogo(int width, int height) {
        try {
            InputStream is = ImageUtils.class.getResourceAsStream("/cae_solutions.jpeg");
            if (is == null) {
                System.err.println("No se encontro cae_solutions.jpeg en resources");
                return null;
            }
            
            BufferedImage img = ImageIO.read(is);

            // Filtro para hacer transparente el fondo blanco (con tolerancia para JPEG)
            ImageFilter filter = new RGBImageFilter() {
                @Override
                public final int filterRGB(int x, int y, int rgb) {
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    
                    // Considerar pixeles casi blancos (>220) como fondo
                    if (r > 220 && g > 220 && b > 220) {
                        return 0x00FFFFFF & rgb; // Transparente
                    }
                    return rgb;
                }
            };

            ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
            Image transparentImg = Toolkit.getDefaultToolkit().createImage(ip);
            
            if (width > 0 && height > 0) {
                return transparentImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
            return transparentImg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
