import java.io.IOException;

public class Terminal {

    public static void type(String output) {
        for (var letter: output.toCharArray()) {
            System.out.print(letter);
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void typeAndWait(String output) {
        type(output);
        System.out.print(' ');
        try {
            System.in.read();
        } catch (IOException ignored) {}
    }

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }

}
