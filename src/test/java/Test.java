// Author: Yvan Burrie

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import SmartHome.*;

/**
 *
 */
public abstract class Test {

    private static Automator automator;

    private static Scanner input = new Scanner(System.in);

    /**
     *
     */
    public static void main(String[] args) {


        Automator automator = new Automator();
    }

    /**
     *
     */
    private static boolean testSynchronizer() {

        Synchronizer thread;

        /* run the thread at normal speed for 5 seconds with 100 executions per second: */
        thread = new Synchronizer(null, 1, 5, 100);
        thread.start();
        while (thread.isAlive()) continue;
        System.out.println("Total thread loops attempted: " + thread.getLoopsAttempted());
        System.out.println("Total thread loops evaluated: " + thread.getLoopsSucceeded());
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - thread.getFirstNanoTime()) / 1000000000));

        /* run each minute as a second for 10 minutes with 1 execution per second: */
        thread = new Synchronizer(null, 60, 60 * 10, 1);
        thread.start();
        while (thread.isAlive()) continue;
        System.out.println("Total thread loops attempted: " + thread.getLoopsAttempted());
        System.out.println("Total thread loops evaluated: " + thread.getLoopsSucceeded());
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - thread.getFirstNanoTime()) / 1000000000));

        return true;
    }
}
