/*
 * main class
 * handles the entire gui and the input
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Handler implements ActionListener, ChangeListener {
    // set up maximum dimensions for replacement panels and the mapPanel
    private int WIDTH = 1000;
    private int HEIGHT = 500;

    // set up the label that will display the output in the console
    private JLabel output = new JLabel("<html>Output :"); // the <html> tag allows the creation of multiple lines in the label
    private String newline = "<br>"; // string that will create a newline character in the output label

    // set up import button that will be used to import the CSV file via dialog
    private JButton importButton = new JButton("Import");
    private JLabel filenameLabel = new JLabel("no file imported");

    // set up button that will generate the map based on the imported CSV file
    private JButton generateButton = new JButton("Generate");

    // set up solving buttons
    private JButton solveButton1 = new JButton("Solve 1");
    private JButton solveButton2 = new JButton("Solve 2");
    private JButton solveButton3 = new JButton("Solve 3");
    private JButton solveButton4 = new JButton("Solve 4");

    // set up button that enables step display
    private JButton stepsButton = new JButton("Show steps");

    // set up solving speed slider
    private JSlider slider = new JSlider();
    private int value;

    // set up mode label
    private JLabel modeLabel = new JLabel("Mode: default");

    // set up overlay object
    private Overlay mapPanel;
    private JPanel holderPanel = new JPanel();
    private JLabel startLabel = new JLabel("NO MAP TO DISPLAY", SwingConstants.CENTER);
    // global variables needed
    private boolean showSteps = false;               // check if the user wants the steps displayed
    private boolean isImported = false;
    private boolean userMode = true;
    private String filePath = "";                    // path to the file that needs to be solved
    private int[][] array;

    private Handler() {


        //set up main panels
        JPanel mainPanel = new JPanel(); // main panel of the JFrame
        JPanel rightPanel = new JPanel();
        JPanel consolePanel = new JPanel();
        JPanel bottomSeparator = new JPanel();

        // panel displayed at start-up
        startLabel.setBackground(Color.darkGray);
        startLabel.setForeground(Color.lightGray);
        startLabel.setFont(Font.getFont(Font.SANS_SERIF));
        JPanel replacingPanel = new JPanel();
        replacingPanel.setLayout(new BorderLayout());
        replacingPanel.add(startLabel, BorderLayout.CENTER);
        replacingPanel.setBackground(Color.darkGray);
        replacingPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // set up main panel layouts
        mainPanel.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());
        consolePanel.setLayout(new BorderLayout());

        // set up console panel and additional set up for the output label
        consolePanel.setPreferredSize(new Dimension(WIDTH, 150));
        consolePanel.setBackground(Color.BLACK);
        output.setForeground(Color.lightGray);
        output.setFont(Font.getFont(Font.SANS_SERIF));
        JScrollPane scroller = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getViewport().setBackground(Color.BLACK);
        scroller.getViewport().setForeground(Color.BLACK);
        scroller.getVerticalScrollBar().setBackground(Color.black);
        scroller.getHorizontalScrollBar().setBackground(Color.darkGray);
        scroller.getHorizontalScrollBar().setForeground(Color.BLACK);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        // a little bit too much trouble for just setting this colors to match the feel, but looks better this way
        scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors(){
                this.thumbColor = Color.darkGray;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(Color.darkGray);
                button.setForeground(Color.gray);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(Color.darkGray);
                button.setForeground(Color.gray);
                return button;
            }
        });
        consolePanel.add(scroller, BorderLayout.CENTER);

        // set up the panel that will contain the controls of the gui
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(150, HEIGHT + 160));
        buttonPanel.setBackground(Color.DARK_GRAY);
        JPanel topPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel botPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(150, 300));
        topPanel.setBackground(Color.DARK_GRAY);
        midPanel.setPreferredSize(new Dimension(150, HEIGHT - 300));
        midPanel.setBackground(Color.DARK_GRAY);
        botPanel.setPreferredSize(new Dimension(150, 160));
        botPanel.setBackground(Color.DARK_GRAY);

        // additional set up for the import button and label
        importButton.addActionListener(this);
        topPanel.add(importButton);
        importButton.setBackground(Color. DARK_GRAY);
        importButton.setForeground(Color.lightGray);
        importButton.setPreferredSize(new Dimension(100, 40));
        importButton.setFont(Font.getFont(Font.SANS_SERIF));
        importButton.setFocusable(false);
        importButton.setToolTipText("import the file used to generate the map");
        topPanel.add(filenameLabel);
        filenameLabel.setForeground(Color.lightGray);
        filenameLabel.setFont(Font.getFont(Font.SANS_SERIF));
        filenameLabel.setToolTipText("displays the name of the imported file");

        // additional set up for the generate button
        generateButton.addActionListener(this);
        topPanel.add(generateButton);
        generateButton.setBackground(Color. DARK_GRAY);
        generateButton.setForeground(Color.lightGray);
        generateButton.setPreferredSize(new Dimension(100, 40));
        generateButton.setFont(Font.getFont(Font.SANS_SERIF));
        generateButton.setFocusable(false);
        generateButton.setToolTipText("generates the map based on the imported file");

        // set up buttons that handle the task solving
        solveButton1.addActionListener(this);
        topPanel.add(solveButton1);
        solveButton1.setBackground(Color.DARK_GRAY);
        solveButton1.setForeground(Color.lightGray);
        solveButton1.setPreferredSize(new Dimension(100, 40));
        solveButton1.setFont(Font.getFont(Font.SANS_SERIF));
        solveButton1.setFocusable(false);
        solveButton1.setEnabled(false);
        solveButton1.setToolTipText("solve the first given task");

        solveButton2.addActionListener(this);
        topPanel.add(solveButton2);
        solveButton2.setBackground(Color.DARK_GRAY);
        solveButton2.setForeground(Color.lightGray);
        solveButton2.setPreferredSize(new Dimension(100, 40));
        solveButton2.setFont(Font.getFont(Font.SANS_SERIF));
        solveButton2.setFocusable(false);
        solveButton2.setToolTipText("solve the second given task");
        solveButton2.setEnabled(false);

        solveButton3.addActionListener(this);
        topPanel.add(solveButton3);
        solveButton3.setBackground(Color.DARK_GRAY);
        solveButton3.setForeground(Color.lightGray);
        solveButton3.setPreferredSize(new Dimension(100, 40));
        solveButton3.setFont(Font.getFont(Font.SANS_SERIF));
        solveButton3.setFocusable(false);
        solveButton3.setToolTipText("solve the third given task");
        solveButton3.setEnabled(false);

        solveButton4.addActionListener(this);
        topPanel.add(solveButton4);
        solveButton4.setBackground(Color.DARK_GRAY);
        solveButton4.setForeground(Color.lightGray);
        solveButton4.setPreferredSize(new Dimension(100, 40));
        solveButton4.setFont(Font.getFont(Font.SANS_SERIF));
        solveButton4.setFocusable(false);
        solveButton4.setToolTipText("solve the fourth given task");
        solveButton4.setEnabled(false);

        JSeparator separator1 = new JSeparator();
        midPanel.add(separator1);
        separator1.setPreferredSize(new Dimension(150, 10));
        separator1.setForeground(Color.darkGray);
        separator1.setBackground(Color.darkGray);

        // additional set up for the step display button
        stepsButton.addActionListener(this);
        midPanel.add(stepsButton);
        stepsButton.setBackground(Color.DARK_GRAY);
        stepsButton.setForeground(Color.lightGray);
        stepsButton.setPreferredSize(new Dimension(120, 50));
        stepsButton.setFont(Font.getFont(Font.SANS_SERIF));
        stepsButton.setFocusable(false);
        stepsButton.setToolTipText("enable displaying of solving steps");
        stepsButton.setEnabled(false);

        JLabel speedLabel = new JLabel("Speed: ");
        midPanel.add(speedLabel);
        speedLabel.setForeground(Color.lightGray);
        speedLabel.setFont(Font.getFont(Font.SANS_SERIF));
        speedLabel.setEnabled(false);

        // additional set up for the speed slider
        midPanel.add(slider);
        slider.setBackground(Color.DARK_GRAY);
        slider.setForeground(Color.lightGray);
        slider.setFont(Font.getFont(Font.SANS_SERIF));
        slider.setPreferredSize(new Dimension(100, 50));
        slider.setMinimum(1);
        slider.setMaximum(5);
        slider.setFocusable(false);
        slider.setPaintLabels(true);
        slider.setEnabled(false);
        slider.setValue(3);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.addChangeListener(this);

        // set up button that clears the console
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        midPanel.add(clearButton);
        clearButton.setBackground(Color.DARK_GRAY);
        clearButton.setForeground(Color.lightGray);
        clearButton.setPreferredSize(new Dimension(120, 50));
        clearButton.setFont(Font.getFont(Font.SANS_SERIF));
        clearButton.setFocusable(false);
        clearButton.setToolTipText("clear the output console");

        JSeparator separator2 = new JSeparator();
        botPanel.add(separator2);
        separator2.setPreferredSize(new Dimension(150, 20));
        separator2.setForeground(Color.darkGray);
        separator2.setBackground(Color.darkGray);
        bottomSeparator.setBackground(Color.DARK_GRAY);
        bottomSeparator.setPreferredSize(new Dimension(WIDTH, 10));

        // set up button that will display help HTML file
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(this);
        botPanel.add(helpButton, BorderLayout.SOUTH);
        helpButton.setBackground(Color. DARK_GRAY);
        helpButton.setForeground(Color.lightGray);
        helpButton.setPreferredSize(new Dimension(100, 40));
        helpButton.setFont(Font.getFont(Font.SANS_SERIF));
        helpButton.setFocusable(false);
        helpButton.setToolTipText("opens up a readme doc file with additional info");

        // set up button that will switch the application mode
        JButton toggleButton = new JButton("Switch");
        toggleButton.addActionListener(this);
        botPanel.add(toggleButton, BorderLayout.SOUTH);
        toggleButton.setBackground(Color. DARK_GRAY);
        toggleButton.setForeground(Color.lightGray);
        toggleButton.setPreferredSize(new Dimension(100, 40));
        toggleButton.setFont(Font.getFont(Font.SANS_SERIF));
        toggleButton.setFocusable(false);
        toggleButton.setToolTipText("toggles the direct solving of the given task on the solve buttons");

        // additional set up for mode label
        botPanel.add(modeLabel);
        modeLabel.setForeground(Color.lightGray);
        modeLabel.setFont(Font.getFont(Font.SANS_SERIF));
        modeLabel.setToolTipText("that's me");

        // panel that will hold the map/replacement for the map at start-up
        holderPanel.setBackground(Color.darkGray);
        holderPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        holderPanel.setLayout(new BorderLayout());
        holderPanel.add(replacingPanel);

        // create the gui layout by adding the panels
        buttonPanel.add(topPanel, BorderLayout.NORTH);
        buttonPanel.add(midPanel, BorderLayout.CENTER);
        buttonPanel.add(botPanel, BorderLayout.SOUTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        leftPanel.add(holderPanel, BorderLayout.NORTH);
        leftPanel.add(bottomSeparator, BorderLayout.CENTER);
        leftPanel.add(consolePanel, BorderLayout.SOUTH);

        // add additional panels to the main panel
        mainPanel.add(leftPanel, BorderLayout.EAST);
        mainPanel.add(rightPanel, BorderLayout.WEST);

        // create the frame and set its properties
        JFrame frame = new JFrame();
        frame.add(mainPanel);
        frame.setTitle("gui");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public static void main(String[] args) {
        new Handler();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch(action) {
            case "Import":
                importFile();
                break;
            case "Generate":
                generateMap();
                break;
            case "Solve 1":
                solveLevel1();
                break;
            case "Solve 2":
                solveLevel2();
                break;
            case "Solve 3":
                solveLevel3();
                break;
            case "Solve 4":
                solveLevel4();
                break;
            case "Show steps":
                showSteps();
                break;
            case "<html> Don't show <br> steps":
                noSteps();
                break;
            case "Clear":
                clearConsole();
                break;
            case "Help":
                displayHelp();
                break;
            case "Switch":
                setMode();
                break;
        }
    }

    // handles the filepath importation
    private void importFile() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            filenameLabel.setText(chooser.getName(file));
            filePath = file.getAbsolutePath();
            //System.out.println(filePath);
            isImported = true;
            if(!generateButton.isEnabled()) {
                generateButton.setEnabled(true);
            }
            String outputString = output.getText();
            output.setText(outputString + newline + "Succesfully imported!");
        } catch (Exception e) {
            e.printStackTrace();
            filenameLabel.setText("no file imported");

            String outputString = output.getText();
            output.setText(outputString + newline + "File not imported!");
        }
    }

    // generates the map
    private void generateMap() {
        if(isImported){
            GetInput input = new GetInput(filePath);
            array = input.getArray();

            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());

            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();
            generateButton.setEnabled(false);
            isImported = false;

            solveButton1.setEnabled(true);
            solveButton2.setEnabled(true);
            solveButton3.setEnabled(true);
            solveButton4.setEnabled(true);
            stepsButton.setEnabled(true);

            String outputString = output.getText();
            output.setText(outputString + newline + "Map generated successfully!");
        } else {
            String outputString = output.getText();
            output.setText(outputString + newline + "No file was imported!");
        }
    }

    // solves first task
    private void solveLevel1() {
        if(userMode) {
            GetInput input = new GetInput(filePath);
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());
            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();

            if(!showSteps) {
                mapPanel.solve_level_1();

                String answer = mapPanel.getAnswer();
                String outputString = output.getText();
                output.setText(outputString + newline + "LEVEL 1 OUTPUT" + newline + answer);
            } else {
                mapPanel.setSpeed(value);
                mapPanel.step_solve_level_1();

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (mapPanel.ansCheck()) {
                            String answer = mapPanel.getAnswer();
                            String outputString = output.getText();
                            output.setText(outputString + newline + "LEVEL 1 OUTPUT" + newline + answer);

                            timer.cancel();
                            timer.purge();
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 100, 100);
            }
        } else {
            GetInput input = new GetInput("inputs/The_Basics.csv");
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());

            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();
            mapPanel.solve_level_1();

            String answer = mapPanel.getAnswer();
            String outputString = output.getText();
            output.setText(outputString + newline + "Input:" + newline + "Number of rows: " + (input.getRow() + 1) +
                    newline + "Number of culumns: " + (input.getCol() + 1) + newline + "Total number of characters: " +
                    ((input.getCol() + 1) * (input.getRow() + 1)) + newline + newline + "LEVEL 1 OUTPUT" + newline + answer);
        }
    }

    //solves second task
    private void solveLevel2() {
        if(userMode) {
            GetInput input = new GetInput(filePath);
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());
            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();

            if(!showSteps) {
                mapPanel.solve_level_2();

                String answer = mapPanel.getAnswer();
                String outputString = output.getText();
                output.setText(outputString + newline + "LEVEL 2 OUTPUT" + newline + answer);
            } else {
                mapPanel.setSpeed(value);
                mapPanel.step_solve_level_2();

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (mapPanel.ansCheck()) {
                            String answer = mapPanel.getAnswer();
                            String outputString = output.getText();
                            output.setText(outputString + newline + "LEVEL 2 OUTPUT" + newline + answer);

                            timer.cancel();
                            timer.purge();
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 100, 100);
            }
        } else {
            GetInput input = new GetInput("inputs/The_Basics.csv");
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());

            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();
            mapPanel.solve_level_2();

            String answer = mapPanel.getAnswer();
            String outputString = output.getText();
            output.setText(outputString + newline + "Input:" + newline + "Number of rows: " + (input.getRow() + 1) +
                    newline + "Number of culumns: " + (input.getCol() + 1) + newline + "Total number of characters: " +
                    ((input.getCol() + 1) * (input.getRow() + 1)) + newline + newline + "LEVEL 2 OUTPUT" + newline + answer);
        }
    }

    //solves third task
    private void solveLevel3() {
        if(userMode) {
            GetInput input = new GetInput(filePath);
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());
            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();

            if(!showSteps) {
                mapPanel.solve_level_3();

                String answer = mapPanel.getAnswer();
                String outputString = output.getText();
                output.setText(outputString + newline + "LEVEL 3 OUTPUT" + newline + answer);
            } else {
                mapPanel.setSpeed(value);
                mapPanel.step_solve_level_3();

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (mapPanel.ansCheck()) {
                            String answer = mapPanel.getAnswer();
                            String outputString = output.getText();
                            output.setText(outputString + newline + "LEVEL 3 OUTPUT" + newline + answer);

                            timer.cancel();
                            timer.purge();
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 100, 100);
            }
        } else {
            GetInput input = new GetInput("inputs/Duplicates.csv");
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());

            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();
            mapPanel.solve_level_3();

            String answer = mapPanel.getAnswer();
            String outputString = output.getText();
            output.setText(outputString + newline + "Input:" + newline + "Number of rows: " + (input.getRow() + 1) +
                    newline + "Number of culumns: " + (input.getCol() + 1) + newline + "Total number of characters: " +
                    ((input.getCol() + 1) * (input.getRow() + 1)) + newline + newline + "LEVEL 3 OUTPUT" + newline + answer);
        }
    }

    // solves fourth task
    private void solveLevel4() {
        if(userMode) {
            GetInput input = new GetInput(filePath);
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());
            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();

            if(!showSteps) {
                mapPanel.solve_level_4();

                String answer = mapPanel.getAnswer();
                String outputString = output.getText();
                output.setText(outputString + newline + "LEVEL 4 OUTPUT" + newline + answer);
            } else {
                mapPanel.setSpeed(value);
                mapPanel.step_solve_level_4();

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (mapPanel.ansCheck()) {
                            String answer = mapPanel.getAnswer();
                            String outputString = output.getText();
                            output.setText(outputString + newline + "LEVEL 4 OUTPUT" + newline + answer);

                            timer.cancel();
                            timer.purge();
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 100, 100);
            }
        } else {
            GetInput input = new GetInput("inputs/Duplicates_Advanced.csv");
            array = input.getArray();
            mapPanel = new Overlay(array);
            mapPanel.setRows(input.getRow());
            mapPanel.setColumns(input.getCol());

            holderPanel.removeAll();
            holderPanel.add(mapPanel, BorderLayout.CENTER);
            holderPanel.revalidate();
            mapPanel.solve_level_4();

            String answer = mapPanel.getAnswer();
            String outputString = output.getText();
            output.setText(outputString + newline + "Input:" + newline + "Number of rows: " + (input.getRow() + 1) +
                    newline + "Number of culumns: " + (input.getCol() + 1) + newline + "Total number of characters: " +
                    ((input.getCol() + 1) * (input.getRow() + 1)) + newline + newline + "LEVEL 4 OUTPUT" + newline + answer);
        }
    }

    // enables the slider and the display of solving steps
    private void showSteps() {
        showSteps = true;
        stepsButton.setText("<html> Don't show <br> steps");
        stepsButton.setHorizontalTextPosition(SwingConstants.CENTER);
        slider.setToolTipText("select the speed at which the steps are displayed");
        slider.setEnabled(true);

        String outputString = output.getText();
        output.setText(outputString + newline + "Step display enabled!");
    }

    // disable the slider and the display of solving steps
    private void noSteps() {
        showSteps = false;
        stepsButton.setText("Show steps");
        slider.setEnabled(false);

        String outputString = output.getText();
        output.setText(outputString + newline + "Step display disabled!");
    }

    // clears the output console
    private void clearConsole() {
        output.setText("<html>Output :");
    }

    // opens up the help HTML file
    private void displayHelp() {
        try {
            String url = "html/help.html";
            File htmlFile = new File(url);
            Desktop.getDesktop().browse(htmlFile.toURI());

            String outputString = output.getText();
            output.setText(outputString + newline + "Opening info file...");
        } catch (Exception e) {
            e.printStackTrace();

            String outputString = output.getText();
            output.setText(outputString + newline + "Unable to open the help file!");
        }
    }

    // opens up the legend HTML file
    private void setMode() {
        if(userMode) {
            userMode = false;
            importButton.setEnabled(false);
            generateButton.setEnabled(false);
            stepsButton.setEnabled(false);
            filenameLabel.setText("no file imported");

            solveButton1.setEnabled(true);
            solveButton2.setEnabled(true);
            solveButton3.setEnabled(true);
            solveButton4.setEnabled(true);

            modeLabel.setText("Mode: auto");
            output.setText("<html>Output :");
            holderPanel.removeAll();
            holderPanel.revalidate();
            JPanel replacingPanel = new JPanel();
            replacingPanel.setLayout(new BorderLayout());
            replacingPanel.add(startLabel, BorderLayout.CENTER);
            replacingPanel.setBackground(Color.darkGray);
            replacingPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            holderPanel.add(replacingPanel, BorderLayout.CENTER);


            String outputString = output.getText();
            output.setText(outputString + newline + "Switched to auto mode...");
        } else {
            userMode = true;
            importButton.setEnabled(true);
            generateButton.setEnabled(true);
            stepsButton.setEnabled(true);

            solveButton1.setEnabled(false);
            solveButton2.setEnabled(false);
            solveButton3.setEnabled(false);
            solveButton4.setEnabled(false);

            modeLabel.setText("Mode: default");
            output.setText("<html>Output :");
            holderPanel.removeAll();
            JPanel replacingPanel = new JPanel();
            replacingPanel.setLayout(new BorderLayout());
            replacingPanel.add(startLabel, BorderLayout.CENTER);
            replacingPanel.setBackground(Color.darkGray);
            replacingPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            holderPanel.add(replacingPanel, BorderLayout.CENTER);
            holderPanel.revalidate();

            String outputString = output.getText();
            output.setText(outputString + newline + "Switched to default mode...");
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        value = slider.getValue();
        System.out.println(value);
        mapPanel.setSpeed(slider.getValue());
    }


}