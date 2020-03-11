import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;


public class TextEditor extends JFrame implements ActionListener {
    //menu bar
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");

    //list of menu items
    String[] strMenuItems = {"New", "Open", "Save", "Save As", "Exit"};

    //list of menu item buttons
    JMenuItem[] jMenuItems = new JMenuItem[5];

    //file chooser
    JFileChooser fileChooser = new JFileChooser();

    //container
    Container container = getContentPane();

    //text area
    JTextArea textArea = new JTextArea();

    //file path
    String filepath = null;

    public TextEditor() {
        //frame
        setIconImage(new ImageIcon("src/editor.png").getImage());
        setSize(700, 600);
        setTitle("Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add menu bar
        setJMenuBar(menuBar);
        menuBar.add(menu);

        //add text area
        container.add(textArea);

        //add menu items with listeners
        populateAndAddComponents();
    }

    public void populateAndAddComponents() {
        for (int i = 0; i < jMenuItems.length; i++) {
            jMenuItems[i] = new JMenuItem(strMenuItems[i]);
            jMenuItems[i].addActionListener(this);

            menu.add(jMenuItems[i]);

            if (i == 1 || i == 3)
                menu.add(new JSeparator());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == jMenuItems[4]) System.exit(0);
        else if (source == jMenuItems[0]) newFile();
        else if (source == jMenuItems[1]) openFile();
        else if (source == jMenuItems[2]) saveFile();
        else if (source == jMenuItems[3]) saveFileAs();
    }

    public void newFile() {
        if (!(textArea.getText().isEmpty())) {
            int y = JOptionPane.showConfirmDialog(this, "Save the document?",
                    "", JOptionPane.YES_NO_OPTION);

            if (y == JOptionPane.YES_OPTION) saveFile();
        }

        textArea.setText("");
        filepath = null;
    }

    public void openFile() {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        newFile();

        try {
            open(fileChooser.getSelectedFile().getPath());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void saveFile() {
        if (filepath == null) {
            saveFileAs();
            return;
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write(textArea.getText());
            out.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    public void saveFileAs() {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        filepath = fileChooser.getSelectedFile().getPath();
        saveFile();
    }

    public void open(String file) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String separator = System.getProperty("line.separator");

        while (in.ready()) {
            textArea.append(in.readLine() + separator);
        }

        in.close();
        filepath = file;
    }

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.setVisible(true);
    }
}
