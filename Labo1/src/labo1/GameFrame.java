/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labo1;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Le conteneur qui contient le jeu
 */
public class GameFrame extends JFrame{

    public static GameView gameView; //hérite de JPanel, gère l’interface et contrôle
    public static GameModel gameModel; //exemplaire du jeu.on

    /**
     * Crée le jeu
     */
    public GameFrame() {//Initialisation de l’exemplaire GameModel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
        gameModel = new GameModel();
        initUI(); //Initialisation de la vue gameView
        gameView = new GameView(gameModel);
        setContentPane(gameView);
    }

    /**
     * Initialise la fenêtre de jeu
     */
    public void initUI() {
        setTitle("Sommurai");
        setSize(860, 310);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Affiche le jeu
     * @param args les agurments passés au jeu
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame game = new GameFrame();
                game.getContentPane().setBackground(Color.WHITE);
                game.setVisible(true);
            }
        });
    }
}
