package com.caesolutions.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AntigravityCellRenderer extends DefaultTableCellRenderer {
    
    // Cosmic translucent glass colors for rows
    private final Color selectionBackground = new Color(0, 180, 216, 80); // Cyan selection glow
    private final Color foregroundColor = Color.decode("#F8FAFC");

    public AntigravityCellRenderer() {
        setOpaque(false);
        setBorder(new EmptyBorder(8, 20, 8, 20));
        setForeground(foregroundColor);
        setFont(new Font("Inter", Font.PLAIN, 14));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        putClientProperty("is_selected", isSelected);
        putClientProperty("col_index", column);
        putClientProperty("col_count", table.getColumnCount());
        
        String colNameRaw = table.getColumnName(column);
        String colName = colNameRaw != null ? colNameRaw.replaceAll("<[^>]*>", "").toUpperCase() : "";
        putClientProperty("col_name", colName);
        putClientProperty("cell_value", value);

        if (colName.contains("TOTAL DEUDA") || colName.contains("RECARGO")) {
            setForeground(new Color(255, 85, 85)); // Red text for debt
            setFont(new Font("Inter", Font.BOLD, 14));
        } else {
            setForeground(foregroundColor);
            setFont(new Font("Inter", Font.BOLD, 13));
        }

        if (colName.contains("ESTADO") || colName.contains("ACCIONES")) {
            setText("");
        }
        
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isSelected = Boolean.TRUE.equals(getClientProperty("is_selected"));
        int width = getWidth();
        int height = getHeight();

        if (isSelected) {
            g2.setColor(selectionBackground);
            g2.fillRect(0, 0, width, height);
        }
        
        String colName = (String) getClientProperty("col_name");
        Object cellValue = getClientProperty("cell_value");
        
        if (colName != null && colName.contains("ESTADO") && cellValue != null) {
            String status = cellValue.toString();
            g2.setFont(new Font("Inter", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(status);
            int sh = fm.getAscent();
            
            int badgeW = sw + 20;
            int badgeH = 26;
            int badgeX = 15;
            int badgeY = (height - badgeH) / 2;
            
            if (status.equalsIgnoreCase("Activa") || status.equalsIgnoreCase("Activo") || status.equalsIgnoreCase("Vigente") || status.equalsIgnoreCase("Pagado") || status.equalsIgnoreCase("Procesado")) {
                g2.setColor(new Color(34, 197, 94));
            } else {
                g2.setColor(new Color(239, 68, 68));
            }
            g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, badgeH, badgeH);
            g2.setColor(Color.WHITE);
            g2.drawString(status, badgeX + 10, badgeY + fm.getAscent() + (badgeH - sh)/2 - 1);
        } else if (colName != null && colName.contains("ACCIONES")) {
            int badgeSize = 28;
            int badgeX = 15;
            int badgeY = (height - badgeSize) / 2;
            
            g2.setColor(new Color(239, 68, 68));
            g2.fillRoundRect(badgeX, badgeY, badgeSize, badgeSize, 10, 10);
            
            g2.setColor(Color.WHITE);
            g2.fillOval(badgeX + badgeSize/2 - 3, badgeY + badgeSize/2 - 3, 6, 6);
        }

        g2.dispose();
        super.paintComponent(g);
    }


}
