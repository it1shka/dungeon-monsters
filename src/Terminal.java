import java.io.IOException;
import java.util.Scanner;

public class Terminal {

    private static Terminal instance;
    private final Scanner scanner;

    private Terminal() {
        scanner = new Scanner(System.in);
    }

    public static Terminal get() {
        if (instance == null) {
            instance = new Terminal();
        }
        return instance;
    }

    public int getInteger(String prompt) {
        System.out.print(prompt);
        while (true) {
            var maybeInt = scanner.nextLine();
            try {
                return Integer.parseInt(maybeInt);
            } catch (NumberFormatException e) {
                System.out.print("Not an integer. Try again: ");
            }
        }
    }

    public int getInteger(String prompt, int min) {
        return Math.max(getInteger(prompt), min);
    }

    public char getMove() {
        System.out.print("Enter your move (WASD): ");
        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.print("Please, provide your move (WASD): ");
                continue;
            }
            var maybeInput = Character.toUpperCase(input.charAt(0));
            if ("WASD".indexOf(maybeInput) == -1) {
                System.out.print("Unrecognized move '" + maybeInput + "'. Please, choose from (WASD): ");
                continue;
            }

            return maybeInput;
        }
    }

    public boolean promptForReplay() {
        while (true) {
            System.out.print("Play again? [y/n]: ");
            var input = scanner.nextLine().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
        }
    }

    public int getEnhanceInput() {
        System.out.print("Select level up: ");
        while (true) {
            var input = scanner.nextLine();
            if (input.length() == 1 && "123".contains(input)) {
                return Integer.parseInt(input);
            }
            System.out.print("Please, try again. Select level up: ");
        }
    }

    private static void readAndClear() {
        try {
            System.in.read();
            clear();
        } catch (IOException ignored) {}
    }

    public static void typewritePage(String output) {
        Terminal.clear();
        var waitingForInput = new Thread(Terminal::readAndClear);
        waitingForInput.start();

        try {
            for (var i = 0; i < output.length(); i++) {
                if (!waitingForInput.isAlive()) {
                    System.out.println(output);
                    readAndClear();
                    return;
                }
                var current = output.charAt(i);
                System.out.print(current);
                Thread.sleep(50);
            }
            System.out.println();
            waitingForInput.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ignored) {}
    }

}
