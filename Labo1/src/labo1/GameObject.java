/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Guillaume
 */
public class GameObject {
    /*public GameModel gameModel;
    private ArrayList listLabelDigits;//Liste des label qu'il faut cliquer
    private boolean isDragging;//Si le joueur est en train de drag
    private boolean modeEntrainement;//Si le mode entrainement est actif
    private boolean modeArcade;// Si le mode arcade est actif
    private boolean modeReplay;//Si le mode replay est actif
    private int nbReset;//Nombre de fois que le jeu a été réinitialisé*/
    
    public String somme;
    public String sommeEnCours;
    public String groups;
    public String reset;
    public boolean noise;
    public boolean mean;
    public boolean noHelp;
    public boolean reverse;
    public String timerMin;
    public String timerSec;
    public String nbPoints;
    public boolean arcade;
    public boolean replay;
    public boolean trainning;
    public String noisePosition;
    public String nbDecoupage;
    public ArrayList<JLabel> listLabel = new ArrayList<>();
    public ArrayList listNumbers = new ArrayList();
    public ArrayList listDigits = new ArrayList();
}
