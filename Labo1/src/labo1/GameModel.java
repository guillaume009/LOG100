/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

import java.util.ArrayList;

/**
 *Initialise les valeurs des chiffres qui vont être affichés
 */
public class GameModel {
    
    private int totalToGet = 0;//Somme totale à atteindre
    private ArrayList listNumbers = new ArrayList();//Liste contenant tout les nombres de départ
    private ArrayList listDigits = new ArrayList();//Liste contenant chacun des chiffres séparés
    private int noisePosition = -1;//Position du chiffre de noise dans la liste
    private int nbDecoupage = 0;//nombre de découpage différent
    private int noiseValue;//valeur du noise
    /***
     * Initialise la partie qui contient les chiffres du jeu
     */
    public GameModel(){
        initialiseNumbers(false,false,false);
    }  

    /**
     * Initialise la partie qui contient les chiffres du jeu avec les paramètres spécifiques
     * @param noise Si l'option noise est activé
     * @param mean Si l'option mean est activé
     */
    public GameModel(boolean noise,boolean mean,boolean reverse,boolean arcade){
        if(arcade){
            
        }
        else{
           initialiseNumbers(noise,mean,reverse); 
        }
    } 
    public int getSomme(){
        return totalToGet;
    } 
    public void setSomme(int somme){
        totalToGet = somme;
    }
    public ArrayList getListeNombres(){
        return listNumbers;
    } 
    public void setListeNombres(ArrayList liste){
        listNumbers = liste;
    }
    public ArrayList getListeChiffres(){
        return listDigits;
    }
    public void setListeChiffres(ArrayList liste){
        listDigits = liste;
    }
    public int getNoisePosition(){
        return noisePosition;
    }
    public void setNoisePosition(int position){
        noisePosition = position;
    }
    public int getNbDecoupage(){
        return nbDecoupage;
    }
    public void setNbDecoupage(int decoupage){
        nbDecoupage = decoupage;
    }
    /***
     * Créer des nouveaux nombres 
     * @param noise si il y a un noise
     * @param mean si c'est une division
     */
    private void initialiseNumbers(boolean noise,boolean mean,boolean reverse){
        generateListNumbers(mean,noise);
        splitListNumbers(reverse);
    }
    private void generateListNumbers(boolean mean, boolean noise){
        nbDecoupage = chooseNbDecoupage();
        totalToGet = 0;
        for(int i = 0; i < nbDecoupage; i++){
            int newNumber;
            if(isHigherThan9()){
               newNumber = gerenerateHighRandom();
            }
            else{
               newNumber = gerenerateLowRandom();
            }            
            listNumbers.add(newNumber);
            totalToGet += newNumber;
        }
        if(mean){
            if(totalToGet % nbDecoupage != 0){
                findNewTotal();      
            }
            else{
                totalToGet = totalToGet / nbDecoupage;
            }
        }
        if(noise){
            noiseValue = gerenerateLowRandom();
            insertNoise();
        }     
    }
    private void splitListNumbers(boolean reverse){
        for(int i = 0; i < listNumbers.size();i++){            
            if ((int)listNumbers.get(i) > 9){                
                int[] numberSplited = splitNumber((int)listNumbers.get(i));
                if(reverse && reverseNumber()){
                        listDigits.add(numberSplited[1]);
                        listDigits.add(numberSplited[0]);
                }
                else{
                    listDigits.add(numberSplited[0]);
                    listDigits.add(numberSplited[1]);    
                }                               
            }
            else{
                listDigits.add(listNumbers.get(i));
            }
        }
    }
    private boolean reverseNumber(){
        double d = Math.random();
        if (d >= 0.5){
            return true;
        }
        else{
            return false;
        }
    }
    /***
     * Trouve le nouveau total lorsque le mean est activé
     */
    private void findNewTotal(){
        int divisionNoDecimal = totalToGet / nbDecoupage;
        int newTotal = nbDecoupage * divisionNoDecimal;
        int differenceBetweenTotals = totalToGet - newTotal;
        totalToGet = newTotal / nbDecoupage;
        substractDifferenceFromList(differenceBetweenTotals);        
    }
    /***
     * Soustrait la difference du nouveau total lors d'une division
     * @param difference la différence à enlever
     */
    private void substractDifferenceFromList(int difference){
        int lower = 0; //inclu
        int higher = listNumbers.size(); //exclu
        int random = (int)(Math.random() * (higher-lower)) + lower;
        if((int)listNumbers.get(random) - difference >= 0){
            listNumbers.set(random,(int)listNumbers.get(random)- difference);
        }
        else{
           substractDifferenceFromList(difference); 
        }
    }
    /***
     * Ajoute une valeur noise dans la liste de chiffres
     */
    private void insertNoise(){
        int lower = 0; //inclu
        int higher = listNumbers.size() + 1; //exclu
        noisePosition = (int)(Math.random() * (higher-lower)) + lower;
        listNumbers.add(noisePosition,noiseValue);
    }
    /***
     * Génére un nombre aléatoire qui sert à avoir le nombre de découpage
     * @return le nombre défini
     */
    private int chooseNbDecoupage(){
        int lower = 3; //inclu
        int higher = 7; //exclu
        return (int)(Math.random() * (higher-lower)) + lower;
    }
    /***
     * Génére un nombre aléatoire entre 0-10
     * @return le nombre défini
     */
    private int gerenerateLowRandom(){
        int lower = 1; //inclu
        int higher = 10; //exclu
        return (int)(Math.random() * (higher-lower)) + lower;
    }
    /***
     * Génére un nombre aléatoire en 10-100
     * @return 
     */
    private int gerenerateHighRandom(){
        int lower = 11; //inclu
        int higher = 100; //exclu
        return (int)(Math.random() * (higher-lower)) + lower;
    }
    /***
     * Détermine si le chiffre à créer doit est plus grand que 9 avec une chance de 30%
     * @return boolean qui dit si oui ou non il faut faire un chiffre plus grand
     */
    private boolean isHigherThan9(){
        double d = Math.random();
        if (d >= 0.7){
            return true;
        }
        else{
            return false;
        }
    }
    /***
     * Prend un nombre et le sépare avec les deux chiffres qui le compose
     * @param numberToSplit Le numéro à séparer
     * @return Les deux chiffres séparés
     */
    private int[] splitNumber(int numberToSplit){
        String number = String.valueOf(numberToSplit);
        char[] digits = number.toCharArray();
        int[] splitedNumer = new int[2];
        splitedNumer[0] = Character.getNumericValue(digits[0]);
        splitedNumer[1] = Character.getNumericValue(digits[1]);
        return splitedNumer;
    }
}