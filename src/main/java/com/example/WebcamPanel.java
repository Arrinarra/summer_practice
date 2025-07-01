package com.example;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
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
    
}
