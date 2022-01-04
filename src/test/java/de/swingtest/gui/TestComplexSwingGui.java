package de.swingtest.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.image.ScreenshotTaker;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TestComplexSwingGui extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private ComplexSwingGui frame;

    boolean firstRun = true;
    long timestamp = 0L;

    @Override
    protected void onSetUp() {
        frame = GuiActionRunner.execute(() -> new ComplexSwingGui());

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

        for (int i = 0; i < 100; i++) {
            long startTime = System.currentTimeMillis();

            Rectangle location = frame.getBounds();

            Point clickPosition = new Point();
            clickPosition.x = random.nextInt(location.width) + location.x;
            clickPosition.y = random.nextInt(location.height) + location.y;

            // Take and save screenshot
            takeScreenshot(location, i, clickPosition);

            robot.click(clickPosition, MouseButton.LEFT_BUTTON, 1);

            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println(estimatedTime);

        }
    }

    public void takeScreenshot(Rectangle location, int i, Point click_Position) {

        try {
            ScreenshotTaker screenshotTaker = new ScreenshotTaker();

            BufferedImage image_screen = screenshotTaker.takeDesktopScreenshot();

            // Add click position as a point to the screenshot
            final Graphics2D graphics2D = image_screen.createGraphics();

            graphics2D.setPaint(Color.RED);
            int diameter = 5;
            graphics2D.fillOval(click_Position.x, click_Position.y, diameter, diameter);
            graphics2D.dispose();

            BufferedImage image = image_screen.getSubimage(location.x, location.y, location.width, location.height);

            String screenshot_directory = "Screenshots";

            // Create Screenshots directory if it does not exist yet
            File directory = new File(screenshot_directory);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save Screenshot
            File f = new File(screenshot_directory, "screenshot_" + i + ".png");
            ImageIO.write(image, "PNG", f);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}