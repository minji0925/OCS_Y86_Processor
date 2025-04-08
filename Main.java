import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;
import java.awt.event.*;

class Main implements ActionListener{
    static String code = "";
    static int PC = 0;
    // decide whether to make icode, ifun, rA, rB, valC, valP global or local
    char icode = code.charAt(PC);
    char ifun = code.charAt(PC+1);

    public static void main(String args[]) throws IOException{
        FileReader fr = new FileReader("file.txt");
        BufferedReader br = new BufferedReader(fr);
        code = br.readLine();
        br.close();

        //Java Swing UI - start button
        JFrame frame = new JFrame();
        JButton start_button = new JButton("start");
        start_button.setBounds(150, 200, 200, 50);
        frame.add(start_button);
        frame.setSize(500, 600);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //button click
        Main listener = new Main();
        start_button.addActionListener(listener);
    }

    public void actionPerformed(ActionEvent e){
        fetch(code);
    }

    public void fetch(String code){
        int rA = 0, rB = 0, valC = 0, valP = 0;
        switch(icode){
            case '0':
            case '1':
            case '2':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valP = PC + 4;
            case '3':
                rA = 15;
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valC = Integer.parseInt(code.substring(2), 16);
                valP = PC + 10;
            case '4':
            case '5':
            case '6':
                
            case '7':
                valC = Integer.parseInt(code.substring(2), 16);
                valP = valC;
            case '8':
                valC = Integer.parseInt(code.substring(2), 16);
                valP = valC;
            case '9':
                
            case 'A':

            case 'B':
        }
        decode(rA, rB, valC, valP);
    }
    
    public void decode(int rA, int rB, int valC, int valP){
        int valA = 0, valB = 0;
        switch(icode){
            case '2':
                valA = registers.get(rA);
                valB = registers.get(rB);
            case '3':
            case '4':
            case '5':
            case '6':
            case 'A':
            case 'B':
        }
    }

    static HashMap<Integer, Integer> registers = new HashMap<Integer, Integer>(){{
        put(0, 0);
        put(1, 0);
        put(2, 0);
        put(3, 0);
        put(4, 0);
        put(5, 0);
        put(6, 0);
        put(7, 0);
        put(8, 0);
        put(9, 0);
        put(10, 0);
        put(11, 0);
        put(12, 0);
        put(13, 0);
        put(14, 0);
    }};
}