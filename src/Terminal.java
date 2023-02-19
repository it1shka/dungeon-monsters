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

    public char getMove() {
        System.out.print("Enter your move (WASD): ");
        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.print("Please, provide your move (WASD): ");
                continue;
            }
            var maybeInput = input.charAt(0);
            if ("WASD".indexOf(maybeInput) == -1) {
                System.out.print("Unrecognized move '" + maybeInput + "'. Please, choose from (WASD): ");
                continue;
            }

            return maybeInput;
        }
    }

    private static void readAndClear() {
        try {
            System.in.read();
            clear();
        } catch (IOException ignored) {}
    }

    public static void typewritePage(String output) {
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
