package main.java.com.khomenko;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Privat on 06.09.2016.
 */
public class ImageComparator {

    private static final int THRESHOLD = 5;

    public static void main(String[] args) throws IOException {
        BufferedImage differenceImage = getDifferenceImage(
                ImageIO.read(new File("image1.png")),
                ImageIO.read(new File("image2.png")));

        ImageIO.write(differenceImage, "png", new File("output.png"));
    }

    private static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int width2 = img2.getWidth();
        int height1 = img1.getHeight();
        int height2 = img2.getHeight();
        if ((width1 != width2) || (height1 != height2)) {
            System.err.println("Error: Images sizes mismatch");
            System.exit(1);
        }

        int[][] matrix = new int[width1][height1];

        BufferedImage outImg = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height1; y++) {
            for (int x = 0; x < width1; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                if (colorDiff(rgb1, rgb2) > 0.1) {
                    matrix[x][y] = 1;
                } else {
                    matrix[x][y] = 0;
                }

                outImg.setRGB(x, y, rgb2);
            }
        }

        Graphics2D graphics = outImg.createGraphics();
        graphics.setColor(Color.RED);

        GroupUtil.groupRegions(matrix, THRESHOLD);
        drawRectangle(matrix, graphics, 2);

        return outImg;
    }


    private static double colorDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = (rgb1) & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = (rgb2) & 0xff;

        double result = Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2))
                / Math.sqrt(Math.pow(255, 2) * 3);
        return result;
    }

    private static void drawRectangle(int[][] matrix, Graphics2D graphics, int counter) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;


        boolean find = false;
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x] == counter) {
                    find = true;

                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;

                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (find) {
            graphics.drawRect(minY, minX, maxY - minY, maxX - minX);
            drawRectangle(matrix, graphics, ++counter);
        }
    }
}
