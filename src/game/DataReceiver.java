package game;

import game.NPC.Enemy;
import game.character.Player;
import server.logic.ServerPKG;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class DataReceiver implements Runnable
{
    private Socket socket;
    private Map<String, Player> players;
    private ArrayList<Enemy> enemies;
    private JPanel jpanel;
    private Semaphore mutex = new Semaphore(1);

    public DataReceiver(Socket socket, JPanel jpanel)
    {
        this.enemies = new ArrayList<>();
        this.players = new HashMap<>();
        this.socket = socket;
        this.players = new HashMap<>();
        this.jpanel = jpanel;
    }

    @Override
    public void run() {
        try {

            System.out.println("Entered in DataReciever" + "\n" + "-------------------------------");
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (true)
            {
                ServerPKG pkg = (ServerPKG) objectInputStream.readObject();
                this.mutex.tryAcquire(100,TimeUnit.MILLISECONDS);
                this.enemies.clear();
                this.players = pkg.getPlayers();
                this.enemies.addAll(pkg.getEnemies());
                this.mutex.release();
                System.out.println(this.enemies.size());

                    //jpanel.repaint();
            }
        } catch (SocketException e) {
            System.out.println("User disconnected");
        } catch (IOException | ClassNotFoundException  e) {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            this.mutex.release();
        }

    }


    public ArrayList<Enemy> getEnemies()
    {
        return enemies;
    }

    public Map<String, Player> getPlayers()
    {
        return players;
    }

    public Semaphore getMutex()
    {
        return mutex;
    }
}
