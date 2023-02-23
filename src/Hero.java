import java.util.Random;

public class Hero {

    private final String name;
    private String color;

    private int health;
    private int damage;
    private int luck;
    private final int level;
    private final static int MAX_LUCK = 100;

    private int[] position;

    private final Random random = new Random();

    public Hero(
            String name, String color,
            int health, int damage, int luck,
            int[] position
    ) {
        this.name = name;
        this.color = color;
        this.health = health;
        this.damage = damage;
        this.luck = luck;
        level = health + damage + luck / 10;
        this.position = position;
    }

    public void makeColorBackground() {
        color = AnsiColors.getBackgroundOf(color);
    }

    public void makeColorForeground() {
        color = AnsiColors.getForegroundOf(color);
    }

    private boolean isLucky() {
        var roll = random.nextInt(MAX_LUCK + 1);
        if (roll >= MAX_LUCK - luck) {
            Terminal.typewritePage(getColoredName() + " is lucky!");
            return true;
        }
        return false;
    }

    public String getColoredName() {
        return color + name + AnsiColors.RESET;
    }
    public String getRawName() {
        return name;
    }

    public String getShortLabel() {
        var first = name.charAt(0);
        var upper = Character.toUpperCase(first);
        return  color + upper + AnsiColors.RESET;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void takeDamage(int damage) {
        if (isLucky()) {
            damage--;
        }
        health -= damage;
        Terminal.typewritePage(getColoredName() + " took " + AnsiColors.RED + damage + AnsiColors.RESET + " damage!");
        if (isDead()) {
            Terminal.typewritePage(getColoredName() + " is now " + AnsiColors.GREEN + "DEAD" + AnsiColors.RESET + "!");
        }
    }

    public int getDamage() {
        var actualDamage = damage + (isLucky() ? 1 : 0);
        Terminal.typewritePage(getColoredName() + " produces " + AnsiColors.RED + actualDamage + AnsiColors.RESET + " damage.");
        return actualDamage;
    }

    public void upgradeDamage(int delta) {
        damage += delta;
        Terminal.typewritePage(getColoredName() + " upgraded damage up to " + AnsiColors.RED + damage + AnsiColors.RESET + "!");
    }

    public void upgradeLuck(int delta) {
        luck += delta;
        luck = Math.min(luck, 100);
        Terminal.typewritePage(getColoredName() + " upgraded luck up to " + AnsiColors.CYAN + luck + AnsiColors.RESET + "!");
    }

    public void heal(int delta) {
        health += delta;
        Terminal.typewritePage(getColoredName() + " healed up to " + AnsiColors.GREEN + health + AnsiColors.RESET + "!");
    }

    public void move(int[] delta) {
        position[0] += delta[0];
        position[1] += delta[1];
    }

    public int getLevel() {
        return level;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    @Override public String toString() {
        return getColoredName() + ", damage: " + AnsiColors.CYAN_BACKGROUND + damage + AnsiColors.RESET +
                ", health: " + AnsiColors.CYAN_BACKGROUND + health + AnsiColors.RESET +
                ", luck: " + AnsiColors.CYAN_BACKGROUND + luck + AnsiColors.RESET +
                ", level: " + AnsiColors.PURPLE_BACKGROUND + level + AnsiColors.RESET;
    }

}
