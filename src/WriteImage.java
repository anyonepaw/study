import java.awt.image.BufferedImage;
import java.io.IOException;

    /**
     * Created by Angelika on 23.03.17.
     */
    public class WriteImage {

        public int height; //высота изображения
        public int width;  //длина изображения
        public int[] pixels;  //массив цветов точек составляющих изображение
        public BufferedImage bi;


        public WriteImage( int height, int width, int[] pixels) throws IOException {


            this.height = height;
            this.width = width;
            this.pixels = pixels;
            bi =copyToBufferedImage();


        }

        // Формирование BufferedImage из массива pixels
        private BufferedImage copyToBufferedImage()  {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    bi.setRGB(j, i, pixels[i*width +j]);
            return bi;
        }


    }


