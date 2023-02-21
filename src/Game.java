import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Game {

    private final int heroesAmount;
    private List<Hero> enemies;
    private Hero knight;

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
        var heroes = HeroGenerator.generateHeroes(dungeon, heroesAmount);
        for (var i = 0; i < heroesAmount; i++) {
            if (heroes.get(i).getRawName().equals("Knight")) {
                knight = heroes.remove(i);
            }
        }
        enemies = heroes;
    }

    private void printDungeon() {
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
        for (var hero: enemies) {
            var position = hero.getPosition();
            var row = position[0] + 1;
            var col = position[1] + 1;
            display[row][col] = hero.getShortLabel();
        }

        if (knight != null) {
            var position = knight.getPosition();
            display[position[0] + 1][position[1] + 1] = knight.getShortLabel();
        }

        for (var i = 0; i < height + 2; i++) {
            for (var j = 0; j < width + 2; j++) {
                var cell = display[i][j];
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private void printStats() {
        System.out.println("Your stats: " + knight);
    }

    public void printRules() {
        Terminal.typewritePage(
                "Hello, " + AnsiColors.CYAN_BACKGROUND + "stranger!" + AnsiColors.RESET +
                " Now you are entering a " + AnsiColors.PURPLE_BACKGROUND + "mysterious dungeon" + AnsiColors.RESET +
                " with " + AnsiColors.RED_BACKGROUND + (heroesAmount - 1) + " " + "dangerous monsters" + AnsiColors.RESET + " inside! " +
                "Be careful or they will simply " + AnsiColors.RED_BACKGROUND + "eat" + AnsiColors.RESET + " you!"
        );
        Terminal.typewritePage(
                "This is your character: " + (knight != null ? knight : "dead knight") +
                ". To win the battle, you have to kill all the " + AnsiColors.RED + "monsters" + AnsiColors.RESET + ". " +
                "Now, let's begin the " + AnsiColors.CYAN + "battle" + AnsiColors.RESET + "!"
        );
    }

    private Optional<Hero> getEnemyAt(int[] position) {
        return enemies
                .stream()
                .filter(hero -> Arrays.equals(hero.getPosition(), position))
                .findFirst();
    }

    private boolean isValidPosition(int[] position) {
        var row = position[0];
        var col = position[1];
        return row >= 0 && col >= 0 && row < height && col < width;
    }

    private String getPossibleStepDescriptionTo(int[] position) {
        if (!isValidPosition(position)) {
            return "Unavailable";
        }

        var enemy = getEnemyAt(position);
        if (enemy.isPresent()) {
            return "Fight enemy " + enemy.get();
        }

        if (dungeon[position[0]][position[1]]) {
            return "Destroy wall";
        }

        return "Move";
    }

    private void printAvailableStepsForKnight() {
        var knightPos = knight.getPosition();
        var moves = new int[][] { {-1, 0}, {0, -1}, {1, 0}, {0, 1} };
        var keys = new String[] { "W (up)", "A (left)", "S (down)", "D (right)" };

        System.out.println("Select a move: ");
        for (var i = 0; i < 4; i++) {
            var move = moves[i];
            var key = keys[i];
            var movePos = new int[] { move[0] + knightPos[0], move[1] + knightPos[1] };
            if (!isValidPosition(movePos)) continue;

            System.out.println(AnsiColors.CYAN + (i + 1) + ") " + AnsiColors.RESET +
                    AnsiColors.GREEN_BACKGROUND + key + AnsiColors.RESET + " -> " +
                    AnsiColors.RESET + getPossibleStepDescriptionTo(movePos));
        }
    }

    private int[] getMovePosition() {
        var input = Terminal.get().getMove();
        var offset = switch (input) {
            case 'W' -> new int[] { -1,  0 };
            case 'A' -> new int[] {  0, -1 };
            case 'S' -> new int[] {  1,  0 };
            case 'D' -> new int[] {  0,  1 };
            default -> new int[] {0, 0};
        };
        var pos = knight.getPosition();
        return new int[] {pos[0] + offset[0], pos[1] + offset[1]};
    }

    private void makeMoveBasedOnInput() {
        var movePos = getMovePosition();
        if (!isValidPosition(movePos)) {
            Terminal.typewritePage("Knight bumped into the " + AnsiColors.RED + "wall" + AnsiColors.RESET + "!");
            return;
        }
        var maybeEnemy = getEnemyAt(movePos);
        if (maybeEnemy.isPresent()) {
            var enemy = maybeEnemy.get();
            var damage = knight.getDamage();
            enemy.takeDamage(damage);
            var name = enemy.getColoredName();
            if (enemy.isDead()) {
                enemies.remove(enemy);
            }
            if (enemiesAreDefeated()) {
                Terminal.typewritePage(AnsiColors.YELLOW + "ALL ENEMIES ARE DEFEATED! YOU WON!" + AnsiColors.RESET);
            }
            return;
        }
        var row = movePos[0];
        var col = movePos[1];
        if (dungeon[row][col]) {
            dungeon[row][col] = false;
            Terminal.typewritePage("You destroyed the " + AnsiColors.RED + "wall" + AnsiColors.RESET + "!");
            return;
        }
        knight.setPosition(movePos);
        Terminal.typewritePage("Knight moved to the next position.");
    }

    private void moveEnemies() {
        if (enemiesAreDefeated()) return;
        Terminal.typewritePage("Now, it is " + AnsiColors.RED + "enemies" + AnsiColors.RESET + " turn.");
    }

    public boolean knightIsDead() {
        return knight == null || knight.isDead();
    }

    public boolean enemiesAreDefeated() {
        return enemies.size() == 0;
    }

    public void nextStep() {
        printDungeon();
        if (knightIsDead()) {
            System.out.println("Knight is dead! " + AnsiColors.RED + "Game over!" + AnsiColors.RESET);
            return;
        }
        if (enemiesAreDefeated()) {
            System.out.println("Enemies are defeated! " + AnsiColors.GREEN + "You won!" + AnsiColors.RESET);
            return;
        }
        printStats();
        printAvailableStepsForKnight();
        makeMoveBasedOnInput();
        moveEnemies();
    }

}
