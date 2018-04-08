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

public class Main {

    static private PDDocument doc;
    static private PDImageXObject pdImage;
    static private PDFont fontLato, fontLatoBold;

    static String infile = "deltakere.txt";
    static String outfile = "result.pdf";

    static private float papersizeX = 595;  //A4 port -- http://www.papersizes.org/a-sizes-in-pixels.htm
    static private float papersizeY = 841;  //A4 port -- = 72pixler per inch. (28.3 piksler per cm)

    static private float cardW = 250;
    static private float cardH = 160;

    static ArrayList<String> entry;
    static int cardsPerPage;
    static int pages;

    public static void main(String[] args) throws IOException {
        doc = new PDDocument();
        fontLato = PDType0Font.load(doc, new File("Lato-Regular.ttf"));
        fontLatoBold = PDType0Font.load(doc, new File("Lato-Bold.ttf"));
        entry = new ArrayList<String>();

        System.out.println("Reading from "+infile);
        BufferedReader br = new BufferedReader(new FileReader(infile));
        String line = br.readLine();
        while (line != null)
        {
            entry.add(line);
            line = br.readLine();
        }
        br.close();


        System.out.println(""+ entry.size()+" cards to print.");
        int cards= entry.size();
        cardsPerPage=10;
        pages=(int) Math.ceil(cards/cardsPerPage);
        for (int i=0;i<=pages;i++) {
            System.out.println("Writing page "+(i+1));
            addPageFront();
        }

        doc.save(outfile);
        System.out.println("Done. Wrote to "+outfile);
    }

    private static float getCardX(int i) {
        return papersizeX/4+papersizeX/2*(i%2);
    }

    private static float getCardY(int i) {return papersizeY-(papersizeY/10)-(papersizeY/5)*(float)Math.floor(i/2);}

    private static void addPageFront() throws IOException {
        PDPage page =PDFHelper.getA4PortraitPage();
        doc.addPage(page);
        PDPageContentStream contents = new PDPageContentStream(doc, page);
        for (int i=0;i<cardsPerPage;i++) {
            if (entry.size()==0) break;
            String name=entry.get(0);
            entry.remove(0);
            addCard(getCardX(i),getCardY(i),contents,name);
            System.out.println(name);
        }
        contents.stroke();
        contents.close();
    }

    private static void addCard(float x, float y, PDPageContentStream contents, String e) throws IOException {

        PDFHelper.drawFilledRect(x,y,cardW,cardH,contents); //adds grey background. (hard coded color)
        contents.stroke();
        contents.setNonStrokingColor(0, 0, 0);

        String name = e.split("-")[0];
        String org = e.split("-")[1];
        PDFHelper.writeTextCentered(contents, name, fontLato, 20, x, y+30);
        PDFHelper.writeTextCentered(contents, org,fontLatoBold, 12, x, y-20);
    }
}
