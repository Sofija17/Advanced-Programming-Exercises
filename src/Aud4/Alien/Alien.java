package Aud4.Alien;

public abstract class Alien {

    private int health; // 0=dead, 100=full strength
    private String name;

    public Alien(int health, String name) {
        this.health = health;
        this.name = name;
    }

    public abstract int getDamage();

    @Override
    public String toString() {
        return "Alien name: " + name + ' ' + "health= " + health;
    }
}
