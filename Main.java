import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.awt.event.*;

class Main implements ActionListener{
    static String code = "";
    static int PC = 0;
    static int icode, ifun, rA, rB, valC, valP = 0;

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
        char icode = code.charAt(PC);
        char ifun = code.charAt(PC+1);
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
                //valC = 
        }
    }
    
}