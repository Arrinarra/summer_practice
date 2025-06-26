
import javax.swing.JFrame;

/*
 * нужно создать окно, в котором будет наше изображение. Для этого нужно наш класс
 * MainFrame расширить с помощью JFrame(если кратко, класс JFrame позволяет создавать окна).
 */


public class MainFrame extends JFrame {
static {
    String nativePath = System.getProperty("user.dir") + "/native";
    System.load(nativePath + "/opencv_java455.dll"); // подключение OpenCV
}
}
