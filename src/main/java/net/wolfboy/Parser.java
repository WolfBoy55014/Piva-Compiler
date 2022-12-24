package net.wolfboy;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONObject;


public class Parser {
    public static JSONObject file = new JSONObject();
    static List<Token> tokens = Tokenizer.tokens;
    public static void Parser(long lineNumber) {


        for (int i = 0; i < tokens.size(); i++) {
            switch (tokens.get(i).type) {
                case "Declarator" :
                    Declarator(i, lineNumber);
                    break;
                case "Function" :
                    Function(i, lineNumber);
                    break;
            }

        }
    }









    static void Function(int i, long lineNumber) {
        switch (tokens.get(i).value) {
            case "out":
                int p = 0;
                String output = "";
                if (i + 1 < tokens.size()) {
                    if (Objects.equals(tokens.get(i + 1).value, "(") | Objects.equals(tokens.get(i + 1).value, "*")) {
                        if (Objects.equals(tokens.get(i + 1).value, "(")) {
                            output = "System.out.println(";
                        } else if (Objects.equals(tokens.get(i + 1).value, "*")) {
                            if (Objects.equals(tokens.get(i + 2).value, "(")) {
                                output = "System.out.print(";
                                i ++;
                            } else {
                                System.out.println(Main.ANSI_RED + "Exception; Invalid Output Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                                break;
                            }
                        }
                        p--;
                        i++;
                        for (int ii = i; ii + 1 < tokens.size(); ) {
                            if (ii + 1 < tokens.size()) {
                                output = output.concat(tokens.get(ii + 1).value);
                                if (Objects.equals(tokens.get(ii + 1).value, ")")) {
                                    p++;
                                } else if (Objects.equals(tokens.get(ii + 1).value, "(")) {
                                    p--;
                                }
                                ii++;
                            }
                        }
                        while (p != 0) {
                            if (p < 0) {
                                output = output.concat(")");
                                p++;
                            } else {
                                System.out.println(Main.ANSI_RED + "Exception; Extra or Trailing Parentheses " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                                break;
                            }
                        }
                        output = output.concat(";");

                        // Outputting Result
                        System.out.println(output);
                    } else {
                        System.out.println(Main.ANSI_RED + "Exception; Empty Output Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                        break;
                    }
                } else {
                    System.out.println(Main.ANSI_RED + "Exception; Empty Output Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                }
        }
    }
    static void Declarator(int i, long lineNumber) {
        String output = "";
        output = output.concat(tokens.get(i).value + " " + tokens.get(i + 1).value);
        i++;
        int p = 0;

        if ((i + 1) < tokens.size()) {
            if (Objects.equals(tokens.get(i + 1).value, "=")) {
                output = output.concat(" = " + tokens.get(i + 2).value);
                i+=2;
                for (int ii = i; ii + 1 < tokens.size(); ) {

                    // Parentheses Counting and Compensating
                    if (Objects.equals(tokens.get(ii).value, "(")) {
                        ii++;
                        output = output.concat(tokens.get(ii).value);
                        p--;
                    } else if (Objects.equals(tokens.get(ii + 1).value, ")")) {
                        output = output.concat(")");
                        ii++;
                        p++;
                    }
                    if (tokens.size() > ii + 1) {
                        if (Objects.equals(tokens.get(ii + 1).type, "Operator")) {
                            if (tokens.size() > ii + 2) {
                                output = output.concat(" " + tokens.get(ii + 1).value + " " + tokens.get(ii + 2).value);
                                ii += 2;
                            } else {
                                System.out.println(Main.ANSI_RED + "Exception; Trailing Operator " + Main.ANSI_RESET + Main.ANSI_PURPLE  + "Line: " + lineNumber + Main.ANSI_RESET);
                                break;
                            }
                        } else {
                            System.out.println(Main.ANSI_RED + "Exception; Not an Operator " + Main.ANSI_RESET + Main.ANSI_PURPLE  + "Line: " + lineNumber + Main.ANSI_RESET);
                            break;
                        }
                    }
                }
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Not an Equals Sign " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
            }
        }
        while (p != 0) {
            if (p < 0) {
                output = output.concat(")");
                p++;
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Extra or Trailing Parentheses " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                break;
            }
        }
        output = output.concat(";");
        System.out.println(output);
    }
}
