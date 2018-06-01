package game;

import game.NPC.Enemy;
import game.character.Player;
import game.map.TiledMap;
import presentation.views.LoginView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GameDrawer extends JPanel implements KeyListener, ActionListener
{
    private int counter;
    private Player player;
    private Enemy enemy;
    private ObjectOutputStream toServer;
    private DataReceiver dataReceiver;
    private Socket socket;


    public GameDrawer()
    {
        super.setFocusable(true);
        this.counter = 0;

        this.player = new Player(new Point(200,200), LoginView.getUsername(),true);
        this.enemy = new Enemy("Dummy", 10,1,new Point2D.Double(200,200), true);
        addKeyListener(this);

        try
        {
            connectionToServer(LoginView.getAddress(), 8000);

        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(this,"Cannot connect to server");
            System.exit(1);
        }

        this.dataReceiver = new DataReceiver(this.socket,this);
        new Thread(this.dataReceiver).start();
        Timer timer = new Timer(1000/60,this);
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        synchronized (this)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;


            TiledMap map = new TiledMap("res/map/map.json");
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            map.debugDraw(g2d);
            player.draw(g2d, this.dataReceiver.getPlayers());
            if (this.dataReceiver.getEnemies().size() > 0)
            {
                System.out.println("Drawing enemies");
                enemy.draw(g2d, this.dataReceiver);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                this.player.setLocation((int) this.player.getLocation().getX(), (int) this.player.getLocation().getY() - 4);
                break;
            case KeyEvent.VK_S:
                this.player.setLocation((int) this.player.getLocation().getX(), (int) this.player.getLocation().getY() + 4);
                break;
            case KeyEvent.VK_D:
                this.player.setLocation((int) this.player.getLocation().getX() + 4, (int) this.player.getLocation().getY());
                break;
            case KeyEvent.VK_A:
                this.player.setLocation((int) this.player.getLocation().getX() - 4, (int) this.player.getLocation().getY());
                break;
            case KeyEvent.VK_SPACE:
                this.player.setAttacking(true);
        }
        try {
            writeEntities();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    @Override
    public void keyReleased(KeyEvent e) {
        this.player.setAttacking(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        player.update(this.dataReceiver.getEnemies());
        repaint();
    }

    public Player getPlayer()
    {
        return player;
    }

    private void connectionToServer(String adress, int port) throws IOException
    {
        this.socket = new Socket(adress, port);
        toServer = new ObjectOutputStream(socket.getOutputStream());
        toServer.reset();

        toServer.writeObject(this.player);
        toServer.reset();
    }
    private void writeEntities() throws IOException
    {
        toServer.writeObject(new ClientPKG(this.player));
        toServer.reset();

    }
}