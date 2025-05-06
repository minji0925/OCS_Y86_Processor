//some comments: why does cmov exist when Y86 doesn't have cmp and test?
//do we need condition codes?
/*things TODO: 
1.  Fill in IFUN 
2. Update flags
3. Finishing out ICODES,
4. Figure out what halt does
5. figure out stack + memory
*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import java.util.HashMap;

public class Test{
    static String code = "";
    static int PC = 0;
    static char icode, ifun = '0';

    //consider each element of the stack array to be one line in the stack (8 bytes, so that it's in sync with the length of valC and pointers)
    static int[] stack = new int[300];

    public static void main(String args[]) throws IOException{
        try {
            code = Files.lines(Paths.get("file.txt"))
                                  .collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        fetch(code);
    }

    public static void fetch(String code){
        int rA = 0, rB = 0, valC = 0, valP = 0;
        icode = code.charAt(PC);
        ifun = code.charAt(PC+1);
        switch(icode){
            case '0':
                System.exit(0);
            //how to deal with a 0? is it padding or halt? padding in between instructions probably doesn't happen
                break;
            case '1':
                valP = PC + 2;
                break;
            case '2':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valP = PC + 4;
                break;
            //valC is now an integer, not hex - no need to worry about conversion from here
            case '3':
                rA = 15;
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valC = Integer.parseInt(code.substring(PC+4, PC+20), 16);
                valP = PC + 20;
                break;
            case '4':
                rA = 15;
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valC = Integer.parseInt(code.substring(PC+4, PC+20), 16);
                valP = PC + 20;
                break;
            case '5':
                rA = 15;
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valC = Integer.parseInt(code.substring(PC+4, PC+20), 16);
                valP = PC + 20;
                break;
            case '6':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = Integer.parseInt(String.valueOf(code.charAt(PC+3)), 16);
                valP = PC + 4;
                break;
            case '7':
                valC = Integer.parseInt(code.substring(PC+2, PC+18), 16);
                valP = valC;
                break;
            case '8':
                valC = Integer.parseInt(code.substring(PC+2, PC+18), 16);
                valP = valC;
                break;
            case '9':
                valP = PC + 1;
                break;
            case 'A':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = 15;
                valP = PC + 4;
                break;
            case 'B':
                rA = Integer.parseInt(String.valueOf(code.charAt(PC+2)), 16);
                rB = 15;
                valP = PC + 4;
                break;
        }
        decode(rA, rB, valC, valP);
    }
    
    public static void decode(int rA, int rB, int valC, int valP){
        int valA = 0, valB = 0;
        switch(icode){
            case '2':
                valA = registers.get(rA);
                break;
            case '4':
                valA = registers.get(rA);
                valB = registers.get(rB);
                break;
            case '5':
                valB = registers.get(rB);
                break;
            case '6':
                valA = registers.get(rA);
                valB = registers.get(rB);
                break;
            case '8': 
                //%esp
                valB = registers.get(4);
                break;
            case '9':
                valA = registers.get(4);
                valB = registers.get(4);
                break;
            case 'A':
                valA = registers.get(rA);
                valB = registers.get(4);
                break;
            case 'B':
                valA = registers.get(4);
                valB = registers.get(4);
                break;
        }
        execute(rA, rB, valA, valB, valC, valP);
    }

    public static void execute(int rA, int rB, int valA, int valB, int valC, int valP){
        //cnd: 0 if condition is not satisfied, 1 if satisfied
        int cnd = 0;
        int valE = 0;
        switch(icode){
            //cmov - do the switch case thing
            case '2':
            switch(ifun){
                case '0':
                    cnd = 1;
                    break;
                //less or equal
                case '1':
                    cnd = (conditionCodes.get("SF") ^ conditionCodes.get("OF")) | conditionCodes.get("ZF");
                    break;
                //less
                case '2':
                    cnd = conditionCodes.get("SF") ^ conditionCodes.get("OF");
                    break;
                //equal
                case '3':
                    cnd = conditionCodes.get("ZF");
                    break;
                //not equal
                case '4':
                    cnd = conditionCodes.get("ZF") == 0 ? 1 : 0;
                    break;
                //greater
                //needs rechecking
                case '5':
                    cnd = ((conditionCodes.get("SF") ^ conditionCodes.get("OF")) == 0 && conditionCodes.get("ZF") == 0) ? 1 : 0;
                    break;
                //greater or equal
                case '6':
                    cnd = (conditionCodes.get("SF") ^ conditionCodes.get("OF")) == 0 ? 1 : 0;
                    break;
            }
                valE = 0 + valA;
                break;
            case '3':
                valE = 0 + valC;
                break;
            case '4':
                valE = valB + valC;
                break;
            case '5':
                valE = valB + valC;
                break;
            //operation - switch case based on ifun
            case '6':
                //reset condition codes
                conditionCodes.replace("ZF", 0);
                conditionCodes.replace("SF", 0);
                conditionCodes.replace("OF", 0);
                switch(ifun){
                    case '0':
                        valE = valB + valA;
                        break;
                    case '1':
                        valE = valB - valA;
                        break;
                    case '2':
                        valE = valB & valA;
                        break;
                    case '3':
                        valE = valB ^ valA;
                        break;
                }
                //set condition codes
                if(valE == 0){
                    conditionCodes.replace("ZF", 1);
                }
                if(valE < 0){
                    conditionCodes.replace("SF", 1);
                }
                if((valA > 0 && valB > 0 && valE < 0) || (valA < 0 && valB < 0 && valE > 0)){
                    conditionCodes.replace("OF", 1);
                }
            //jump - switch case based on which condition
            //use valE to check condition (true or false)
            case '7':
                switch(ifun){
                    case '0':
                        cnd = 1;
                        break;
                    //less or equal
                    case '1':
                        cnd = (conditionCodes.get("SF") ^ conditionCodes.get("OF")) | conditionCodes.get("ZF");
                        break;
                    //less
                    case '2':
                        cnd = conditionCodes.get("SF") ^ conditionCodes.get("OF");
                        break;
                    //equal
                    case '3':
                        cnd = conditionCodes.get("ZF");
                        break;
                    //not equal
                    case '4':
                        cnd = conditionCodes.get("ZF") == 0 ? 1 : 0;
                        break;
                    //greater or equal
                    case '5':
                        cnd = (conditionCodes.get("SF") ^ conditionCodes.get("OF")) == 0 ? 1 : 0;
                        break;
                    //greater
                    //needs rechecking
                    case '6':
                        cnd = ((conditionCodes.get("SF") ^ conditionCodes.get("OF")) == 0 && conditionCodes.get("ZF") == 0) ? 1 : 0;
                        break;
                }
            //move the stack pointer down 8 bytes (= one element in the stack array) to store valP on the top of the stack
            case '8':
                valE = valB - 1;
                break;
            case '9':
                valE = valB + 1;
                break;
            case 'A':
                valE = valB - 1;
                break;
            case 'B':
                valE = valB + 1;
                break;
        }
        memory(rA, rB, valA, valB, valC, valE, valP, cnd);
    }

    //this needs some editing - memory should be organized in a way where each element is one digit of a hex number
    public static void memory(int rA, int rB, int valA, int valB, int valC, int valE, int valP, int cnd){
        int valM = 0;
        switch(icode){
            case '4':
                stack[valE] = valA;
                break;
            case '5':
                valM = stack[valE];
                break;
            case '8':
                stack[registers.get(4)] = valP;
                break;
            case '9':
                valM = stack[valA];
                break;
            case 'A':
                stack[valE] = valA;
                break;
            case 'B':
                valM = stack[valA];
                break;
        }
        writeBack(rA, rB, valA, valB, valC, valE, valP, valM, cnd);
    }

    public static void writeBack(int rA, int rB, int valA, int valB, int valC, int valE, int valP, int valM, int cnd){
        switch(icode){
            //case 2 is conditional move - should depend on condition
            case '2':
                if(cnd == 1){
                    registers.replace(rB, valE);
                }
                break;
            case '5':
                registers.replace(rA, valM);
                break;
            case '3':
            case '6':
                registers.replace(rB, valE);
                break;
            case '8':
            case '9':
            case 'A':
                registers.replace(4, valE);
                break;
            case 'B':
                registers.replace(4, valE);
                registers.replace(rA, valM);
                break;
        }
        PCupdate(rA, rB, valA, valB, valC, valE, valP, valM, cnd);
    }

    public static void PCupdate(int rA, int rB, int valA, int valB, int valC, int valE, int valP, int valM, int cnd){
        switch(icode){
            case '7':
                if(cnd == 1){
                    PC = valC;
                }
                else{
                    PC = valP;
                }
                break;
            case '8':
                PC = valC;
                break;
            case '9':
                PC = valM;
                break;
            default:
                PC = valP;
        }
    }

    public static void printResult(){

    }

    static HashMap<Integer, Integer> registers = new HashMap<Integer, Integer>(){{
        put(0, 0);
        put(1, 0);
        put(2, 0);
        put(3, 0);
        //%esp - stack pointer
        put(4, 299);
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
        //we probably don't need the carry flag? since we're not doing unsigned arithmetic
        put("SF", 0); //Sign flag
        put("ZF", 0); //Zero flag
        put("OF", 0); //Overflow flag
    }};
}