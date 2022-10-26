import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuizCardPlayer {
    private ArrayList<QuizCard> cardList;
    private int currentCardIndex;
    private QuizCard currentCard;
    private JTextArea display;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String[] args) {
        new QuizCardPlayer().go();
    }

    public void go() {
        frame = new JFrame("Quiz Card Player");
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane scroller = new JScrollPane(display);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scroller);

        nextButton = new JButton("Show Question");
        nextButton.addActionListener(e -> nextCard());
        mainPanel.add(nextButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(e -> open());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(550, 400);
        frame.setVisible(true);
    }

    private void open() {
        JFileChooser fileOpen = new JFileChooser();
        fileOpen.showOpenDialog(frame);
        loadFile(fileOpen.getSelectedFile());
    }

    private void loadFile(File File) {
        cardList = new ArrayList<>();
        currentCardIndex = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(File))) {
            String line;
            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }
        } catch (IOException ex) {
            System.out.println("Couldn't write the cardList out: " + ex.getMessage());
        }
        showNextCard();
    }

    private void makeCard(String line) {
        String[] result = line.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("made a card");
    }

    private void nextCard() {
        if (!isShowAnswer) {
            display.setText(currentCard.getAnswer());
            nextButton.setText("NextCard");
            isShowAnswer = true;
        } else {
            if (currentCardIndex < cardList.size()) {
                showNextCard();
            } else {
                display.setText("That was the last card");
                nextButton.setEnabled(false);
            }
        }
    }

    private void showNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = false;
    }

}
