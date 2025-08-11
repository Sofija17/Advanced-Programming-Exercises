package Aud4.Alien;

public class OgreAlien extends Alien{

    private final static int damage=6;

    public OgreAlien(int health, String name) {
        super(health, name);
    }


    @Override
    public int getDamage() {
        return damage;
    }
}
