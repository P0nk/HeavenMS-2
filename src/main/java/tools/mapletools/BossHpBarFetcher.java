package tools.mapletools;

import provider.wz.WZFiles;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RonanLana
 * <p>
 * This application parses the Mob.wz file inputted and generates a report showing
 * all cases where a mob has a boss HP bar and doesn't have a "boss" label.
 * <p>
 * Running it should generate a report file under "lib" folder with the search results.
 */
public class BossHpBarFetcher {
    private static final String OUTPUT_FILE_NAME = "boss_hp_bar_report.txt";
    private static final int INITIAL_STRING_LENGTH = 50;
    private static final List<Integer> missingBosses = new ArrayList<>();

    private static BufferedReader bufferedReader = null;
    private static byte status = 0;
    private static int curBoss;
    private static int curHpTag;
    private static int curMobId;

    private static String getName(String token) {
        int i, j;
        char[] dest;
        String d;

        i = token.lastIndexOf("name");
        i = token.indexOf("\"", i) + 1; //lower bound of the string
        j = token.indexOf("\"", i);     //upper bound

        dest = new char[INITIAL_STRING_LENGTH];
        token.getChars(i, j, dest, 0);

        d = new String(dest);
        return (d.trim());
    }

    private static String getValue(String token) {
        int i, j;
        char[] dest;
        String d;

        i = token.lastIndexOf("value");
        i = token.indexOf("\"", i) + 1; //lower bound of the string
        j = token.indexOf("\"", i);     //upper bound

        dest = new char[INITIAL_STRING_LENGTH];
        token.getChars(i, j, dest, 0);

        d = new String(dest);
        return (d.trim());
    }

    private static void forwardCursor(int st) {
        String line = null;

        try {
            while (status >= st && (line = bufferedReader.readLine()) != null) {
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

    private static void readMobLabel(String token) {
        String name = getName(token);
        String value = getValue(token);

        switch (name) {
            case "boss" -> curBoss = Integer.parseInt(value);
            case "hpTagColor" -> curHpTag = Integer.parseInt(value);
        }
    }

    private static void processMobData() {
        if (curHpTag != Integer.MAX_VALUE && curBoss == Integer.MAX_VALUE) {
            missingBosses.add(curMobId);
        }
    }

    private static void translateToken(String token) {
        String d;

        if (token.contains("/imgdir")) {
            status -= 1;

            if (status == 1) {
                processMobData();
            }
        } else if (token.contains("imgdir")) {
            if (status == 0) {
                String mobText = getName(token);
                curMobId = Integer.parseInt(mobText.substring(0, mobText.lastIndexOf('.')));
            } else if (status == 1) {           //getting info
                d = getName(token);

                if (!d.contentEquals("info")) {
                    forwardCursor(status);
                } else {
                    curBoss = Integer.MAX_VALUE;
                    curHpTag = Integer.MAX_VALUE;
                }
            } else if (status == 2) {
                forwardCursor(status);
            }

            status += 1;
        } else {
            if (status == 2) {      //info tags
                readMobLabel(token);
            }
        }
    }

    private static void readBossHpBarData() throws IOException {
        final Path mobDirectory = WZFiles.MOB.getFile();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(mobDirectory)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    try (BufferedReader br = Files.newBufferedReader(path)) {
                        bufferedReader = br;
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            translateToken(line);
                        }
                    }
                }
            }
        }
    }

    private static void printReportFileHeader(PrintWriter writer) {
        writer.println(" # Report File autogenerated from the MapleBossHpBarFetcher feature by Ronan Lana.");
        writer.println(" # Generated data takes into account several data info from the server-side WZ.xmls.");
        writer.println();
    }

    private static void printReportFileResults(PrintWriter writer) {
        for (int mobId : missingBosses) {
            writer.println("Missing 'isBoss' on id: " + mobId);
        }
    }

    private static void reportBossHpBarData() {
        // This will reference one line at a time

        try (PrintWriter printWriter = new PrintWriter(Files.newOutputStream(ToolConstants.getOutputFile(OUTPUT_FILE_NAME)))) {
            System.out.println("Reading WZs...");
            readBossHpBarData();

            System.out.println("Reporting results...");

            printReportFileHeader(printWriter);
            printReportFileResults(printWriter);

            System.out.println("Done!");
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open mob file.");
        } catch (IOException ex) {
            System.out.println("Error reading mob file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	Instant instantStarted = Instant.now();
        reportBossHpBarData();
        Instant instantStopped = Instant.now();
        Duration durationBetween = Duration.between(instantStarted, instantStopped);
        System.out.println("Get elapsed time in milliseconds: " + durationBetween.toMillis());
        System.out.println("Get elapsed time in seconds: " + durationBetween.toSeconds());
    }

}
