package server.logic;

import game.NPC.Enemy;
import game.character.Player;
import server.presentation.ServerFrame;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server implements Runnable, Serializable
{
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private Map<String, Player> players = new HashMap<>();
    private ArrayList<Enemy> monsters = new ArrayList<>();

    public Server()
    {
        createMonster();
    }

    @Override
    public void run()
    {
        ServerFrame frame = new ServerFrame("TreacherousMUD - BootServer");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        try
        {
            frame.getTextArea().append("BootServer started on " + formatter.format(new Date()));
            ServerSocket serverSocket = new ServerSocket(8000);
            frame.getTextArea().append("\nThe current server IP: " + Inet4Address.getLocalHost().getHostAddress()+ " with port " + serverSocket.getLocalPort());
            int clientNR = 1;
            while (true)
            {

                Socket socket = serverSocket.accept();
                frame.getTextArea().append(frame.standardClientText(socket.getInetAddress()));
                ClientHandler clientHandler = new ClientHandler(socket, clientNR, frame.getTextArea(), this);

                new Thread(clientHandler).start();
                clientNR++;

                frame.getTextArea().append("Amount of players online: " + (players.size() + 1));

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void createMonster()
    {
        for(int i = 1; i < 10; i++)
        {
            this.monsters.add(new Enemy("Skeleton", 15, 0.5, new Point2D.Double(Math.random() * 2000, Math.random() * 2000), true));
        }
    }

    public List<Enemy> getMonsters()
    {
        return monsters;
    }

    public ArrayList<ClientHandler> getClients()
    {
        return clients;
    }

    public Map<String, Player> getPlayers()
    {
        return players;
    }
}
