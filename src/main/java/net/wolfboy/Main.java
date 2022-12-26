package net.wolfboy;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
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
        long count = Files.lines(programPath).count();

        // Setting up Loading Bar
        ProgressBar pb = new ProgressBar("", count * 2, 1);


        // Running and making Tokens
        for (long i = 0; i < count; i++) {
            try (Stream<String> lines = Files.lines(programPath)) {
                currentLineRead = lines.skip(i).findFirst().get();
            } catch (IOException e) {
                System.out.println(e);
            }
            pb.setExtraMessage("Lexing");
            Tokenizer.tokenizer(currentLineRead);
            pb.step();
            // Parsing
            pb.setExtraMessage("Parsing");
            Parser.Parser(i + 1);
            pb.stepTo(i * 2);
        }
        pb.setExtraMessage("Finishing");
        pb.stepTo(count * 2);
        pb.close();
        if (!Parser.hasException) {
            CodeWriter.CodeWriter(name);
        }
    }
}