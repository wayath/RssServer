/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Bastien
 */
public class FeedListRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
        super.getListCellRendererComponent(list, value, index, isSelected, isSelected);
        
        setForeground(Color.white);
        
        return (this);
    }
}
