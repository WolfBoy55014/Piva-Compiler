package net.wolfboy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public static void main(String[] args) throws IOException {

        Tokenizer.addTokensToList();
        String currentLineRead = "";
        Path programPath = Path.of("test.pv");
        long count = Files.lines(programPath).count();

        // Running and making Tokens
        for (long i = 0; i < count; i++) {
            try (Stream<String> lines = Files.lines(programPath)) {
                currentLineRead = lines.skip(i).findFirst().get();
            } catch (IOException e) {
                System.out.println(e);
            }

            Tokenizer.tokenizer(currentLineRead);
            // Parsing
            Parser.Parser(i + 1);
        }
    }
}