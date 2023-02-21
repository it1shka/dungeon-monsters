public class Main {

    private static Game getGame() {
        Terminal.clear();
        var term = Terminal.get();
        var rows = term.getInteger("Amount of rows (min: 5): ", 5);
        var columns = term.getInteger("Amount of columns (min: 5): ", 5);
        var heroes = term.getInteger("Amount of heroes (min: 2): ", 2);
        return new Game(rows, columns, heroes);
    }

    public static void main(String[] args) {
        var game = getGame();
        game.start();
        game.printRules();

        var isPlaying = true;
        while (isPlaying) {
            game.nextStep();
            if (game.knightIsDead() || game.enemiesAreDefeated()) {
                var hitReplay = Terminal.get().promptForReplay();
                if (hitReplay) {
                    Terminal.clear();
                    game.start();
                }
                else isPlaying = false;
            }
        }
        System.out.println("Finished gaming session.");
    }

}
