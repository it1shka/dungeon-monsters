import java.util.*;

public class Game {

    private final int heroesAmount;
    private List<Hero> enemies;
    private Hero knight;

    private final int height;
    private final int width;
    private boolean[][] dungeon;
    private final Random random;

    public Game(int rows, int columns, int heroes) {
        height = rows;
        width = columns;
        heroesAmount = heroes;
        random = new Random();
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
        Terminal.typewritePage("Now, it is " + AnsiColors.RED + "the enemies'" + AnsiColors.RESET + " turn.");
        enemies.forEach(this::moveEnemy);
    }

    private final static int[][] deltas = new int[][] { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };

    private List<int[]> getPathTo(int[] startPoint, int[] endpoint) {
        var unavailable = new boolean[height][width];
        for (var enemy: enemies) {
            var pos = enemy.getPosition();
            unavailable[pos[0]][pos[1]] = true;
        }
        unavailable[startPoint[0]][startPoint[1]] = true;
        var tracker = new int[height][width][];
        var queue = new LinkedList<int[]>();
        queue.add(startPoint);
        while (!queue.isEmpty()) {
            var current = queue.pop();
            if (Arrays.equals(current, endpoint)) {
                break;
            }
            for (var pos: deltas ) {
                var movePos = new int[] { pos[0] + current[0], pos[1] + current[1] };
                if (!isValidPosition(movePos)) continue;
                var row = movePos[0];
                var col = movePos[1];
                if (unavailable[row][col] || dungeon[row][col]) continue;
                unavailable[row][col] = true;
                tracker[row][col] = current;
                queue.add(movePos);
            }
        }

        var path = new LinkedList<int[]>();
        var current = endpoint;
        while (tracker[current[0]][current[1]] != null) {
            path.addFirst(current);
            current = tracker[current[0]][current[1]];
        }

        return path;
    }

    private List<int[]> getPositionsAround(int[] position) {
        var list = new LinkedList<int[]>();
        for (var delta: deltas) {
            var move = new int[] {position[0] + delta[0], position[1] + delta[1]};
            if (!isValidPosition(move)) continue;
            if (getEnemyAt(move).isPresent() || dungeon[move[0]][move[1]]) continue;
            list.add(move);
        }
        return list;
    }

    private static final int searchDistance = 10;
    private void moveEnemy(Hero enemy) {
        var path = getPathTo(enemy.getPosition(), knight.getPosition());
        if (path.size() == 0 || path.size() > searchDistance) {
            var available = getPositionsAround(enemy.getPosition());
            var index = random.nextInt(available.size());
            enemy.setPosition(available.get(index));
            return;
        }
        if (path.size() == 1) {
            var damage = enemy.getDamage();
            knight.takeDamage(damage);
            if (knight.isDead()) {
                Terminal.typewritePage("Knight is dead! " + AnsiColors.RED + "GAME OVER!" + AnsiColors.RESET);
            }
            return;
        }
        enemy.setPosition(path.get(0));
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
