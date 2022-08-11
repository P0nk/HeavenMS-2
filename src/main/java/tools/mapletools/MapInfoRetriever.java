package tools.mapletools;

import provider.wz.WZFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RonanLana
 * <p>
 * The main objective of this tool is to locate all mapids that doesn't have
 * the "info" node in their WZ node tree.
 */
public class MapInfoRetriever {
    private static final Path OUTPUT_FILE = ToolConstants.getOutputFile("map_info_report.txt");
    private static final List<Integer> missingInfo = new ArrayList<>();

    private static byte status = 0;
    private static boolean hasInfo;

    private static String getName(String token) {
        int i, j;
        char[] dest;
        String d;

        i = token.lastIndexOf("name");
        i = token.indexOf("\"", i) + 1; //lower bound of the string
        j = token.indexOf("\"", i);     //upper bound

        dest = new char[50];
        token.getChars(i, j, dest, 0);

        d = new String(dest);
        return (d.trim());
    }

    private static void forwardCursor(BufferedReader reader, int st) {
        String line = null;

        try {
            while (status >= st && (line = reader.readLine()) != null) {
                simpleToken(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void simpleToken(String token) {
        if (token.contains("/imgdir")) {
            status -= 1;
        } else if (token.contains("imgdir")) {
            status += 1;
        }
    }

    private static boolean translateToken(BufferedReader reader, String token) {
        String d;
        int temp;

        if (token.contains("/imgdir")) {
            status -= 1;
        } else if (token.contains("imgdir")) {
            if (status == 1) {
                d = getName(token);
                if (d.contains("info")) {
                    hasInfo = true;
                    return true;
                }

                temp = status;
                forwardCursor(reader, temp);
            }

            status += 1;
        }

        return false;
    }

    private static void searchMapDirectory(int mapArea) {
        final Path mapDirectory = Path.of(WZFiles.MAP.getFilePath() + "/Map/Map" + Integer.toString(mapArea));
        System.out.println("Parsing map area " + mapArea);
        try {
            Files.walk(mapDirectory)
                    .filter(MapInfoRetriever::isRelevantFile)
                    .forEach(MapInfoRetriever::searchMapFile);
        } catch (UncheckedIOException | IOException e) {
            System.err.println("Directory " + mapDirectory.getFileName().toString() + " does not exist");
        }
    }

    private static boolean isRelevantFile(Path file) {
        return file.getFileName().toString().endsWith(".xml");
    }

    private static void searchMapFile(Path file) {
        // This will reference one line at a time
        String line = null;

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            hasInfo = false;
            status = 0;

            while ((line = reader.readLine()) != null) {
                if (translateToken(reader, line)) {
                    break;
                }
            }

            if (!hasInfo) {
                missingInfo.add(Integer.valueOf(file.getFileName().toString().split(".img.xml")[0]));
            }
        } catch (IOException ex) {
            System.out.println("Error reading file '" + file.getFileName().toString() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeReport() {
        try (PrintWriter printWriter = new PrintWriter(Files.newOutputStream(OUTPUT_FILE))) {
            if (!missingInfo.isEmpty()) {
                for (Integer i : missingInfo) {
                    printWriter.println(i);
                }
            } else {
                printWriter.println("All map files contain 'info' node.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 9; i++) {
            searchMapDirectory(i);
        }
        writeReport();
    }

}

