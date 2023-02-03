package net.centralfloridaattorney;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class DepoLoader extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JLabel label1;
    private JTextField textbox1;
    private JButton button1;
    private JLabel label2;
    private JTextArea textbox2;
    private String file_name;
    private String pdf_text;
    private String clean_text;

    public DepoLoader() {
        this.setTitle("Depo Loader");
        this.setSize(500, 500);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        label1 = new JLabel("Input File Name:");
        label1.setBounds(10, 10, 100, 20);
        this.add(label1);

        textbox1 = new JTextField();
        textbox1.setBounds(120, 10, 300, 20);
        this.add(textbox1);

        button1 = new JButton("Load");
        button1.setBounds(10, 40, 100, 20);
        button1.addActionListener(this);
        this.add(button1);

        label2 = new JLabel("Cleaned Text:");
        label2.setBounds(10, 70, 100, 20);
        this.add(label2);

        textbox2 = new JTextArea();
        textbox2.setBounds(10, 100, 460, 350);
        this.add(textbox2);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                file_name = selectedFile.getAbsolutePath();
                textbox1.setText(file_name);
                pdf_text = convert_pdf_to_txt(file_name);
                DepoCleaner depocleaner = new DepoCleaner(pdf_text);
                clean_text = depocleaner.clean_up();
                textbox2.setText(clean_text);
            }
        }
    }

    public String convert_pdf_to_txt(String path) {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.getClass();
        if (!document.isEncrypted()) {
            PDFTextStripperByArea stripper = null;
            try {
                stripper = new PDFTextStripperByArea();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stripper.setSortByPosition(true);
            PDFTextStripper tStripper = null;
            try {
                tStripper = new PDFTextStripper();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String pdfFileInText = null;
            try {
                pdfFileInText = tStripper.getText(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pdfFileInText;
        }
        return null;
    }

    public static void main(String[] args) {
        DepoLoader depoloader = new DepoLoader();
    }
}

class DepoCleaner {
    private String text;

    public DepoCleaner(String text) {
        this.text = text;
    }

    public String remove_numbers(String _text_value) {
        return _text_value.replaceAll("[0-9]", "");
    }

    public String remove_colons(String _text_value) {
        return _text_value.replaceAll(":\\s*", " ");
    }

    public String remove_newlines(String _text_value) {
        return _text_value.replaceAll("\n", " ");
    }

    public String remove_extra_spaces(String text) {
        text = text.replace("·", "");
        return text.replaceAll("\\s+", " ");
    }

    public String clean_up() {
        String cleaned = remove_numbers(text);
        cleaned = remove_colons(cleaned);
        cleaned = remove_newlines(cleaned);
        cleaned = remove_extra_spaces(cleaned);
        return cleaned;
    }
}