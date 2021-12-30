package de.swingtest.gui;

import java.awt.event.KeyEvent;
import java.io.File;

import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TestComplexSwingGui extends AssertJSwingJUnitTestCase {

    private static final String EXPORT_DATEI = "diesIstEinDateiname.csv";

    private FrameFixture window;

    int[] vorname = { KeyEvent.VK_A, KeyEvent.VK_X, KeyEvent.VK_SPACE };
    int[] nachname = { KeyEvent.VK_U, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_M,
            KeyEvent.VK_A, KeyEvent.VK_N, KeyEvent.VK_N, KeyEvent.VK_ENTER };
    int[] strasse = { KeyEvent.VK_U, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_S,
            KeyEvent.VK_T, KeyEvent.VK_R, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_S, KeyEvent.VK_E, KeyEvent.VK_SPACE,
            KeyEvent.VK_1, KeyEvent.VK_ENTER };
    int[] ortTeil1 = { KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_SPACE };
    int[] ortTeil2 = { KeyEvent.VK_U, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_S,
            KeyEvent.VK_T, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_T };

    boolean firstRun = true;
    long timestamp = 0L;

    @Override
    protected void onSetUp() {
        ComplexSwingGui frame = GuiActionRunner.execute(() -> new ComplexSwingGui());
        window = new FrameFixture(robot(), frame);
        window.show();
        if (firstRun) {
            timestamp = System.currentTimeMillis();
            firstRun = false;
        }

    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
        // Export/Impot Datei löschen
        File fileToDetele = new File(EXPORT_DATEI);
        fileToDetele.delete();
    }

    // @Test
    public void componentsShouldBeVisible() {
        JLabelFixture adresseLbl = window.label("adresseLbl");
        adresseLbl.requireVisible();
        adresseLbl.requireText("Adresse:");

        JTextComponentFixture adresseTextBox = window.textBox("adresseTextArea");
        adresseTextBox.requireVisible();

        JLabelFixture alterLbl = window.label("alterLbl");
        alterLbl.requireVisible();
        alterLbl.requireText("Alter:");

        JTextComponentFixture alterTextBox = window.textBox("alterTextField");
        alterTextBox.requireVisible();

        JLabelFixture formatErrorLbl = window.label(JLabelMatcher.withName("formatErrorLbl"));
        formatErrorLbl.requireNotVisible();
        formatErrorLbl.requireText("Es sind nur numerische Werte (0..9) von 1 bis 100 erlaubt.");

        JButtonFixture addBtn = window.button("addBtn");
        addBtn.requireVisible();
        addBtn.requireEnabled();
        addBtn.requireText("Hinzufügen");

        JButtonFixture resetBtn = window.button("resetBtn");
        resetBtn.requireVisible();
        resetBtn.requireEnabled();
        resetBtn.requireText("Zurücksetzen");

        JTableFixture table = window.table("dataTable");
        table.requireVisible();

        JButtonFixture exportBtn = window.button("exportBtn");
        exportBtn.requireVisible();
        exportBtn.requireEnabled();
        exportBtn.requireText("Exportieren");

        JButtonFixture importBtn = window.button("importBtn");
        importBtn.requireVisible();
        importBtn.requireEnabled();
        importBtn.requireText("Importieren");

        JButtonFixture clearBtn = window.button("clearBtn");
        clearBtn.requireVisible();
        clearBtn.requireEnabled();
        clearBtn.requireText("Tabelle leeren");

        JButtonFixture closeBtn = window.button("closeBtn");
        closeBtn.requireVisible();
        closeBtn.requireEnabled();
        closeBtn.requireText("Schließen");
    }

    @Test
    public void tableHasCorrectColumns() {
        JTableFixture table = window.table("dataTable");
        table.requireVisible();
        table.requireColumnCount(4);

        JTableFixture columnID = table.requireColumnNamed("ID");
        columnID.requireVisible();
        JTableFixture columnAdresse = table.requireColumnNamed("Adresse");
        columnAdresse.requireVisible();
        JTableFixture columnAlter = table.requireColumnNamed("Alter");
        columnAlter.requireVisible();
        JTableFixture columnZeitstempel = table.requireColumnNamed("Zeitstempel");
        columnZeitstempel.requireVisible();
    }

    @Test
    public void shouldBeAddData() {
        addData();
    }

    @Test
    public void shouldBeExportData() {
        addData();
        exportData();
    }

    @Test
    public void shouldBeRemoveData() {
        addData();
        removeData();
    }

    @Test
    public void shouldBeSImportData() {
        addData();
        exportData();
        removeData();
        importData();
    }

    @Test
    public void shouldBeSImportDataClearForm() {
        removeData();
        importData();
    }

    private void addData() {
        JTextComponentFixture adresseTextBox = window.textBox("adresseTextArea");
        typeUpperCaseLetter(KeyEvent.VK_M, adresseTextBox);
        adresseTextBox.pressAndReleaseKeys(vorname);
        typeUpperCaseLetter(KeyEvent.VK_M, adresseTextBox);
        adresseTextBox.pressAndReleaseKeys(nachname);
        typeUpperCaseLetter(KeyEvent.VK_M, adresseTextBox);
        adresseTextBox.pressAndReleaseKeys(strasse);
        adresseTextBox.pressAndReleaseKeys(ortTeil1);
        typeUpperCaseLetter(KeyEvent.VK_M, adresseTextBox);
        adresseTextBox.pressAndReleaseKeys(ortTeil2);

        adresseTextBox.requireText("Max Mustermann\nMusterstrasse 1\n12345 Musterstadt");

        JTextComponentFixture alterTextBox = window.textBox("alterTextField");
        alterTextBox.pressAndReleaseKeys(KeyEvent.VK_3, KeyEvent.VK_1);

        alterTextBox.requireText("31");
        JLabelFixture label = window.label(JLabelMatcher.withName("formatErrorLbl"));
        label.requireNotVisible();

        JButtonFixture addBtn = window.button("addBtn");
        addBtn.requireEnabled();
        addBtn.click();

        JTableFixture table = window.table("dataTable");
        table.requireRowCount(1);
    }

    private void exportData() {
        JButtonFixture exportBtn = window.button("exportBtn");
        exportBtn.requireEnabled();
        exportBtn.click();

        JFileChooserFixture fileChooserFixture = window.fileChooser();
        fileChooserFixture.requireVisible();

        fileChooserFixture.fileNameTextBox().setText(EXPORT_DATEI);
        fileChooserFixture.approveButton().click();
    }

    private void removeData() {
        JButtonFixture clearBtn = window.button("clearBtn");
        clearBtn.click();
        JTableFixture table = window.table("dataTable");
        table.requireRowCount(0);
    }

    private void importData() {
        JButtonFixture importBtn = window.button("importBtn");
        importBtn.requireEnabled();
        importBtn.click();

        JFileChooserFixture fileChooserFixture = window.fileChooser();
        fileChooserFixture.requireVisible();

        fileChooserFixture.fileNameTextBox().setText(EXPORT_DATEI);
        fileChooserFixture.approveButton().click();

        JTableFixture table = window.table("dataTable");
        table.requireRowCount(1);
    }

    private void typeUpperCaseLetter(int letter, JTextComponentFixture textBox) {
        textBox.pressKey(KeyEvent.VK_SHIFT);
        textBox.pressAndReleaseKeys(letter);
        textBox.releaseKey(KeyEvent.VK_SHIFT);
    }

}