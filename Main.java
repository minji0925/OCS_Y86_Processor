//some comments: why does cmov exist when Y86 doesn't have cmp and test?
//do we need condition codes?
/*things TODO: 
1.  Fill in IFUN 
2. Update flags
3. Finishing out ICODES,
4. Figure out what halt does
5. figure out stack + memory
*/
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
        System.out.println("start");
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
        System.out.println("Button Pressed");
        fetch(code);
        System.out.println("After Fetch");
    }

    public void fetch(String code){
        int rA = 0, rB = 0, valC = 0, valP = 0;
        switch(icode){
            case '0':
            case '1':
            //make this 4 (four 'blocks') or 2 (two bytes)?
            case '2':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valP = PC + 4;
            case '3':
                rA = 15;
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valC = Integer.parseInt(code.substring(2), 16);
                valP = PC + 20;
            case '4':
                //same as 3
            case '5':
                //same as 3
            case '6':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valP = PC + 4;
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
                valB = 0;
            case '3':
                //nothing needed, because it's irmovq?
            case '4':
                valA = registers.get(rA);
                valB = registers.get(rB);
            case '5':
                valA = registers.get(rA);
                valB = registers.get(rB);
            case '6':
                valA = registers.get(rA);
                valB = registers.get(rB);
            case 'A':
            case 'B':
        }
        execute(valA, valB, valC);
    }

    public void execute(int valA, int valB, int valC){
        int valE = 0;
        switch(icode){
            case '2':
                valE = valA + valB;
            case '3':
                //irmovq - probably nothing needed
            case '4':
                valE = valB + valE;
            case '5':
                valE = valB + valE;
        }
    }

    public void memory(){

    }

    public void writeBack(){

    }

    public void PCupdate(){

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

    static HashMap<String, Integer> conditionCodes = new HashMap<String, Integer>(){{
        put("CF", 0); //Carry flag
        put("SF", 0); //Sign flag
        put("ZF", 0); //Zero flag
        put("OF", 0); //Overflow flag
    }};
}