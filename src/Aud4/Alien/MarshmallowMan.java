package Aud4.Alien;

public class MarshmallowMan extends Alien{

    private static final int damage = 1;

    public MarshmallowMan(int health, String name) {
        super(health, name);
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
