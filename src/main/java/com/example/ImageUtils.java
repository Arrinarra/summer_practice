
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


//преобразование (конвертация) между OpenCV (Mat) и BufferedImage (Java)
public class ImageUtils {
    public static BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte mob = new MatOfByte(); // контейнер для байтов
        Imgcodecs.imencode(".jpg", mat, mob); // кодируем Mat в байтовый поток JPG
        byte[] byteArray = mob.toArray(); // получаем сырые байты изображения
        return bytesToBufferedImage(byteArray); // конвертируем байты в BufferedImage
    }
    
    public static BufferedImage bytesToBufferedImage(byte[] imageData) {
        try {
            return ImageIO.read(new ByteArrayInputStream(imageData)); // декодируем поток в BufferedImage (создаем поток из байтов)
        } catch (IOException e) {
            throw new RuntimeException("Image conversion error", e); // ошибка если байты повреждены или в нераспозноваемом виде
        }
    }
    
    public static Mat bufferedImageToMat(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); // создаем поток в памяти 
            ImageIO.write(image, "jpg", baos); // кодируем BufferedImage в jpg файлы
            byte[] bytes = baos.toByteArray(); // получаем сырые байты
            return Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_COLOR); // декодируем байты в Mat
        } catch (IOException e) {
            throw new RuntimeException("Mat conversion error", e);
        }
    }
}