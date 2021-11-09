package BankModificator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class MainGuiForm extends JFrame {
    private JButton myButton;
    private JTextField sumTextField;
    private JPanel myPanel;
    private JLabel sumLable;
    private JTextField folderField;
    private JButton buttonClear;
    private JTextField textFieldReplace;

    public MainGuiForm(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(myPanel);
        this.setSize(500, 500);
        this.pack();
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File folder = new File(folderField.getText());
                /**
                Бежим по дереву файлов и формируем список файлов <=50 byte
                 */
                File[] filelist = folder.listFiles();
                ArrayList<File> listFile2 = new ArrayList<>();
                ArrayDeque<String> dirQ = new ArrayDeque<>();
                for (int i = 0; i < filelist.length; i++) {
                    if (filelist[i].isFile() && fileSize50Bite(filelist[i])) {
                        listFile2.add(filelist[i]);
                    } else if (filelist[i].isDirectory()) dirQ.offer((filelist[i].getPath()));
                }

                while (dirQ.size() > 0) {
                    folder = new File(dirQ.poll());
                    filelist = folder.listFiles();
                    for (int i = 0; i < filelist.length; i++) {
                        if (filelist[i].isFile() && fileSize50Bite(filelist[i])) {
                            listFile2.add(filelist[i]);
                        } else if (filelist[i].isDirectory()) dirQ.offer((filelist[i].getPath()));
                    }
                }

                for (File list : listFile2
                ) {
                    System.out.println("Отправили файл на запись: " + list.getPath());
                    List<String> line = null; //прочитали строку из файла и записали в массив
                    try {
                        line = Files.readAllLines(Paths.get(list.getPath()));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    System.out.println(line);

                    /* Очищаем только что прочитаный файл */
                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(list.getPath());
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    pw.close();

                    /* Очищаем только что прочитаный файл */
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(list.getPath());
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                    int a = Integer.parseInt(line.get(0));
                    System.out.println("Получили цифру: " + a);
                    a = a + Integer.parseInt(sumTextField.getText());
                    String buf = Integer.toString(a);
                    byte[] buffer = buf.getBytes();
                    try {
                        fileOutputStream.write(buffer);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    try {
                        fileOutputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(myPanel, "Все");
            }

            public boolean fileSize50Bite(File file) {
                if (file.length() <= 50 && !file.getName().equals("allFilesContent.txt")) return true;
                else return false;
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame myGuIForm = new MainGuiForm("Bank Modificator");
        myGuIForm.setVisible(true);
    }
}
