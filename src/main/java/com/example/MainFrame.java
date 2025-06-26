
import java.awt.MenuBar;
import javax.swing.JFrame;
import javax.swing.JMenu;
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
// Менюшечка(изображение)
JMenuBar menuBar = new JMenuBar();
JMenu fileMenu = new JMenu("File");
JMenuItem openItem = new JMenuItem("Open image");
JMenuItem webcamItem = new JMenuItem("Webcam shoot");
fileMenu.add(openItem);
fileMenu.add(webcamItem);
// Менюшечка (каналы)
JMenu channelJMenu = new JMenu("Channels");
JMenuItem redItem = new JMenuItem("Red");
JMenuItem greenItem = new JMenuItem("Green");
JMenuItem blueItem = new JMenuItem("Blue");
channelJMenu.add(redItem);
channelJMenu.add(greenItem);
channelJMenu.add(blueItem);
}
// Менюшечка (операции)
JMenu operationMenu = new JMenu("Operations");
JMenuItem sharpenItem = new JMenuItem("Sharpen");
JMenuItem rotateItem = new JMenuItem("Rotate");
JMenuItem drawLineItem = new JMenuItem("Draw Line");
operationMenu.add(sharpenItem);
operationMenu.add(rotateItem);
operationMenu.add(drawLineItem);

menuBar.add(filemenu);
menuBar.add(channelMenu);
menuBar.add(operationMenu);
setJmenuBar(menuBar);
}
