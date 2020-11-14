package ru.kpfu.itis.rodsher.services;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import ru.kpfu.itis.rodsher.models.Identity;
import ru.kpfu.itis.rodsher.models.Passport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TemplatePdfCreator {
    private final String SAVE_PATH;
    private final String TEMPLATES_PATH;
    private final String FONTS_PATH;

    private final String TIMES_NEW_ROMAN = "times.ttf";

    private final String PASS_LEVEL_A = "PassLevelA.pdf";
    private final String PASS_LEVEL_B = "PassLevelB.pdf";
    private final String PASS_LEVEL_C = "PassLevelC.pdf";
    private final String PASSPORT = "Passport.pdf";

    private final String DATE_FORMAT = "dd_MM_yyyy_kk_mm_ss_SSSS";
    private final SimpleDateFormat dateFormat;

    public TemplatePdfCreator(String templatesPath, String savePath, String fontsPath) {
        TEMPLATES_PATH = templatesPath;
        SAVE_PATH = savePath;
        FONTS_PATH = fontsPath;
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    public TemplatePdfCreator() {
        this("", "applications", "");
    }

    public boolean createPassportFromImage(File main, File add, String id, String saveDirectory) {
        try {
            PdfDocument pdfDocument = createPdfDocument("", id + "_"
                    + dateFormat.format(new Date(System.currentTimeMillis())) + "_PASSPORT_IMAGE.pdf", true);
            Document document = new Document(pdfDocument, PageSize.A4);
            document.setFont(createFont(TIMES_NEW_ROMAN)).setFontSize(12f);

            Paragraph heading = new Paragraph();
            heading.setTextAlignment(TextAlignment.CENTER);
            heading.add("СКАН ПАСПОРТА " + id);
            document.add(heading);

            Image imageMain = new Image(ImageDataFactory.create(main.getPath()));
            imageMain.setHorizontalAlignment(HorizontalAlignment.CENTER);
            imageMain.scaleToFit(300, 300);
            Image imageAdd = new Image(ImageDataFactory.create(add.getPath()));
            imageAdd.setHorizontalAlignment(HorizontalAlignment.CENTER);
            imageAdd.scaleToFit(300, 300);

            document.add(imageMain);
            document.add(imageAdd);

            System.out.println("BEFORE");
            pdfDocument.close();
            System.out.println("AFTER");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createPassportFromData(Passport passport, String saveDirectory) {
        try {
            System.out.println("PASSPORT: " + passport);
            PdfDocument pdfDocument = createPdfDocument(PASSPORT, passport.getSurname() + "_"
                    + dateFormat.format(new Date(System.currentTimeMillis())) + "_PASSPORT_DATA.pdf", false);

            Map<String, String> map = new HashMap<>();
            map.put("Name", passport.getSurname() + " " + passport.getName() + " " + passport.getMiddleName());
            map.put("Sex", passport.isMan() ? "Мужчина" : "Женщина");
            map.put("City", passport.getCity());
            map.put("Address", passport.getAddress());
            map.put("Series", String.valueOf(passport.getSeries()));
            map.put("IssuedByCode", String.valueOf(passport.getIssuedByCode()));
            map.put("IssuedBy", passport.getIssuedBy());
            map.put("IssueDate", new SimpleDateFormat("dd.MM.yyyy").format(passport.getIssueDate()));
            map.put("Birth", new SimpleDateFormat("dd.MM.yyyy").format(passport.getBirth()));
            map.put("Unique", UUID.randomUUID().toString());

            fillFields(PdfAcroForm.getAcroForm(pdfDocument, true), map, createFont(TIMES_NEW_ROMAN));

            System.out.println("BEFORE");
            pdfDocument.close();
            System.out.println("AFTER");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createPassLevelA(Identity identity, String signature, String saveDirectory) {
        return createPassLevel("A", identity, signature, saveDirectory);
    }

    public boolean createPassLevelB(Identity identity, String signature, String saveDirectory) {
        return createPassLevel("B", identity, signature, saveDirectory);
    }

    public boolean createPassLevelC(Identity identity, String signature, String saveDirectory) {
        return createPassLevel("C", identity, signature, saveDirectory);
    }

    private boolean createPassLevel(String level, Identity identity, String signature, String saveDirectory) {
        try {
            String passLevel = PASS_LEVEL_A;
            switch (level) {
                case "B":
                    passLevel = PASS_LEVEL_B;
                    break;
                case "C":
                    passLevel = PASS_LEVEL_C;
                    break;
            }
            PdfDocument pdfDocument = createPdfDocument(passLevel, identity.getSurname() + "_"
                    + dateFormat.format(new Date(System.currentTimeMillis())) + "_PASS_LEVEL_" + level + ".pdf", false);

            Map<String, String> map = new HashMap<>();
            map.put("Name", identity.getSurname() + " " + identity.getName() + " " + identity.getMiddleName());
            map.put("Sex", identity.isMan() ? "Мужчина" : "Женщина");
            map.put("Email", identity.getEmail());
            map.put("ValidUntil", new SimpleDateFormat("dd.MM.yyyy")
                    .format(new Date(System.currentTimeMillis() + 315576000000L)));
            map.put("Signature", signature);
            switch (level) {
                case "C":
                    map.put("Address", identity.getAddress());
                case "B":
                    map.put("Phone", String.valueOf(identity.getPhone()));
                    map.put("Birth", new SimpleDateFormat("dd.MM.yyyy").format(identity.getBirth()));
                    break;
            }
            fillFields(PdfAcroForm.getAcroForm(pdfDocument, true), map, createFont(TIMES_NEW_ROMAN));

            System.out.println("BEFORE");
            pdfDocument.close();
            System.out.println("AFTER");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void fillFields(PdfAcroForm form, Map<String, String> map, PdfFont font) {
        for(Map.Entry<String, String> entry : map.entrySet()) {
            form.getField(entry.getKey()).setValue(entry.getValue(), font, 12f).setReadOnly(true);
        }
    }

    private PdfFont createFont(String fileName) throws IOException {
        try {
            return PdfFontFactory.createFont(FontProgramFactory.createFont( (!FONTS_PATH.equals("") ? FONTS_PATH + "/" : "") + fileName), "Cp1251", true);
        } catch (IOException e) {
            throw new IOException("Can't find '" + fileName + "' font in '" + FONTS_PATH + "' path!", e);
        }
    }

    private PdfDocument createPdfDocument(String source, String destination, boolean onlyWriter) throws IOException {
        try {
            if(onlyWriter) {
                return new PdfDocument(new PdfWriter((!SAVE_PATH.equals("") ? SAVE_PATH + "/" : "") + destination));
            } else {
                return new PdfDocument(
                        new PdfReader((!TEMPLATES_PATH.equals("") ? TEMPLATES_PATH + "/" : "") + source),
                        new PdfWriter((!SAVE_PATH.equals("") ? SAVE_PATH + "/" : "") + destination));
            }
        } catch (IOException e) {
            throw new IOException("Can't find source file or destination path for PdfDocument", e);
        }
    }
}
