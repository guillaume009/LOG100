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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Contient tout le jeu
 */
public class GameView extends javax.swing.JPanel implements MouseListener, MouseMotionListener{

    /*private GameModel gameModel;//Exemplaire de jeu
    private ArrayList<JLabel> listLabel;//Liste des label qu'il faut cliquer
    private ArrayList listLabelDigits;//Liste des label qu'il faut cliquer
    private int groups;//Nombre de regroupement
    private int nbDecoupage;//Nombre de découpage qu'il faut avoir
    private boolean noise;//Si le noise est activé
    private boolean mean;//Si le mean est activé
    private boolean noHelp;//Si le no help est activé
    private boolean reverse;//Si le reverse est activé
    private boolean modeEntrainement;//Si le mode entrainement est actif
    private boolean modeArcade;// Si le mode arcade est actif
    private boolean modeReplay;//Si le mode replay est actif
    private int nbReset;//Nombre de fois que le jeu a été réinitialisé
    private int nbPoints;//Nombre de point
    private int timerMin;//La valeur des minutes du timer
    private int timerSec;//La valeur des secondes du timer*/
    
    private GameObject gameObject;
    private ArrayList<JLabel> listDrag;//Liste des label qu'il faut cliquer
    private ArrayList<GameObject> listGameObject;//Liste des label qu'il faut cliquer
    private Color[] colors;//Tableau de couleur pour les regroupements
    private Timer timer;//Le composant qui augmente le temps
    private Validation valid;//Classe pour la validation
    private saveData save;//Classe pour la sauvegarde
    private boolean isDragging;//Si le joueur est en train de drag
    private int timerMinSave;//La valeur des minutes du timer sauvegardé
    private int timerSecSave;//La valeur des secondes du timer sauvegardé
    private int currentReplay;//L'index de la reprise actuelle
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
        initFirstTimeComponents(gameNumbers);
        initialiseColors();        
        setNewValues();          
    }
    private void initFirstTimeComponents(GameModel gameNumbers){
        valid = new Validation();
        save = new saveData();
        gameObject = new GameObject();
        gameObject.gameModel = gameNumbers; 
        //Initialisation des boolean
        gameObject.modeEntrainement = true;
        gameObject.modeArcade = false;
        gameObject.modeReplay = false;
        gameObject.noise = false;
        gameObject.mean = false;
        gameObject.reverse = false;
        gameObject.noHelp = false;
        isDragging = false;
        //Initialisation des ArrayList
        listDrag = new ArrayList<>();
        gameObject.listLabelDigits = new ArrayList();
        //Initialisation des JPanel
        Border b = BorderFactory.createLineBorder(Color.black);
        pnlInfo.setBackground(Color.WHITE);        
        pnlCheckBox.setBackground(Color.WHITE);
        pnlGameMode.setBackground(Color.WHITE);
        pnlInfo.setBorder(b);
        pnlCheckBox.setBorder(b);
        pnlGameMode.setBorder(b);
        //Assignation des valeurs de départ
        gameObject.nbPoints = 0;
        currentReplay = 0;
        lblPointsValue.setText(String.valueOf(gameObject.nbPoints));
        //Initialisation du timer
        timer = new Timer(1000, timerUpdate);
        timer.setInitialDelay(1000);
        //Initialisation des RadioButton
        ButtonGroup group = new ButtonGroup();
        group.add(radioArcade);
        group.add(radioReplay);
        group.add(radioTraining);
        radioTraining.setSelected(true);
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
        lblResetNumberValue.setText("0");
        gameObject.nbReset = 0;
        panelDigits.removeAll();
        panelDigits.setLayout(new GridLayout());        
        changeDigits();
        resetValue();
    }
    /***
     * Réinitialise les actions effectuées
     */
    private void resetValue(){         
        lblChiffreSomme.setText(String.valueOf(gameObject.gameModel.getSomme()));
        lblSommeCoursChiffre.setText("0");
        lblGroupsValue.setText("0");
        gameObject.groups = 0;        
        btnSave.setEnabled(false);
        btnNext.setEnabled(true);
        isDragging = false;
        gameObject.listLabelDigits = new ArrayList();
        for (int i = 0; i < gameObject.listLabel.size(); i++) {
            gameObject.listLabel.get(i).removeMouseListener(this); // Pour eviter d'avoir plusieurs MouseListener si le bouton "reset" est cliqué plusieurs fois
            gameObject.listLabel.get(i).setBackground(Color.WHITE);
            gameObject.listLabel.get(i).addMouseListener(this);
        }
        if(gameObject.modeReplay){
            resetTimer(timerMinSave,timerSecSave); 
        }
        else{
            resetTimer(0,0); 
        }
        if(gameObject.noHelp){
            lblSommeCoursChiffre.setVisible(false);
            lblGroupsValue.setVisible(false);
        }
        else{
            lblSommeCoursChiffre.setVisible(true);
            lblGroupsValue.setVisible(true);
        }
        this.updateUI();
    }
    /***
     * Redémarre le timer avec de nouvelles valeurs
     * @param minValue Valeur des minutes
     * @param secValue Valeur des secondes
     */
    private void resetTimer(int minValue, int secValue){ 
        if(timer != null){
            timer.stop();
        }          
        gameObject.timerMin = minValue;
        gameObject.timerSec = secValue;
        timer.start();
    }
    /***
     * Méthode qui est apellé chaque seconde et qui met le timer à jour
     */
    ActionListener timerUpdate = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if(gameObject.modeReplay){
                if (gameObject.timerSec == 00) { 
                    gameObject.timerSec = 60;
                    gameObject.timerMin--;
                }
                gameObject.timerSec--;
                if(gameObject.timerMin == 0 && gameObject.timerSec == 0){
                    gameOver();
                    updateUI();
                }
            }
            else{
                gameObject.timerSec++;
                if (gameObject.timerSec == 60) {
                    gameObject.timerSec = 00;
                    gameObject.timerMin++;
                }    
                if (gameObject.modeArcade){
                    if ((gameObject.timerMin >= 1 && gameObject.timerSec == 30) || (gameObject.timerMin >= 2 && gameObject.timerSec == 00)){//-2 points chaque 30 seconde après 1:30 
                        gameObject.nbPoints = valid.findNewPointsTotal(gameObject.nbPoints, -2);
                        lblPointsValue.setText(String.valueOf(gameObject.nbPoints));
                    }
                }
            }       
            if(gameObject.timerSec < 10){
               lblTimeValue.setText(gameObject.timerMin + ":0" + gameObject.timerSec); 
            }
            else{
                lblTimeValue.setText(gameObject.timerMin + ":" + gameObject.timerSec);
            }
      }
    };
    /***
     * Affiche de nouveaux chiffres 
     */
    private void changeDigits(){
        gameObject.listDigits = new ArrayList();
        gameObject.listDigits = gameObject.gameModel.getListeChiffres();
        gameObject.listLabel = new ArrayList();
        JLabel digitLabel;
        LineBorder line = new LineBorder(Color.BLACK,3,true);
        for (int i = 0; i < gameObject.listDigits.size(); i++) {
            digitLabel = new JLabel(gameObject.listDigits.get(i).toString());
            digitLabel.setFont(new Font("Arial",Font.PLAIN,24));
            digitLabel.setVerticalAlignment(SwingConstants.CENTER);
            digitLabel.setHorizontalAlignment(SwingConstants.CENTER);
            digitLabel.setBorder(line);
            digitLabel.setPreferredSize(new Dimension (100, 100));
            digitLabel.setOpaque(true);
            gameObject.listLabel.add(digitLabel);
            panelDigits.add(gameObject.listLabel.get(i)); 
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
        int validationResult = valid.checkGameOver(sommeEnCours, sommeAAvoir,gameObject.groups, 
                gameObject.gameModel.getNbDecoupage(),gameObject.noise,gameObject.mean,gameObject.listLabelDigits,gameObject.listLabel);
        if(validationResult == 1){
            gameWin();
        }
        else if (validationResult == 2){            
            gameOver();
        }
    }
    /***
     * Quand la partie est terminé et que le joueur a perdu
     */
    private void gameOver(){
        for (int i = 0; i < gameObject.listLabel.size(); i++) {
            gameObject.listLabel.get(i).removeMouseListener(this);
            gameObject.listLabel.get(i).setBackground(Color.RED);
            timer.stop();
        }  
    }
    /***
     * Quand la partie est terminé et que le joueur a gagné
     */
    private void gameWin(){
        for (int i = 0; i < gameObject.listLabel.size(); i++) {
            gameObject.listLabel.get(i).setBackground(Color.GREEN);
            timer.stop();
            if(!gameObject.modeReplay){
                btnSave.setEnabled(true);
            }
        }
    }
    /***
     * Change la couleur du label cliqué
     * @param labelClicked Le label à changer de couleur
     */
    private void changeDigitColor(JLabel labelClicked){
        labelClicked.setBackground(colors[gameObject.groups]);
        gameObject.groups++;
        lblGroupsValue.setText(String.valueOf(gameObject.groups));
        checkIfGameEnd();
        this.updateUI();
    }
    
    /***
     * Ajoute le label à la liste de label qui sont présentement selectionnés
     * @param me Le label cliquer
     */
    @Override
    public void mousePressed(MouseEvent me) {
        isDragging = true;
        Object event = me.getComponent();        
        JLabel label = gameObject.listLabel.get(gameObject.listLabel.lastIndexOf((JLabel) event));
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
        changeDigitColor(listDrag.get(0));
        Color c = listDrag.get(0).getBackground();
        for (int i = 0; i < listDrag.size(); i++) {
            if(i < 2){ // Évite de selectionner des nombres de 3 chiffres
                listDrag.get(i).setBackground(c);
                listDrag.get(i).removeMouseListener(this);
                newTotal += listDrag.get(i).getText();
            }   
        }
        gameObject.listLabelDigits.add(Integer.parseInt(newTotal));
        setNewCurrentTotalValue(Integer.parseInt(newTotal));        
        checkIfGameEnd();
    }
    /***
     * Lorsqu'un nouvel élément est selectionné et que le drag est actif
     * @param me Le nouvel élément que la souris vient de toucher
     */
    @Override
    public void mouseEntered(MouseEvent me) {
        if(isDragging){            
            //TODO : Retirer si sa brise pas
            //java.awt.Point p = new java.awt.Point(me.getLocationOnScreen());
            //SwingUtilities.convertPointFromScreen(p, me.getComponent()); 
            JLabel label = (JLabel) me.getSource();
            listDrag.add(label);
        }
    }
    /***
     * Prend l'index de la reprise et met les données à jour en fonction des valeurs de la sauvegarde
     */
    private void setReplayValue(){
        gameObject.gameModel.setListeChiffres(listGameObject.get(currentReplay).listDigits);
        gameObject.gameModel.setListeNombres(listGameObject.get(currentReplay).listNumbers);
        gameObject.gameModel.setNbDecoupage(listGameObject.get(currentReplay).gameModel.getNbDecoupage());
        panelDigits.removeAll();
        panelDigits.setLayout(new GridLayout());        
        changeDigits();
        resetValue();
        gameObject.modeReplay = true;
        gameObject.modeEntrainement = false;
        gameObject.modeArcade = false;
        checkMean.setEnabled(true);
        checkNoHelp.setEnabled(true);
        checkNoise.setEnabled(true);
        checkReverse.setEnabled(true);
        //Insertion des valeurs de la sauvegarde
        lblChiffreSomme.setText(String.valueOf(listGameObject.get(currentReplay).gameModel.getSomme()));
        gameObject.nbReset = listGameObject.get(currentReplay).reset;
        lblResetNumberValue.setText(String.valueOf(listGameObject.get(currentReplay).reset));
        gameObject.noise = listGameObject.get(currentReplay).noise;
        checkNoise.setSelected(listGameObject.get(currentReplay).noise);
        checkNoise.setEnabled(false);
        gameObject.mean = listGameObject.get(currentReplay).mean;
        checkMean.setSelected(listGameObject.get(currentReplay).mean);
        checkMean.setEnabled(false);
        gameObject.noHelp = listGameObject.get(currentReplay).noHelp;
        checkNoHelp.setSelected(listGameObject.get(currentReplay).noHelp);
        checkNoHelp.setEnabled(false);
        gameObject.reverse = listGameObject.get(currentReplay).reverse;
        checkReverse.setSelected(listGameObject.get(currentReplay).reverse);
        checkReverse.setEnabled(false);
        timerMinSave = listGameObject.get(currentReplay).timerMin;
        timerSecSave = listGameObject.get(currentReplay).timerSec;
        resetTimer(timerMinSave, timerSecSave);
        gameObject.gameModel.setNoisePosition(listGameObject.get(currentReplay).gameModel.getNoisePosition());
        gameObject.gameModel.setSomme(listGameObject.get(currentReplay).gameModel.getSomme()); 
        if(!checkIfNextReplayAvailable()){
            btnNext.setEnabled(false);
        }
        this.updateUI();
    }
    /***
     * Vérifie s'il y a une sauvegarde suivante disponible
     * @return vrai ou faux en fonction du résultat
     */
    private boolean checkIfNextReplayAvailable(){
        if(listGameObject.size() > currentReplay + 1){
            return true;
        }
        return false;
    }
    private boolean checkBooleanValue(boolean b){
        if (b){
            return true;
        }
        return false;
    }
    private void createNewGameModel(){
        //gameObject = new GameObject();
        gameObject.gameModel = new GameModel(gameObject.noise,gameObject.mean,gameObject.reverse,gameObject.modeArcade);
        //this.gameModel = new GameModel(noise,mean,reverse,modeArcade);
        setNewValues();
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
        lblTime = new javax.swing.JLabel();
        lblTimeValue = new javax.swing.JLabel();
        lblPoints = new javax.swing.JLabel();
        lblPointsValue = new javax.swing.JLabel();
        pnlGameMode = new javax.swing.JPanel();
        lblGameMode = new javax.swing.JLabel();
        radioArcade = new javax.swing.JRadioButton();
        radioTraining = new javax.swing.JRadioButton();
        radioReplay = new javax.swing.JRadioButton();
        btnSave = new javax.swing.JButton();

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnNext.getAccessibleContext().setAccessibleName("btnNext");
        btnGiveUp.getAccessibleContext().setAccessibleName("btnGiveUp");
        btnReset.getAccessibleContext().setAccessibleName("btnReset");
        jLabel1.getAccessibleContext().setAccessibleName("lblSomme");
        lblChiffreSomme.getAccessibleContext().setAccessibleName("lblChiffreSomme");
        lblSommeCoursChiffre.getAccessibleContext().setAccessibleName("lblSommeCoursChiffre");

        checkMean.setText("Find mean");
        checkMean.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkMeanItemStateChanged(evt);
            }
        });

        checkNoise.setText("Noise");
        checkNoise.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkNoiseItemStateChanged(evt);
            }
        });

        checkNoHelp.setText("No help");
        checkNoHelp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkNoHelpItemStateChanged(evt);
            }
        });

        checkReverse.setText("Reverse");
        checkReverse.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkReverseItemStateChanged(evt);
            }
        });

        lblTime.setText("Temps :");

        lblTimeValue.setText("0:00");

        lblPoints.setText("Points :");

        lblPointsValue.setText("0");

        javax.swing.GroupLayout pnlCheckBoxLayout = new javax.swing.GroupLayout(pnlCheckBox);
        pnlCheckBox.setLayout(pnlCheckBoxLayout);
        pnlCheckBoxLayout.setHorizontalGroup(
            pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkReverse)
                    .addComponent(checkNoHelp)
                    .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                        .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkNoise)
                            .addComponent(checkMean))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPoints, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTime, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTimeValue, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPointsValue, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        pnlCheckBoxLayout.setVerticalGroup(
            pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                        .addComponent(checkNoise)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkMean))
                    .addGroup(pnlCheckBoxLayout.createSequentialGroup()
                        .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTime)
                            .addComponent(lblTimeValue))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPoints)
                            .addComponent(lblPointsValue))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkNoHelp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkReverse)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        checkMean.getAccessibleContext().setAccessibleName("checkMean");
        checkNoise.getAccessibleContext().setAccessibleName("checkNoise");

        lblGameMode.setText("Mode de jeu :");

        radioArcade.setText("Arcade");
        radioArcade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioArcadeItemStateChanged(evt);
            }
        });

        radioTraining.setText("Entrainnement");
        radioTraining.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioTrainingItemStateChanged(evt);
            }
        });

        radioReplay.setText("Replay");
        radioReplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioReplayItemStateChanged(evt);
            }
        });

        btnSave.setText("Sauver la partie");
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlGameModeLayout = new javax.swing.GroupLayout(pnlGameMode);
        pnlGameMode.setLayout(pnlGameModeLayout);
        pnlGameModeLayout.setHorizontalGroup(
            pnlGameModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGameModeLayout.createSequentialGroup()
                .addGroup(pnlGameModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGameModeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblGameMode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlGameModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioArcade)
                            .addComponent(radioReplay)
                            .addComponent(radioTraining)))
                    .addGroup(pnlGameModeLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnSave)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlGameModeLayout.setVerticalGroup(
            pnlGameModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGameModeLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlGameModeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGameMode)
                    .addComponent(radioTraining))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioArcade)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioReplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDigits, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlGameMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDigits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlGameMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    /***
     * Quand le bouton next est cliqué, un nouveau jeu est créer en fonction des
     * options
     * @param evt Le clic
     */
    private void btnNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextMouseClicked
        if(btnNext.isEnabled()){
            if(gameObject.modeReplay){
                currentReplay++;   
                setReplayValue();
            }
            else{
                gameObject.gameModel = new GameModel(gameObject.noise,gameObject.mean,gameObject.reverse,gameObject.modeArcade);
                setNewValues();
            }
        }
    }//GEN-LAST:event_btnNextMouseClicked
    /***
     * Quand le bouton reset est cliqué, les valeurs sont remisent à zéro
     * @param evt Le clic
     */
    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        resetValue();
        if(gameObject.modeArcade){
            gameObject.nbPoints = valid.findNewPointsTotal(gameObject.nbPoints, -3);        
            lblPointsValue.setText(String.valueOf(gameObject.nbPoints));
            gameObject.nbReset++; 
        }
        else if(gameObject.modeReplay){
            gameObject.nbReset--; 
            if(gameObject.nbReset < 0){
                gameObject.nbReset = 0;
                gameOver(); 
                updateUI(); 
            }
        }
        else if(gameObject.modeEntrainement){
            gameObject.nbReset++; 
        }     
        lblResetNumberValue.setText(String.valueOf(gameObject.nbReset));
        
    }//GEN-LAST:event_btnResetMouseClicked
    /***
     * Quand le bouton Give Up est cliqué, la solution est affichée
     * @param evt Le clic
     */
    private void btnGiveUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGiveUpMouseClicked
        timer.stop();
        int noisePosition = gameObject.gameModel.getNoisePosition();
        ArrayList listNumbers = gameObject.gameModel.getListeNombres();
        lblSommeCoursChiffre.setText(String.valueOf(gameObject.gameModel.getSomme()));
        lblGroupsValue.setText(String.valueOf(gameObject.gameModel.getNbDecoupage()));
        gameObject.groups = 0;
        int k = 0;
        int j = 0;
        while(k < listNumbers.size()){
            gameObject.listLabel.get(j).setBackground(colors[gameObject.groups]);
            gameObject.listLabel.get(j).removeMouseListener(this);
            if((int)listNumbers.get(k) > 9){
                gameObject.listLabel.get(j + 1).setBackground(colors[gameObject.groups]);
                gameObject.listLabel.get(j + 1).removeMouseListener(this);
                j++;
            }
            else if(k == noisePosition){
              gameObject.listLabel.get(j).setBackground(Color.BLACK); 
            }
            gameObject.groups++;
            j++;
            k++;
        }        
        updateUI();
    }//GEN-LAST:event_btnGiveUpMouseClicked

    private void checkNoiseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkNoiseItemStateChanged
        gameObject.noise = checkBooleanValue(checkNoise.isSelected());
        createNewGameModel();
    }//GEN-LAST:event_checkNoiseItemStateChanged

    private void checkMeanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkMeanItemStateChanged
        gameObject.mean = checkBooleanValue(checkMean.isSelected());
        createNewGameModel();
    }//GEN-LAST:event_checkMeanItemStateChanged

    private void checkNoHelpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkNoHelpItemStateChanged
        gameObject.noHelp = checkBooleanValue(checkNoHelp.isSelected());
        createNewGameModel();
    }//GEN-LAST:event_checkNoHelpItemStateChanged

    private void checkReverseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkReverseItemStateChanged
        gameObject.reverse = checkBooleanValue(checkReverse.isSelected());
        createNewGameModel();
    }//GEN-LAST:event_checkReverseItemStateChanged

    private void radioTrainingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioTrainingItemStateChanged
        if(radioTraining.isSelected()){
            createNewGameModel();
            gameObject.modeEntrainement = true;
            gameObject.modeArcade = false;
            gameObject.modeReplay = false;
            checkMean.setEnabled(true);
            checkNoHelp.setEnabled(true);
            checkNoise.setEnabled(true);
            checkReverse.setEnabled(true);
            
        }
    }//GEN-LAST:event_radioTrainingItemStateChanged

    private void radioArcadeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioArcadeItemStateChanged
        if(radioArcade.isSelected()){
            createNewGameModel();
            gameObject.modeArcade = true;
            gameObject.modeEntrainement = false;
            gameObject.modeReplay = false;            
            checkMean.setEnabled(false);
            checkNoHelp.setEnabled(false);
            checkNoise.setEnabled(false);
            checkReverse.setEnabled(false);
        }
    }//GEN-LAST:event_radioArcadeItemStateChanged

    private void radioReplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioReplayItemStateChanged
        if(radioReplay.isSelected()){
            listGameObject = new ArrayList();
            listGameObject = save.readFile();
            if(listGameObject != null){
                currentReplay = 0;
                setReplayValue();
            }
            else{                
                JOptionPane.showMessageDialog(null, "Aucune sauvegarde disponible, retour au mode entrainnement");
                radioTraining.setSelected(true);
                createNewGameModel();
            }
            
        }
    }//GEN-LAST:event_radioReplayItemStateChanged

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        save.saveToFile(gameObject/*gameModel.getSomme(),lblSommeCoursChiffre.getText(),groups,
                lblResetNumberValue.getText(),noise,mean,noHelp,reverse,timerMin,
                timerSec,nbPoints,radioArcade.isSelected(),radioReplay.isSelected(),radioTraining.isSelected(),
                gameModel.getNoisePosition(),gameModel.getNbDecoupage(),listLabel,gameModel.getListeNombres(),gameModel.getListeChiffres()*/);
        btnSave.setEnabled(false);
        JOptionPane.showMessageDialog(null, "Partie sauvegardée");        
    }//GEN-LAST:event_btnSaveMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGiveUp;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnRestart;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox checkMean;
    private javax.swing.JCheckBox checkNoHelp;
    private javax.swing.JCheckBox checkNoise;
    private javax.swing.JCheckBox checkReverse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblChiffreSomme;
    private javax.swing.JLabel lblGameMode;
    private javax.swing.JLabel lblGroups;
    private javax.swing.JLabel lblGroupsValue;
    private javax.swing.JLabel lblPoints;
    private javax.swing.JLabel lblPointsValue;
    private javax.swing.JLabel lblResetNumber;
    private javax.swing.JLabel lblResetNumberValue;
    private javax.swing.JLabel lblSommeCoursChiffre;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTimeValue;
    private javax.swing.JPanel panelDigits;
    private javax.swing.JPanel pnlCheckBox;
    private javax.swing.JPanel pnlGameMode;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JRadioButton radioArcade;
    private javax.swing.JRadioButton radioReplay;
    private javax.swing.JRadioButton radioTraining;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseDragged(MouseEvent me) {
    }
    @Override
    public void mouseMoved(MouseEvent me) {
    }
    @Override
    public void mouseExited(MouseEvent me) {
    }
    @Override
    public void mouseClicked(MouseEvent me) {
    }
}
