import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

/**
 * @author lin
 */
public class sc2 {
    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        Random random = new Random();
//        robot.delay(1000);
        robot.mouseMove(1570,930);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
//        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        int v1 =(int)((0.2+random.nextDouble()*0.2)*10000);
        robot.delay(v1);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
//        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
}
