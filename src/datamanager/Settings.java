package datamanager;

import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;

import java.awt.*;

public class Settings
{
    private int clientWidth;
    private int clientHeight;
    private boolean audio;

    public Settings(int clientWidth, int clientHeight)
    {
        this.clientWidth = clientWidth;
        this.clientHeight = clientHeight;
    }

    public Settings()
    {
        this.clientWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        this.clientHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    @Override
    public String toString()
    {
        return "Settings{" +
                "clientWidth=" + clientWidth +
                ", clientHeight=" + clientHeight +
                ", audio=" + audio +
                '}';
    }

    public int getClientWidth()
    {
        return clientWidth;
    }

    public void setClientWidth(int clientWidth)
    {
        this.clientWidth = clientWidth;
    }

    public int getClientHeight()
    {
        return clientHeight;
    }

    public void setClientHeight(int clientHeight)
    {
        this.clientHeight = clientHeight;
    }

    public boolean isAudio()
    {
        return audio;
    }

    public void setAudio(boolean audio)
    {
        this.audio = audio;
    }
}
