package mexiapp.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class Pdf {

    private static final String PDF_CODIGO_SEGURIDAD = "./attachments/CodigoSeguridadCita.pdf";
    public static final String PDF_CONFIRMACION = "ConfirmacionCita.pdf";

    public static String readCode() {
        File imageFile = new File(PDF_CODIGO_SEGURIDAD);

        ITesseract instance = new Tesseract();
        instance.setDatapath("tessdata");
        instance.setLanguage("eng");

        try {
            String result = instance.doOCR(imageFile);
            return Helper.Result.getCode(result);
        } catch (TesseractException e) {
            // TODO: Write a file to save error
            return "Error";
        }
    }

    public static String readToken() {
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;
        File file = new File(PDF_CODIGO_SEGURIDAD);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(1);
            String parsedText = pdfStripper.getText(pdDoc);
            return Helper.Result.getToken(parsedText);
        } catch (Exception e) {
            // TODO: Save error into file.
            return "Error";
        }
    }
}
