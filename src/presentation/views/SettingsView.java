package presentation.views;

import presentation.GameFrame;
import presentation.template.Colors;
import presentation.template.Fonts;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick de Jong
 */
public class SettingsView extends JPanel
{
    private JComboBox<Dimension> resolutionPullDown;
    private List<Dimension> resolutionsformats;
    private JLabel resolutionLabel;
    private JButton returnView;

    public SettingsView()
    {
        super(new GridBagLayout());
        initialise();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridy++;
        super.add(this.resolutionLabel, gbc);
        gbc.gridx++;
        super.add(this.resolutionPullDown, gbc);
        gbc.gridy++;
        super.add(this.returnView, gbc);

    }

    public void initialise()
    {
        this.resolutionLabel = new JLabel("Screen Resolution");
        this.resolutionLabel.setFont(Fonts.settings());
        buildResolutions();
        String[] resolutions = new String[this.resolutionsformats.size()];
        for(int i = 0; i < this.resolutionsformats.size(); i++)
        {
            resolutions[i] = "Width: " + this.resolutionsformats.get(i).getWidth() +
                            " Height: " + this.resolutionsformats.get(i).getHeight();
        }


        this.resolutionPullDown = new JComboBox(resolutions);
        this.resolutionPullDown.setFont(Fonts.settings());

        this.resolutionPullDown.addActionListener(e ->
        {
            int width = Integer.parseInt(this.resolutionPullDown.getSelectedItem().toString().substring(7,11));
            int height;
            if(this.resolutionPullDown.getSelectedItem().toString().substring(22).length() > 5)
            {
                height = Integer.parseInt(this.resolutionPullDown.getSelectedItem().toString().substring(22, 26));
            }
            else
            {
                height = Integer.parseInt(this.resolutionPullDown.getSelectedItem().toString().substring(22, 25));
            }
            System.out.println("Width: " + width + " Height: " + height);
                GameFrame.getFrame().setSize(width, height);
        });

        this.returnView = new JButton("Return");
        this.returnView.setFont(Fonts.settings());
        this.returnView.setPreferredSize(new Dimension(335,50));
        this.returnView.setOpaque(true);
        this.returnView.setFocusPainted(false);
        this.returnView.setBackground(Colors.buttonBackground());
        this.returnView.setForeground(Colors.buttonFontColor());
        this.returnView.addActionListener(e ->
        {
            GameFrame.getFrame().getContentPane().removeAll();
            GameFrame.getFrame().setContentPane(new IntroView());
            GameFrame.getFrame().revalidate();
        });
    }

    public void buildResolutions()
    {
        this.resolutionsformats = new ArrayList<>();
        this.resolutionsformats.add(new Dimension(1980,1080));
        this.resolutionsformats.add(new Dimension(1600,900));
        this.resolutionsformats.add(new Dimension(1280,720));
        this.resolutionsformats.add(new Dimension(1024,576));
    }
}
