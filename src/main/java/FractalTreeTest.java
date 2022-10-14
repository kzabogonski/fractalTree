import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class FractalTreeTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFractalFrame frame = new JFractalFrame();
                frame.setVisible(true);
            }
        });
    }
}
 class JFractalFrame extends JFrame {
    boolean startAnimation=false;
    JPanel paintPanel;

    public JFractalFrame() {
        setTitle("FractalTree");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        paintPanel = new PaintPanel();
        paintPanel.setBackground(Color.DARK_GRAY);
        add(paintPanel);
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()>0 && !startAnimation){
                    pack();
                    startAnimation=true;
                    startAnimation ();
                }
            }
        });
        paintPanel.repaint();
        pack();
    }
    public void startAnimation(){
        Runnable galaxyRun = new FractalRunnable((PaintPanel) paintPanel);
        Thread t = new Thread(galaxyRun);
        t.start();
    }
}

class PaintPanel extends JPanel{

    List<Branch> listBranch = new CopyOnWriteArrayList<>();
    public double angel = 0;

    public void setAngel(double angel){
        this.angel = angel;
    }

    public void fractal(int startLength, Point2D startPoint, double alpha, int step){
        if(alpha<0) alpha = 360;
        double radian = (alpha/(180/Math.PI));
        Point2D endPoint1 = new Point2D();
        Point2D endPoint2 = new Point2D();

        endPoint1.setX((float) (startPoint.getX()-startLength*Math.cos(radian)));
        endPoint1.setY((float) (startPoint.getY()-startLength*Math.sin(radian)));
        addBranch(new Branch(startPoint, endPoint1, startLength));

        endPoint2.setX((float) (startPoint.getX()-startLength*Math.cos(radian)));
        endPoint2.setY((float) (startPoint.getY()-startLength*Math.sin(radian)));
        addBranch(new Branch(startPoint, endPoint2, startLength));

        if (step > 0){
            step--;
            startLength=50;
            fractal(startLength, endPoint1, alpha-(20+angel), step);
            fractal(startLength, endPoint2, alpha+(20-angel), step);
        }
    }

    public void addBranch(Branch b){
        listBranch.add(b);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        fractal(60, new Point2D(960,920),90, 10);
        Random randomX = new Random();
        Graphics2D g2d = (Graphics2D) g;

        for(Branch b: listBranch){
            if(b.length > 50){
                g2d.setColor(Color.ORANGE.darker());
            } else {
                g2d.setColor(Color.GREEN);
            }
            g2d.draw(b.getShape());
        }
        listBranch.clear();
    }
    public Dimension getPreferredSize(){
        return new Dimension(1920,1080);
    }
}

class Point2D{

    private float x, y;

    public Point2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point2D(){

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

class Branch{
    Point2D begin;
    Point2D end;
    int length;

    public Branch(Point2D begin, Point2D end, int length){
        this.begin = begin;
        this.end = end;
        this.length = length;
    }

    public Line2D getShape(){
        return new Line2D.Double(begin.getX(), begin.getY(), end.getX(), end.getY());
    }
}

class FractalRunnable implements Runnable{

    PaintPanel paintPanel;

    public FractalRunnable(PaintPanel paintPanel){
        this.paintPanel = paintPanel;
    }

    @Override
    public void run() {
        double count=0;
        double a = 50;
        boolean leftDir = true;

        while (true){
            if(count>8 && a < count){
                leftDir = false;
            }
            if(count < -8 && count>-9){
                leftDir=true;
            }
            if(leftDir)
                count+=0.01;
            else
                count-=0.01;

            paintPanel.setAngel(a);
            paintPanel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


