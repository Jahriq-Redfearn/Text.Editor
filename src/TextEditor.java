import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JScrollPane scrollPane;
    //adjust current size of font
    JSpinner  fontSizeSpinner;
    JLabel fontLabel;
    JButton fontColorButton;
    //let user pick the font they want
    JComboBox fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;




    // constructor for text editor class
    TextEditor() {
        //create frames
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Jahriq Text Editor");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout());
        this.setVisible(new FlowLayout().getAlignOnBaseline());

        textArea = new JTextArea();
        //get text to move down to next line
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial",Font.PLAIN,20));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontLabel = new JLabel("Font: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();


        // we can add options by passing in an array
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        //-----Menu Bar Area-----//

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        //add action listener for open, save, exit
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        // add all items to menu
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);




        //-----Menu Bar Area-----//



        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == fontColorButton){
            JColorChooser colorChooser = new JColorChooser();

            Color color = JColorChooser.showDialog(null,"Choose a color",Color.black);

            textArea.setForeground(color);
        }

        if(e.getSource() == fontBox){
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        if(e.getSource() == openItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files","txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()){
                        while(fileIn.hasNextLine()) {
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileIn.close();
                }
            }

        }
        //select a file location to save the text to
        //create instance

        if(e.getSource() == saveItem){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        int response = fileChooser.showSaveDialog(null);

        if(response == JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter fileOut = null;

            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileOut = new PrintWriter(file);
                fileOut.println(textArea.getText());
            }
            catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            finally {
                fileOut.close();
            }
        }
        }

        if(e.getSource() == exitItem){
    System.exit(0);
        }

    }
}
