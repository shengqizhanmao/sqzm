import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

/**
 * @author lin
 */
public class sc3 {
    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        Random random = new Random();
        robot.delay(1000);
        Color pixelColor = robot.getPixelColor(950, 500);
        System.out.println(pixelColor.getRed());
        System.out.println(pixelColor.getBlue());
        System.out.println(pixelColor.getGreen());
    }
}
