package com.caesolutions.util;

import javax.swing.*;
import java.awt.*;

public class AntigravityContainer extends JPanel {
    
    // Cosmic translucent glass colors
    private final Color glassColor = new Color(15, 12, 41, 160); // Deep cosmic purple/blue with alpha
    private final Color glintColor1 = new Color(0, 180, 216, 255); // Bright cyan glowing edge
    private final Color glintColor2 = new Color(0, 180, 216, 30);  // Faded cyan
    private final int arc = 50; // Very rounded pebble shape

    public AntigravityContainer() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Background (Glassmorphism effect)
        g2.setColor(glassColor);
        g2.fillRoundRect(2, 2, width - 4, height - 4, arc, arc);

        // 2. Glowing border / Glint (Cyan gradient)
        GradientPaint borderGradient = new GradientPaint(
            0, 0, glintColor1, 
            width, height, glintColor2
        );
        g2.setPaint(borderGradient);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawRoundRect(2, 2, width - 4, height - 4, arc, arc);

        // 3. Inner subtle light trail (top edge reflection)
        g2.setStroke(new BasicStroke(1.0f));
        g2.setColor(new Color(255, 255, 255, 40));
        g2.drawRoundRect(4, 4, width - 8, height - 8, arc - 4, arc - 4);

        g2.dispose();
        super.paintComponent(g);
    }
}
