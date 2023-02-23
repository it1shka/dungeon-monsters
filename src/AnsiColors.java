public class AnsiColors {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static String getBackgroundOf(String color) {
        return switch(color) {
            case BLACK -> BLACK_BACKGROUND;
            case RED -> RED_BACKGROUND;
            case GREEN -> GREEN_BACKGROUND;
            case YELLOW -> YELLOW_BACKGROUND;
            case BLUE -> BLUE_BACKGROUND;
            case PURPLE -> PURPLE_BACKGROUND;
            case CYAN -> CYAN_BACKGROUND;
            case WHITE -> WHITE_BACKGROUND;

            default -> BLACK_BACKGROUND;
        };
    }

    public static String getForegroundOf(String color) {
        return switch (color) {
            case BLACK_BACKGROUND -> BLACK;
            case RED_BACKGROUND -> RED;
            case GREEN_BACKGROUND -> GREEN;
            case YELLOW_BACKGROUND -> YELLOW;
            case BLUE_BACKGROUND -> BLUE;
            case PURPLE_BACKGROUND -> PURPLE;
            case CYAN_BACKGROUND -> CYAN;
            case WHITE_BACKGROUND -> WHITE;

            default -> WHITE;
        };
    }
}
