/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * Valide les données entrées et les opérations effectuées
 */
public class Validation {
    /***
     * Calcul le nouveau total après un clic sur un label
     * @param currentTotal le total en cours
     * @param labelClickedValue la valeur du label cliqué
     * @return le nouveau total
     */
  public int calculateCurrentTotalValue(int currentTotal,int labelClickedValue){
        currentTotal += labelClickedValue;
        return currentTotal;
    }
    /***
     * Vérifie si le total courrant corespond au total attendu
     * @param sommeEnCours Somme que le joueur a accumulé
     * @param sommeAAvoir somme que le joueur doit avoir
     * @param noise s'il y a un noise
     * @param mean si c'est une division
     * @param listLabelDigits la liste de toutes les valeurs des labels selectionnés
     * @param listLabel la liste des labels
     * @return retourne une valeur en fonction de si le joueur a gagné, perdu, ou s'il peut continuer à jouer
     */
    public int checkGameOver(int sommeEnCours, int sommeAAvoir,int nbGroups,int nbDecoupage,
            boolean noise,boolean mean,ArrayList listLabelDigits,ArrayList listLabel){
        if(numberDigitsRemaining(listLabel) == 0){
            if(sommeEnCours == sommeAAvoir && checkTotalResult(sommeAAvoir,listLabelDigits,nbGroups,nbDecoupage)){
                return 1;//Le joueur a gagné
            }
            else if(mean && checkMeanResult(sommeEnCours, sommeAAvoir, nbGroups,nbDecoupage)){
                return 1;
            }
            else{            
                return 2;//Le joueur a perdu  
            }
        }
        else if(noise){
            if(numberDigitsRemaining(listLabel) == 1 || numberDigitsRemaining(listLabel) == 0){
                if(mean){
                    if(checkMeanResult(sommeEnCours, sommeAAvoir, nbGroups,nbDecoupage)){
                        return 1;
                    }
                }
                else if(sommeEnCours == sommeAAvoir && checkTotalResult(sommeAAvoir,listLabelDigits,nbGroups,nbDecoupage)){
                    return 1;//Le joueur a gagner puisqu'il y a du bruit
                }
                else{
                    return 2;
                }
            }
        }
        return 0;//Le joueur continue à jouer
    }
    /***
     * Verifie si le resultat de la division est valide
     * @param sommeEnCours Somme que le joueur a accumulé
     * @param sommeAAvoir Somme que le joueur doit avoir
     * @param nbGroups nombre de groupe que le joueur a fait
     * @param nbDecoupage nombre de groupe que le joueur doit faire
     * @return vrai ou faux si le résultat est valide
     */
    private boolean checkMeanResult(int sommeEnCours, int sommeAAvoir, int nbGroups, int nbDecoupage){
        if(sommeEnCours / nbGroups == sommeAAvoir){
            double divResult = sommeEnCours / nbDecoupage;
            if(divResult == sommeAAvoir){
                return true;
            }
        }
        return false;
    }
    /***
     * Verifie si le résultat de la somme est valide
     * @param sommeAAvoir Somme que le joueur doit avoir
     * @param listLabelDigits La liste des valeurs des labels selectionnés
     * @param nbGroups nombre de groupe que le joueur a fait
     * @param nbDecoupage nombre de groupe que le joueur doit faire
     * @return vrai ou faux si le résultat est valide
     */
    private boolean checkTotalResult(int sommeAAvoir,ArrayList<Integer> listLabelDigits, int nbGroups, int nbDecoupage){
        int totalSelected = 0;
        for (int i = 0; i < listLabelDigits.size(); i++) {
                totalSelected += listLabelDigits.get(i);
        }
        if(totalSelected == sommeAAvoir){
            if(nbGroups == nbDecoupage){
                return true;
            }
        }
        return false;
    }
    /***
     * Compte le nombre de ciffre pas utilisé restant
     * @param listLabel La liste des labels
     * @return le nombre de label pas utilisé restant
     */
    private int numberDigitsRemaining(ArrayList<JLabel> listLabel){
        int digitRemaining = 0;
        for (int i = 0; i < listLabel.size(); i++) {
            if(listLabel.get(i).getBackground() == Color.WHITE){
                digitRemaining++;
            }
        }
        return digitRemaining;
    }
}
