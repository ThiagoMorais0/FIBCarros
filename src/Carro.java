import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Carro {
    private static final int LARGURA = 50;
    private static final int ALTURA = 100;
    private static final int VELOCIDADE = 10;
    private BufferedImage carImage;

    private int x;
    private int y;
    private boolean movendoParaEsquerda;
    private boolean movendoParaDireita;

    public Carro(int x, int y) {
        this.x = x;
        this.y = y - 50;
        this.movendoParaEsquerda = false;
        this.movendoParaDireita = false;

        try {
            InputStream inputStream = getClass().getResourceAsStream("/imgs/fibcar.png");
            carImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMovendoParaEsquerda(boolean movendoParaEsquerda) {
        this.movendoParaEsquerda = movendoParaEsquerda;
    }

    public void setMovendoParaDireita(boolean movendoParaDireita) {
        this.movendoParaDireita = movendoParaDireita;
    }

    public void move() {
        if (movendoParaEsquerda) {
            x -= VELOCIDADE;
        }
        if (movendoParaDireita) {
            x += VELOCIDADE;
        }
    }

    public boolean testaColisao(Obstaculo obstaculo) {
        return x + LARGURA > obstaculo.getX() + 10 &&
                x < obstaculo.getX() + obstaculo.getLARGURA() - 10 &&
                y < obstaculo.getY() + obstaculo.getALTURA() - 10 &&
                y + ALTURA > obstaculo.getY() + 10;
    }

    public void draw(Graphics g) {
        g.drawImage(carImage, x, y, null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
