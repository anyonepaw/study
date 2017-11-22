/**
 * Created by Angelika on 10.03.17.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Vector;

public class Solution extends JFrame {

    static int Metka = 10101; //создание метки для определения конца массива


    //объявление глобальных переменных
    static BufferedImage img;
    static BufferedImage img2;
    static int k0;
    static int R;
    static int[] K;
    static int[] contBlueArray;
    static int[] CopyOfcontBlueArray;
    static int[] newMass;
    static int X;
    static int Y;
    static int XY;

    static String fileName;
    static String fileName1;


    static ReadImage container;
    static  ReadImage file2;

    JFrame frame;
    static  JButton button;

    JTextField keyField;
    JTextField RoundField;

    JLabel imageCVZ;

    JPanel panel;
    JPanel panel3;

    JPanel panel0;

    JLabel imageLabel;
    JLabel imageLabel1;

    JLabel imageCont;

    JTextField f;
    JTextField f1;
    JTextField f2;
    JTextField f3;

    int a;
    int b;
    int c;
    int d;

    public static void main(String[] args) {

       Solution mainclass = new Solution();
        mainclass.go();
    }
    class Encryption implements ActionListener{  //внутренний класс для кнопки "Зашифровка"
        @Override
        public void actionPerformed(ActionEvent event){


            //читаем тест из текстовых полей
            k0 = new Integer(keyField.getText()); //начальное значение для массива
            R = new Integer(RoundField.getText());


            //формирование ключа
            K = new int[2 * R]; //создание массива для ключа
            FormKey(k0, K);


            //создаем массив, в который затем запишем пиксели Blue-канала картинки
            contBlueArray = new int[container.img.getWidth() * container.img.getHeight()];


            //получаем массив пикселей Blue-канала картинки
            convertToBlue(container.img, contBlueArray);


            //для алгоритмов оценки качества и для метода расшифровки получим его копию
            CopyOfcontBlueArray = contBlueArray.clone();

            //для удобства вычислений, запишем ширину и высоту картинки-контейнера
            X = container.width;
            Y = container.height;

            //встраиваем пиксели ЦВЗ в синий контейнер
            Method(X, Y, R, K, contBlueArray, file2.pixels);


            //вставляем синий контейнер обратно в картинку
            addPixelsBlueChannel(X, Y, contBlueArray, container.pixels);


            //создание вектор-массива для записи значений ЦВЗ
            Vector<Integer> cvz = new Vector<>();

            //расшифровываем и записываем пиксели цвз в вектор-массив
            MethodRash(K, contBlueArray, CopyOfcontBlueArray, R, X, Y, cvz);


            //из вектора-массива запишем ЦВЗ в обычный массив
            newMass = new int[cvz.size()];

            VectToMass(cvz, newMass);

            //из массива 1 и 0 превращаем его в массив пикселей ЦВЗ

            for (int z = 0; z < newMass.length; z++) {
                if (newMass[z] == 0) {
                    newMass[z] = 16777215;
                }
                if (newMass[z] == 0) {
                    newMass[z] = 1;
                }
            }


            //вычислим новые координаты массива
            double z = newMass.length;
            XY = (int) Math.sqrt(z);


            //алгоритмы оценки качества
            a = SNR(contBlueArray, CopyOfcontBlueArray);
            b = PSNR(X, Y, contBlueArray, CopyOfcontBlueArray);
            c = MSE(X, Y, contBlueArray, CopyOfcontBlueArray);
            d = NMSE(contBlueArray, CopyOfcontBlueArray);


            try {
                //выводим контейнер
                WriteImage   image = new WriteImage( Y, X, container.pixels);

                imageCont = new JLabel(new ImageIcon(image.bi));

                //записываем массив как изображение
                WriteImage image1 = new WriteImage( XY, XY, newMass);
                //выводим на панель
                imageCVZ = new JLabel(new ImageIcon(image1.bi));


            } catch (IOException e) {
            }
            f.setText("");
            f1.setText("");
            f2.setText("");
            f3.setText("");
            panel.removeAll();
            panel.add(imageCont);
            panel3.removeAll();
        }
    }
    class Decryption implements ActionListener {  //внутренний класс
        @Override
        public void actionPerformed(ActionEvent event)  {


            panel3.add(imageCVZ);
            f.setText(Integer.toString(a));
            f1.setText(Integer.toString(b));
            f2.setText(Integer.toString(c));
            f3.setText(Integer.toString(d));
            panel.removeAll();
            panel.add(imageLabel);


        }
    }
    public void go() {

        //объект формы
        frame = new JFrame();
        //устанавливаем размер
        frame.setSize(1500, 1500);
        frame.setTitle("Метод псевдослучайной перестановки");
        //кнопка закрытия
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //объявление элементов

        //текст/текстовые метки
        JLabel label = new JLabel("Введите ключ, k0");
        label.setForeground(Color.WHITE); //устанавливаем цвет

        JLabel label0 = new JLabel("Введите количество раундов, R");
        label0.setForeground(Color.WHITE);

        JLabel label1 = new JLabel("Среднеквадратическая ошибка, SNR");
        label1.setForeground(Color.WHITE);

        JLabel label2 = new JLabel("Нормированная среднеквадратическая ошибка, PSNR");
        label2.setForeground(Color.WHITE);

        JLabel label3 = new JLabel("Отношение “сигнал/шум”, MSE");
        label3.setForeground(Color.WHITE);

        JLabel label4 = new JLabel("Максимальное отношение “сигнал/шум”, NMSE");
        label4.setForeground(Color.WHITE);

        JLabel label5 = new JLabel("                              ");

        //кнопки
        button = new JButton("Зашифровать  ");


        JButton button1 = new JButton("Расшифровать");

        //панели, которые размещаются на форме
        panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel3 = new JPanel();

        //выбор файла
        JFileChooser fileopen = new JFileChooser();

        //текстовые поля
        keyField = new JTextField(5);
        RoundField = new JTextField(5);
        f = new JTextField(5);
        f1 = new JTextField(5);
        f2 = new JTextField(5);
        f3 = new JTextField(5);


        //делаем форму видимой и добавляем панели
        frame.setVisible(true);
        frame.add(panel);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.WEST, panel2);
        frame.getContentPane().add(BorderLayout.EAST, panel3);


        //устанавливаем цвет панелей и добавляем элементы на панели
        panel.setBackground(Color.darkGray);
        panel2.setBackground(Color.darkGray);
        panel3.setBackground(Color.darkGray);


        panel.add(fileopen);
        panel2.add(label5);
        panel2.add(label5);

        panel2.add(button);
        panel2.add(button1);
        panel2.add(label);
        panel2.add(keyField);
        panel2.add(label0);
        panel2.add(RoundField);
        panel2.add(label1);
        panel2.add(f);
        panel2.add(label2);
        panel2.add(f1);
        panel2.add(label3);
        panel2.add(f2);
        panel2.add(label4);
        panel2.add(f3);

        //чтобы текстовые метки, кнопки и текст поля были на одной линии, делаем их
        //ориентированными по оси Y
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));


        fileopen.setFileFilter(new FileNameExtensionFilter("BMP file", "bmp"));
        //диалоги для выбора файлов
        int choose = fileopen.showDialog(null, "Открыть файл-контейнер");
        if (choose == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            fileName = file.getPath();

            try {
                container = new ReadImage(fileName, "image");

                img = ImageIO.read(file);
            } catch (IOException e) {
            }
            imageLabel = new JLabel(new ImageIcon(img));
            panel.add(imageLabel);

        }
        int choose1 = fileopen.showDialog(null, "Открыть файл-скрываемое изображение");
        if (choose1 == JFileChooser.APPROVE_OPTION) {
            File file1 = fileopen.getSelectedFile();
            fileName1 = file1.getPath();
            try {
                file2 = new ReadImage(fileName1, "imagecvz");

                // fileopen.setFileFilter(new FileNameExtensionFilter("BMP file", "bmp"));
                img2 = ImageIO.read(file1);
            } catch (IOException e) {
            }
            imageLabel1 = new JLabel(new ImageIcon(img2));
            panel3.add(imageLabel1);

        }

        button.addActionListener(new Encryption());


        button1.addActionListener(new Decryption());

    }

    public static int[] FormKey ( int k0, int[] K){


        K[0] = k0;

        for (int i = 1; i < K.length; i++) {

            K[i] = (int) (Math.pow(K[i - 1], 2));

            K[i] = new Integer(Integer.toString(K[i]).substring(0, 3));

            if (K[i] >= 255) {
                K[i] = new Integer(Integer.toString(K[i]).substring(0, 2));
            }

        }

        return K;

    }


    public static int[] Method ( int X, int Y, int R, int[] K, int[] contBlueArray, int[] messagePixels){

        int L = messagePixels.length;


        int x;
        int y;

        HashSet newSet = new HashSet<Integer>();

        for (int c = 0; c < L; c++) {

            x = c / Y + 1;

            y = c % Y + 1;

            for (int s = 1; s < R; s++) {

                x = (((x + (K[2 * s - 1] ^ y)) % X) + 1);
                y = (((y + (K[2 * s] ^ x)) % Y) + 1);

            }


            if ((x * Y + y) >= X * Y) {

                newSet.add(y);

                contBlueArray[y] = (messagePixels[c] + contBlueArray[ y] );
            } else {
                newSet.add(x * Y + y );
                contBlueArray[x * Y + y] = (messagePixels[c] + contBlueArray[x * Y + y]);

            }

        }
        contBlueArray[(messagePixels.length - 1)] = Metka;

        System.out.println(newSet.size());


        return contBlueArray;

    }

    public static Vector MethodRash(int[] K, int[] contBlueArray, int[] copyOfcontBlueArray, int R, int X, int Y, Vector V) {

        int z = 0;
        int x;
        int y;


        for (int i = 0; i < contBlueArray.length; i++) {

            if (contBlueArray[i] == Metka) {
                z = i;
                break;
            }
        }


        for (int c = 0; c < z + 1; c++) {


            x = c / Y + 1; //координаты х и у
            y = c % Y + 1;

            for (int s = 1; s < R; s++) {

                x = (((x + (K[2 * s - 1] ^ y)) % X) + 1);
                y = (((y + (K[2 * s] ^ x)) % Y) + 1);

            }

            if ((x * Y + y) >= X * Y) {


                V.addElement(contBlueArray[y] - copyOfcontBlueArray[y]);


            } else {
                V.addElement(contBlueArray[x * Y + y] - copyOfcontBlueArray[x*Y +y]);

            }
        }

        System.out.println(V);
        return V;

    }

    public static int[] VectToMass(Vector V, int[] newmass) {

        for (int i = 0; i < newmass.length; i++) {
            newmass[i] = (int) V.elementAt(i);
        }
        return newmass;
    }


    private static int[] convertToBlue(BufferedImage bi, int[] pict) {


        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                pict[i * bi.getWidth() + j] = (bi.getRGB(j, i) & 0x000000FF);
            }
        }


        return pict;

    }


    public static int[] addPixelsBlueChannel(int width, int height, int[] contBlueArray, int[] pixels) {


        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int newBlue = contBlueArray[i * width + j];

                pixels[i * width + j] = pixels[i * width + j] & 0xFFFF00 | (newBlue);
            }

        return pixels;
    }


    public static int SNR(int[] S, int[] C) {

        int c = 0;
        int difference = 0;

        for (int i = 0; i < C.length; i++) {

            c = (int) Math.pow(C[i], 2) + c;
        }


        for (int i = 0; i < C.length; i++) {

            difference = (int) Math.pow(C[i] - S[i], 2) + difference;
        }

        return c / difference;
    }

    public static int PSNR(int X, int Y, int[] S, int[] C) {

        int max = 0;
        int difference = 0;
        for (int i = 0; i < C.length; i++) {
            if (max < C[i]) max = C[i];
        }

        for (int i = 0; i < C.length; i++) {

            difference = (int) Math.pow(C[i] - S[i], 2) + difference;
        }

        return X * Y * max / difference;
    }

    public static int MSE(int X, int Y, int[] S, int[] C) {


        int difference = 0;


        for (int i = 0; i < C.length; i++) {

            difference = (int) Math.pow(C[i] - S[i], 2) + difference;
        }

        return (1 / X * Y) * difference;
    }

    public static int NMSE(int[] S, int[] C) {

        int c = 0;
        int difference = 0;

        for (int i = 0; i < C.length; i++) {

            c = (int) Math.pow(C[i], 2) + c;
        }

        for (int i = 0; i < C.length; i++) {

            difference = (int) Math.pow(C[i] - S[i], 2) + difference;
        }

        return difference / c;
    }


}
