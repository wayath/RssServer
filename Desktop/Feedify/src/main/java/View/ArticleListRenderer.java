/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Bastien
 */
public class ArticleListRenderer extends DefaultListCellRenderer {
    
    JSeparator separator;
    final String SEPARATOR = "SEPARATOR";
     public ArticleListRenderer() {
      setOpaque(true);
      setBorder(new EmptyBorder(1, 1, 1, 1));
      separator = new JSeparator(JSeparator.HORIZONTAL);
    }
     
    @Override
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
      String str = (value == null) ? "" : value.toString();
       if (SEPARATOR.equals(str)) {
        return separator;
      }
      return this;
    }
}
