import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static int record = 0;
    private static final int LARGURA_PAINEL = 800;
    private static final int ALTURA_PAINEL = 600;
    private static final int LARGURA_CARRO = 50;
    private static final int ALTURA_CARRO = 100;
    private static final int LARGURA_ESTRADA = 400;
    private static final int ALTURA_ESTRADA = 600;
    private static final int X_INICIAL_ESTRADA = (LARGURA_PAINEL - LARGURA_ESTRADA) / 2;
    private static final int Y_INICIAL_ESTRADA = 0;
    private static final int LARGURA_FAIXA_ESTRADA = 5;
    private static final int X_INICIAL_CARRO = (LARGURA_PAINEL - LARGURA_CARRO) / 2;
    private static final int Y_INICIAL_CARRO = ALTURA_PAINEL - ALTURA_CARRO - 10;
    private static double velocidadeOponente = 5.0;
    private boolean checkPoint10 = true;
    private boolean checkPoint20 = true;
    private boolean checkPoint50 = true;
    private boolean checkPoint100 = true;
    private BufferedImage backgroundImage;
    private Carro carro;
    private List<Obstaculo> obstaculos;
    private int pontuacao;
    private boolean gameOver;
    private boolean iniciado;
    private boolean naoBateu;
    private Clip musica;
    private Clip beep;
    private Clip somBatida;

    public GamePanel() {
        setPreferredSize(new Dimension(LARGURA_PAINEL, ALTURA_PAINEL));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        initGame();
        Timer timer = new Timer(16, this);
        timer.start();
        gameOver = false;
        iniciado = true;
        record = 0;

        try {
            URL musicURL = getClass().getResource("/audio/song.wav");
            musica = AudioSystem.getClip();
            musica.open(AudioSystem.getAudioInputStream(musicURL));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            URL beepURL = getClass().getResource("/audio/beep.wav");
            beep = AudioSystem.getClip();
            beep.open(AudioSystem.getAudioInputStream(beepURL));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            URL somURL = getClass().getResource("/audio/crash.wav");
            somBatida = AudioSystem.getClip();
            somBatida.open(AudioSystem.getAudioInputStream(somURL));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        if (musica != null) {
            musica.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private void initGame() {
        carro = new Carro(X_INICIAL_CARRO, Y_INICIAL_CARRO);
        obstaculos = new ArrayList<>();
        pontuacao = 0;
        velocidadeOponente = 5.0;
        naoBateu = true;

        try {
            InputStream backgroundInputStream = getClass().getResourceAsStream("/imgs/background.png");
            backgroundImage = ImageIO.read(backgroundInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void criarObstaculo() {
        if (obstaculos.size() < 7 && !iniciado) {
            int posX = gerarPosicaoAleatoria();
            Obstaculo obstaculo = new Obstaculo(posX, -ALTURA_CARRO);

            //Verifica se o carro vai aparecer em cima de algum que já existe, se sim, a posição x é gerada novamente.
            boolean colide = verificaColisao(obstaculo);
            while (colide) {
                posX = gerarPosicaoAleatoria();
                obstaculo.setX(posX);
                colide = verificaColisao(obstaculo);
            }

            obstaculos.add(obstaculo);
        }
    }

    private boolean verificaColisao(Obstaculo obstaculo) {
        for (Obstaculo existingObstaculo : obstaculos) {
            if (obstaculo.testaColisao(existingObstaculo)) {
                return true;
            }
        }
        return false;
    }


    private int gerarPosicaoAleatoria() {
        int pos = (int) (Math.random() * (LARGURA_ESTRADA / LARGURA_FAIXA_ESTRADA));
        int posX = X_INICIAL_ESTRADA + pos * LARGURA_FAIXA_ESTRADA;

        if (posX + LARGURA_CARRO > X_INICIAL_ESTRADA + LARGURA_ESTRADA) {
            posX = X_INICIAL_ESTRADA + LARGURA_ESTRADA - LARGURA_CARRO;
        }

        return posX;
    }

    public void pararMusica() {
        // Para a reprodução da música
        if (musica != null && musica.isRunning()) {
            musica.stop();
            musica.close();
        }
    }

    private void update() {
        if (!gameOver) {
            carro.move();

            if (pontuacao == 10 && checkPoint10) {
                beep.loop(1);
                velocidadeOponente += 1;
                checkPoint10 = false;
            }

            if (pontuacao == 20 && checkPoint20) {
                beep.loop(1);
                velocidadeOponente += 2;
                checkPoint20 = false;
            }

            if (pontuacao == 50 && checkPoint50) {
                beep.loop(1);
                velocidadeOponente += 2;
                checkPoint50 = false;
            }
            if (pontuacao == 100 && checkPoint100) {
                beep.loop(1);
                velocidadeOponente += 2;
                checkPoint100 = false;
            }

            if (carro.getX() < X_INICIAL_ESTRADA) {
                carro.setX(X_INICIAL_ESTRADA);
            } else if (carro.getX() > X_INICIAL_ESTRADA + LARGURA_ESTRADA - LARGURA_CARRO) {
                carro.setX(X_INICIAL_ESTRADA + LARGURA_ESTRADA - LARGURA_CARRO);
            }

            if(carro.getY() < Y_INICIAL_ESTRADA){
                carro.setY(Y_INICIAL_ESTRADA);
            } else if(carro.getY() > Y_INICIAL_ESTRADA + ALTURA_ESTRADA - (ALTURA_CARRO + 50)){
                carro.setY(Y_INICIAL_ESTRADA + ALTURA_ESTRADA - (ALTURA_CARRO + 50));
            }

            Iterator<Obstaculo> iterator = obstaculos.iterator();
            while (iterator.hasNext()) {
                Obstaculo obstaculo = iterator.next();
                obstaculo.setY(obstaculo.getY() + (int) velocidadeOponente);

                if (obstaculo.getY() > ALTURA_PAINEL) {
                    iterator.remove();
                    pontuacao++;
                }

                if (carro.testaColisao(obstaculo)) {
                    // Se o jogador encostar em outro carro, o jogo acaba
                    gameOver = true;
                    break;
                }
            }

            if(pontuacao > record){
                record = pontuacao;
            }
            if (Math.random() < 0.04) {
                criarObstaculo();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        desenhaBackground(g);
        carro.draw(g);

        for (Obstaculo obstaculo : obstaculos) {
            obstaculo.draw(g);
        }

        desenhaPontuacao(g);

        if(iniciado){
            desenhaInicio(g);
        }

        if (gameOver) {
            bateu(g);
            desenhaMensagemGameOver(g);
        }
    }

    private void desenhaBackground(Graphics g) {
        if (!gameOver) {
            long currentTime = System.currentTimeMillis();
            int YEstrada = (int) (ALTURA_ESTRADA - ((currentTime / 7) % ALTURA_ESTRADA));
            int deslocamentoInicial = -YEstrada;

            while (deslocamentoInicial < ALTURA_PAINEL) {
                // Desenha a imagem de fundo repetidamente para criar a animação de movimento para baixo
                g.drawImage(backgroundImage, 0, deslocamentoInicial, null);
                deslocamentoInicial += ALTURA_ESTRADA;
            }

            if (YEstrada > 0) {
                // Desenha a imagem de fundo novamente para preencher a parte inferior da tela
                g.drawImage(backgroundImage, 0, deslocamentoInicial - ALTURA_ESTRADA, null);
            }
        } else {
            g.drawImage(backgroundImage, 0, Y_INICIAL_ESTRADA, null);
        }
    }


    private void desenhaPontuacao(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fontMetrics = g.getFontMetrics();
        String pontuacaoLabel = "Pontuação: " + pontuacao;
        String recordLabel = "Record: " + GamePanel.record;
        int textWidth = fontMetrics.stringWidth(pontuacaoLabel);
        int textHeight = fontMetrics.getHeight();
        int x = LARGURA_PAINEL - textWidth - 40;
        int y = 20 + textHeight;
        g.drawString(pontuacaoLabel, x, y);
        g.drawString(recordLabel, x, y + 30);
    }

    private void desenhaMensagemGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fontMetrics = g.getFontMetrics();
        String gameOverText = "Bateu! Pressione espaço para reiniciar. Seu score: " + pontuacao;
        String record = "Seu record atual é: " + GamePanel.record;
        int larguraTexto = fontMetrics.stringWidth(gameOverText);
        int alturaTexto = fontMetrics.getHeight();
        int x = (LARGURA_PAINEL - larguraTexto) / 2;
        int y = (ALTURA_PAINEL - alturaTexto) / 2;
        g.drawString(gameOverText, x, y);
        g.drawString(record, x, y + 50);
    }

    private void desenhaInicio(Graphics g) {
        BufferedImage imagem = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/imgs/intro.png");
            imagem = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagem != null) {
            int x = (getWidth() - imagem.getWidth()) / 2;
            int y = (getHeight() - imagem.getHeight()) / 2;
            g.drawImage(imagem, x + 5, y, null);
        }
    }

    private void bateu(Graphics g){
        BufferedImage imagemBatida = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/imgs/bateu.png");
            imagemBatida = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagemBatida != null) {
            int x = carro.getX();
            int y = carro.getY() - 20;
            g.drawImage(imagemBatida, x, y, null);
        }

        if(naoBateu){
            somBatida.loop(1);
            naoBateu = false;
        }


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (iniciado && keyCode == KeyEvent.VK_SPACE) {
            initGame();
            iniciado = false;
        }

        if(keyCode == KeyEvent.VK_C){
            pararMusica();
        }

        if (gameOver && keyCode == KeyEvent.VK_SPACE) {
            // Reinicia o jogo ao pressionar a tecla espaço após o fim do jogo
            initGame();
            gameOver = false;
        }

        if (!gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                carro.setMovendoParaEsquerda(true);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                carro.setMovendoParaDireita(true);
            } else if(keyCode == KeyEvent.VK_UP){
                carro.setMovendoParaFrente(true);
            } else if(keyCode == KeyEvent.VK_DOWN){
                carro.setMovendoParaTras(true);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (!gameOver) {
            if (keyCode == KeyEvent.VK_LEFT) {
                carro.setMovendoParaEsquerda(false);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                carro.setMovendoParaDireita(false);
            }else if(keyCode == KeyEvent.VK_UP){
                carro.setMovendoParaFrente(false);
            } else if(keyCode == KeyEvent.VK_DOWN){
                carro.setMovendoParaTras(false);
            }
        }
    }
}
