import java.util.List;
import java.util.Optional;

public class Game {

    private final int heroesAmount;
    private List<Hero> heroes;

    private final int height;
    private final int width;
    private boolean[][] dungeon;

    public Game(int rows, int columns, int heroes) {
        height = rows;
        width = columns;
        heroesAmount = heroes;
    }

    public void start() {
        dungeon = DungeonGenerator.createDungeon(height, width, 5, 6, 3);
        heroes = HeroGenerator.generateHeroes(dungeon, heroesAmount);
    }

    public void printDungeon() {
        var display = new String[height + 2][width + 2];

        // drawing border
        for (var i = 0; i < height + 2; i++) {
            display[i][0] = display[i][width + 1] = AnsiColors.YELLOW_BACKGROUND + "@" + AnsiColors.RESET;
        }
        for (var i = 0; i < width + 2; i++) {
            display[0][i] = display[height + 1][i] = AnsiColors.YELLOW_BACKGROUND + "@" + AnsiColors.RESET;
        }

        // placing all the caves
        for (var i = 0; i < height; i++) {
            for (var j = 0; j < width; j++) {
                display[i + 1][j + 1] = dungeon[i][j]
                        ? AnsiColors.GREEN_BACKGROUND + "#" + AnsiColors.RESET
                        : " ";
            }
        }

        // placing heroes on the canvas
        for (var hero: heroes) {
            var position = hero.getPosition();
            var row = position[0] + 1;
            var col = position[1] + 1;
            display[row][col] = hero.getShortLabel();
        }

        for (var i = 0; i < height + 2; i++) {
            for (var j = 0; j < width + 2; j++) {
                var cell = display[i][j];
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    public void printRules() {
        Terminal.typeAndWait("Hello, " + AnsiColors.CYAN_BACKGROUND + "stranger!" + AnsiColors.RESET);
        Terminal.typeAndWait("Now you are entering a " +
                AnsiColors.PURPLE_BACKGROUND + "mysterious dungeon" + AnsiColors.RESET +
                " with " + AnsiColors.RED_BACKGROUND + (heroesAmount - 1) + " " + "dangerous monsters" + AnsiColors.RESET + " inside!");
        Terminal.typeAndWait("Be careful or they will simply " + AnsiColors.RED_BACKGROUND + "eat" + AnsiColors.RESET + " you!");
        Terminal.clear();
        Terminal.typeAndWait("Now, you will see your character: ");
        Terminal.typeAndWait(getKnight().get().toString());
        Terminal.typeAndWait("So, lets begin the battle! " + AnsiColors.RED_BACKGROUND + "Kill them all!" + AnsiColors.RESET);
        Terminal.clear();
    }

    private Optional<Hero> getKnight() {
        return heroes
                .stream()
                .filter(hero -> hero.getRawName().equals("Knight"))
                .findFirst();
    }

}
