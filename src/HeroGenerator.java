import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HeroGenerator {

    private static final Random random = new Random();

    private final static String[][] enemyTypes = new String[][] {
            {AnsiColors.CYAN, "Wizard"},
            {AnsiColors.GREEN, "Gnome"},
            {AnsiColors.PURPLE, "Minion"},
            {AnsiColors.YELLOW, "Goblin"}
    };

    private static final int MAX_ENEMY_HEALTH = 10;
    private static final int MAX_ENEMY_DAMAGE = 5;
    private static final int MAX_ENEMY_LUCK = 100;

    private static Hero generateRandomHero(int[] position) {
        var enemyType = enemyTypes[random.nextInt(enemyTypes.length)];
        var name = enemyType[1];
        var color = enemyType[0];
        var health = random.nextInt(MAX_ENEMY_HEALTH) + 1;
        var damage = random.nextInt(MAX_ENEMY_DAMAGE) + 1;
        var luck = random.nextInt(MAX_ENEMY_LUCK) + 1;
        return new Hero(name, color, health, damage, luck, position);
    }

    private static Hero generateKnight(int[] position) {
        return new Hero(
                "Knight", AnsiColors.RED_BACKGROUND,
                10, 3, 30,
                position
        );
    }

    public static List<Hero> generateHeroes(boolean[][] dungeon, int amount) {
        var heroes = new ArrayList<Hero>();
        var positions = getEmptyPositions(dungeon);
        for (var i = 0; i < Math.min(positions.size() - 1, amount - 1); i++) {
            var position = chooseAndRemove(positions);
            var enemy = generateRandomHero(position);
            heroes.add(enemy);
        }
        if (positions.size() > 0) {
            var position = chooseAndRemove(positions);
            var knight = generateKnight(position);
            heroes.add(knight);
        }
        return heroes;
    }

    private static List<int[]> getEmptyPositions(boolean[][] dungeon) {
        var height = dungeon.length;
        var width = height > 0 ? dungeon[0].length : 0;
        var positions = new LinkedList<int[]>();
        for (var i = 0; i < height; i++) {
            for (var j = 0; j < width; j++) {
                if (dungeon[i][j]) continue;
                var pos = new int[] {i, j};
                positions.add(pos);
            }
        }
        return positions;
    }

    private static <T> T chooseAndRemove(List<T> list) {
        var index = random.nextInt(list.size());
        return list.remove(index);
    }

}
