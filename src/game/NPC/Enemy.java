package game.NPC;

import game.DataReceiver;
import game.skills.Skills;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Enemy implements Serializable
{
    private final String name;
    private Point2D location;
    private transient BufferedImage npcSkin;
    private boolean isAlive;
    private Skills skills;

    public Enemy(String name, int hitpoints, int strength, Point2D location, boolean isAlive)
    {
        this.name = name;
        skills = new Skills();
        skills.getHitpoints().setLevel(hitpoints);
        skills.getStrength().setLevel(strength);
        this.location = location;
        this.isAlive = isAlive;
        try {
            npcSkin = ImageIO.read(new FileInputStream("res/enemies/skeleton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update()
    {

    }


    public void draw(Graphics2D g2d, DataReceiver dataReceiver)
    {
        ArrayList<Enemy> enemyArrayList = new ArrayList<>();
        try
        {
            dataReceiver.getMutex().tryAcquire(100, TimeUnit.MILLISECONDS);
            enemyArrayList = dataReceiver.getEnemies();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            dataReceiver.getMutex().release();
        }

        for(int i = 0; i < enemyArrayList.size(); i++)
        {
            AffineTransform af = new AffineTransform();
            af.translate(
                    (int) enemyArrayList.get(i).getLocation().getX() + 16 - this.getNpcSkin().getWidth() / 2,
                    (int) enemyArrayList.get(i).getLocation().getY() + 12 - this.getNpcSkin().getHeight() / 2);
            g2d.drawImage(this.getNpcSkin(), af, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString( enemyArrayList.get(i).getName(),
                          (int) enemyArrayList.get(i).getLocation().getX(),
                        (int) enemyArrayList.get(i).getLocation().getY() - 20);
        }
    }

    public String getName()
    {
        return name;
    }
    public Point2D getLocation()
    {
        return location;
    }
    public void setLocation(Point2D location)
    {
        this.location = location;
    }
    public BufferedImage getNpcSkin()
    {
        return npcSkin;
    }
    public void setNpcSkin(BufferedImage npcSkin)
    {
        this.npcSkin = npcSkin;
    }
    public boolean isAlive()
    {
        return isAlive;
    }
    public void setAlive(boolean alive)
    {
        isAlive = alive;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", npcSkin=" + npcSkin +
                ", isAlive=" + isAlive +
                ", skills=" + skills.toString() +
                '}';
    }
}