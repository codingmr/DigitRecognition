package DigitRecogniser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Drawing area component
 *
 * @author codingmroberts
 *
 */
public class DrawArea extends JComponent {

    private Image image;
    private Graphics2D g2;

    private int currentX, currentY, oldX, oldY;

    /**
     * Sets the space the panel takes up and applies mouse listeners
     */
    public DrawArea() {
        setDoubleBuffered(false);
        setMinimumSize(new Dimension(280, 280));
        setMaximumSize(new Dimension(280, 280));
        setPreferredSize(new Dimension(280, 280));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();

                if (g2 != null) {
                    // draw line if g2 context not null
                    g2.setStroke(new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    g2.drawLine(oldX, oldY, currentX, currentY);
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    /**
     * Removes any drawings from the drawArea space
     */
    public void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    /**
     * A getter to retrieve the drawn image from the drawArea space
     * @return image
     */
    public File getImage(){
        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        File image = new File("tmp.jpg");
        this.paint(img.getGraphics());
        try {
            ImageIO.write(img, "jpg", image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}