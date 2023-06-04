import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Obstaculo {
    private int x;
    private int y;
    private final int LARGURA = 50;
    private final int ALTURA = 100;
    private BufferedImage carImage;

    public Obstaculo(int x, int y) {
        this.x = x;
        this.y = y;

        Random random = new Random();
        int numeroAleatorio = random.nextInt(5);
        if (numeroAleatorio == 0) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente1.png");
                carImage = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (numeroAleatorio == 1) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente2.png");
                carImage = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (numeroAleatorio == 2) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente3.png");
                carImage = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (numeroAleatorio == 3) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente4.png");
                carImage = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente5.png");
                carImage = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLARGURA() {
        return LARGURA;
    }

    public int getALTURA() {
        return ALTURA;
    }

    public void draw(Graphics g) {
        g.drawImage(carImage, x, y, null);
    }

    public boolean testaColisao(Obstaculo outro) {
        return x + LARGURA > outro.getX() &&
                x < outro.getX() + outro.getLARGURA() &&
                y < outro.getY() + outro.getALTURA() &&
                y + ALTURA > outro.getY();
    }
}
