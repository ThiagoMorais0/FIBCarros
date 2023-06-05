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
        int numeroAleatorio = random.nextInt(9);
        switch (numeroAleatorio){
            case 0:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente1.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 1:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente2.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 2:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente3.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 3:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente4.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 4:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente5.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 5:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente6.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 6:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente7.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 7:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente8.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;
            case 8:
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/imgs/oponente9.png");
                    carImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } break;

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
        int xOutro = outro.getX();
        int yOutro = outro.getY();
        int larguraOutro = outro.getLARGURA();
        int alturaOutro = outro.getALTURA();

        return x + LARGURA > xOutro && // Verifica se há colisão pela esquerda
                x < xOutro + larguraOutro && // Verifica se há colisão pela direita
                y < yOutro + alturaOutro && // Verifica se há colisão por cima
                y + ALTURA > yOutro; // Verifica se há colisão por baixo
    }

}
