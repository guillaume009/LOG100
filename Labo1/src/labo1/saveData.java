/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Guillaume
 */
public class saveData {
    public void saveToFile(GameObject game/*int somme, String sommeEnCours, int groups, String reset, boolean noise, 
            boolean mean, boolean noHelp, boolean reverse, int timerMin, int timerSec, 
            int nbPoints,boolean arcade, boolean replay, boolean trainning,int noisePosition, int nbDecoupage,ArrayList<JLabel> listLabel,ArrayList listNumbers,ArrayList listDigits*/){
        try {
            File file = new File("saves.txt");
            if(!file.exists()){
                file.createNewFile();
            } 
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.valueOf(game.gameModel.getSomme()) + System.getProperty( "line.separator" ));
            bw.write(game.reset + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.noise) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.mean) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.noHelp) + System.getProperty( "line.separator" ));
            System.out.println(" string value no help : " + String.valueOf(game.noHelp));
            bw.write(String.valueOf(game.reverse) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.timerMin) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.timerSec) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.nbPoints) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.modeArcade) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.modeReplay) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.modeEntrainement) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.gameModel.getNoisePosition()) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(game.gameModel.getNbDecoupage()) + System.getProperty( "line.separator" ));            
            bw.write(String.valueOf(game.gameModel.getListeNombres().size()) + System.getProperty( "line.separator" ));
            for (int i = 0; i < game.gameModel.getListeNombres().size(); i++) {
                bw.write(String.valueOf(game.gameModel.getListeNombres().get(i)) + System.getProperty( "line.separator" ));
            }
            bw.write(String.valueOf(game.listDigits.size()) + System.getProperty( "line.separator" ));
            for (int i = 0; i < game.listDigits.size(); i++) {
                bw.write(String.valueOf(game.listDigits.get(i)) + System.getProperty( "line.separator" ));
            }
            bw.write("//");
            bw.close();
            fw.close();  
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    
    public ArrayList readFile(){
        ArrayList <GameObject> listGameObject = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader("saves.txt"));
            StringBuilder sb = new StringBuilder();
            String currentLine = br.readLine();
            while (currentLine != null) {
                sb.append(currentLine);
                sb.append("\n");
                currentLine = br.readLine();
            }
            
            String completeFile = sb.toString();
            String[] tabGames = completeFile.split("//");
            String[] tabGameValue;
            
            GameObject g;
            for (int i = 0; i < tabGames.length; i++) {
                if(!tabGames[i].equals("\n")){
                    g = new GameObject();
                    tabGameValue = tabGames[i].split("\n");
                    g.gameModel = new GameModel();
                    g.gameModel.setSomme(Integer.parseInt(tabGameValue[0]));
                    g.nbReset = Integer.parseInt(tabGameValue[1]);
                    g.noise = getBoolFromString(tabGameValue[2]);
                    g.mean = getBoolFromString(tabGameValue[3]);
                    g.noHelp = getBoolFromString(tabGameValue[4]);
                    g.reverse = getBoolFromString(tabGameValue[5]);
                    g.timerMin = Integer.parseInt(tabGameValue[6]);
                    g.timerSec = Integer.parseInt(tabGameValue[7]);
                    g.nbPoints = Integer.parseInt(tabGameValue[8]);
                    g.modeArcade = getBoolFromString(tabGameValue[9]);
                    g.modeReplay = getBoolFromString(tabGameValue[10]);
                    g.modeEntrainement = getBoolFromString(tabGameValue[11]);
                    g.gameModel.setNoisePosition(Integer.parseInt(tabGameValue[12]));
                    g.gameModel.setNbDecoupage(Integer.parseInt(tabGameValue[13]));
                    int listNumbersLength = Integer.parseInt(tabGameValue[14]);
                    ArrayList listNumbers = new ArrayList();
                    for (int j = 0; j < listNumbersLength; j++) {
                        listNumbers.add(Integer.parseInt(tabGameValue[15 + j]));
                    }
                    int listDigitsLength = Integer.parseInt(tabGameValue[listNumbersLength + 15]);
                    ArrayList listDigits = new ArrayList();
                    for (int j = 0; j < listDigitsLength; j++) {
                        listDigits.add(Integer.parseInt(tabGameValue[listNumbersLength + 16 +j]));
                    }
                    g.listNumbers = listNumbers;
                    g.listDigits = listDigits;
                    listGameObject.add(g);
                }
            }
            br.close();            
        } 
        catch(java.io.FileNotFoundException e){
            return null;
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return listGameObject;
    }
    
    private boolean getBoolFromString(String s){
        if(s.equals("true")){
            return true;
        }
        return false;
    }
}
