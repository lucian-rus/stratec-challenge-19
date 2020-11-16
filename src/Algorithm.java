/*
 * class that handles the creation of entities
 * can only create one entity/call
 */

public class Algorithm {
    private int[][] array;
    private int start_i = 0;
    private int start_j = 10000;
    private int end_j = -1;
    private int end_i;
    private int height;
    private int max_length;

    private String entity;
    private String entity90;
    private String entity180;
    private String entity270;

    private boolean hasNext = true;
    private boolean noise = false;

    // search for the first occurrence of a 1
    // when found, break the cycle as an entity/noise was found
    public Algorithm(int[][] _array, int _rows, int _cols) {
        array = _array;

        boolean skip = false;
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                if(i == _rows - 1 && j == _cols - 1) {
                    hasNext = false;
                }
                if (array[i][j] == 1) {
                    start_i = i - 1;
                    start_j = j - 1;
                    create(i, j);
                    skip = true;
                    break;
                }
            }
            if (skip) {
                break;
            }
        }

        // check if the found entity is noise
        if (height == 1 || max_length == 1) {
            noise = true;
        }

        // outline only if the entity is non-noise
        if (!noise) {
            outline();
        }

        create_entity_0();
        create_entity_90();
        create_entity_180();
        create_entity_270();

    }

    // "creates" the entity by searching for all the 1s in the near vicinity of the first occurrence
    private int create(int i, int j) {
        int len = 0;
        int first_occ = -1;
        boolean check = false;

        if(array[i][j-1] == 1 ) {
            while(array[i][j-1] == 1) {
                j--;
            }
            if(start_j >= j) {
                start_j = j - 1;
            }
        }

        while(array[i][j] == 1) {
            array[i][j] = 3;

            if(array[i+1][j] == 1 && !check) {
                first_occ = j;
                check = true;
            }
            if(end_j <= j) {
                end_j = j + 1;
            }
            j++;
            len++;
        }

        if(len > max_length) {
            max_length = len;
        }
        if(first_occ != -1) {
            height++;
            return create(i + 1, first_occ);
        }

        height++;
        return 0;
    }

    // creates the borders around the entity
    private void outline() {
        end_i = start_i + height + 1;

        for(int i = start_i; i <= end_i; i++) {
            for(int j = start_j; j <= end_j; j++) {
                if(i == start_i && j == start_j) {
                    array[i][j] = 4; // set the coordinates of the entity
                    continue;
                }
                if (array[i][j] == 1) {
                    array[i][j] = 3; // bypass the algorithms untreated condition of searching upwards
                }
                if(i == start_i || i == end_i) {
                    array[i][j] = 2;
                    continue;
                }
                if(j == start_j || j == end_j) {
                    array[i][j] = 2;
                }
            }
        }
    }

    // creates the entity string at rotation 0
    private void create_entity_0() {
        for(int i = start_i + 1; i <= end_i - 1; i++) {
            for(int j = start_j + 1; j <= end_j - 1; j++) {
                if(array[i][j] == 0) {
                    entity += "z";
                    continue;
                }
                if(array[i][j] == 3) {
                    entity += ("X");
                }
            }
        }
    }

    // creates the entity string at rotation 90
    private void create_entity_90() {
        for(int j = start_j + 1; j <= end_j - 1; j++) {
            for(int i = end_i - 1; i >= start_i + 1; i--) {

                if(array[i][j] == 0) {
                    entity90 += "z";
                    continue;
                }
                if(array[i][j] == 3) {
                    entity90 += ("X");
                }
            }
        }
    }

    // creates the entity string at rotation 180
    private void create_entity_180() {
        for(int i = end_i - 1; i >= start_i + 1; i--) {
            for(int j = end_j - 1; j >= start_j + 1; j--) {
                if(array[i][j] == 0) {
                    entity180 += "z";
                    continue;
                }
                if(array[i][j] == 3) {
                    entity180 += ("X");
                }
            }
        }
    }

    // creates the entity string at rotation 270
    private void create_entity_270() {
        for(int j = end_j - 1; j >= start_j + 1; j--) {
            for(int i = start_i + 1; i <= end_i - 1; i++) {
                if(array[i][j] == 0) {
                    entity270 += "z";
                    continue;
                }
                if(array[i][j] == 3) {
                    entity270 += ("X");
                }
            }
        }
    }

    public int[][] getArray() {
        return array;
    }

    public int getHeight() {
        return height + 2;
    }

    public int getMax_length() {
        return max_length + 2;
    }

    public int getStart_i() {
        return start_i;
    }

    public int getStart_j() {
        return start_j;
    }

    public boolean isNoise() {
        return noise;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public String getEntity() {
        return entity;
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
}