
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Scalar;

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
setJMenuBar(menuBar);

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
sharpenItem.addActionListener(e -> sharpenImage());
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

// метод для захвата изображения с вебкамеры
private void captureFromwebcam() {
    try {
        WebcamPanel webcamPanel = new WebcamPanel();
        int result = JOptionPane.showConfirmDialog(this, webcamPanel, "Capture Image",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            BufferedImage captured = webcamPanel.getCaptureImage();
            if (captured != null) {
                currentImage = captured;
                currentMat = ImageUtils.bufferedImageToMat(captured);
                displayImage(captured);
            }
        }
        webcamPanel.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
        "Webcam error:\n1. Check camera connection\n2. Grant camera permissions\n3." +
        "Close other apps using camera", "Camera error", JOptionPane.ERROR_MESSAGE);
    }
}

private void displayImage(BufferedImage img) {
    imageLabel.setIcon(new ImageIcon(img));
}

private void showChannel(int channelIndex) {
    if (currentMat == null) return;

    List<Mat> channels = new ArrayList<>();
    org.opencv.core.Core.split(currentMat, channels);

    MAt result = new Mat();
    Mat empty = Mat.zeros(channels.get(0).size(), channels.get(0).type());

    switch (channelIndex) {
        case 0: // Blue
            org.opencv.core.Core.merge(Arrays.asList(channels.get(0), empty, empty), result);
            break;
        case 1: // Green
            org.opencv.core.Core.merge(Arrays.asList(empty, empty, channels.get(2)), result);
            break;
        }
        
        displayImage(ImageUtils.matToBufferedImage(result));
        
    }

    private void rotateImage() {
        String input = JOptionPane.showInputDialog(this, "Enter rotation angle (degrees): ");
        if (input == null) return;

        try {
            double angle = Double.parseDouble(input);
            Point center = new Point(currentMat.cols()/2, currentMat.rows()/2);
            Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0);

            Mat rotated = new Mat();
            Imgproc.warpAffine(currentMat, rotated, rotationMatrix, currentMat.size());
            currentMat = rotated;
            displayImage(ImageUtils.matToBufferedImage(rotated));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid angle value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawLine() {
        if (currentMat == null) return;

        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField x1Field = new JTextField(5);
        JTextField y1Field = new JTextField(5);
        JTextField x2Field = new JTextField(5);
        JTextField y2Field = new JTextField(5);
        JTextField thicknessField = new JTextField(5);

        panel.add(new JLabel("Start X: "));
        panel.add(x1Field);
        panel.add(new JLabel("Start Y:"));
        panel.add(y1Field);
        panel.add(new JLabel("End X:"));
        panel.add(x2Field);
        panel.add(new JLabel("End Y:"));
        panel.add(y2Field);
        panel.add(new JLabel("Thickness:"));
        panel.add(thicknessField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Line parameters", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int x1 = Integer.parseInt(x1Field.getText());
                int y1 = Integer.parseInt(y1Field.getText());
                int x2 = Integer.parseInt(x2Field.getText());
                int y2 = Integer.parseInt(y2Field.getText());
                int thickness = Integer.parseInt(thicknessField.getText());
                
                Mat imgWithLine = currentMat.clone();
                Imgproc.line(imgWithLine, 
                    new Point(x1, y1), 
                    new Point(x2, y2), 
                    new Scalar(0, 255, 0), // Green color
                    thickness);
                
                currentMat = imgWithLine;
                displayImage(ImageUtils.matToBufferedImage(imgWithLine));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input values", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
}
}
