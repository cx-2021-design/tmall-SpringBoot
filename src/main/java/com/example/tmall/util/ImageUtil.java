package com.example.tmall.util;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class ImageUtil {
    /**
     * 将文件转换为JPEG格式的BufferedImage。
     * @param f 要转换的文件
     * @return 转换后的JPEG格式的BufferedImage
     */
    public static BufferedImage change2jpg(File f) {
        try {
            // 创建一个Image对象
            Image i = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());

            // 创建PixelGrabber对象，用于获取图像的像素数据
            PixelGrabber pg = new PixelGrabber(i, 0, 0, -1, -1, true);
            pg.grabPixels();

            // 获取图像的宽度和高度
            int width = pg.getWidth();
            int height = pg.getHeight();

            // 定义RGB颜色掩码和颜色模型
            final int[] RGB_MASKS = { 0xFF0000, 0xFF00, 0xFF };
            final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

            // 创建数据缓冲区
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());

            // 创建WritableRaster对象
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);

            // 创建BufferedImage对象
            BufferedImage img = new BufferedImage(RGB_OPAQUE, raster, false, null);

            return img;
        } catch (InterruptedException e) {
            // 如果发生中断异常，则打印堆栈跟踪信息并返回null
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调整图像尺寸并将其保存为指定的文件。
     * @param srcFile 源文件
     * @param width 要调整的宽度
     * @param height 要调整的高度
     * @param destFile 目标文件
     */
    public static void resizeImage(File srcFile, int width, int height, File destFile) {
        try {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            // 读取源文件的图像
            Image i = ImageIO.read(srcFile);

            // 调用resizeImage方法对图像进行尺寸调整
            i = resizeImage(i, width, height);

            // 将调整后的图像写入目标文件
            ImageIO.write((RenderedImage) i, "jpg", destFile);
        } catch (IOException e) {
            // 如果发生IO异常，则打印堆栈跟踪信息
            e.printStackTrace();
        }
    }

    /**
     * 调整图像尺寸。
     * @param srcImage 源图像
     * @param width 要调整的宽度
     * @param height 要调整的高度
     * @return 调整尺寸后的图像
     */
    public static Image resizeImage(Image srcImage, int width, int height) {
        try {
            BufferedImage buffImg = null;
            // 创建一个新的BufferedImage对象，指定调整后的宽度、高度和颜色类型为TYPE_INT_RGB
            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取Graphics对象，并使用drawImage方法将源图像缩放到指定的尺寸并绘制到新的BufferedImage对象上
            buffImg.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            return buffImg;
        } catch (Exception e) {
            // 如果发生异常，则打印堆栈跟踪信息
            e.printStackTrace();
        }
        return null;
    }

}
