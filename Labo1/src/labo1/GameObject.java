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
    public GameModel gameModel;

    public int nbReset;//Nombre de fois que le jeu a été réinitialisé
    public int sommeEnCours;
    public int groups;
    public int reset;
    public int timerMin;
    public int timerSec;
    public int nbPoints;
    public int arcadeLvl;
    
    public boolean noise;
    public boolean mean;
    public boolean noHelp;
    public boolean reverse;
    public boolean modeEntrainement;//Si le mode entrainement est actif
    public boolean modeArcade;// Si le mode arcade est actif
    public boolean modeReplay;//Si le mode replay est actif

    public ArrayList<JLabel> listLabel;
    public ArrayList listLabelDigits;//Liste des label qu'il faut cliquer
    public ArrayList listNumbers;
    public ArrayList listDigits;
}
