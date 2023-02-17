import java.util.Random;

public class Hero {

    private final String name;
    private final String color;

    private int health;
    private int damage;
    private int luck;
    private final static int MAX_LUCK = 100;

    private final int[] position;

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
        this.position = position;
    }

    private boolean isLucky() {
        var roll = random.nextInt(MAX_LUCK + 1);
        return roll >= MAX_LUCK - luck;
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
    }

    public int getDamage() {
        return damage + (isLucky() ? 1 : 0);
    }

    public void upgradeDamage(int delta) {
        damage += delta;
    }

    public void upgradeLuck(int delta) {
        luck += delta;
    }

    public void heal(int delta) {
        health += delta;
    }

    public void move(int[] delta) {
        position[0] += delta[0];
        position[1] += delta[1];
    }

    public int[] getPosition() {
        return position;
    }

    @Override public String toString() {
        return getColoredName() + ", damage: " + AnsiColors.CYAN_BACKGROUND + damage + AnsiColors.RESET +
                ", health: " + AnsiColors.CYAN_BACKGROUND + health + AnsiColors.RESET +
                ", luck: " + AnsiColors.CYAN_BACKGROUND + luck + AnsiColors.RESET;
    }

}
