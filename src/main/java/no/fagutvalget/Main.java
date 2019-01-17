package no.fagutvalget;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    static int cardsPerPage;
    private static String infile1 = "fu.txt";
    private static String infile2 = "ir.txt";
    private static String outfile = "result.pdf";
    private static int pages = 500;
    static private PDDocument doc;
    static private PDImageXObject pdImage;
    static private PDFont fontLato, fontLatoBold;
    static private float papersizeX = 595;  //A4 port -- http://www.papersizes.org/a-sizes-in-pixels.htm
    static private float papersizeY = 841;  //A4 port -- = 72pixler per inch. (28.3 piksler per cm)
    static private float cardW = 250;
    static private float cardH = 160;

    public static void main(String[] args) throws IOException {
        doc = new PDDocument();
        fontLato = PDType0Font.load(doc, new File("Lato-Regular.ttf"));
        fontLatoBold = PDType0Font.load(doc, new File("Lato-Bold.ttf"));
        ArrayList<String> fuNavn = new ArrayList<String>();
        ArrayList<String> irNavn = new ArrayList<String>();

        System.out.println("Reading fu from " + infile1);
        BufferedReader br = new BufferedReader(new FileReader(infile1));
        String line = br.readLine();
        while (line != null) {
            fuNavn.add(line);
            line = br.readLine();
        }
        br.close();

        System.out.println("Reading ir from " + infile2);
        br = new BufferedReader(new FileReader(infile2));
        line = br.readLine();
        while (line != null) {
            irNavn.add(line);
            line = br.readLine();
        }
        br.close();


        for (int i = 0; i < pages; i++) {
            System.out.println("Writing page " + (i + 1));
            addPage(fuNavn, irNavn);
        }

        doc.save(outfile);
        System.out.println("Done. Wrote to " + outfile);
    }

    private static float getCardX(int i) {
        return papersizeX / 4 + papersizeX / 2 * (i % 2);
    }

    private static float getCardY(int i) {
        return papersizeY - (papersizeY / 10) - (papersizeY / 5) * (float) Math.floor(i / 2);
    }

    private static void addPage(ArrayList<String> fuNavn, ArrayList<String> irNavn) throws IOException {

        Collections.shuffle(fuNavn);
        Collections.shuffle(irNavn);
        System.out.println(irNavn.size());
        PDPage page = PDFHelper.getA4PortraitPage();
        doc.addPage(page);
        PDPageContentStream contents = new PDPageContentStream(doc, page);

        float y = papersizeY - 50;
        float x = 80;

        PDFHelper.writeText(contents, "Stemmeseddel - fagutvalget/instituttrådet H18/V19", fontLato, 20, x + 20, y);
        y -= 20;

        PDFHelper.writeText(contents, "Ranger kandidatene fra 1 og oppover slik at 1 er ditt høyeste ønske", fontLato, 12, x + 20, y);
        y -= 16;
        PDFHelper.writeText(contents, "Du kan rangere opp til 12 kandidater til fagutvalget, og 3 til instituttrådet", fontLato, 12, x + 20, y);
        y -= 16;
        y -= 16;

        PDFHelper.writeText(contents, "Om stemmen din har rangert høyere enn 12, har to like tall (til samme valg), eller har", fontLato, 12, x + 20, y);
        y -= 16;
        PDFHelper.writeText(contents, "hoppet over et tall vil den anses som ugyldig", fontLato, 12, x + 20, y);
        y -= 16;
        y -= 16;

        PDFHelper.writeText(contents, "Når du er ferdig brettes arket 1 gang på langsiden og legges i stemmekassen", fontLato, 12, x + 20, y);
        y -= 16;

        y = papersizeY - 250;
        x = 80;
        int fontsize = 14;
        PDFHelper.writeText(contents, "Fagutvalget", fontLato, 22, x + 20, y + 20);
        fillPDF(fuNavn, contents, y, x, fontsize);

        x = 350;
        PDFHelper.writeText(contents, "Instituttrådet", fontLato, 22, x + 20, y + 20);
        fillPDF(irNavn, contents, y, x, fontsize);

        contents.close();
    }

    private static void fillPDF(ArrayList<String> fuNavn, PDPageContentStream contents, float y, float x, int fontsize) throws IOException {
        for (int i = 0; i < fuNavn.size(); i++) {
            PDFHelper.writeText(contents, fuNavn.get(i), fontLato, fontsize, x + 20, y - fontsize * 1.5f * i);
            contents.setStrokingColor(0, 0, 0);
            contents.setLineWidth(0.5f);
            contents.moveTo(x, y - fontsize * 1.5f * i);
            contents.lineTo(x + 15, y - fontsize * 1.5f * i);
            contents.stroke();
        }
    }

    private static void addCard(float x, float y, PDPageContentStream contents, String e) throws IOException {

        PDFHelper.drawFilledRect(x, y, cardW, cardH, contents); //adds grey background. (hard coded color)
        contents.stroke();
        contents.setNonStrokingColor(0, 0, 0);

        String name = e.split("-")[0];
        String org = e.split("-")[1];
        PDFHelper.writeTextCentered(contents, name, fontLato, 20, x, y + 30);
        PDFHelper.writeTextCentered(contents, org, fontLatoBold, 12, x, y - 20);
    }
}
