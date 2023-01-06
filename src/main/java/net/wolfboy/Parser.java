package net.wolfboy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Parser {
    public static List<String> file = new ArrayList<String>();
    static List<Token> tokens = Tokenizer.tokens;
    static boolean hasException = false;

    static int numOfInFunctions = 0;
    public static void Parser(long lineNumber) {
        int i = 0;
        if (i < tokens.size()) {
            switch (tokens.get(i).type) {
                case "Declarator" -> Declarator(i, lineNumber);
                case "Function" -> Function(i, lineNumber);
                case "Identifier" -> Reassign(i, lineNumber);
            }
        }
    }









    static void Function(int i, long lineNumber) {
        switch (tokens.get(i).value) {
            case "out" -> OutFunction(i, lineNumber);
            case "in" -> InFunction(i, lineNumber);
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
                                hasException = true;
                                break;
                            }
                        } else {
                            System.out.println(Main.ANSI_RED + "Exception; Not an Operator " + Main.ANSI_RESET + Main.ANSI_PURPLE  + "Line: " + lineNumber + Main.ANSI_RESET);
                            hasException = true;
                            break;
                        }
                    }
                }
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Not an Equals Sign " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                hasException = true;
            }
        }
        while (p != 0) {
            if (p < 0) {
                output = output.concat(")");
                p++;
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Extra or Trailing Parentheses " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                hasException = true;
                break;
            }
        }
        output = output.concat(";");

        // Outputting result
        file.add(output);
    }

    static void OutFunction(int i, long lineNumber) {
        int p = 0;
        String output = "";
        out:
        if (i + 1 < tokens.size()) {
            if (Objects.equals(tokens.get(i + 1).value, "(") | Objects.equals(tokens.get(i + 1).value, "*")) {
                if (Objects.equals(tokens.get(i + 1).value, "(")) {
                    output = "System.out.println(";
                } else if (Objects.equals(tokens.get(i + 1).value, "*")) {
                    if (tokens.size() > i + 2) {
                        if (Objects.equals(tokens.get(i + 2).value, "(")) {
                            output = "System.out.print(";
                            i++;
                        } else {
                            System.out.println(Main.ANSI_RED + "Exception; Invalid Output Syntax " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                            hasException = true;
                            break out;
                        }
                    } else {
                        System.out.println(Main.ANSI_RED + "Exception; Invalid Output Syntax " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                        hasException = true;
                        break out;
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
                        hasException = true;
                        break;
                    }
                }
                output = output.concat(";");

                // Outputting Result
                file.add(output);
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Empty Output Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                hasException = true;
            }
        } else {
            System.out.println(Main.ANSI_RED + "Exception; Empty Output Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
            hasException = true;
        }
    }

    static void InFunction(int i, long lineNumber) {
        String output = "";
        int p = 0;

        if (i + 1 < tokens.size()) {
            if (Objects.equals(tokens.get(i + 1).value, "(")) {

                // Setting up Behind-The-Scenes Scanner Work
                if (numOfInFunctions == 0) {
                    numOfInFunctions++;
                    file.add("java.util.Scanner inputScanner = new java.util.Scanner(System.in);");
                    file.add("String helloFromTheCreator = \"\";");
                }
                file.add("helloFromTheCreator = inputScanner.nextLine();");
                i++;

                if (tokens.size() > i + 1) {
                    if (Objects.equals(tokens.get(i + 1).type, "Identifier")) {
                        file.add(tokens.get(i + 1).value + " = Integer.parseInt( helloFromTheCreator );");
                    } else {
                        System.out.println(Main.ANSI_RED + "Exception; Invalid Input Syntax " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                        hasException = true;
                    }
                } else {
                    System.out.println(Main.ANSI_RED + "Exception; Empty Input Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                    hasException = true;
                }
                /*
                for (int ii = i; ii < tokens.size(); i++) {
                    if (ii + 1 < tokens.size()) {

                    }
                }

                 */
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Invalid Input Syntax " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                hasException = true;
            }
        } else {
            System.out.println(Main.ANSI_RED + "Exception; Empty Input Function " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
            hasException = true;
        }




        //hasException = true;
    }

    static void Reassign(int i, long lineNumber) {
        String output = "";
        output = output.concat(tokens.get(i).value + " " + tokens.get(i + 1).value + " ");
        int p = 0;

        if ((i + 1) < tokens.size()) {
            if (Objects.equals(tokens.get(i + 1).value, "=")) {
                output = output.concat(tokens.get(i + 2).value);
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
                                hasException = true;
                                break;
                            }
                        } else {
                            System.out.println(Main.ANSI_RED + "Exception; Not an Operator " + Main.ANSI_RESET + Main.ANSI_PURPLE  + "Line: " + lineNumber + Main.ANSI_RESET);
                            hasException = true;
                            break;
                        }
                    }
                }
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Not an Equals Sign " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                hasException = true;
            }
        }
        while (p != 0) {
            if (p < 0) {
                output = output.concat(")");
                p++;
            } else {
                System.out.println(Main.ANSI_RED + "Exception; Extra or Trailing Parentheses " + Main.ANSI_RESET + Main.ANSI_PURPLE + "Line: " + lineNumber + Main.ANSI_RESET);
                hasException = true;
                break;
            }
        }
        output = output.concat(";");

        // Outputting result
        file.add(output);
    }
}