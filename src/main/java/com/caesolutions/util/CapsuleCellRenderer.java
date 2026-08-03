package com.caesolutions.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CapsuleCellRenderer extends DefaultTableCellRenderer {
    
    private final Color defaultBackground = Color.decode("#1E293B"); // Slate 800
    private final Color selectionBackground = Color.decode("#334155"); // Slate 700
    private final Color foregroundColor = Color.decode("#F8FAFC");
    private final int arc = 20;

    public CapsuleCellRenderer() {
        setOpaque(false); // Let paintComponent handle the background
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Inner padding for cell text
        setForeground(foregroundColor);
        setFont(new Font("Inter", Font.PLAIN, 13));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Save these for paintComponent
        putClientProperty("is_selected", isSelected);
        putClientProperty("col_index", column);
        putClientProperty("col_count", table.getColumnCount());
        
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isSelected = Boolean.TRUE.equals(getClientProperty("is_selected"));
        int col = (int) getClientProperty("col_index");
        int count = (int) getClientProperty("col_count");

        g2.setColor(isSelected ? selectionBackground : defaultBackground);

        int width = getWidth();
        int height = getHeight();

        // Draw shape based on column position to form a continuous capsule for the row
        if (count == 1) {
            // Single column: round all corners
            g2.fillRoundRect(0, 0, width, height, arc, arc);
        } else if (col == 0) {
            // First column: round left corners
            g2.fillRoundRect(0, 0, width + arc, height, arc, arc);
            g2.fillRect(arc, 0, width - arc, height);
        } else if (col == count - 1) {
            // Last column: round right corners
            g2.fillRoundRect(-arc, 0, width + arc, height, arc, arc);
            g2.fillRect(0, 0, width - arc, height);
        } else {
            // Middle columns: flat
            g2.fillRect(0, 0, width, height);
        }

        g2.dispose();
        
        super.paintComponent(g); // Paint the text
    }
}
