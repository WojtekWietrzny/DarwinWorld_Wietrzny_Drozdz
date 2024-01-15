package agh.ics.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReadParameters {
    private static final String FILE = "src/main/resources/setup.csv";

    public static List<String[]> read() throws FileNotFoundException {
        List<String[]> options = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                options.add(line.split(" "));
            }
        }

        return options;
    }
    public static void insertData(String[] settings) throws Exception {
        FileWriter fileWriter = new FileWriter(FILE, true);

        String line = String.join(" ", settings);
        line =line + "\n";
        fileWriter.write(line);

        fileWriter.flush();
        fileWriter.close();
    }
}