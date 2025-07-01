
import nu.pattern.OpenCV;

public class App {
    public static void main(String[] args) {
        OpenCV.loadShared();
        new MainFrame().setVisible(true);
    }
}