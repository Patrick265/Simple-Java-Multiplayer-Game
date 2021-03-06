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
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class DataReceiver implements Runnable
{
    private Socket socket;
    private Map<String, Player> players;
    private ArrayList<Enemy> enemies;
    private JPanel jpanel;
    private ReentrantLock lock;
    public DataReceiver(Socket socket, JPanel jpanel)
    {
        this.enemies = new ArrayList<>();
        this.players = new HashMap<>();
        this.socket = socket;
        this.jpanel = jpanel;
        this.lock = new ReentrantLock();
    }

    @Override
    public void run() {
        try {

            System.out.println("Entered in DataReceiver" + "\n" + "-------------------------------");
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {

                try {
                    ServerPKG pkg = (ServerPKG) objectInputStream.readObject();
                    lock.lock();
                    this.enemies.clear();
                    this.players = pkg.getPlayers();
                    this.enemies.addAll(pkg.getEnemies());
                } finally {
                    lock.unlock();
                }
            }
        } catch(IllegalMonitorStateException e2) {
            JOptionPane.showMessageDialog(null, "Server has closed!");
            System.exit(1);
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Server has closed!");
            System.exit(1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }


    public ArrayList<Enemy> getEnemies()
    {
        ArrayList<Enemy> listofenemies = new ArrayList<>();
        lock.lock();
        try{
            listofenemies.addAll(enemies);
        }finally
        {
            lock.unlock();
        }
        return listofenemies;
    }

    public Map<String, Player> getPlayers()
    {
        return players;
    }

    public ReentrantLock getLock()
    {
        return lock;
    }

    //    public Semaphore getMutex()
//    {
//        return mutex;
//    }
}
