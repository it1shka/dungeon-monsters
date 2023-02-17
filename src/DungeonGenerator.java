import java.util.Random;

public class DungeonGenerator {

    public static boolean[][] createDungeon(int rows, int columns, int smoothness, int birthThreshold, int survivalThreshold) {
        var dungeon = getRandomDungeon(rows, columns);
        return smoothDungeon(dungeon, smoothness, birthThreshold, survivalThreshold);
    }

    public static void printDungeon(boolean[][] dungeon) {
        var height = dungeon.length;
        var width = height > 0 ? dungeon[0].length : 0;

        for (var i = 0; i < height; i++) {
            for (var j = 0; j < width; j++) {
                var cell = dungeon[i][j];
                System.out.print(cell ? ' ' : '*');
            }
            System.out.println();
        }
    }

    private static boolean[][] getRandomDungeon(int rows, int columns) {
        var random = new Random();
        var dungeon = new boolean[rows][columns];
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                dungeon[i][j] = random.nextBoolean();
            }
        }
        return dungeon;
    }

    private static boolean[][] smoothDungeon(boolean[][] dungeon, int levels, int birthThreshold, int survivalThreshold) {
        var output = dungeon;
        var height = dungeon.length;
        var width = height > 0 ? dungeon[0].length : 0;

        for (var level = 0; level < levels; level++) {
            var temp = new boolean[height][width];
            for (var i = 0; i < height; i++) {
                for (var j = 0; j < width; j++) {
                    var living = output[i][j];
                    var neighbors = countNeighbors(output, i, j);
                    temp[i][j] = (living && neighbors >= survivalThreshold) || (!living && neighbors >= birthThreshold);
                }
            }
            output = temp;
        }

        return output;
    }

    private static int countNeighbors(boolean[][] dungeon, int row, int col) {
        var neighbors = 0;
        var height = dungeon.length;
        var width = height > 0 ? dungeon[0].length : 0;

        for (var i = Math.max(0, row - 1); i < Math.min(row + 2, height); i++) {
            for (var j = Math.max(0, col - 1); j < Math.min(col + 2, width); j++) {
                if (i == row && j == col) continue;
                if (dungeon[i][j]) neighbors++;
            }
        }
        return neighbors;
    }

}
