// Author: Yvan Burrie

import java.util.HashMap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import SmartHome.*;

/**
 *
 */
class MainFrame extends JFrame implements ActionListener, Automator.OutputCaller {

    final static String APPLICATION_NAME = "Home Auto";

    private JFileChooser fileChooser = new JFileChooser();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuNew = new JMenuItem("New");
    private JMenuItem menuLoad = new JMenuItem("Load");
    private JMenuItem menuSave = new JMenuItem("Save");
    private JMenuItem menuSaveAs = new JMenuItem("Save As");
    private JMenuItem menuClose = new JMenuItem("Close");
    private JMenuItem menuExit = new JMenuItem("Exit");
    private JMenu menuSimulation = new JMenu("Simulation");
    private JMenuItem menuOptions = new JMenuItem("Options");
    private JMenuItem menuDetails = new JMenuItem("Details");
    private JMenuItem menuStart = new JMenuItem("Start");
    private JMenuItem menuPause = new JMenuItem("Pause");
    private JMenuItem menuStop = new JMenuItem("Stop");
    private JMenuItem menuIncrease = new JMenuItem("Increase Speed");
    private JMenuItem menuDecrease = new JMenuItem("Decrease Speed");
    private JMenu menuHelp = new JMenu("Help");
    private JMenuItem menuAbout = new JMenuItem("About");
    private JMenuItem menuGuide = new JMenuItem("Guide");

    private NavigationPanel navigationPanel = new NavigationPanel(this);

    private AutomationPanel automationPanel = new AutomationPanel(this);

    private JPanel statusPanel = new JPanel();

    private JLabel statusLabel = new JLabel("Welcome to " + APPLICATION_NAME + "!!!");

    private OptionsFrame optionsFrame;

    private AboutFrame aboutFrame;

    private GuideFrame guideFrame;

    private String projectFileName;

    private JSONObject projectBuffer;

    private boolean projectChanged = false;

    void handleProjectChanged() {

        projectChanged = true;
        update();
    }

    private static HashMap<String, String> READABLE_TYPES = new HashMap<>();

    private static void initializeReadableTypes() {

        /* Make sure to add sub-types first and super-types last: */
        READABLE_TYPES.put(Trigger.ApparatusDetectionEvent.class.toString(), "Apparatus Detection Event");
        READABLE_TYPES.put(Trigger.ApparatusOpaqueEvent.class.toString(), "Apparatus Opaque Event");
        READABLE_TYPES.put(Trigger.ChangeTriggerStartingAction.class.toString(), "Change Trigger Starting Action");
        READABLE_TYPES.put(Trigger.class.toString(), "Trigger");
        READABLE_TYPES.put(RefrigeratorDevice.class.toString(), "Refrigerator");
        READABLE_TYPES.put(LightDevice.class.toString(), "Light");
        READABLE_TYPES.put(VentilatorDevice.class.toString(), "Ventilator");
        READABLE_TYPES.put(AirConditionerDevice.class.toString(), "Air Conditioner");
        READABLE_TYPES.put(SprinklerDevice.class.toString(), "Sprinkler");
        READABLE_TYPES.put(DoorDevice.class.toString(), "Door Detector");
        READABLE_TYPES.put(RollerDoorDevice.class.toString(), "Roller Door");
        READABLE_TYPES.put(LockableDoorDevice.class.toString(), "Lockable Door Detector");
        READABLE_TYPES.put(OvenDevice.class.toString(), "Oven");
        READABLE_TYPES.put(MotionSensorDevice.class.toString(), "MotionSensor");
        READABLE_TYPES.put(WindowDevice.class.toString(), "Window");
        READABLE_TYPES.put(ClothesWasherDevice.class.toString(), "Clothes Washer");
        READABLE_TYPES.put(DishWasherDevice.class.toString(), "Dish Washer");
        READABLE_TYPES.put(StoveDevice.class.toString(), "Stove");
        READABLE_TYPES.put(ThermostatDevice.class.toString(), "Thermostat");
        READABLE_TYPES.put(WallFixture.class.toString(), "Wall");
        READABLE_TYPES.put(BenchFixture.class.toString(), "Bench");
        READABLE_TYPES.put(WindowFixture.class.toString(), "Window");
        READABLE_TYPES.put(DoorFixture.class.toString(), "Door");
        READABLE_TYPES.put(Venue.class.toString(), "Venue");
        READABLE_TYPES.put(Device.class.toString(), "Device");
        READABLE_TYPES.put(Fixture.class.toString(), "Fixture");
        READABLE_TYPES.put(Entity.class.toString(), "Entity");
    }

    static HashMap<String, String> getReadableTypes() {

        if (READABLE_TYPES.isEmpty()) {
            initializeReadableTypes();
        }
        return READABLE_TYPES;
    }

    static String getReadableType(Object object) {

        if (READABLE_TYPES.isEmpty()) {
            initializeReadableTypes();
        }
        return READABLE_TYPES.get(object.getClass().toString());
    }

    /**
     * Contains the current automator or automator instance.
     */
    Simulator simulator;

    public static void main(String[] args) {

        new MainFrame();
    }

    MainFrame() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        setupMenuBar();
        setJMenuBar(menuBar);

        add(new JScrollPane(navigationPanel), BorderLayout.LINE_START);
        add(new JScrollPane(automationPanel), BorderLayout.CENTER);

        setupStatusBar();
        add(statusPanel, BorderLayout.SOUTH);

        update();

        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupMenuBar() {

        menuBar.add(menuFile);
        menuFile.add(menuNew);
        menuNew.addActionListener(this);
        menuFile.add(menuLoad);
        menuLoad.addActionListener(this);
        menuFile.add(menuSave);
        menuSave.addActionListener(this);
        menuFile.add(menuSaveAs);
        menuSaveAs.addActionListener(this);
        menuFile.add(menuClose);
        menuClose.addActionListener(this);
        menuFile.add(menuExit);
        menuExit.addActionListener(this);

        menuBar.add(menuSimulation);
        menuSimulation.add(menuOptions);
        menuOptions.addActionListener(this);
        menuSimulation.add(menuDetails);
        menuDetails.addActionListener(this);
        menuSimulation.add(menuStart);
        menuStart.addActionListener(this);
        menuSimulation.add(menuPause);
        menuPause.addActionListener(this);
        menuSimulation.add(menuStop);
        menuStop.addActionListener(this);
        menuSimulation.add(menuIncrease);
        menuIncrease.addActionListener(this);
        menuSimulation.add(menuDecrease);
        menuDecrease.addActionListener(this);

        menuBar.add(menuHelp);
        menuHelp.add(menuAbout);
        menuAbout.addActionListener(this);
        menuHelp.add(menuGuide);
        menuGuide.addActionListener(this);
    }

    private void setupStatusBar() {

        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.add(statusLabel);
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private boolean setupSimulator() {

        if (projectBuffer == null) {
            simulator = new Simulator();
        } else {
            try {
                simulator = new Simulator(projectBuffer);
            } catch (JsonDeserializedError exception) {
                showErrorMessage("Failed to JSON-deserialize the simulator: " + exception.getMessage() + ".");
                return false;
            }
        }
        simulator.registerOutputCaller(this);
        navigationPanel.initialize(simulator);
        automationPanel.initialize(simulator);
        setStatusText("Simulator setup...");
        return true;
    }

    private void update() {

        /* Update application title: */
        setTitle(APPLICATION_NAME + (simulator != null ? " - " + (simulator.getName() != null ? simulator.getName() : "Untitled Project") + (projectChanged ? "*" : "") : ""));

        /* Update menu bar: */
        menuSave.setEnabled(simulator != null && projectChanged);
        menuSaveAs.setEnabled(simulator != null);
        menuClose.setEnabled(simulator != null);
        menuSimulation.setEnabled(simulator != null);
        menuPause.setEnabled(simulator != null && simulator.isStarted());
        menuStop.setEnabled(simulator != null && simulator.isStarted());
        menuStart.setText(simulator != null && simulator.isStarted() ? "Restart" : "Start");
        menuPause.setText(simulator != null && simulator.isPaused() ? "Resume" : "Pause");

        /* Update panels: */
        navigationPanel.setEnabled(simulator != null);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        Object actionEventSource = actionEvent.getSource();

        if (actionEventSource == menuNew) {
            handleNew();
            return;
        }
        if (actionEventSource == menuLoad) {
            handleLoad();
            return;
        }
        if (actionEventSource == menuSave) {
            handleSave();
            return;
        }
        if (actionEventSource == menuSaveAs) {
            handleSaveAs();
            return;
        }
        if (actionEventSource == menuExit) {
            handleExit();
            return;
        }
        if (actionEventSource == menuClose) {
            handleClose();
            return;
        }
        if (actionEventSource == menuOptions) {
            handleSimulationOptions();
            return;
        }
        if (actionEventSource == menuDetails) {
            handleSimulationDetails();
            return;
        }
        if (actionEventSource == menuStart) {
            handleSimulationRun();
            return;
        }
        if (actionEventSource == menuPause) {
            handleSimulationPause();
            return;
        }
        if (actionEventSource == menuStop) {
            handleSimulationStop();
            return;
        }
        if (actionEventSource == menuIncrease) {
            if (simulator != null) {
                simulator.setSyncSpeed(simulator.getSyncSpeed() * 2);
            }
            return;
        }
        if (actionEventSource == menuDecrease) {
            if (simulator != null) {
                simulator.setSyncSpeed(simulator.getSyncSpeed() - 2 * simulator.getSyncSpeed());
            }
            return;
        }
        if (actionEventSource == menuAbout) {
            handleAbout();
            return;
        }
        if (actionEventSource == menuGuide) {
            handleGuide();
            return;
        }
    }

    private boolean handleNew() {

        if (!handleClose()) {
            return false;
        }

        /* Request new project name: */
        while (true) {
            String inputtedProjectName = JOptionPane.showInputDialog(this, "Name of new project:");

            /* Cancel: */
            if (inputtedProjectName == null) {
                return false;
            }

            /* Check project name: */
            long minimumProjectNameLength = 3;
            if (inputtedProjectName.length() < minimumProjectNameLength) {
                showErrorMessage("The name of the project must have a minimum of " + minimumProjectNameLength + " characters.");
                /* Continue asking... */
                continue;
            }

            if (!setupSimulator()) {
                return false;
            }
            simulator.setName(inputtedProjectName);
            break;
        }
        projectChanged = true;
        update();
        return true;
    }

    private boolean handleLoad() {

        if (!handleClose()) {
            return false;
        }
        int openDialogResult = fileChooser.showOpenDialog(this);
        switch (openDialogResult) {
            case JFileChooser.APPROVE_OPTION:
                /* Click ok: */
                try {
                    File file = fileChooser.getSelectedFile();
                    projectFileName = file.getAbsolutePath();
                    if (!file.canRead()) {
                        showErrorMessage("JSON configuration file is not readable.");
                        return false;
                    }
                    projectBuffer = openJsonFile(projectFileName);
                } catch (IOException exception) {
                    showErrorMessage("JSON configuration file could not be opened: " + exception.getMessage() + ".");
                    return false;
                } catch (ParseException exception) {
                    showErrorMessage("JSON parser could not convert raw data: " + exception.getMessage() + ".");
                    return false;
                }
                if (!setupSimulator()) {
                    return false;
                }
                update();
                return true;
            default:
                /* Click cancel: */
                return false;
        }
    }

    private boolean handleSave() {

        if (simulator == null) {
            return false;
        }
        if (projectFileName == null) {
            /* Maybe the project was newly created and does not exist as a file yet: */
            return handleSaveAs();
        }
        return handleSaveForced();
    }

    private boolean handleSaveAs() {

        /* The user must choose a file name: */
        JFileChooser fileChooser = new JFileChooser();
        int saveDialogResult = fileChooser.showSaveDialog(this);
        switch (saveDialogResult) {
            case JFileChooser.APPROVE_OPTION:
                File file = fileChooser.getSelectedFile();
                projectFileName = file.getAbsolutePath();
                return handleSaveForced();
            default:
                /* Cancel choosing file name: */
                return false;
        }
    }

    private boolean handleSaveForced() {

        if (projectFileName == null || simulator == null) {
            return false;
        }
        try {
            projectBuffer = simulator.jsonSerialize();
        } catch (JsonSerializedError exception) {
            showErrorMessage("Failed to serialize simulator: " + exception.getMessage() + ".");
            return false;
        }
        try {
            saveFile(projectFileName, projectBuffer.toString());
        } catch (IOException exception) {
            showErrorMessage("Failed to save project: " + exception.getMessage() + ".");
            return false;
        }
        projectChanged = false;
        update();
        setStatusText("Project saved...");
        return true;
    }

    private boolean handleClose() {

        if (simulator == null || !projectChanged) {
            handleCloseForced();
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
                if (projectFileName == null) {
                    /* Maybe the project was newly created and does not exist as a file: */
                    if (!handleSaveAs()) {
                        return false;
                    }
                } else {
                    if (!handleSaveForced()) {
                        return false;
                    }
                }
            case JOptionPane.NO_OPTION:
                handleCloseForced();
                return true;
            default:
                /* Cancel saving. */
                return false;
        }
    }

    private void handleCloseForced() {

        projectChanged = false;
        projectFileName = null;
        projectBuffer = null;
        simulator = null;
        update();
        setStatusText("Project closed...");
    }

    private boolean handleExit() {

        if (handleClose()) {
            int confirmDialogResult = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION);
            switch (confirmDialogResult) {
                case JOptionPane.YES_OPTION:
                    dispose();
                    return true;
            }
        }
        return false;
    }

    private void handleSimulationOptions() {

        if (optionsFrame == null) {
            optionsFrame = new OptionsFrame(this);
        }
        optionsFrame.setVisible(true);
    }

    private void handleSimulationDetails() {

    }

    private boolean handleSimulationRun() {

        if (simulator == null) {
            return false;
        }
        if (simulator.isStarted()) {
            if (!simulator.restart()) {
                return false;
            }
            setStatusText("Simulation restarted...");
        } else {
            if (!simulator.start()) {
                return false;
            }
            setStatusText("Simulation started...");
        }
        update();
        return true;
    }

    private boolean handleSimulationPause() {

        if (simulator == null) {
            return false;
        }
        if (simulator.isPaused()) {
            if (!simulator.resume()) {
                return false;
            }
            setStatusText("Simulation resumed...");
        } else {
            if (!simulator.pause()) {
                return false;
            }
            setStatusText("Simulation paused...");
        }
        update();
        return true;
    }

    private boolean handleSimulationStop() {

        if (simulator == null) {
            return false;
        }
        if (!simulator.stop()) {
            return false;
        }
        setStatusText("Simulation stopped...");
        update();
        return true;
    }

    private void handleAbout() {

        if (aboutFrame == null) {
            aboutFrame = new AboutFrame();
        }
        aboutFrame.setVisible(true);
    }

    private void handleGuide() {

        if (guideFrame == null) {
            guideFrame = new GuideFrame();
        }
        guideFrame.setVisible(true);
    }

    static String loadFile(String fileName) throws IOException {

        File file = new File(fileName);
        long fileSize = file.length();
        return readFile(file, fileSize);
    }

    static String readFile(File file, long length) throws IOException {

        InputStream inputStream = new FileInputStream(file.getAbsolutePath());
        byte[] fileBytes = new byte[(int) length];
        long numBytesRead = inputStream.read(fileBytes);
        if (numBytesRead == length) {
            return new String(fileBytes);
        }
        return null;
    }

    static void saveFile(String fileName, String fileContent) throws IOException {

        FileWriter file = new FileWriter(fileName);
        file.write(fileContent);
        file.flush();
        file.close();
    }

    static JSONObject parseJsonString(String jsonString) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);
        return (JSONObject) jsonObject;
    }

    static JSONObject openJsonFile(String fileName) throws IOException, ParseException {

        return parseJsonString(loadFile(fileName));
    }

    void setStatusText(String message) {

        statusLabel.setText(message);
    }

    @Override
    public void setOutputText(String message) {

        setStatusText(message);
    }

    static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    void showWarningMessage(String message) {

    }
}
