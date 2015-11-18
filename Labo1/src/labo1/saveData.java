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
    public void saveToFile(int somme, String sommeEnCours, int groups, String reset, boolean noise, 
            boolean mean, boolean noHelp, boolean reverse, int timerMin, int timerSec, 
            int nbPoints,boolean arcade, boolean replay, boolean trainning,int noisePosition, int nbDecoupage,ArrayList<JLabel> listLabel,ArrayList listNumbers,ArrayList listDigits){
        try {
            File file = new File("saves.txt");
            if(!file.exists()){
                file.createNewFile();
            } 
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.valueOf(somme) + System.getProperty( "line.separator" ));
            bw.write(sommeEnCours + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(groups) + System.getProperty( "line.separator" ));
            bw.write(reset + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(noise) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(mean) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(noHelp) + System.getProperty( "line.separator" ));
            System.out.println(" string value no help : " + String.valueOf(noHelp));
            bw.write(String.valueOf(reverse) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(timerMin) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(timerSec) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(nbPoints) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(arcade) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(replay) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(trainning) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(noisePosition) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(nbDecoupage) + System.getProperty( "line.separator" ));            
            bw.write(String.valueOf(listNumbers.size()) + System.getProperty( "line.separator" ));
            for (int i = 0; i < listNumbers.size(); i++) {
                bw.write(String.valueOf(listNumbers.get(i)) + System.getProperty( "line.separator" ));
            }
            bw.write(String.valueOf(listDigits.size()) + System.getProperty( "line.separator" ));
            for (int i = 0; i < listDigits.size(); i++) {
                bw.write(String.valueOf(listDigits.get(i)) + System.getProperty( "line.separator" ));
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
                    g.somme = tabGameValue[0];
                    g.sommeEnCours = tabGameValue[1];
                    g.groups = tabGameValue[2];
                    g.reset = tabGameValue[3];
                    g.noise = getBoolFromString(tabGameValue[4]);
                    g.mean = getBoolFromString(tabGameValue[5]);
                    g.noHelp = getBoolFromString(tabGameValue[6]);
                    System.out.println(g.noHelp);
                    g.reverse = getBoolFromString(tabGameValue[7]);
                    g.timerMin = tabGameValue[8];
                    g.timerSec = tabGameValue[9];
                    g.nbPoints = tabGameValue[10];
                    g.arcade = getBoolFromString(tabGameValue[11]);
                    g.replay = getBoolFromString(tabGameValue[12]);
                    g.trainning = getBoolFromString(tabGameValue[13]);
                    g.noisePosition = tabGameValue[14];
                    g.nbDecoupage = tabGameValue[15];
                    int listNumbersLength = Integer.parseInt(tabGameValue[16]);
                    ArrayList listNumbers = new ArrayList();
                    for (int j = 0; j < listNumbersLength; j++) {
                        listNumbers.add(Integer.parseInt(tabGameValue[17 + j]));
                    }
                    int listDigitsLength = Integer.parseInt(tabGameValue[listNumbersLength + 17]);
                    ArrayList listDigits = new ArrayList();
                    for (int j = 0; j < listDigitsLength; j++) {
                        listDigits.add(Integer.parseInt(tabGameValue[listNumbersLength + 18 +j]));
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
