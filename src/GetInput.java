/*
 * class that handles the input importing
 * only reads CSV files that are being converted to 2D arrays
 * maximum array capacity is 500x1000
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class GetInput {
    private int[][] array = new int[500][1000];
    private int row = 0;
    private int col = 0;

    public GetInput(String filePath) {
        Scanner scan;

        try {
            scan = new Scanner(new BufferedReader(new FileReader(filePath)));
            while (scan.hasNextLine()) {
                String inputLine = "";
                inputLine = scan.nextLine();
                String[] inputArray = inputLine.split(",");

                for (int i = 0; i < inputArray.length; i++) {
                    if(inputArray[i].equals("1")) {
                        array[row][i] = 1;
                    }
                    if(col < i) {
                        col = i;
                    }
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[][] getArray() {
        return array;
    }

    public int getRow() {
        return row - 1;
    }

    public int getCol() {
        return col;
    }
}