package no.fagutvalget;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

public class PDFHelper {
    public static PDPage getA4LandscapePage() {
        float POINTS_PER_INCH = 72;
        float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
        return new PDPage(new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM));
    }

    static PDPage getA4PortraitPage() {
        float POINTS_PER_INCH = 72;
        float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
        return new PDPage(new PDRectangle(210 * POINTS_PER_MM, 297 * POINTS_PER_MM));
    }

    static void writeTextCentered(PDPageContentStream contents, String string, PDFont font, int fontSize, float x, float y) throws IOException {
        float text_width = (font.getStringWidth(string) / 1000.0f) * fontSize;
        float text_height = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        contents.setFont(font, fontSize);
        contents.beginText();
        contents.newLineAtOffset(x - text_width / 2, y - text_height / 4);
        contents.showText(string);
        contents.endText();
    }

    static void writeText(PDPageContentStream contents, String string, PDFont font, int fontSize, float x, float y) throws IOException {
        contents.setFont(font, fontSize);
        contents.beginText();
        contents.newLineAtOffset(x, y);
        contents.showText(string);
        contents.endText();
    }

    static void drawFilledRect(float x, float y, float w, float h, PDPageContentStream contents) throws IOException {
        contents.setNonStrokingColor(230, 230, 230); //gray background
        contents.fillRect(x - w / 2, y - h / 2, w, h);
    }
}
