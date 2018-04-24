package GUI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class App {
    private JTextField URL;
    private JPanel panel1;

    public App() {
        URL.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });
    }
}
