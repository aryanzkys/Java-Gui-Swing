import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public static void ensureFileExists(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();

        try {
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Gagal membuat file: " + filePath);
            System.out.println("Detail: " + e.getMessage());
        }
    }

    public static List<String> readLines(String filePath) {
        ensureFileExists(filePath);

        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + filePath);
            System.out.println("Detail: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Gagal menutup reader: " + e.getMessage());
                }
            }
        }

        return lines;
    }

    public static void writeLines(String filePath, List<String> lines) {
        ensureFileExists(filePath);

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis file: " + filePath);
            System.out.println("Detail: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static boolean isFileEmpty(String filePath) {
        ensureFileExists(filePath);
        File file = new File(filePath);
        return file.length() == 0;
    }

    public static void initializeWithDummyDataIfEmpty(String filePath, List<String> dummyData) {
        if (isFileEmpty(filePath)) {
            writeLines(filePath, dummyData);
        }
    }
}
