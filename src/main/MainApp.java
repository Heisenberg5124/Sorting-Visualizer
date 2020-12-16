package main;

import screens.MainMenuScreen;
import screens.Screen;

import javax.swing.*;
import java.util.ArrayList;

public class MainApp {

    private final JFrame window;

    public static final int WIN_WIDTH = 1280;
    public static final int WIN_HEIGHT = 720;

    private final ArrayList<Screen> screens;

    public MainApp() {
        screens = new ArrayList<>();
        window = new JFrame("Sort Visualiser");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }

    public void popScreen() {
        if (!screens.isEmpty()) {
            Screen previousScreen = getCurrentScreen();
            screens.remove(previousScreen);
            window.remove(previousScreen);
            if (!screens.isEmpty()) {
                Screen currentScreen = getCurrentScreen();
                window.setContentPane(currentScreen);
                window.validate();
                currentScreen.onOpen();
            } else
                window.dispose();
        }
    }

    public void pushScreen(Screen screen) {
        if (!screens.isEmpty())
            window.remove(getCurrentScreen());
        screens.add(screen);
        window.setContentPane(screen);
        window.validate();
        screen.onOpen();
    }

    private Screen getCurrentScreen() {
        return screens.get(screens.size() - 1);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(() -> new MainApp().start());
    }

    private void start() {
        pushScreen(new MainMenuScreen(this));
        window.pack();
    }
}
