
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/*
 * нужно создать окно, в котором будет наше изображение. Для этого нужно наш класс
 * MainFrame расширить с помощью JFrame(если кратко, класс JFrame позволяет создавать окна).
 */


public class MainFrame extends JFrame {
static {
    String nativePath = System.getProperty("user.dir") + "/native";
    System.load(nativePath + "/opencv_java455.dll"); // подключение OpenCV
}
private void initUI(){
// Менюшечка
JMenuBar menuBar = new JMenuBar();
JMenu fileMenu = new JMenu("File");
JMenuItem openItem = new JMenuItem("Open image");
JMenuItem webcamItem = new JMenuItem("Webcam shoot");
fileMenu.add(openItem);
fileMenu.add(webcamItem);
}
}
