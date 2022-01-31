package minesweeper;


import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

public class Main {
    static char[][] field;
    static char[][] surface;
    static int count = 0;
    static int found = 0;
    static int flag = 0;
    static int unopened;
    static Scanner scanner = new Scanner(System.in);
    static int[][] list;

    public static void main(String[] args) {

        initField(11,11);
        initSurface(11,11);
        play();

    }

    static void initField(int row, int col) {
        field = new char[row][col];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = '/';
            }
        }
        unopened = (row - 2) * (col - 2);
    }

    static void initSurface(int row, int col) {
        surface = new char[row][col];
        for (int i = 0; i < surface.length; i++) {
            for (int j = 0; j < surface[0].length; j++) {
                surface[i][j] = '.';
            }
        }
    }

    static void play() {
        int x;
        int y;
        String option;
        int m = 0;

        while (m < 1 || m > 80) {
            System.out.print("How many mines do you want on the field? ");
            m = scanner.nextInt();
            if (m < 1 || m > 80) {
                System.out.println("No. of mines should be between 1 and 80 Please retry./n");
            }
        }

        printSurface();
        System.out.print("Set/unset mines marks or claim a cell as free: ");
        y = scanner.nextInt();
        x = scanner.nextInt();
        option = scanner.next();
        initMines(m, x, y);
        calculateMines();
        processAction(x, y, option);

        while (true) {
            if (found == count && flag == count ) {
                win();
            }
            if (unopened == count) {
                win();
            }
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            y = scanner.nextInt();
            x = scanner.nextInt();
            option = scanner.next();
            processAction(x,y, option);
        }
    }

    static void processAction(int x, int y, String option) {
        if ("mine".equals(option)) {
            flagAction(x, y);
            printSurface();
            return;
        }
        if ("free".equals(option)) {
            freeAction(x, y);
            printSurface();
            return;
        }
        System.out.println("The option should either be free or mine. Please retry.");
        printSurface();
    }


    static void initMines(int m, int xExclude, int yExclude) {

        list = new int[m][2];
        while (count != m) {
            int x = ThreadLocalRandom.current().nextInt(0,field.length - 2 ) + 1;
            int y = ThreadLocalRandom.current().nextInt(0,field[0].length - 2) + 1;
            if (x == xExclude && y == yExclude) {
                continue;
            }
            list[count][0] = x;
            list[count][1] = y;
            if (field[x][y] == '/') {
                field[x][y] = 'X';
                count ++;
            }
        }
    }

    static void printField() {
        System.out.print(" |");
        for (int i = 1; i < field.length - 1; i++) {
            System.out.print(i);
        }
        System.out.print("|\n");
        System.out.print("-|");
        for (int i = 1; i < field.length - 1; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");
        for (int i = 1; i < field.length - 1; i++) {
            System.out.printf("%d|",i);
            for (int j = 1; j < field[0].length - 1; j++) {
                System.out.printf("%c", field[i][j]);
            }
            System.out.print("|\n");
        }
        System.out.print("-|");
        for (int i = 1; i < field.length - 1; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");

    }

    static void printSurface() {
        System.out.print(" |");
        for (int i = 1; i < surface.length - 1; i++) {
            System.out.print(i);
        }
        System.out.print("|\n");
        System.out.print("-|");
        for (int i = 1; i < surface.length - 1; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");
        for (int i = 1; i < surface.length - 1; i++) {
            System.out.printf("%d|",i);
            for (int j = 1; j < surface[0].length - 1; j++) {
                System.out.printf("%c", surface[i][j]);
            }
            System.out.print("|\n");
        }
        System.out.print("-|");
        for (int i = 1; i < surface.length - 1; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");

    }

    static void calculateMines() {
        for (int[] ints : list) {
            addMine(ints[0], ints[1]);
        }
    }

    static void addMine(int x, int y) {

        field[x - 1][y - 1] = addOne(field[x - 1][y - 1]);
        field[x - 1][y    ] = addOne(field[x - 1][y    ]);
        field[x - 1][y + 1] = addOne(field[x - 1][y + 1]);
        field[x    ][y - 1] = addOne(field[x    ][y - 1]);
        field[x    ][y + 1] = addOne(field[x    ][y + 1]);
        field[x + 1][y - 1] = addOne(field[x + 1][y - 1]);
        field[x + 1][y    ] = addOne(field[x + 1][y    ]);
        field[x + 1][y + 1] = addOne(field[x + 1][y + 1]);

    }

    static char addOne(char cell) {
        if (cell == 'X') return cell;
        if (cell == '/') return '1';
        return (char) (cell + 1);

    }



    public static void flagAction(int x, int y) {
        if (surface[x][y] == '.') {
            surface[x][y] = '*';
            flag++;
            unopened--;
            if (field[x][y] == 'X') {
                found++;
            }
        }else if (surface[x][y] == '*'){
            surface[x][y] = '.';
            flag--;
            unopened++;
            if (field[x][y] == 'X') {
                found--;
            }
        } else {
            System.out.println("This cell is explored. Please retry.");
        }
    }

    public static void freeAction(int x, int y) {
        if (surface[x][y] == '.') {
            if (field[x][y] == 'X') {
                lose();
            }
            if (field[x][y] == '/') {
                exploreCell(x,y);
            } else { // field cell is num
                surface[x][y] = field[x][y];
                unopened--;
            }

        }
    }


    static void win() {
        System.out.println("Congratulations! You found all mines!");
        System.exit(0);
    }

    static void lose() {
        printField();
        System.out.println("You stepped on a mine and failed!");
        System.exit(0);
    }

    static void exploreCell(int x, int y) {
        if (surface[x][y] == '.' || surface[x][y] == '*') {
            if (field[x][y] == '/') {
                surface[x][y] = field[x][y];
                unopened--;
                if (x > 1) {
                    exploreCell(x - 1, y);
                }
                if (x < field.length - 2) {
                    exploreCell(x + 1, y);
                }
                if (y > 1) {
                    exploreCell(x, y - 1);
                }
                if (y < field[0].length - 2) {
                    exploreCell(x, y + 1);
                }
                if (x > 1 && y > 1) {
                    exploreCell(x - 1, y - 1);
                }
                if (x > 1 && y < field[0].length - 2) {
                    exploreCell(x - 1, y + 1);
                }
                if (x < field.length - 2 && y > 1) {
                    exploreCell(x + 1, y - 1);
                }
                if (x < f && y < field[0].length - 2) {
                    exploreCell(x + 1, y + 1);
                }

            }
            if (Character.isDigit(field[x][y])) {
                surface[x][y] = field[x][y];
                unopened--;
            }
        }

    }

}


