/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 * Contient tout le jeu
 */
public class GameView extends javax.swing.JPanel implements MouseListener, MouseMotionListener{

    private GameModel gameModel;//Exemplaire de jeu
    private ArrayList<JLabel> listLabel;//Liste des label qu'il faut cliquer
    private ArrayList listLabelDigits;//Liste des label qu'il faut cliquer
    private ArrayList<JLabel> listDrag;//Liste des label qu'il faut cliquer
    private int groups;//Nombre de regroupement
    private int nbDecoupage;//Nombre de découpage qu'il faut avoir
    private Color[] colors;//Tableau de couleur pour les regroupements
    private boolean noise;//Si le noise est activé
    private boolean mean;//Si le mean est activé
    private boolean isDragging;//Si le joueur est en train de drag
    private Validation valid;//Classe pour la validation
    private String[] sColors = new String[] { "0X00FFFF", "0X7FFFD4", "0X89CFF0",
    "0XF4C2C2", "0X98777B", "0XFFBF00", "0XFBCEB1", "0XF0F8FF", "0X6495ED",
    "0X654321", "0X9BDDFF", "0XFBEC5D","0XFF7F50", "0X00FFFF", "0X99BADD",
    "0XF0E130", "0XFFF8DC"};//Liste de couleur à utiliser pour les regroupements
    
    /***
     * Initialise la fenêtre de jeu
     * @param gameNumbers Le model qui contient les labels avec les chiffres
     */
    public GameView(GameModel gameNumbers) {
        initComponents();
        valid = new Validation();
        gameModel = gameNumbers; 
        nbDecoupage = gameModel.getNbDecoupage();
        noise = false;
        mean = false;
        isDragging = false;
        listDrag = new ArrayList<>();
        listLabelDigits = new ArrayList();
        pnlInfo.setBackground(Color.WHITE);
        pnlCheckBox.setBackground(Color.WHITE);
        initialiseColors();
        setNewValues();          
    }
    /***
     * Initialise les couleurs pour les regroupements
     */
    private void initialiseColors(){
        colors = new Color[sColors.length];
        for (int i=0; i<sColors.length; i++)
            colors[i] = Color.decode(sColors[i]);
    }
    /***
     * Remet les valeurs à zéro et affiche de nouveaux chiffres à combiner
     */
    private void setNewValues(){
        panelDigits.removeAll();
        panelDigits.setLayout(new GridLayout());
        changeDigits();
        resetValue();
    }
    /***
     * Réinitialise les actions effectuées
     */
    private void resetValue(){ 
        lblChiffreSomme.setText(String.valueOf(gameModel.getSomme()));
        lblSommeCoursChiffre.setText("0");
        lblGroupsValue.setText("0");
        groups = 0;
        isDragging = false;
        listLabelDigits = new ArrayList();
        nbDecoupage = gameModel.getNbDecoupage();
        for (int i = 0; i < listLabel.size(); i++) {
            listLabel.get(i).removeMouseListener(this); // Pour eviter d'avoir plusieurs MouseListener si le bouton "reset" est cliqué plusieurs fois
            listLabel.get(i).setBackground(Color.WHITE);
            listLabel.get(i).addMouseListener(this);
        }
        this.updateUI();
    }
    /***
     * Affiche de nouveaux chiffres 
     */
    private void changeDigits(){
        ArrayList listDigits = gameModel.getListeChiffres();
        listLabel = new ArrayList();
        JLabel digitLabel;
        LineBorder line = new LineBorder(Color.BLACK,3,true);
        for (int i = 0; i < listDigits.size(); i++) {
            digitLabel = new JLabel(listDigits.get(i).toString());
            digitLabel.setFont(new Font("Arial",Font.PLAIN,24));
            digitLabel.setVerticalAlignment(SwingConstants.CENTER);
            digitLabel.setHorizontalAlignment(SwingConstants.CENTER);
            digitLabel.setBorder(line);
            digitLabel.setPreferredSize(new Dimension (100, 100));
            digitLabel.setOpaque(true);
            listLabel.add(digitLabel);
            panelDigits.add(listLabel.get(i)); 
        }       
    }    
    /***
     * Ajoute la valeur du label cliqué au total
     * @param labelClickedValue La valeur du label qui a été cliquer
     */
    private void setNewCurrentTotalValue(int labelClickedValue){
        int currentTotal = Integer.parseInt(lblSommeCoursChiffre.getText());
        int newCurrentTotal = valid.calculateCurrentTotalValue(currentTotal, labelClickedValue);
        lblSommeCoursChiffre.setText(String.valueOf(newCurrentTotal));
    }
    /***
     * Vérifie si le total courrant corespond au total attendu et change la couleur
     */
    private void checkIfGameEnd(){ 
        int sommeEnCours = Integer.parseInt(lblSommeCoursChiffre.getText());
        int sommeAAvoir = Integer.parseInt(lblChiffreSomme.getText());
        int validationResult = valid.checkGameOver(sommeEnCours, sommeAAvoir,groups, 
                nbDecoupage,noise,mean,listLabelDigits,listLabel);
        if(validationResult == 1){
            for (int i = 0; i < listLabel.size(); i++) {
                listLabel.get(i).setBackground(Color.GREEN);
            }
        }
        else if (validationResult == 2){            
            for (int i = 0; i < listLabel.size(); i++) {
                listLabel.get(i).setBackground(Color.RED);
            }  
        }
    }
    /***
     * Change la couleur du label cliqué
     * @param labelClicked Le label à changer de couleur
     */
    private void changeDigitColor(JLabel labelClicked){
        labelClicked.setBackground(colors[groups]);
        groups++;
        lblGroupsValue.setText(String.valueOf(groups));
        checkIfGameEnd();
        this.updateUI();
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
    }
    /***
     * Ajoute le label à la liste de label qui sont présentement selectionnés
     * @param me Le label cliquer
     */
    @Override
    public void mousePressed(MouseEvent me) {
        isDragging = true;
        Object event = me.getComponent();        
        JLabel label = listLabel.get(listLabel.lastIndexOf((JLabel) event));
        listDrag = new ArrayList<>();    
        listDrag.add(label);
    }
    /***
     * Change la couleur des labels selectionnés et change la somme totale
     * @param me Le clic
     */
    @Override
    public void mouseReleased(MouseEvent me) {
        isDragging = false;
        String newTotal = "";
        Color c = Color.decode("0X00FFFF");
        boolean orderValid = true;
        if(listDrag.size() >= 2){ //Vérifie si la selection de plusieurs chiffre est de gauche à droite
           if(listDrag.get(0).getX() > listDrag.get(1).getX()){
              orderValid = false;
           }
        }
        if(orderValid){
            changeDigitColor(listDrag.get(0));
            c = listDrag.get(0).getBackground();
            for (int i = 0; i < listDrag.size(); i++) {
                if(i < 2){ // Évite de selectionner des nombres de 3 chiffres
                    listDrag.get(i).setBackground(c);
                    listDrag.get(i).removeMouseListener(this);
                    newTotal += listDrag.get(i).getText();
                }   
            }
            listLabelDigits.add(Integer.parseInt(newTotal));
            setNewCurrentTotalValue(Integer.parseInt(newTotal));        
            checkIfGameEnd();
        }
    }
    /***
     * Lorsqu'un nouvel élément est selectionné et que le drag est actif
     * @param me Le nouvel élément que la souris vient de toucher
     */
    @Override
    public void mouseEntered(MouseEvent me) {
        if(isDragging){            
            java.awt.Point p = new java.awt.Point(me.getLocationOnScreen());
            SwingUtilities.convertPointFromScreen(p, me.getComponent());
            JLabel label = (JLabel) me.getSource();
            listDrag.add(label);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDigits = new javax.swing.JPanel();
        pnlInfo = new javax.swing.JPanel();
        btnNext = new javax.swing.JButton();
        btnGiveUp = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblChiffreSomme = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblSommeCoursChiffre = new javax.swing.JLabel();
        lblGroups = new javax.swing.JLabel();
        lblGroupsValue = new javax.swing.JLabel();
        lblResetNumber = new javax.swing.JLabel();
        lblResetNumberValue = new javax.swing.JLabel();
        btnRestart = new javax.swing.JButton();
        pnlCheckBox = new javax.swing.JPanel();
        checkMean = new javax.swing.JCheckBox();
        checkNoise = new javax.swing.JCheckBox();
        checkNoHelp = new javax.swing.JCheckBox();
        checkReverse = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(800, 300));

        javax.swing.GroupLayout panelDigitsLayout = new javax.swing.GroupLayout(panelDigits);
        panelDigits.setLayout(panelDigitsLayout);
        panelDigitsLayout.setHorizontalGroup(
            panelDigitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelDigitsLayout.setVerticalGroup(
            panelDigitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnNext.setText("Next");
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNextMouseClicked(evt);
            }
        });
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnGiveUp.setText("Give up");
        btnGiveUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGiveUpMouseClicked(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });

        jLabel1.setText("Total à atteindre :");

        lblChiffreSomme.setText("0");
        lblChiffreSomme.setName("lblChiffreSomme"); // NOI18N

        jLabel2.setText("Somme en cours :");

        lblSommeCoursChiffre.setText("0");
        lblSommeCoursChiffre.setAlignmentY(0.0F);

        lblGroups.setText("Nombre de groupes :");

        lblGroupsValue.setText("0");

        lblResetNumber.setText("Nombre de rénitialisation :");

        lblResetNumberValue.setText("0");

        btnRestart.setText("Restart");

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGroups)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(lblResetNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGroupsValue)
                    .addComponent(lblSommeCoursChiffre)
                    .addComponent(lblChiffreSomme)
                    .addComponent(lblResetNumberValue))
                .addGap(33, 33, 33)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGiveUp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnRestart))
                .addGap(17, 17, 17))
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lblChiffreSomme))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGiveUp)
                            .addComponent(jLabel2)
                            .addComponent(lblSommeCoursChiffre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReset)
                            .addComponent(lblGroups)
                            .addComponent(lblGroupsValue))))
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblResetNumber)
                            .addComponent(lblResetNumberValue)))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(btnRestart)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        btnNext.getAccessibleContext().setAccessibleName("btnNext");
        btnGiveUp.getAccessibleContext().setAccessibleName("btnGiveUp");
        btnReset.getAccessibleContext().setAccessibleName("btnReset");
        jLabel1.getAccessibleContext().setAccessibleName("lblSomme");
        lblChiffreSomme.getAccessibleContext().setAccessibleName("lblChiffreSomme");
        lblSommeCoursChiffre.getAccessibleContext().setAccessibleName("lblSommeCoursChiffre");

        checkMean.setText("Find mean");

        checkNoise.setText("Noise");

        checkNoHelp.setText("No help");

        checkReverse.setText("Reverse");

        javax.swing.GroupLayout pnlCheckBoxLayout = new javax.swing.GroupLayout(pnlCheckBox);
        pnlCheckBox.setLayout(pnlCheckBoxLayout);
        pnlCheckBoxLayout.setHorizontalGroup(
            pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkNoise)
                    .addComponent(checkMean)
                    .addComponent(checkNoHelp)
                    .addComponent(checkReverse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCheckBoxLayout.setVerticalGroup(
            pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(checkNoise)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkMean)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkNoHelp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkReverse)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        checkMean.getAccessibleContext().setAccessibleName("checkMean");
        checkNoise.getAccessibleContext().setAccessibleName("checkNoise");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(pnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(205, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDigits, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDigits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNextActionPerformed
    /***
     * Quand le bouton next est cliqué, un nouveau jeu est créer en fonction des
     * options
     * @param evt Le clic
     */
    private void btnNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextMouseClicked
        if(checkNoise.isSelected() && !checkMean.isSelected()){
            noise = true;
            mean = false;
        }
        else if(!checkNoise.isSelected() && checkMean.isSelected()){
            noise = false;
            mean = true;
        }
        else if(checkNoise.isSelected() && checkMean.isSelected()){
            noise = true;
            mean = true;
        }
        this.gameModel = new GameModel(noise,mean);
        setNewValues();    
    }//GEN-LAST:event_btnNextMouseClicked
    /***
     * Quand le bouton reset est cliqué, les valeurs sont remisent à zéro
     * @param evt Le clic
     */
    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        resetValue();
    }//GEN-LAST:event_btnResetMouseClicked
    /***
     * Quand le bouton Give Up est cliqué, la solution est affichée
     * @param evt Le clic
     */
    private void btnGiveUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGiveUpMouseClicked
        int noisePosition = gameModel.getNoisePosition();
        ArrayList listNumbers = gameModel.getListeNombres();
        lblSommeCoursChiffre.setText(String.valueOf(gameModel.getSomme()));
        lblGroupsValue.setText(String.valueOf(gameModel.getNbDecoupage()));
        groups = 0;
        int k = 0;
        int j = 0;
        while(k < listNumbers.size()){
            if((int)listNumbers.get(k) > 9){
                listLabel.get(j).setBackground(colors[groups]);
                listLabel.get(j + 1).setBackground(colors[groups]);
                listLabel.get(j + 1).removeMouseListener(this);
                j++;
            }
            else if(k == noisePosition){
              listLabel.get(j).setBackground(Color.BLACK); 
            }
            else{
              listLabel.get(j).setBackground(colors[groups]);  
            }
            listLabel.get(j).removeMouseListener(this);
            groups++;
            j++;
            k++;
        }        
        this.updateUI();
    }//GEN-LAST:event_btnGiveUpMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGiveUp;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnRestart;
    private javax.swing.JCheckBox checkMean;
    private javax.swing.JCheckBox checkNoHelp;
    private javax.swing.JCheckBox checkNoise;
    private javax.swing.JCheckBox checkReverse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblChiffreSomme;
    private javax.swing.JLabel lblGroups;
    private javax.swing.JLabel lblGroupsValue;
    private javax.swing.JLabel lblResetNumber;
    private javax.swing.JLabel lblResetNumberValue;
    private javax.swing.JLabel lblSommeCoursChiffre;
    private javax.swing.JPanel panelDigits;
    private javax.swing.JPanel pnlCheckBox;
    private javax.swing.JPanel pnlInfo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }
}
