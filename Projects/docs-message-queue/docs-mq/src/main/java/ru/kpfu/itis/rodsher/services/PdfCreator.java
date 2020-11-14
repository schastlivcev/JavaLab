package ru.kpfu.itis.rodsher.services;

import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import ru.kpfu.itis.rodsher.models.PersonalData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PdfCreator {
    private final String SAVE_DIRECTORY;
    private final String FONT_PATH;

    public PdfCreator() {
        SAVE_DIRECTORY = "applications";
        FONT_PATH = "";
    }

    public PdfCreator(String saveDirectory, String fontPath) {
        SAVE_DIRECTORY = saveDirectory;
        FONT_PATH = fontPath;
    }

    public boolean createResignation(PersonalData personalData) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(SAVE_DIRECTORY
                    + "\\" + personalData.getSurname() + "_" + personalData.getPassport().getSeries() + "_RESIGNATION.pdf")));
            Document document = new Document(pdfDocument, PageSize.A4);
            document.setFont(PdfFontFactory.createFont(FONT_PATH + "times.ttf", "CP1251", true)).setFontSize(12);

            addForWhom(document, personalData);

            Paragraph heading = new Paragraph();
            heading.setTextAlignment(TextAlignment.CENTER);
            heading.add("ЗАЯВЛЕНИЕ\nоб увольнении по собственному желанию");
            document.add(heading);

            Paragraph mainPart = new Paragraph();
            mainPart.add(new Tab());
            mainPart.add("В соответствии со ст. 80 Трудового кодекса РФ прошу уволить меня по собственному желанию 1 января 2021 года.");
            document.add(mainPart);

            addSignature(document, personalData);

            document.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createVacation(PersonalData personalData) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(SAVE_DIRECTORY
                    + "\\" + personalData.getSurname() + "_" + personalData.getPassport().getSeries() + "_VACATION.pdf")));
            Document document = new Document(pdfDocument, PageSize.A4);
            document.setFont(PdfFontFactory.createFont(FONT_PATH + "times.ttf", "CP1251", true)).setFontSize(12);

            addForWhom(document, personalData);

            Paragraph heading = new Paragraph();
            heading.setTextAlignment(TextAlignment.CENTER);
            heading.add("ЗАЯВЛЕНИЕ");
            document.add(heading);

            Paragraph mainPart = new Paragraph();
            mainPart.add(new Tab());
            mainPart.add("Прошу предоставить мне отпуск без сохранения заработной платы с __.__.____ г. продолжительностью __ календарных дней.");
            document.add(mainPart);

            addSignature(document, personalData);

            document.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createPromotion(PersonalData personalData) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(SAVE_DIRECTORY
                    + "\\" + personalData.getSurname() + "_" + personalData.getPassport().getSeries() + "_PROMOTION.pdf")));
            Document document = new Document(pdfDocument, PageSize.A4);
            document.setFont(PdfFontFactory.createFont(FONT_PATH + "times.ttf", "CP1251", true)).setFontSize(12);

            addForWhom(document, personalData);

            Paragraph heading = new Paragraph();
            heading.setTextAlignment(TextAlignment.CENTER);
            heading.add("ЗАЯВЛЕНИЕ");
            document.add(heading);

            Paragraph mainPart = new Paragraph();
            mainPart.add(new Tab());
            mainPart.add("Прошу рассмотреть возможность повышения моего оклада с _______ до _______ рублей. За время моей работы в ОСО \"Java Lab\" привлекательность языка Java выросла на __ % по всему миру.");
            document.add(mainPart);

            addSignature(document, personalData);

            document.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void addForWhom(Document document, PersonalData personalData) {
        Paragraph forWhom = new Paragraph();
        forWhom.setMultipliedLeading(1.15f);
        forWhom.setTextAlignment(TextAlignment.RIGHT);
        forWhom.add("Генеральному преподавателю\nОСО \"Java Lab\"\nСидикову М. Р.\nот обучающего студента\n" +
                personalData.getSurname() + " " + personalData.getName() + " " + personalData.getMiddleName() + ", "
                + personalData.getAge() + " лет" + "\n"
                + (personalData.getPassport().getSeries() / 1000000) + " "
                + (personalData.getPassport().getSeries() % 1000000) + " "
                + new SimpleDateFormat("dd.MM.yyyy").format(personalData.getPassport().getIssueDate()) + "\n");
        document.add(forWhom);
    }

    private void addSignature(Document document, PersonalData personalData) {
        Paragraph signature = new Paragraph();
        signature.add("_________________");
        signature.add(new Tab());
        signature.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
        signature.add(personalData.getSurname() + " " + personalData.getName().charAt(0) + ". "
                + personalData.getMiddleName().charAt(0) + ".\n\n___ ________ 20___ г.");
        document.add(signature);
    }
}
