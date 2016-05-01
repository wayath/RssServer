/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Bastien
 */
class PopUpMenu extends JPopupMenu {
    JMenuItem anItem;
    int indexToDelete;
    
    private OnClickOnDropDownMenu FeedPopUpEvent;

    public void setFeedPopUpEvent(OnClickOnDropDownMenu FeedPopUpEvent) {
        this.FeedPopUpEvent = FeedPopUpEvent;
    }
    
    public int getIndexToDelete() {
        return indexToDelete;
    }

    public void setIndexToDelete(int indexToDelete) {
        this.indexToDelete = indexToDelete;
    }
    
    public PopUpMenu(){
        anItem = new JMenuItem("Remove Feed");
        anItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FeedPopUpEvent.OnDeleteFeed(indexToDelete);
            }
           
        });
        
        add(anItem);
    }
    
    public interface  OnClickOnDropDownMenu {
        void OnDeleteFeed(int indexFeed);
    }
}
