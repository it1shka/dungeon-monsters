import java.util.Scanner;

public class Main {

    private static Game getGame() {
        Terminal.clear();
        var term = Terminal.get();
        var rows = term.getInteger("Amount of rows: ");
        var columns = term.getInteger("Amount of columns: ");
        var monsters = term.getInteger("Amount of monsters: ");
        return new Game(rows, columns, monsters);
    }

    public static void main(String[] args) {
        var game = getGame();
        game.start();
        game.printRules();
        game.printDungeon();
    }

}
