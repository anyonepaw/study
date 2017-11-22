import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.bmp.BMPImageReaderSpi;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Angelika on 22.03.17.
 */
public class ReadImage {

    public int height; //высота изображения
    public int width;  //длина изображения
    public int[] pixels;  //массив цветов точек составляющих изображение
    public BufferedImage img;
    public String metka;


    //конструктор - чтение изображения из любого переданного классу файла
    public ReadImage(String fileName,String metka) throws IOException {
        String filename = fileName;
        img = readFromFile(filename);
        this.height = img.getHeight();
        this.width = img.getWidth();

        if(metka == "image") this.pixels = copyFromBufferedImage1(img);
        if(metka == "imagecvz") this.pixels = copyFromBufferedImage2(img);
    }

    //метод для чтения изображения формата .bmp в поток BufferedImage
    private BufferedImage readFromFile(String fileName) throws IOException {

        ImageReader r  = new BMPImageReader(new BMPImageReaderSpi());
        r.setInput(new FileImageInputStream(new File(fileName)));
        BufferedImage  bi = r.read(0, new ImageReadParam());
        ((FileImageInputStream) r.getInput()).close();

        return bi;
    }



    //метод для создания массива пикселей
    private int[] copyFromBufferedImage1(BufferedImage bi) {


        int[]pict   = new int[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pict[i * width + j] = (bi.getRGB(j, i)& 0xFFFFFF); //0xFFFFFF
            }
        }


        return pict;

    }


    //метод для создания массива пикселей
    private int[] copyFromBufferedImage2(BufferedImage bi) {


        int[]pict   = new int[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if((bi.getRGB(j, i)& 0xFFFFFF) == 16777215) pict[i * width + j] = 0;

                if((bi.getRGB(j, i)& 0xFFFFFF) == 0) pict[i * width + j] = 1;

            }
        }


        return pict;

    }
}




