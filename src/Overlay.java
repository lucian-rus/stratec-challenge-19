/*
 * class that handles the solving of the tasks and the display of the array on the map
 * map is zoomable to some degree
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class Overlay extends JPanel implements MouseWheelListener{

    // set maximum size of the Overlay panel
    private int HEIGHT = 500;
    private int WIDTH = 1000;
    private int size = 10; // initial size of the grid cells

    private int[][] array;
    private int rows;
    private int columns;

    private int speedValue; // variables to store the drawing speed
    private String answer;
    private String newline = "<br>";
    private int objects = 0;

    // global variables for solving the tasks with steps enabled
    private Algorithm stepEnt;
    private ArrayList<Answers> stepAnswers = new ArrayList<Answers>();
    private int stepObjects;
    private boolean ansCheck = false;
    private boolean stepUp = true;

    public Overlay(int[][] array) {
        this.array = array;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        addMouseWheelListener(this);
    }

    // paint the grid
    public void paintComponent(Graphics g) {
        //g.setColor(Color.gray);
        g.clearRect(0, 0, WIDTH, HEIGHT);
        int height = HEIGHT / size + 1;
        int width = WIDTH / size + 1;

        // get the whole map green and fill it with 0
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                g.setColor(style.greenHighlight);
                g.fillRect(j * size, i * size, size, size);
                if (size >= 15) {
                    g.setColor(Color.gray);
                    Font smallNumbers = new Font("TimesRoman", Font.PLAIN, size / 2);
                    int x = j * size + size / 3;
                    int y = i * size + (size * 100) / 145;
                    g.setFont(smallNumbers);

                    g.drawString("0", x, y);
                }
            }
        }

        // overlay the array
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch (array[i][j]) {
                    case 0:
                        g.setColor(style.greenHighlight);
                        g.fillRect(j * size, i * size, size, size);
                        if (size >= 15) {
                            g.setColor(Color.gray);
                            Font smallNumbers = new Font("TimesRoman", Font.PLAIN, size / 2);
                            int x = j * size + size / 3;
                            int y = i * size + (size * 100) / 145;
                            g.setFont(smallNumbers);

                            g.drawString("0", x, y);
                        }
                        continue;
                    case 1:
                    case 3:
                        g.setColor(Color.gray);
                        g.fillRect(j * size, i * size, size, size);
                        if (size >= 15) {
                            g.setColor(style.redHighlight);
                            Font smallNumbers = new Font("TimesRoman", Font.PLAIN, size / 2);
                            int x = j * size + size / 3;
                            int y = i * size + (size * 100) / 145;
                            g.setFont(smallNumbers);

                            g.drawString("1", x, y);
                        }
                        continue;
                    case 2:
                        g.setColor(style.yellowHighlight);
                        g.fillRect(j * size, i * size, size, size);
                        if (size >= 15) {
                            g.setColor(Color.gray);
                            Font smallNumbers = new Font("TimesRoman", Font.PLAIN, size / 2);
                            int x = j * size + size / 3;
                            int y = i * size + (size * 100) / 145;
                            g.setFont(smallNumbers);

                            g.drawString("0", x, y);
                        }
                        continue;
                    case 4:
                        g.setColor(style.blueHighlight);
                        g.fillRect(j * size, i * size, size, size);
                        if (size >= 15) {
                            g.setColor(Color.gray);
                            Font smallNumbers = new Font("TimesRoman", Font.PLAIN, size / 2);
                            int x = j * size + size / 3;
                            int y = i * size + (size * 100) / 145;
                            g.setFont(smallNumbers);

                            g.drawString("0", x, y);
                        }
                }
            }
        }

        // draw the grid
        g.setColor(Color.darkGray);
        for (int i = 0; i < getWIDTH(); i++) {
            g.drawLine(i * size, 0, i * size, HEIGHT);
        }
        for (int i = 0; i < getHEIGHT(); i++) {
            g.drawLine(0, i * size, WIDTH, i * size);
        }
    }

    @Override
    // Scales the map with mouse wheel scroll
    public void mouseWheelMoved(MouseWheelEvent m) {
        int rotation = m.getWheelRotation();
        int scroll = 3;

        // Changes size of grid based on scroll
        if (rotation == -1 && size + scroll < 200) {
            size += scroll;
        } else if (rotation == 1 && size - scroll > 2) {
            size += -scroll;
        }
        repaint();
    }

    // solve the first task
    public void solve_level_1() {
        answer = "";
        objects = 0;
        Algorithm ent;

        // create entities while the last cell of the array has not been reached
        do {
            ent = new Algorithm(array, rows, columns);
            array = ent.getArray();
            if (!ent.isNoise()) {
                objects++;
            }
            repaint();
        } while (ent.hasNext());
        answer +=  (objects - 1) + newline;
    }

    // solve the first task by displaying the steps at given speeds
    public void step_solve_level_1() {
        stepObjects = 0;
        answer = "";

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // check if there are additional steps to be made
                if (stepUp) {
                    stepEnt = new Algorithm(array, rows, columns);
                    array = stepEnt.getArray();
                    if (!stepEnt.isNoise()) {
                        stepObjects++;
                        stepUp = stepEnt.hasNext();
                        repaint();
                    }
                } else {
                    // create the answer
                    answer += (stepObjects - 1) + newline;
                    ansCheck = true;
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.scheduleAtFixedRate(task, speedValue, speedValue);
    }

    // solve the second task
    public void solve_level_2() {
        answer = "";
        String auxiliar = "";
        objects = 0;
        Algorithm ent;
        ArrayList<Answers> answers = new ArrayList<>(); // array list of objects that will hold the necessary properties

        do {
            ent = new Algorithm(array, rows, columns);
            array = ent.getArray();
            if (!ent.isNoise()) {
                objects++;
                repaint();
                if (ent.getMax_length() > 2) {
                    auxiliar = "(" + ent.getStart_j() + ", " + ent.getStart_i() + ")" +
                            " W: " + ent.getMax_length() + " H: " + ent.getHeight();
                }
                answers.add(new Answers(auxiliar, ent.getStart_j()));
            }
        } while (ent.hasNext());

        // sort the array list by its X coordinate
        answers.sort(new Comparator<Answers>() {
            @Override
            public int compare(Answers o1, Answers o2) {
                return o1.getCoord() - o2.getCoord();
            }
        });

        // crate the answer based on the required format
        answer +=  (objects - 1) + newline;
        for(int i = 0; i < answers.size() - 1; i++) {
            answer += answers.get(i).getAnswer() + newline;
        }
    }

    // solve the second task by displaying the steps at given speeds
    public void step_solve_level_2() {
        stepObjects = 0;
        answer = "";
        stepAnswers.clear();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // check if there are additional steps to be made
                if (stepUp) {
                    stepEnt = new Algorithm(array, rows, columns);
                    array = stepEnt.getArray();
                    String auxiliar = "";
                    if (!stepEnt.isNoise()) {
                        stepObjects++;
                        stepUp = stepEnt.hasNext();
                        repaint();
                        if (stepEnt.getMax_length() > 2) {
                            auxiliar = "(" + stepEnt.getStart_j() + ", " + stepEnt.getStart_i() + ")" +
                                    " W: " + stepEnt.getMax_length() + " H: " + stepEnt.getHeight();
                        }
                        stepAnswers.add(new Answers(auxiliar, stepEnt.getStart_j()));
                    }
                } else {
                    // if no additional steps can be made, sort the arraylist by its X coordinate
                    stepAnswers.sort(new Comparator<Answers>() {
                        @Override
                        public int compare(Answers o1, Answers o2) {
                            return o1.getCoord() - o2.getCoord();
                        }
                    });

                    // crate the answer based on the required format
                    answer +=  (stepObjects - 1) + newline;
                    for(int i = 0; i < stepAnswers.size() - 1; i++) {
                        answer += stepAnswers.get(i).getAnswer() + newline;
                    }
                    ansCheck = true;
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.scheduleAtFixedRate(task, speedValue, speedValue);
    }

    // solve the third task
    public void solve_level_3() {
        answer = "";
        String auxiliar = "";
        objects = 0;
        Algorithm ent;
        ArrayList<Answers> answers = new ArrayList<>();

        do {
            ent = new Algorithm(array, rows, columns);
            array = ent.getArray();
            if (!ent.isNoise()) {
                objects++;
                repaint();
                if (ent.getMax_length() > 2) {
                    auxiliar = "(" + ent.getStart_j() + ", " + ent.getStart_i() + ")" +
                            " W: " + ent.getMax_length() + " H: " + ent.getHeight();
                }
                answers.add(new Answers(auxiliar, ent.getStart_j(), ent.getStart_i(), ent.getEntity(), ent.getHeight()));
            }
        } while (ent.hasNext());

        // sort the arraylist by its X coordinate
        answers.sort(new Comparator<Answers>() {
            @Override
            public int compare(Answers o1, Answers o2) {
                return o1.getXCoord() - o2.getXCoord();
            }
        });

        // crate the answer based on the required format
        for(int i = 0; i < answers.size() - 1; i++) {
            boolean check = true;
            for(int j = i + 1; j < answers.size() - 1; j++) {
                if(answers.get(j).getEntity().equals(answers.get(i).getEntity()) && !answers.get(i).isSkip()) {
                    if(answers.get(j).getHeight() != answers.get(i).getHeight()) {
                        continue;
                    }
                    if(check) {
                        String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + ")";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                        check = false;
                    } else {
                        String aux = answers.get(i).getAnswer() + " and at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + ")";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                    }
                }
            }
        }

        answer +=  (objects - 1) + newline;
        for(int i = 0; i < answers.size() - 1; i++) {
            if(!answers.get(i).isSkip()) {
                answer += answers.get(i).getAnswer() + newline;
            }
        }
    }

    // solve the third task by displaying the steps at given speeds
    public void step_solve_level_3() {
        stepObjects = 0;
        answer = "";
        stepAnswers.clear();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (stepUp) {
                    stepEnt = new Algorithm(array, rows, columns);
                    array = stepEnt.getArray();
                    String auxiliar = "";
                    if (!stepEnt.isNoise()) {
                        stepObjects++;
                        stepUp = stepEnt.hasNext();
                        repaint();
                        if (stepEnt.getMax_length() > 2) {
                            auxiliar = "(" + stepEnt.getStart_j() + ", " + stepEnt.getStart_i() + ")" +
                                    " W: " + stepEnt.getMax_length() + " H: " + stepEnt.getHeight();
                        }
                        stepAnswers.add(new Answers(auxiliar, stepEnt.getStart_j(), stepEnt.getStart_i(),
                                stepEnt.getEntity(), stepEnt.getHeight()));
                    }
                } else {
                    // sort the array list by its X coordinate
                    stepAnswers.sort(new Comparator<Answers>() {
                        @Override
                        public int compare(Answers o1, Answers o2) {
                            return o1.getXCoord() - o2.getXCoord();
                        }
                    });
                    // crate the answer based on the required format
                    for(int i = 0; i < stepAnswers.size() - 1; i++) {
                        boolean check = true;
                        for(int j = i + 1; j < stepAnswers.size() - 1; j++) {
                            if(stepAnswers.get(j).getEntity().equals(stepAnswers.get(i).getEntity()) && !stepAnswers.get(i).isSkip()) {
                                if(stepAnswers.get(j).getHeight() != stepAnswers.get(i).getHeight()) {
                                    continue;
                                }
                                if(check) {
                                    String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + ")";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                    check = false;
                                } else {
                                    String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + ")";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                }
                            }
                        }
                    }

                    answer +=  (stepObjects - 1) + newline;
                    for(int i = 0; i < stepAnswers.size() - 1; i++) {
                        if(!stepAnswers.get(i).isSkip()) {
                            answer += stepAnswers.get(i).getAnswer() + newline;
                        }
                    }
                    ansCheck = true;
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.scheduleAtFixedRate(task, speedValue, speedValue);
    }

    // solve the fourth task
    public void solve_level_4() {
        answer = "";
        String auxiliar = "";
        objects = 0;
        Algorithm ent;
        ArrayList<Answers> answers = new ArrayList<>();

        do {
            ent = new Algorithm(array, rows, columns);
            array = ent.getArray();
            if (!ent.isNoise()) {
                objects++;
                repaint();
                if (ent.getMax_length() > 2) {
                    auxiliar = "(" + ent.getStart_j() + ", " + ent.getStart_i() + ")" +
                            " W: " + ent.getMax_length() + " H: " + ent.getHeight();
                }
                answers.add(new Answers(auxiliar, ent.getStart_j(), ent.getStart_i(), ent.getHeight(), ent.getMax_length(),
                        ent.getEntity(), ent.getEntity90(), ent.getEntity180(), ent.getEntity270()));
            }
        } while (ent.hasNext());

        // sort the arraylist by its X coordinate
        answers.sort(new Comparator<Answers>() {
            @Override
            public int compare(Answers o1, Answers o2) {
                return o1.getXCoord() - o2.getXCoord();
            }
        });

        // crate the answer based on the required format
        for(int i = 0; i < answers.size() - 1; i++) {
            boolean check = true;
            for(int j = i + 1; j < answers.size() - 1; j++) {
                if(answers.get(j).getEntity().equals(answers.get(i).getEntity()) && !answers.get(i).isSkip()) {
                    if(answers.get(j).getEntity().equals(answers.get(i).getEntity90())) {
                        if(answers.get(j).getHeight() == answers.get(i).getHeight() &&
                                answers.get(i).getWidth() == answers.get(j).getWidth()) {
                            if(check) {
                                String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                        answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + ")";
                                answers.get(i).setAnswer(aux);
                                answers.get(j).setSkip(true);
                                check = false;
                            } else {
                                String aux = answers.get(i).getAnswer() + " and at (" +
                                        answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + ")";
                                answers.get(i).setAnswer(aux);
                                answers.get(j).setSkip(true);
                            }
                            continue;
                        }
                        if(answers.get(j).getHeight() == answers.get(i).getWidth() ||
                                answers.get(i).getHeight() == answers.get(j).getWidth()) {
                            if(check) {
                                String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                        answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() +
                                        "), rotated by 90 degrees";
                                answers.get(i).setAnswer(aux);
                                answers.get(j).setSkip(true);
                                check = false;
                            } else {
                                String aux = answers.get(i).getAnswer() + " and at (" +
                                        answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() +
                                        "), rotated by 90 degrees";
                                answers.get(i).setAnswer(aux);
                                answers.get(j).setSkip(true);
                            }
                            continue;
                        }
                    }

                    if(check) {
                        String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + ")";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                        check = false;
                    } else {
                        String aux = answers.get(i).getAnswer() + " and at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + ")";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                    }
                }

                if(answers.get(j).getEntity90().equals(answers.get(i).getEntity()) && !answers.get(i).isSkip()) {
                    if(check) {
                        String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + "), rotated by 90 degrees";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                        check = false;
                    } else {
                        String aux = answers.get(i).getAnswer() + " and at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + "), rotated by 90 degrees";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                    }
                }

                if(answers.get(j).getEntity180().equals(answers.get(i).getEntity()) && !answers.get(i).isSkip()) {
                    if(check) {
                        String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + "), rotated by 180 degrees";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                        check = false;
                    } else {
                        String aux = answers.get(i).getAnswer() + " and at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + "), rotated by 180 degrees";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                    }
                }

                if(answers.get(j).getEntity270().equals(answers.get(i).getEntity()) && !answers.get(i).isSkip()) {
                    if(check) {
                        String aux = answers.get(i).getAnswer() + " - this object can also be found at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + "), rotated by 270 degrees";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                        check = false;
                    } else {
                        String aux = answers.get(i).getAnswer() + " and at (" +
                                answers.get(j).getXCoord() + ", " + answers.get(j).getYCoord() + "), rotated by 270 degrees";
                        answers.get(i).setAnswer(aux);
                        answers.get(j).setSkip(true);
                    }
                }
            }
        }

        answer +=  (objects - 1) + newline;
        for(int i = 0; i < answers.size() - 1; i++) {
            if(!answers.get(i).isSkip()) {
                answer += answers.get(i).getAnswer() + newline;
            }
        }
    }

    // solve the fourth task by displaying the steps at given speeds
    public void step_solve_level_4() {
        stepObjects = 0;
        answer = "";
        stepAnswers.clear();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (stepUp) {
                    stepEnt = new Algorithm(array, rows, columns);
                    array = stepEnt.getArray();
                    String auxiliar = "";
                    if (!stepEnt.isNoise()) {
                        stepObjects++;
                        stepUp = stepEnt.hasNext();
                        repaint();
                        if (stepEnt.getMax_length() > 2) {
                            auxiliar = "(" + stepEnt.getStart_j() + ", " + stepEnt.getStart_i() + ")" +
                                    " W: " + stepEnt.getMax_length() + " H: " + stepEnt.getHeight();
                        }
                        stepAnswers.add(new Answers(auxiliar, stepEnt.getStart_j(), stepEnt.getStart_i(),
                                stepEnt.getHeight(), stepEnt.getMax_length(), stepEnt.getEntity(),
                                stepEnt.getEntity90(), stepEnt.getEntity180(), stepEnt.getEntity270()));
                    }
                } else {
                    // sort the arraylist by its X coordinate
                    stepAnswers.sort(new Comparator<Answers>() {
                        @Override
                        public int compare(Answers o1, Answers o2) {
                            return o1.getXCoord() - o2.getXCoord();
                        }
                    });

                    // crate the answer based on the required format
                    for(int i = 0; i < stepAnswers.size() - 1; i++) {
                        boolean check = true;
                        for(int j = i + 1; j < stepAnswers.size() - 1; j++) {
                            if(stepAnswers.get(j).getEntity().equals(stepAnswers.get(i).getEntity()) && !stepAnswers.get(i).isSkip()) {
                                if(stepAnswers.get(j).getEntity().equals(stepAnswers.get(i).getEntity90())) {
                                    if(stepAnswers.get(j).getHeight() == stepAnswers.get(i).getHeight() &&
                                            stepAnswers.get(i).getWidth() == stepAnswers.get(j).getWidth()) {
                                        if(check) {
                                            String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                                    stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + ")";
                                            stepAnswers.get(i).setAnswer(aux);
                                            stepAnswers.get(j).setSkip(true);
                                            check = false;
                                        } else {
                                            String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                                    stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + ")";
                                            stepAnswers.get(i).setAnswer(aux);
                                            stepAnswers.get(j).setSkip(true);
                                        }
                                        continue;
                                    }
                                    if(stepAnswers.get(j).getHeight() == stepAnswers.get(i).getWidth() ||
                                            stepAnswers.get(i).getHeight() == stepAnswers.get(j).getWidth()) {
                                        if(check) {
                                            String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                                    stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() +
                                                    "), rotated by 90 degrees";
                                            stepAnswers.get(i).setAnswer(aux);
                                            stepAnswers.get(j).setSkip(true);
                                            check = false;
                                        } else {
                                            String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                                    stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() +
                                                    "), rotated by 90 degrees";
                                            stepAnswers.get(i).setAnswer(aux);
                                            stepAnswers.get(j).setSkip(true);
                                        }
                                        continue;
                                    }
                                }

                                if(check) {
                                    String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + ")";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                    check = false;
                                } else {
                                    String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + ")";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                }
                            }

                            if(stepAnswers.get(j).getEntity90().equals(stepAnswers.get(i).getEntity()) && !stepAnswers.get(i).isSkip()) {
                                if(check) {
                                    String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + "), rotated by 90 degrees";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                    check = false;
                                } else {
                                    String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + "), rotated by 90 degrees";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                }
                            }

                            if(stepAnswers.get(j).getEntity180().equals(stepAnswers.get(i).getEntity()) && !stepAnswers.get(i).isSkip()) {
                                if(check) {
                                    String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + "), rotated by 180 degrees";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                    check = false;
                                } else {
                                    String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + "), rotated by 180 degrees";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                }
                            }

                            if(stepAnswers.get(j).getEntity270().equals(stepAnswers.get(i).getEntity()) && !stepAnswers.get(i).isSkip()) {
                                if(check) {
                                    String aux = stepAnswers.get(i).getAnswer() + " - this object can also be found at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + "), rotated by 270 degrees";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                    check = false;
                                } else {
                                    String aux = stepAnswers.get(i).getAnswer() + " and at (" +
                                            stepAnswers.get(j).getXCoord() + ", " + stepAnswers.get(j).getYCoord() + "), rotated by 270 degrees";
                                    stepAnswers.get(i).setAnswer(aux);
                                    stepAnswers.get(j).setSkip(true);
                                }
                            }
                        }
                    }

                    answer +=  (stepObjects - 1) + newline;
                    for(int i = 0; i < stepAnswers.size() - 1; i++) {
                        if(!stepAnswers.get(i).isSkip()) {
                            answer += stepAnswers.get(i).getAnswer() + newline;
                        }
                    }
                    ansCheck = true;
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.scheduleAtFixedRate(task, speedValue, speedValue);
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public String getAnswer() {
        return answer;
    }

    public void setSpeed(int speed) {
        switch (speed) {
            case 1:
                speedValue = 1000;
                break;
            case 2:
                speedValue = 750;
                break;
            case 3:
                speedValue = 500;
                break;
            case 4:
                speedValue = 250;
                break;
            case 5:
                speedValue = 100;
                break;

            default:
                speedValue = 500;
                break;
        }
    }

    public boolean ansCheck() {
        return ansCheck;
    }

    // class that is used to store the necessary properties for formulating the answers
    private class Answers{
        // second level variables
        private String answer;
        private int coord;

        // third and fourth level variables
        private String entity;
        private int XCoord;
        private int YCoord;
        private boolean skip = false;

        // fourth level variables
        private int height;
        private int width;
        private String entity90;
        private String entity180;
        private String entity270;

        // constructor for second level
        public Answers(String answer, int coord) {
            this.answer = answer;
            this.coord = coord;
        }

        // constructor for third level
        public Answers(String answer, int XCoord, int YCoord, String entity, int height) {
            this.answer = answer;
            this.XCoord = XCoord;
            this.YCoord = YCoord;
            this.entity = entity;
            this.height = height;
        }

        // constructor for fourth level
        public Answers(String answer, int XCoord, int YCoord, int height, int width,
                       String entity, String entity90, String entity180, String entity270) {
            this.answer = answer;
            this.XCoord = XCoord;
            this.YCoord = YCoord;
            this.height = height;
            this.width = width;
            this.entity = entity;
            this.entity90 = entity90;
            this.entity180 = entity180;
            this.entity270 = entity270;
        }
        // mainly used for second level
        public String getAnswer() {
            return answer;
        }

        public int getCoord() {
            return coord;
        }

        // mainly used for third level
        public void setAnswer(String answer) {
            this.answer = answer;
        }

        private void setSkip(boolean skip) {
            this.skip = skip;
        }

        public String getEntity() {
            return entity;
        }

        public int getXCoord() {
            return XCoord;
        }

        public int getYCoord() {
            return YCoord;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getEntity90() {
            return entity90;
        }

        public String getEntity180() {
            return entity180;
        }

        public String getEntity270() {
            return entity270;
        }

        public boolean isSkip() {
            return skip;
        }
    }
}