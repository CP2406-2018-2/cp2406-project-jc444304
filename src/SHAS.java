// Author: Yvan Burrie

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 */
public class SHAS {

    /**
     * Determines whether the application will print debugging information, assertion tests, and dump log files.
     */
    final private static boolean DEBUGGING_MODE = true;

    /**
     * Determines whether the application is using CLI (false) or GUI (true).
     */
    final private static boolean GRAPHIC_INTERFACE = false;

    /**
     * A pointer to the input buffer.
     */
    private static Scanner input;

    /**
     * Points to the current automator or simulator instance.
     */
    private static Automator automator;

    /**
     * Points to the synchronization thread.
     */
    private static Synchronizer synchronizer;

    /**
     *
     */
    public static void main(String[] args) {

        if (SHAS.GRAPHIC_INTERFACE) {
            System.out.println("Graphical interface not available in this version.");
        } else {

            input = new Scanner(System.in);

            String choice;

            MAIN_MENU:
            while (true) {

                System.out.println("Welcome to the Smart Home Automation Simulator (SHAS):");
                if (automator == null) {
                    System.out.println(" - CONFIG to configure a new automator.");
                } else {
                    System.out.println(" - CONFIG to re-configure the existing automator.");
                    System.out.println(" - INFO to show details about the existing automator.");
                }
                System.out.println(" - START to initialize.");
                System.out.println(" - EXIT to quit.");
                choice = input.nextLine();

                if (choice.toLowerCase().startsWith("conf")) {

                    do {
                        System.out.println("Specify configuration JSON file name (or CANCEL to abort):");
                        String jsonFileName = input.nextLine();

                        if (jsonFileName.toUpperCase().equals("CANCEL")) {
                            continue MAIN_MENU;
                        }

                        String jsonFileContent;
                        try {
                            jsonFileContent = fetchFileContent(jsonFileName);
                        } catch (IOException exception) {
                            System.out.println("Unable to process JSON file!");
                            continue;
                        }
                        try {
                            parseConfigurations(jsonFileContent);
                        } catch (ParseException exception) {
                            System.out.println("Unable to process JSON configurations!");
                            continue;
                        }

                        System.out.println("JSON file parsed successfully.");

                        while (true) {

                            System.out.println("Choose the system to configure:");
                            System.out.println(" - AUTOMATOR (provides venues and apparatuses interacting from triggers)");
                            System.out.println(" - SIMULATOR (an extension to the automator system but provides scenarios and time period)");
                            choice = input.nextLine();

                            if (choice.toLowerCase().startsWith("sim")) {
                                automator = new Simulator();
                                System.out.println("Simulator successfully created.");
                                break;
                            }
                            if (choice.toLowerCase().startsWith("aut")) {
                                automator = new Automator();
                                System.out.println("Automator successfully created.");
                                break;
                            }
                        }
                        automator.jsonDeserialize(configurations);
                        System.out.println("Configurations successfully loaded.");

                    } while (automator == null);
                }

                if (automator != null && choice.toLowerCase().startsWith("info")) {

                    if (automator instanceof Simulator) {

                        System.out.println("Currently using a simulator:");

                    } else {

                        System.out.println("Currently using an automator:");

                    }
                }

                if (choice.toLowerCase().startsWith("start")) {

                    if (automator == null) {
                        System.out.println("Automator not created yet. Please configure first.");
                        continue MAIN_MENU;
                    }

                    for (; ; ) {

                        System.out.println("Are you sure you want to initialize the automator/simulator?");
                        System.out.println(" - YES to continue.");
                        System.out.println(" - NO to cancel.");
                        choice = input.nextLine();

                        if (choice.toLowerCase().startsWith("yes")) {
                            synchronizer = new Synchronizer(automator);
                            synchronizer.start();
                        }

                        if (choice.toLowerCase().startsWith("no")) {
                            break;
                        }
                    }
                }

                if (choice.toLowerCase().startsWith("exit")) {

                    for (; ; ) {

                        System.out.println("Are you sure you want to exit?");
                        System.out.println(" - YES to continue.");
                        System.out.println(" - NO to cancel.");

                        choice = input.nextLine();

                        if (choice.toLowerCase().startsWith("y")) {
                            break;
                        }

                        if (choice.toLowerCase().startsWith("n")) {
                            continue MAIN_MENU;
                        }
                    }
                    break;
                }
            }
        }
    }

    private static JSONObject configurations;

    private static void parseConfigurations(String jsonString) throws ParseException {

        JSONParser jsonParser = new JSONParser();

        Object bufferObject = jsonParser.parse(jsonString);

        configurations = (JSONObject) bufferObject;
    }

    private static String fetchFileContent(String fileName) throws IOException {

        File file = new File(fileName);
        long fileSize = file.length();

        InputStream inputStream = new FileInputStream(fileName);

        byte[] fileBytes = new byte[(int) fileSize];

        inputStream.read(fileBytes);

        String fileContent = new String(fileBytes);

        return fileContent;
    }

}
