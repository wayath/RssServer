/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Articles.AllArticlesModel;
import Model.Articles.GetArticleResponse.Articles;
import Model.User.UsersResponse;
import Model.User.UserModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Bastien
 */
public class FeedView extends javax.swing.JPanel implements PopUpMenu.OnClickOnDropDownMenu {
    JList feedList;
    JList feedListContent;
    DefaultListModel model;
    DefaultListModel ContentModel;
    FeedListRenderer customCell = new FeedListRenderer();
    ArticleDetailView DetailPanel;
    JScrollPane spane;
    Boolean DetailViewOpen;
    Boolean UserViewOpen;
    int currentPage;
    DefaultListModel usernames = new DefaultListModel();
    JScrollPane UserListpane;
    
    private OnFeedViewEventRaised FeedEvent;
    
    /**
     * Creates new form FeedView
     */
    public FeedView() {
        initComponents();
        this.setBackground(new Color(27, 193, 132));
        this.MenuPanelHolder.setBackground(new Color(44, 62, 80));
        this.FeedListPanel.setBackground(new Color(44, 62, 80));
        this.UserInfosPanel.setBackground(new Color(44, 62, 80));
        this.LoginSettings.setText(UserModel.Instance.getLogin());
        this.currentPage = 1;
        this.PageLabel.setText(String.valueOf(this.currentPage));
        this.DetailViewOpen = false;
        this.UserViewOpen = false;
        initFeedListView();
        initContentFeedView();
    }
    
    public void initFeedListView() {
        this.FeedListPanel.setLayout(new BorderLayout());
        model = new DefaultListModel();
        model.addElement("All articles");
        feedList = new JList(model);
        feedList.setBackground(new Color(44, 62, 80));
        feedList.setCellRenderer(customCell);
        
        feedList.setSelectedIndex(0);
        feedList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 2) {
                    setCurrentPage(1);
                    if (feedList.getSelectedIndex() == 0) {
                        if (FeedEvent != null) {
                            FeedEvent.getAllFeed(-1, currentPage);
                        }
                    } else {
                        // System.out.println(feedList.getSelectedIndex() - 1);
                        if (FeedEvent != null) {
                            FeedEvent.getAllFeed(UserModel.Instance.getUserFeeds().get(feedList.getSelectedIndex() - 1).getId(), currentPage);
                        }
                    }
                    FeedName.setText(feedList.getSelectedValue().toString());
                }
            }
        });
        PopUpMenu menu = new PopUpMenu();
        menu.setFeedPopUpEvent(this);
        feedList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JList list = (JList) e.getSource();
                    int index = list.locationToIndex(e.getPoint());
                    list.setSelectedIndex(index);
                    if (index != 0) {
                        menu.setIndexToDelete(index);
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

        });
        JScrollPane pane = new JScrollPane(feedList);
        pane.setBorder(null);
        JButton addButton = new JButton("Add Feed");
        this.FeedListPanel.add(pane, BorderLayout.CENTER);
        this.FeedListPanel.add(addButton, BorderLayout.PAGE_END);
        addButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 DisplayAddFeedPopup();
             }
         });
    }
    
    public void reloadFeed() {
        if (model.size() > 1) {
            model.removeAllElements();
            model.addElement("All Articles");
        }
        for (int i = 0; i < UserModel.Instance.getUserFeeds().size(); i++) {
            //+ 1 to leave the "Tous" on top of the list
            model.add(i + 1, UserModel.Instance.getUserFeeds().get(i).getName());// + "   " + UserModel.Instance.getUserFeeds().get(i).getNew_articles());
        }
        feedList.setSelectedIndex(0);
        FeedName.setText("All Articles");
    }
    
    public void initContentFeedView() {
        FeedPanel.setLayout(new BorderLayout());
        ContentModel = new DefaultListModel();
        feedListContent = new JList(ContentModel);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer)feedListContent.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        spane = new JScrollPane(feedListContent);    
        FeedPanel.add(spane, BorderLayout.CENTER);
        feedListContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() >= 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    DetailPanel = new ArticleDetailView();
                    try {
                        DetailPanel.setDesignWithData(AllArticlesModel.Instance.getAllArticles().get(index));
                        
                    } catch (BadLocationException ex) {
                        Logger.getLogger(FeedView.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FeedView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    FeedPanel.remove(spane);
                    FeedPanel.add(DetailPanel, BorderLayout.CENTER);
                    DetailViewOpen = true;
                    BackButton.setEnabled(true);
                    feedListContent.setVisible(false);
                    spane.setVisible(false);
                    FeedPanel.revalidate();
                }
            }
        });
    }

    @Override
    public void OnDeleteFeed(int indexFeed) {
        if (FeedEvent != null) {
            FeedEvent.DeleteFeed(UserModel.Instance.getUserFeeds().get(indexFeed - 1).getId());
        }
    }
    
    public void RefreshAndDumpArticles() {
        ContentModel.removeAllElements();
        if (DetailViewOpen) {
            CloseDetailView();
        } else if (UserViewOpen) {
            CloseUserView();
        }
        for (int i = 0; i < AllArticlesModel.Instance.getAllArticles().size(); i++) {
            Articles article = AllArticlesModel.Instance.getAllArticles().get(i);
            ContentModel.addElement(article.getTitle());
            //previewPanel.add(new ArticlePreviewView(article.getTitle(), article.getPreview()));
            
        }
    }
    
    void CloseDetailView() {
        DetailViewOpen = false;
        FeedPanel.remove(DetailPanel);
        DetailPanel = null;
        FeedPanel.add(spane, BorderLayout.CENTER);
        feedListContent.setVisible(true); 
        spane.setVisible(true);
        BackButton.setEnabled(false);
        FeedPanel.revalidate();
    }
    
    void CloseUserView() {
        UserViewOpen = false;
        FeedPanel.remove(UserListpane);
        UserListpane = null;
        FeedPanel.add(spane, BorderLayout.CENTER);
        feedListContent.setVisible(true);
        spane.setVisible(true);
        BackButton.setEnabled(false);
        FeedPanel.revalidate();
    }
    
    public void addThisFeedToList(String elem) {
        model.addElement(elem);
    }
    
    void DisplayAddFeedPopup() {
        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Feed Name :"));
        panel.add(field1);
        panel.add(new JLabel("URL :"));
        panel.add(field2);
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter feed informations",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (field1.getText().isEmpty() || field2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There is an empty field", "Add feed", JOptionPane.ERROR_MESSAGE);
            } else {
                if (FeedEvent != null) {
                    FeedEvent.OnAddFeedComplete(field1.getText(), field2.getText());    
                }
            }
            FeedListPanel.revalidate();
            FeedListPanel.repaint();
        } else {
            System.out.println("Cancelled");
        }
    }

    
    //Raise Events
    public interface OnFeedViewEventRaised {
        void OnAddFeedComplete(String name, String URL);
        void getAllFeed(int id, int page);
        void UpdateUserInfos(String username, String Password, Boolean type);
        void getAllUsers();
        void logout();
        void DeleteFeed(int feedid);
        void MarkArticleAsRead(int articleid);
    }
    
    public void setOnFeedViewEventRaised(OnFeedViewEventRaised connectEvent) {
        this.FeedEvent = connectEvent;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        this.PageLabel.setText(String.valueOf(currentPage));
        if (this.currentPage > 1) {
            this.PreviousPageButton.setEnabled(true);
        } else {
            this.PreviousPageButton.setEnabled(false);
        }
    }
    
    public void DisplayUserList(List<UsersResponse.User> users) {
        this.FeedName.setText("USER LIST");
        usernames.removeAllElements();
        users.stream().forEach((user) -> {
            usernames.addElement(user.getUsername() + "      [" + user.getType() + "]");
            System.out.println(user.getUsername() + "      [" + user.getType() + "]");
        });
        if (DetailViewOpen) {
            DetailViewOpen = false;
            FeedPanel.remove(DetailPanel);
            DetailPanel = null;
        } else {
            FeedPanel.remove(spane);
        }
        if (UserListpane != null) {
            FeedPanel.remove(UserListpane);
        }
        this.UserViewOpen = true;
        JList userList = new JList(usernames);
        UserListpane = new JScrollPane(userList);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) userList.getCellRenderer();
        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() >= 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    UsersResponse.User User = users.get(index);
                    Boolean isAdmin = (User.getType().equals("admin")) ? true : false;
                    openChangeInfoUser(User.getUsername(), isAdmin);
                }
            }
        });
        renderer.setHorizontalAlignment(JLabel.CENTER);
        FeedPanel.add(UserListpane, BorderLayout.CENTER);
        FeedPanel.revalidate();
        FeedPanel.repaint();
    }
    
    void openChangeInfoUser(String username, Boolean isAdmin) {
        JTextField loginField = new JTextField(username);
        loginField.setEditable(false);
        JPasswordField field2 = new JPasswordField(""); 
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Login :"));
        panel.add(loginField);
        panel.add(new JLabel("Password :"));
        panel.add(field2);
        JRadioButton checkBox = new JRadioButton("admin");
        if (UserModel.Instance.getIsAdmin()) {
            checkBox.setSelected(isAdmin);
            panel.add(checkBox);
        }
        
       int result = JOptionPane.showConfirmDialog(null, panel, "Change user informations",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (loginField.getText().isEmpty() || field2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "A field is empty!", "Add feed", JOptionPane.ERROR_MESSAGE);
            } else {
                if (FeedEvent != null) {
                    FeedEvent.UpdateUserInfos(loginField.getText(), field2.getText(), checkBox.isSelected());
                }
            }
            FeedListPanel.revalidate();
            FeedListPanel.repaint();
        } else {
            System.out.println("Cancelled");
        }
    }
    
    void deleteAccountWarning() {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ta mere?", "WARNING",
                JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
        if (FeedEvent != null) {
            FeedEvent.logout();     
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        MenuPanelHolder = new javax.swing.JPanel();
        UserInfosPanel = new javax.swing.JPanel();
        FEEDIFY = new javax.swing.JLabel();
        LoginSettings = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        FeedListPanel = new javax.swing.JPanel();
        FeedPanelHolder = new javax.swing.JPanel();
        FeedPanelHeader = new javax.swing.JPanel();
        Refresh = new javax.swing.JButton();
        FeedName = new javax.swing.JLabel();
        BackButton = new javax.swing.JButton();
        FeedPanel = new javax.swing.JPanel();
        FeedPanelFooter = new javax.swing.JPanel();
        PreviousPageButton = new javax.swing.JButton();
        PageLabel = new javax.swing.JLabel();
        NextPageButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        MenuPanelHolder.setPreferredSize(new java.awt.Dimension(180, 580));
        MenuPanelHolder.setLayout(new java.awt.BorderLayout());

        UserInfosPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UserInfosPanel.setPreferredSize(new java.awt.Dimension(180, 100));
        UserInfosPanel.setLayout(new java.awt.GridBagLayout());

        FEEDIFY.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        FEEDIFY.setForeground(new java.awt.Color(255, 255, 255));
        FEEDIFY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FEEDIFY.setText("FEEDIFY");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 15, 0);
        UserInfosPanel.add(FEEDIFY, gridBagConstraints);

        LoginSettings.setText("Login");
        LoginSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginSettingsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        UserInfosPanel.add(LoginSettings, gridBagConstraints);

        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        UserInfosPanel.add(Logout, gridBagConstraints);

        MenuPanelHolder.add(UserInfosPanel, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout FeedListPanelLayout = new javax.swing.GroupLayout(FeedListPanel);
        FeedListPanel.setLayout(FeedListPanelLayout);
        FeedListPanelLayout.setHorizontalGroup(
            FeedListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        FeedListPanelLayout.setVerticalGroup(
            FeedListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );

        MenuPanelHolder.add(FeedListPanel, java.awt.BorderLayout.CENTER);

        add(MenuPanelHolder, java.awt.BorderLayout.WEST);

        FeedPanelHolder.setPreferredSize(new java.awt.Dimension(1000, 609));
        FeedPanelHolder.setLayout(new java.awt.BorderLayout());

        FeedPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        FeedPanelHeader.setLayout(new java.awt.GridBagLayout());

        Refresh.setBackground(new java.awt.Color(27, 193, 132));
        Refresh.setForeground(new java.awt.Color(255, 255, 255));
        Refresh.setText("Refresh");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        FeedPanelHeader.add(Refresh, gridBagConstraints);

        FeedName.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        FeedName.setText("All articles");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        FeedPanelHeader.add(FeedName, gridBagConstraints);

        BackButton.setBackground(new java.awt.Color(27, 193, 132));
        BackButton.setForeground(new java.awt.Color(255, 255, 255));
        BackButton.setText("Back");
        BackButton.setEnabled(false);
        BackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        FeedPanelHeader.add(BackButton, gridBagConstraints);

        FeedPanelHolder.add(FeedPanelHeader, java.awt.BorderLayout.PAGE_START);

        FeedPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout FeedPanelLayout = new javax.swing.GroupLayout(FeedPanel);
        FeedPanel.setLayout(FeedPanelLayout);
        FeedPanelLayout.setHorizontalGroup(
            FeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        FeedPanelLayout.setVerticalGroup(
            FeedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );

        FeedPanelHolder.add(FeedPanel, java.awt.BorderLayout.CENTER);

        FeedPanelFooter.setBackground(new java.awt.Color(255, 255, 255));
        FeedPanelFooter.setLayout(new java.awt.GridBagLayout());

        PreviousPageButton.setBackground(new java.awt.Color(27, 193, 132));
        PreviousPageButton.setForeground(new java.awt.Color(255, 255, 255));
        PreviousPageButton.setText("Previous Page");
        PreviousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        FeedPanelFooter.add(PreviousPageButton, gridBagConstraints);

        PageLabel.setText("1");
        FeedPanelFooter.add(PageLabel, new java.awt.GridBagConstraints());

        NextPageButton.setBackground(new java.awt.Color(27, 193, 132));
        NextPageButton.setForeground(new java.awt.Color(255, 255, 255));
        NextPageButton.setText("Next Page");
        NextPageButton.setMaximumSize(new java.awt.Dimension(101, 23));
        NextPageButton.setMinimumSize(new java.awt.Dimension(101, 23));
        NextPageButton.setPreferredSize(new java.awt.Dimension(101, 27));
        NextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        FeedPanelFooter.add(NextPageButton, gridBagConstraints);

        FeedPanelHolder.add(FeedPanelFooter, java.awt.BorderLayout.PAGE_END);

        add(FeedPanelHolder, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void NextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextPageButtonActionPerformed
        if (feedList.getSelectedIndex() == 0) {
            if (FeedEvent != null) {
                FeedEvent.getAllFeed(-1, currentPage + 1);
            }
        } else {
            if (FeedEvent != null) {
                FeedEvent.getAllFeed(UserModel.Instance.getUserFeeds().get(feedList.getSelectedIndex() - 1).getId(), currentPage + 1);
            }
        }
    }//GEN-LAST:event_NextPageButtonActionPerformed

    private void PreviousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreviousPageButtonActionPerformed
        if (feedList.getSelectedIndex() == 0) {
            if (FeedEvent != null) {
                FeedEvent.getAllFeed(-1, currentPage - 1);
            }
        } else {
            if (FeedEvent != null) {
                FeedEvent.getAllFeed(UserModel.Instance.getUserFeeds().get(feedList.getSelectedIndex() - 1).getId(), currentPage - 1);
            }
        }
    }//GEN-LAST:event_PreviousPageButtonActionPerformed

    private void BackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackButtonActionPerformed
       if (this.DetailViewOpen) {
            CloseDetailView();
       } else if (this.UserViewOpen) {
           CloseUserView();
       }
    }//GEN-LAST:event_BackButtonActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        if (FeedEvent != null) {
            FeedEvent.logout();  
        }
    }//GEN-LAST:event_LogoutActionPerformed

    private void LoginSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginSettingsActionPerformed
        if (UserModel.Instance.getIsAdmin()) {
            if (FeedEvent != null) {
                FeedEvent.getAllUsers();
            }
        } else {
            openChangeInfoUser(UserModel.Instance.getLogin(), false);
        }
    }//GEN-LAST:event_LoginSettingsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackButton;
    private javax.swing.JLabel FEEDIFY;
    private javax.swing.JPanel FeedListPanel;
    private javax.swing.JLabel FeedName;
    private javax.swing.JPanel FeedPanel;
    private javax.swing.JPanel FeedPanelFooter;
    private javax.swing.JPanel FeedPanelHeader;
    private javax.swing.JPanel FeedPanelHolder;
    private javax.swing.JButton LoginSettings;
    private javax.swing.JButton Logout;
    private javax.swing.JPanel MenuPanelHolder;
    private javax.swing.JButton NextPageButton;
    private javax.swing.JLabel PageLabel;
    private javax.swing.JButton PreviousPageButton;
    private javax.swing.JButton Refresh;
    private javax.swing.JPanel UserInfosPanel;
    // End of variables declaration//GEN-END:variables

}
