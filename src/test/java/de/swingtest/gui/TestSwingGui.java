package de.swingtest.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.swing.JFrame;

import javax.imageio.ImageIO;

import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.image.ScreenshotTaker;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TestSwingGui extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private JFrame frame;
    String screenshot_directory = "Screenshots";

    @Override
    protected void onSetUp() {
        frame = GuiActionRunner.execute(() -> new Calculator());

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }

    @Test
    public void randomClicker() {

        Robot robot = robot();
        Random random = new Random();

        for (int timestep = 0; timestep < 100; timestep++) {

            long startTime = System.currentTimeMillis();

            // Generate random click coordinates
            Rectangle location = frame.getBounds();
            Point clickPosition = new Point();
            clickPosition.x = random.nextInt(location.width) + location.x;
            clickPosition.y = random.nextInt(location.height) + location.y;

            // Take and save screenshot
            String filename = "screenshot_" + timestep + ".png";
            takeScreenshot(location, screenshot_directory, filename, clickPosition);

            // Perform click
            robot.click(clickPosition, MouseButton.LEFT_BUTTON, 1);

            // Print elapsed time
            System.out.println(System.currentTimeMillis() - startTime);
        }
    }

    public static void takeScreenshot(Rectangle location, String directoryname, String filename, Point click_Position) {

        try {
            // Take screenshot
            ScreenshotTaker screenshotTaker = new ScreenshotTaker();
            BufferedImage image_screen = screenshotTaker.takeDesktopScreenshot();

            // Add click position as a red point to the screenshot image
            final Graphics2D graphics2D = image_screen.createGraphics();
            graphics2D.setPaint(Color.RED);
            int diameter = 5;
            graphics2D.fillOval(click_Position.x, click_Position.y, diameter, diameter);
            graphics2D.dispose();

            // Crop screenshot image to app size
            BufferedImage image = image_screen.getSubimage(location.x, location.y, location.width, location.height);

            // Create screenshots directory if it does not exist yet
            File directory = new File(directoryname);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save screenshot to file
            File file = new File(directoryname, filename);
            ImageIO.write(image, "PNG", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}