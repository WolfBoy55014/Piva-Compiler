package net.wolfboy;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static String ANSI_RESET = "\u001B[0m";
    public static String ANSI_RED = "\u001B[31m";
    public static String ANSI_GREEN = "\u001B[32m";
    public static String ANSI_YELLOW = "\u001B[33m";
    public static String ANSI_BLUE = "\u001B[34m";
    public static String ANSI_PURPLE = "\u001B[35m";
    public static String ANSI_CYAN = "\u001B[36m";
    public static String ANSI_WHITE = "\u001B[37m";
    public static void main(String[] args) throws IOException, InterruptedException {


        Tokenizer.addTokensToList();
        System.out.println(Main.ANSI_PURPLE +
                "80 105 118 97 \n" +
                Main.ANSI_RESET);


        // Taking input
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("File Name: ");
        String name = inputScanner.nextLine();
        String currentLineRead = "";
        Path programPath = Path.of(name + ".pv");
        long count = 0;




        // Running and making Tokens
        if (Files.exists(programPath)) {

            count = Files.lines(programPath).count();

            for (long i = 0; i < count; i++) {

                try (Stream<String> lines = Files.lines(programPath)) {

                    currentLineRead = lines.skip(i).findFirst().get();

                } catch (IOException e) {
                    System.out.println(e);
                }

                // Lexing
                Tokenizer.tokenizer(currentLineRead);

                // Parsing
                Parser.Parser(i + 1);

                // Updating Progress Bar
                progressPercentage((int) i, (int) count);

            }
            progressPercentage((int) count, (int) count);
            if (!Parser.hasException) {
                CodeWriter.CodeWriter(name);
            }
        } else {
            System.out.println(Main.ANSI_RED + "Exception; Can't Find File " + Main.ANSI_RESET + Main.ANSI_PURPLE + "File: " + programPath + Main.ANSI_RESET);
        }
    }


    public static void progressPercentage(int done, int total) {
        int size = 50;
        String iconLeftBoundary = "[";
        String iconDone = "=";
        String iconRemain = ".";
        String iconRightBoundary = "]";

        if (done > total) {
            throw new IllegalArgumentException();
        }
        int donePercents = (100 * done) / total;
        int doneLength = size * donePercents / 100;

        StringBuilder bar = new StringBuilder(iconLeftBoundary);
        for (int i = 0; i < size; i++) {
            if (i < doneLength) {
                bar.append(iconDone);
            } else {
                bar.append(iconRemain);
            }
        }
        bar.append(iconRightBoundary);

        System.out.print(ANSI_CYAN + "\r" + bar + " " + donePercents + "%" + ANSI_RESET);

        if (done == total) {
            System.out.println();
        }

    }

    public static void weLikeColor() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            ANSI_RESET = "\u001B[0m";
            ANSI_RED = "\u001B[31m";
            ANSI_GREEN = "\u001B[32m";
            ANSI_YELLOW = "\u001B[33m";
            ANSI_BLUE = "\u001B[34m";
            ANSI_PURPLE = "\u001B[95m";
            ANSI_CYAN = "\u001B[93m";
            ANSI_WHITE = "\u001B[37m";
        }
    }
}