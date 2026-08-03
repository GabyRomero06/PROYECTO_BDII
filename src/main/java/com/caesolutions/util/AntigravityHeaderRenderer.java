package com.caesolutions.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AntigravityHeaderRenderer extends DefaultTableCellRenderer {
    
    private final Color foregroundColor = new Color(0, 200, 255); // Light cyan
    private final Color separatorColor = new Color(30, 58, 138); // #1E3A8A (discreet line)

    public AntigravityHeaderRenderer() {
        setOpaque(false);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setForeground(foregroundColor);
        setFont(new Font("Inter", Font.BOLD, 13));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        // Convert text to UPPERCASE (HTML tags like <html> will be uppercased too, which is valid)
        Object upperValue = value != null ? value.toString().toUpperCase() : "";
        
        super.getTableCellRendererComponent(table, upperValue, isSelected, hasFocus, row, column);
        putClientProperty("col_index", column);
        putClientProperty("col_count", table.getColumnCount());
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw a thin horizontal separator at the bottom of the header
        g2.setColor(separatorColor);
        g2.drawLine(0, height - 1, width, height - 1);

        g2.dispose();
        super.paintComponent(g);
    }
}
