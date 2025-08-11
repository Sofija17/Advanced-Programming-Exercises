package Aud4.Alien;

import java.util.Arrays;

public class AlienPack {

    private Alien[] aliens;

    public AlienPack(int numAliens) {
        aliens = new Alien[numAliens];
    }

    public void addAlien(Alien newAlien, int index) {
        aliens[index] = newAlien;
    }

    public Alien[] getAliens() {
        return aliens;
    }

    public int calculateDamage(){
        int damage=0;

        for (Alien alien : aliens){
            damage += alien.getDamage();
        }
        return damage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Alien alien : aliens){
            sb.append(alien);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SnakeAlien a1 = new SnakeAlien(100, "Snake");
        OgreAlien a2 = new OgreAlien(100, "Ogre");
        MarshmallowMan a3 = new MarshmallowMan(100, "Marshmallow");

        AlienPack pack = new AlienPack(3);
        pack.addAlien(a1,0);
        pack.addAlien(a2,1);
        pack.addAlien(a3,2);

        System.out.println("All aliens: " + pack);

        System.out.println("Total damage done by aliens:" + pack.calculateDamage());

    }
}
