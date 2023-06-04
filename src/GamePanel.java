import javax.imageio.ImageIO;
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
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
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
    private static double VELOCIDADE_OPONENTE = 5.0;

    private boolean checkPoint10 = true;
    private boolean checkPoint20 = true;
    private boolean checkPoint50 = true;
    private boolean checkPoint100 = true;
    private BufferedImage backgroundImage;
    private Carro carro;
    private List<Obstaculo> obstaculos;
    private int pontuacao;
    private boolean gameOver;

    public GamePanel() {
        setPreferredSize(new Dimension(LARGURA_PAINEL, ALTURA_PAINEL));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        initGame();
        Timer timer = new Timer(16, this);
        timer.start();
        gameOver = false;
    }

    private void initGame() {
        carro = new Carro(X_INICIAL_CARRO, Y_INICIAL_CARRO);
        obstaculos = new ArrayList<>();
        pontuacao = 0;

        try {
            InputStream backgroundInputStream = getClass().getResourceAsStream("/imgs/background.png");
            backgroundImage = ImageIO.read(backgroundInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void criarObstaculo() {
        if (obstaculos.size() < 5) {
            int laneX = gerarPosicaoAleatoria();
            Obstaculo obstaculo = new Obstaculo(laneX, -ALTURA_CARRO);

            // Verifica se há colisão com outros carros inimigos existentes
            boolean collides = false;
            for (Obstaculo existingObstaculo : obstaculos) {
                if (obstaculo.testaColisao(existingObstaculo)) {
                    collides = true;
                    break;
                }
            }

            // Se houver colisão, escolhe um novo caminho para o carro inimigo
            while (collides) {
                laneX = gerarPosicaoAleatoria();
                obstaculo.setX(laneX);
                collides = false;
                for (Obstaculo existingObstaculo : obstaculos) {
                    if (obstaculo.testaColisao(existingObstaculo)) {
                        collides = true;
                        break;
                    }
                }
            }

            obstaculos.add(obstaculo);
        }
    }

    private int gerarPosicaoAleatoria() {
        int lane = (int) (Math.random() * (LARGURA_ESTRADA / LARGURA_FAIXA_ESTRADA));
        int laneX = X_INICIAL_ESTRADA + lane * LARGURA_FAIXA_ESTRADA;

        if (laneX + LARGURA_CARRO > X_INICIAL_ESTRADA + LARGURA_ESTRADA) {
            laneX = X_INICIAL_ESTRADA + LARGURA_ESTRADA - LARGURA_CARRO;
        }

        return laneX;
    }

    private void update() {
        if (!gameOver) {
            carro.move();

            if (pontuacao == 10 && checkPoint10) {
                VELOCIDADE_OPONENTE += 1;
                checkPoint10 = false;
            }

            if (pontuacao == 20 && checkPoint20) {
                VELOCIDADE_OPONENTE += 1;
                checkPoint20 = false;
            }

            if (pontuacao == 50 && checkPoint50) {
                VELOCIDADE_OPONENTE += 1.5;
                checkPoint50 = false;
            }
            if (pontuacao == 100 && checkPoint100) {
                VELOCIDADE_OPONENTE += 2;
                checkPoint100 = false;
            }

            if (carro.getX() < X_INICIAL_ESTRADA) {
                carro.setX(X_INICIAL_ESTRADA);
            } else if (carro.getX() > X_INICIAL_ESTRADA + LARGURA_ESTRADA - LARGURA_CARRO) {
                carro.setX(X_INICIAL_ESTRADA + LARGURA_ESTRADA - LARGURA_CARRO);
            }

            for (Obstaculo obstaculo : obstaculos) {
                obstaculo.setY(obstaculo.getY() + (int) VELOCIDADE_OPONENTE);

                if (obstaculo.getY() > ALTURA_PAINEL) {
                    obstaculo.setY(-ALTURA_CARRO);
                    obstaculo.setX(gerarPosicaoAleatoria());
                    pontuacao++;


                }

                if (carro.testaColisao(obstaculo)) {
                    // Se o jogador encostar em outro carro, o jogo acaba
                    gameOver = true;
                    break;
                }
            }

            if (Math.random() < 0.01) {
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

        if (gameOver) {
            desenhaMensagemGameOver(g);
        }
    }

    private void desenhaBackground(Graphics g) {
        if (!gameOver) {
            long currentTime = System.currentTimeMillis();
            int roadY = (int) (ALTURA_ESTRADA - ((currentTime / 7) % ALTURA_ESTRADA)); // Invertendo o cálculo de roadY
            int startOffset = -roadY;

            while (startOffset < ALTURA_PAINEL) {
                // Desenha a imagem de fundo repetidamente para criar a animação de movimento para baixo
                g.drawImage(backgroundImage, 0, startOffset, null);
                startOffset += ALTURA_ESTRADA;
            }

            if (roadY > 0) {
                // Desenha a imagem de fundo novamente para preencher a parte inferior da tela
                g.drawImage(backgroundImage, 0, startOffset - ALTURA_ESTRADA, null);
            }
        } else {
            g.drawImage(backgroundImage, 0, Y_INICIAL_ESTRADA, null);
        }
    }


    private void desenhaPontuacao(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fontMetrics = g.getFontMetrics();
        String scoreText = "Score: " + pontuacao;
        int textWidth = fontMetrics.stringWidth(scoreText);
        int textHeight = fontMetrics.getHeight();
        int x = LARGURA_PAINEL - textWidth - 40;
        int y = 20 + textHeight;
        g.drawString(scoreText, x, y);
    }

    private void desenhaMensagemGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fontMetrics = g.getFontMetrics();
        String gameOverText = "Bateu! Pressione espaço para reiniciar. Seu score: " + pontuacao;
        int textWidth = fontMetrics.stringWidth(gameOverText);
        int textHeight = fontMetrics.getHeight();
        int x = (LARGURA_PAINEL - textWidth) / 2;
        int y = (ALTURA_PAINEL - textHeight) / 2;
        g.drawString(gameOverText, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

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
            }
        }
    }
}
