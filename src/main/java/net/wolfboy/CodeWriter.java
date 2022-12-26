package net.wolfboy;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class CodeWriter {

    private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static void CodeWriter(String name) throws IOException, InterruptedException {
        //initializing FileWriter
        FileWriter classFile;

        try {
            classFile = new FileWriter(name + ".java");

            // Initializing BufferedWriter
            BufferedWriter bf = new BufferedWriter(classFile);

            bf.write("public class " + name + " {");
            bf.newLine();
            bf.write("    public static void main(String[] args) {");
            bf.newLine();

            for (int i = 0; i < Parser.file.size(); i++) {
                bf.write("        " + Parser.file.get(i));
                bf.newLine();
            }

            bf.write("    }");
            bf.newLine();
            bf.write("}");


            // Closing BufferWriter to end operation
            bf.close();
        } catch (IOException except) {
            except.printStackTrace();
        }

        // Setting Compiler Working Directory
        File workingDirectory = new File(System.getProperty("user.dir"));

        // Retrieving OS
        String os = System.getProperty("os.name");

        // Printing both points
        System.out.println(Main.ANSI_PURPLE + workingDirectory + "\\" + name + ".pv" + " | " + Main.ANSI_CYAN + os + Main.ANSI_RESET);

        // Compiling Java
        Runtime.getRuntime().exec("javac " + workingDirectory + "\\" + name + ".java");
        System.out.println("Compiled successfully");

        String[] commands;

        // Executing Java
        if (isWindows) {
            commands = new String[]{"cmd.exe", "/c", "java " + workingDirectory + "\\" + name + ".java"};
        } else {
            commands = new String[]{"sh", "-c", "java " + workingDirectory + "\\" + name + ".java"};
        }

        Process process = Runtime.getRuntime().exec(commands, null, workingDirectory);

        // Outputting result
        OutputStream outputStream = process.getOutputStream();
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        printStream(inputStream);
        printStream(errorStream);

        // Exiting
        process.waitFor();
        int returnValue = process.exitValue();
        System.out.println(Main.ANSI_PURPLE + "Exit - " + Main.ANSI_CYAN + returnValue);

        outputStream.flush();
        outputStream.close();

        // Deleting Java files
        File javaFiles = new File(name + ".java");
        javaFiles.delete();
        javaFiles = new File(name + ".class");
        javaFiles.delete();
    }



    private static void printStream(InputStream inputStream) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        }
    }
}
