// Author: Yvan Burrie

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import SmartHome.*;

abstract public class Home {

    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame();
    }
}

class HomeError extends RuntimeException {

    private String errorMessage;

    HomeError(String messgae) {
        this.errorMessage = messgae;
    }
}

/**
 *
 */
class MainFrame extends JFrame implements ActionListener {

    final public static String APPLICATION_NAME = "Home Auto";

    private JFileChooser fileChooser = new JFileChooser();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuNew = new JMenuItem("New");
    private JMenuItem menuLoad = new JMenuItem("Load");
    private JMenuItem menuSave = new JMenuItem("Save");
    private JMenuItem menuClose = new JMenuItem("Close");
    private JMenuItem menuExit = new JMenuItem("Exit");
    private JMenu menuSimulation = new JMenu("Simulation");
    private JMenuItem menuDetails = new JMenuItem("Details");
    private JMenuItem menuRun = new JMenuItem("Run");
    private JMenuItem menuPause = new JMenuItem("Pause");
    private JMenuItem menuStop = new JMenuItem("Stop");
    private JMenu menuHelp = new JMenu("Help");
    private JMenuItem menuAbout = new JMenuItem("About");
    private JMenuItem menuGuide = new JMenuItem("Guide");

    private JPanel infoFrame = new JPanel();

    private JLabel statusBar = new JLabel();

    private String configurationsFileName;

    private JSONObject configurationsObject;

    private String projectName;

    private boolean changesMade = false;

    /**
     * Contains the current automator or automator instance.
     */
    private Simulator simulator;

    MainFrame() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        setJMenuBar(menuBar);

        menuBar.add(menuFile);
        menuFile.add(menuNew);
        menuNew.addActionListener(this);
        menuFile.add(menuLoad);
        menuLoad.addActionListener(this);
        menuFile.add(menuSave);
        menuSave.addActionListener(this);
        menuFile.add(menuClose);
        menuClose.addActionListener(this);
        menuFile.add(menuExit);
        menuExit.addActionListener(this);

        menuBar.add(menuSimulation);
        menuSimulation.add(menuDetails);
        menuDetails.addActionListener(this);
        menuSimulation.add(menuRun);
        menuRun.addActionListener(this);
        menuSimulation.add(menuPause);
        menuPause.addActionListener(this);
        menuSimulation.add(menuStop);
        menuStop.addActionListener(this);

        menuBar.add(menuHelp);
        menuHelp.add(menuAbout);
        menuAbout.addActionListener(this);
        menuHelp.add(menuGuide);
        menuGuide.addActionListener(this);

        add(infoFrame);

        add(statusBar);

        update();

        setVisible(true);
    }

    private void setupSimulator() {

        if (configurationsObject == null) {
            simulator = new Simulator();
        } else {
            simulator = new Simulator(configurationsObject);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object eventSource = event.getSource();

        if (eventSource == menuNew) {
            handleNew();
            return;
        }
        if (eventSource == menuLoad) {
            handleLoad();
            return;
        }
        if (eventSource == menuSave) {
            handleSave();
            return;
        }
        if (eventSource == menuExit) {
            handleExit();
            return;
        }
        if (eventSource == menuClose) {
            handleClose();
            return;
        }
        if (eventSource == menuDetails) {
            handleSimulationDetails();
            return;
        }
        if (eventSource == menuRun) {
            handleSimulationRun();
            return;
        }
        if (eventSource == menuPause) {
            handleSimulationPause();
            return;
        }
        if (eventSource == menuStop) {
            handleSimulationStop();
            return;
        }
        if (eventSource == menuAbout) {
            handleAbout();
            return;
        }
        if (eventSource == menuGuide) {
            handleGuide();
            return;
        }
    }

    private void update() {

        /* Update application title: */
        if (projectName != null) {
            setTitle(String.format("%s - %s", APPLICATION_NAME, projectName));
        } else {
            setTitle(String.format("%s", APPLICATION_NAME));
        }

        /* Update menu visibility based on automator existence: */
        if (simulator == null) {
            menuSave.setEnabled(false);
            menuClose.setEnabled(false);
            menuSimulation.setEnabled(false);
        } else {
            menuSave.setEnabled(true);
            menuClose.setEnabled(true);
            menuSimulation.setEnabled(true);
        }
        if (changesMade) {
            menuSave.setEnabled(true);
        } else {
            menuSave.setEnabled(false);
        }
    }

    private void handleNew() {

        if (!handleClose()) {
            return;
        }

        /* Request new project name: */
        while (true) {
            String projectNewName = JOptionPane.showInputDialog(this, "Name of new project:");

            /* Cancel: */
            if (projectNewName == null) {
                return;
            }

            /* Check project name: */
            if (projectNewName.length() <= 3) {
                JOptionPane.showMessageDialog(
                        this,
                        "The name of the project must have a minimum of 3 characters.",
                        "Project Name Error",
                        JOptionPane.ERROR_MESSAGE);
                continue;
            }

            projectName = projectNewName;
            setupSimulator();
            break;
        }
        changesMade = true;
        update();
    }

    private void handleLoad() {

        int openDialogResult = fileChooser.showOpenDialog(this);
        if (openDialogResult == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try{
                String fileContent = openFile(file.getName());
                configurationsObject = parseJsonString(fileContent);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(
                        this,
                        "JSON configuration file could not be opened.",
                        "Project Loading Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ParseException exception) {
                JOptionPane.showMessageDialog(
                        this,
                        "JSON parser could not convert raw data.",
                        "Project Loading Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            setupSimulator();
        }
    }

    private void handleSave() {

        if (simulator == null) {
            return;
        }
        if (configurationsFileName == null) {
            /* Maybe the project was newly created and does not exist as a file: */
            if (!handleSaveAs()) {
                return;
            }
        } else {
            handleSaveForced();
        }
    }

    private boolean handleSaveAs() {

        /* The user must choose a file name: */
        JFileChooser fileChooser = new JFileChooser();
        int saveDialogResult = fileChooser.showSaveDialog(this);
        switch (saveDialogResult) {
            case JFileChooser.APPROVE_OPTION:
                File file = fileChooser.getSelectedFile();
                configurationsFileName = file.getAbsolutePath();
                handleSaveForced();
                return true;
            default:
                /* Cancel choosing file name. */
                return false;
        }
    }

    private void handleSaveForced() {

        if (configurationsObject == null) {
            return;
        }
        try{
            saveFile(configurationsFileName, configurationsObject.toJSONString());
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save project.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        changesMade = false;
    }

    private boolean handleClose() {

        if (simulator == null) {
            return true;
        }
        if (!changesMade) {
            return true;
        }
        /* Ask the user to save or discard project: */
        int confirmDialogResult = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save changes?",
                "Save Changes",
                JOptionPane.YES_NO_CANCEL_OPTION);
        switch (confirmDialogResult) {
            case JOptionPane.YES_OPTION:
                if (configurationsFileName == null) {
                    /* Maybe the project was newly created and does not exist as a file: */
                    if (!handleSaveAs()) {
                        return true;
                    }
                    handleSaveForced();
                }
                return true;
            case JOptionPane.NO_OPTION:
                handleCloseForced();
                return true;
            default:
                /* Cancel saving. */
                return false;
        }
    }

    private void handleCloseForced() {
        projectName = null;
        configurationsFileName = null;
        configurationsObject = null;
        simulator = null;
    }

    private void handleExit() {
        handleClose();
        dispose();
    }

    private void handleSimulationDetails() {
        // TODO
    }

    private void handleSimulationRun() {
        if (simulator != null) {
            simulator.launch();
        }
        update();
    }

    private void handleSimulationPause() {
        if (simulator != null) {
            simulator.pause();
        }
        update();
    }

    private void handleSimulationStop() {
        if (simulator != null) {
            simulator.halt();
        }
        update();
    }

    private void handleAbout() {
        // TODO
    }

    private void handleGuide() {
        // TODO
    }

    private static String openFile(String fileName) throws IOException {

        File file = new File(fileName);
        long fileSize = file.length();
        InputStream inputStream = new FileInputStream(fileName);
        byte[] fileBytes = new byte[(int) fileSize];
        long numBytesRead = inputStream.read(fileBytes);
        if (numBytesRead != fileSize) {
            throw new HomeError("");
        }
        return new String(fileBytes);
    }

    private static void saveFile(String fileName, String fileContent) throws IOException {

        FileWriter file = new FileWriter(fileName);
        file.write(fileContent);
        file.flush();
        file.close();
    }

    private static JSONObject parseJsonString(String jsonString) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);
        return (JSONObject) jsonObject;
    }

    private void setStatusText(String message) {

        statusBar.setText(message);
    }
}
