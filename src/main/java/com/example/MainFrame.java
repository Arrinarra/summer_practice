
import java.awt.BorderLayout;
import java.awt.MenuBar;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/*
 * нужно создать окно, в котором будет наше изображение. Для этого нужно наш класс
 * MainFrame расширить с помощью JFrame(если кратко, класс JFrame позволяет создавать окна).
 */


public class MainFrame extends JFrame {
    private BufferedImage currentImage;
    private JLabel imageLabel;
    private Mat currentMat;
static {
    String nativePath = System.getProperty("user.dir") + "/native";
    System.load(nativePath + "/opencv_java455.dll"); // подключение OpenCV
}

// Экран(основной)
public MainFrame() {
    setTitle("Image Processor");
    setSize(800,600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initUI();
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

// Панель изображения (экран -> панель(типа матрешки))
imageLabel = new JLabel();
imageLabel.setHorizontalAlignment(JLabel.CENTER); // выравнивание по центру
add(new JScrollPane(imageLabel), BorderLayout.CENTER); // компонент помещается в прокручиваемую панель,
// панель добавляется в центр главного окна
 /*
  * JScrollPane обеспечивает полосы прокрутки, если изображение больше окна
  * BorderLayout.CENTER область в центре окна, которая растягивается при изменении размеров
  */

// обработчики событий 
/*
 * Чтобы сказать программе: "Когда жмается эта кнопка делай вот это"
 */
openItem.addActionListener(e -> openImage());
webcamItem.addActionListener(e -> captureFromwebcam());

// обработка цветовых каналов
redItem.addActionListener(e -> showChannel(2)); // OpenCV BGR порядок
greenItem.addActionListener(e -> showChannel(1));
blueItem.addActionListener(e -> showChannel(0));

// обработка операций варианта 10
sharpenItem.addActionListener(e -> sharpemImage());
rotateItem.addActionListener(e -> rotateImage());
drawLineItem.addActionListener(e -> drawLine());
}

/*
 * метод для открытия изображения с компьютера
 */
private void openImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", 
    "jpg", "png"));

    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        try {
            File file = fileChooser.getSelectedFile();
            BufferedImage img = ImageIO.read(file);
            currentImage = img;
            currentMat = ImageUtils.bufferedImageToMat(img);
            displayImage(img);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
}
