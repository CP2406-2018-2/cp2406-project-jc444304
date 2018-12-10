// Author: Yvan Burrie

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import SmartHome.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.swing.*;

/**
 *
 */
public abstract class Test {

    private static Scanner input = new Scanner(System.in);

    /**
     *
     */
    public static void main(String[] args) {

        Automator automator = new Simulator();

        System.out.println("Attempting to test Jython...");
        testJython(automator);

        //System.out.println("Attempting to test SVG canvas...");
        //testGraphics();

        System.out.println("Attempting to test web browser...");
        testWebBrowser();

        System.out.println("Attempting to test synchronizer...");
        testSynchronizer(automator);
    }

    /**
     *
     */
    private static boolean testSynchronizer(Automator automator) {

        Synchronizer thread;

        /* run the thread at normal speed for 5 seconds with 100 executions per second: */
        thread = new Synchronizer(automator, 1, 100);
        thread.start();
        while (thread.isAlive()) continue;
        System.out.println("Total thread loops attempted: " + thread.getLoopsAttempted());
        System.out.println("Total thread loops evaluated: " + thread.getLoopsSucceeded());
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - thread.getFirstNanoTime()) / 1000000000));

        /* run each minute as a second for 10 minutes with 1 execution per second: */
        thread = new Synchronizer(automator, 60, 1);
        thread.start();
        while (thread.isAlive()) continue;
        System.out.println("Total thread loops attempted: " + thread.getLoopsAttempted());
        System.out.println("Total thread loops evaluated: " + thread.getLoopsSucceeded());
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - thread.getFirstNanoTime()) / 1000000000));

        return true;
    }

    private static boolean testWebBrowser() {

        JFXPanel panel = new JFXPanel();
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.getEngine().loadContent("<h1>TEST</h1>");
            panel.setScene(new Scene(webView));
        });

        /* Show the SVG panel inside the frame: */
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setVisible(true);

        return true;
    }

    private static boolean testJython(Simulator simulator) {

        PythonInterpreter interpreter = new PythonInterpreter();

        interpreter.exec(
                "from SmartHome import Simulator\n" +
                        "simulator = Simulator()\n");

        PyObject sandboxSimulatorInstance = interpreter.get("simulator");
        interpreter.exec("print simulator.jsonSerialize()");
        System.out.println(sandboxSimulatorInstance.getClass());

        return true;
    }
}
