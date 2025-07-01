import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.opencv.videoio.VideoCapture;


public class WebcamPanel extends JPanel {
    private VideoCapture camera;
    private BufferedImage currentFrame;
    private ScheduledExecutorService timer;
    private boolean capturing = false;

    public WebcamPanel() {
        setLayout(new BorderLayout());
        camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            throw new RuntimeException("Camera not available");
        }

        JButton captureButton = new JButton("Capture");
        captureButton.addActionListener(e -> capturing = true);

        add(captureButton, BorderLayout.SOUTH);
        startCamera();
    }

    private void startCamera() {
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> {
            if (!capturing) {
                Mat frame = new Mat();
                if (camera.read(frame)) {
                    currentFrame = matToBufferedImage(frame);
                    repaint();
                }
            }
        }, 0, 33, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentFrame != null) {
            g.drawImage(currentFrame, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public BufferedImage getCapturedImage() {
        return currentFrame;
    }

    public void close() {
        timer.shutdown();
        camera.release();
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, mob);
        byte[] byteArray = mob.toArray();
        return ImageUtils.bytesToBufferedImage(byteArray);
    }
}
