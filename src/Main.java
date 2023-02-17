import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        System.out.print(AnsiColors.YELLOW_BACKGROUND + "Rows:" + AnsiColors.RESET + " ");
        var rows = scanner.nextInt();
        System.out.print(AnsiColors.YELLOW_BACKGROUND + "Columns:" + AnsiColors.RESET + " ");
        var columns = scanner.nextInt();
        System.out.print(AnsiColors.YELLOW_BACKGROUND + "Monsters:" + AnsiColors.RESET + " ");
        var heroes = scanner.nextInt() + 1;
        Terminal.clear();

        var game = new Game(rows, columns, heroes);
        game.start();
        game.printRules();
        game.printDungeon();
    }

}
