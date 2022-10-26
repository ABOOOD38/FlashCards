import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuizCardBuilder {

    private final ArrayList<QuizCard> cardList = new ArrayList<>();
    private JTextArea question;
    private JTextArea answer;
    private JFrame frame;

    public static void main(String[] args) {
        new QuizCardBuilder().go();
    }

    public void go() {
        frame = new JFrame("Quiz Card Builder");
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question = createTextArea(bigFont);
        JScrollPane qScroller = createScroller(question);
        answer = createTextArea(bigFont);
        JScrollPane aScroller = createScroller(answer);

        mainPanel.add(new JLabel("Question:"));
        mainPanel.add(qScroller);
        mainPanel.add(new JLabel("Answer:"));
        mainPanel.add(aScroller);

        JButton nextButton = new JButton("Next Card");
        nextButton.addActionListener(e -> nextCard());
        mainPanel.add(nextButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> clearAll());

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveCard());

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(550, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void saveCard() {
        QuizCard card = new QuizCard(question.getText(), answer.getText());
        cardList.add(card);

        JFileChooser fileSave = new JFileChooser();
        fileSave.showSaveDialog(frame);
        saveFile(fileSave.getSelectedFile());
    }

    private void saveFile(File selectedFile) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))){
            for (QuizCard card : cardList) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Couldn't write the cardList out: " + ex.getMessage());
        }
    }

    private void clearAll() {
        cardList.clear();
        clearCard();
    }

    private void nextCard() {
        QuizCard card = new QuizCard(question.getText(), answer.getText());
        cardList.add(card);
        clearCard();
    }

    private void clearCard() {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    private @NotNull JScrollPane createScroller(JTextArea textArea) {
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroller;
    }

    private @NotNull JTextArea createTextArea(Font bigFont) {
        JTextArea textArea = new JTextArea(6, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(bigFont);
        return textArea;
    }


}
