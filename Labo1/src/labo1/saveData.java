/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

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
            int nbPoints,boolean arcade, boolean replay, boolean trainning,int noisePosition, int nbDecoupage,ArrayList<JLabel> listLabel){
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
            bw.write(String.valueOf(reverse) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(timerMin) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(timerSec) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(nbPoints) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(arcade) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(replay) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(trainning) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(noisePosition) + System.getProperty( "line.separator" ));
            bw.write(String.valueOf(nbDecoupage) + System.getProperty( "line.separator" ));
            bw.write("=" + System.getProperty( "line.separator" ));
            for(int i = 0; i < listLabel.size(); i++){
                bw.write(reset);
                bw.write(listLabel.get(i).getText() + ";");
                bw.write(String.valueOf(listLabel.get(i).getBackground()));
                bw.write(System.getProperty( "line.separator" ));
            }
            bw.write("=" + System.getProperty( "line.separator" ));
            bw.close();
            fw.close();  
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    
    public GameObject readFile(){
        GameObject g = new GameObject();        
        try {
            BufferedReader br = new BufferedReader(new FileReader("saves.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            br.close();
        } catch(java.io.FileNotFoundException e){
            return null;
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return g;
    }
}
