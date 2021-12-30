package de.swingtest.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class ComplexSwingGui extends JFrame {

    private static final long serialVersionUID = -2973208254912255252L;

    private JTextArea adresseTextArea;
    private JTextField alterTextField;
    private JLabel formatErrorLbl;
    private JButton addBtn;
    private JButton exportBtn;
    private DefaultTableModel model;

    public ComplexSwingGui() {
        init();
    }

    private void init() {
        this.setTitle("ComplexSwingGui");
        this.setBounds(0, 0, 500, 680);
        MigLayout layout = new MigLayout(new LC().wrapAfter(1), new AC().align("center"), new AC());
        this.setLayout(layout);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        adresseTextArea = new JTextArea();
        adresseTextArea.setName("adresseTextArea");
        adresseTextArea.setPreferredSize(new Dimension(250, 50));
        this.add(getLblComponent("Adresse:", "adresseLbl", new JScrollPane(adresseTextArea)));

        alterTextField = new JTextField();
        alterTextField.setName("alterTextField");
        alterTextField.setPreferredSize(new Dimension(250, 25));
        alterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                boolean isCharAllowed = ((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE));
                if (!isCharAllowed) {
                    getToolkit().beep();
                    e.consume();
                }
            };

            @Override
            public void keyReleased(KeyEvent e) {
                boolean alterIsCorrect = false;
                try {
                    String text = alterTextField.getText();
                    if (!text.isEmpty()) {
                        Integer.parseInt(text);
                    }
                    alterIsCorrect = true;
                } catch (Exception ex) {
                    // ex.printStackTrace();
                } finally {
                    addBtn.setEnabled(alterIsCorrect);
                    formatErrorLbl.setVisible(!alterIsCorrect);
                }
            }

        });
        this.add(getLblComponent("Alter:", "alterLbl", alterTextField));

        formatErrorLbl = new JLabel("Es sind nur numerische Werte (0..9) von 1 bis 100 erlaubt.");
        formatErrorLbl.setName("formatErrorLbl");
        formatErrorLbl.setForeground(Color.RED);
        formatErrorLbl.setVisible(false);
        this.add(formatErrorLbl);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setPreferredSize(new Dimension(450, 45));
        addBtn = new JButton("Hinzufügen");
        addBtn.setName("addBtn");
        addBtn.addActionListener(event -> {
            long millis = System.currentTimeMillis();
            model.addRow(new Object[] { String.valueOf(millis), adresseTextArea.getText(), alterTextField.getText(),
                    getFormatedTimestamp(millis) });
        });

        btnPanel.add(addBtn);
        JButton resetBtn = new JButton("Zurücksetzen");
        resetBtn.setName("resetBtn");
        resetBtn.addActionListener(event -> {
            adresseTextArea.setText("");
            alterTextField.setText("");
        });
        btnPanel.add(resetBtn);
        this.add(btnPanel);

        model = new DefaultTableModel(new Object[] { "ID", "Adresse", "Alter", "Zeitstempel" }, 0);
        JTable table = new JTable(model);
        table.setName("dataTable");
        this.add(new JScrollPane(table));

        JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel2.setPreferredSize(new Dimension(450, 45));

        exportBtn = new JButton("Exportieren");
        exportBtn.setName("exportBtn");
        exportBtn.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(ComplexSwingGui.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
                    int rowCount = model.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        long id = (long) Long.parseLong(model.getValueAt(i, 0).toString());
                        String adresse = (String) model.getValueAt(i, 1);
                        adresse = adresse.replaceAll("\n", " ");
                        int alter = Integer.parseInt(model.getValueAt(i, 2).toString());
                        String zeitstempel = (String) model.getValueAt(i, 3);
                        String row = String.format("%d;%s;%d;%s\r\n", id, adresse, alter, zeitstempel);
                        bw.write(row);
                        bw.flush();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnPanel2.add(exportBtn);

        JButton importBtn = new JButton("Importieren");
        importBtn.setName("importBtn");
        importBtn.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ComplexSwingGui.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        String[] values = line.split(";");
                        model.addRow(new Object[] { values[0], values[1], values[2], values[3] });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnPanel2.add(importBtn);

        JButton clearBtn = new JButton("Tabelle leeren");
        clearBtn.setName("clearBtn");
        clearBtn.addActionListener(event -> {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
        });
        btnPanel2.add(clearBtn);

        JButton closeBtn = new JButton("Schließen");
        closeBtn.setName("closeBtn");
        closeBtn.addActionListener(event -> {
            System.exit(0);
        });
        btnPanel2.add(closeBtn);
        this.add(btnPanel2);
    }

    private JPanel getLblComponent(String lblBez, String lblName, JComponent comp) {
        JPanel pnl = new JPanel(new FlowLayout());
        JLabel lbl = new JLabel(lblBez);
        lbl.setName(lblName);
        lbl.setPreferredSize(new Dimension(150, 25));
        pnl.add(lbl);
        pnl.add(comp);
        return pnl;
    }

    private String getFormatedTimestamp(long millis) {
        DateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateformat.format(new Date(millis));
    }
}