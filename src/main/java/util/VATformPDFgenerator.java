package util;//package util;
//
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;
import model.VATform;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.util.List;

public class VATformPDFgenerator {


        public static void generateVATFormPDF(VATform form, InputStream templateInputStream, String filePath) throws IOException {

        try (PDDocument document = PDDocument.load(templateInputStream)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                // List all field names to verify correct identification
                List<PDField> fields = acroForm.getFields();
                System.out.println("Field Names in the PDF:");
                for (PDField field : fields) {
                    System.out.println(field.getFullyQualifiedName());
                }

                // Update field names with actual ones from the PDF
                setField(acroForm, "1.0", form.getCompanyName());
                setField(acroForm, "1.1.0", form.extractStreetName(form.getCompanyAddress()));
                setField(acroForm, "1.1.1_HN", form.extractHouseNumber(form.getCompanyAddress()));
                setField(acroForm, "1.1.2", form.extractAddition(form.getCompanyAddress()));
                setField(acroForm, "1.2_PC", form.extractPostalCode(form.getCompanyAddress()));
                setField(acroForm, "1.3", form.extractCity(form.getCompanyAddress()));
                setField(acroForm, "1.date01.d", form.getYear(form.getBusinessStartDate()));
                setField(acroForm, "1.date01.m", form.getMonth(form.getBusinessStartDate()));
                setField(acroForm, "1.date01.y", form.getDay(form.getBusinessStartDate()));
                setField(acroForm, "1.5_TEL", form.getBusinessPhoneNumber());
                setField(acroForm, "1.6", form.getBusinessWebsite());

                setField(acroForm, "2.0","1. Eenmanszaak. Vul vraag 3 in en sla vraag 4 over." );

                setField(acroForm, "3_NAME", form.getStudentName());
                setField(acroForm, "3_ST", form.extractStreetName(form.getStudentAddress()));
                setField(acroForm, "3_HN",form.extractHouseNumber(form.getStudentAddress()));
                setField(acroForm, "3_ADD",form.extractAddition(form.getStudentAddress()));
                setField(acroForm,"3_PC", form.extractPostalCode(form.getStudentAddress()));
                setField(acroForm,"3_CITY", form.extractCity(form.getStudentAddress()));
                setField(acroForm,"3_TN", form.getStudentPhoneNumber());
                setField(acroForm,"3_DATE", form.getYear(form.getDateOfBirth())); // field name is switched between year and day
                setField(acroForm,"3_MONTH", form.getMonth(form.getDateOfBirth()));
                setField(acroForm,"3_YEAR",form.getDay(form.getDateOfBirth()));
                setField(acroForm,"3_BSN",form.getBSN());
                setField(acroForm,"6_JA","Yes_vtct");
                setField(acroForm,"7_NOB",form.getNatureOfBusiness());

                setField(acroForm,"8.0_A9", form.getEarningsFirstYear().toString());
                setField(acroForm,"8.1_A9", form.getCompanySpendings().toString());
                setField(acroForm,"8.2", " Nee");
                setField(acroForm,"8.3", form.getNoOfEmployers());
                setField(acroForm,"8.4", "1. Bedrijfsadres (gelijk aan vraag 1b)");

                setField(acroForm, "11.0", form.getStudentName());
                setField(acroForm,"11.1.0", form.extractStreetName(form.getStudentAddress()));
                setField(acroForm, "11.1.1_HN", form.extractHouseNumber(form.getStudentAddress()));
                setField(acroForm, "11.1.2", form.extractAddition(form.getStudentAddress()));
                setField(acroForm, "11.2_PC", form.extractPostalCode(form.getStudentAddress()));
                setField(acroForm, "11.3", form.extractCity(form.getStudentAddress()));
                setField(acroForm, "11.4_TEL", form.getStudentPhoneNumber());
                setField(acroForm, "11.date04.d_F", form.getYear(form.getDateOfBirth()));
                setField(acroForm, "11.date04.m_F", form.getMonth(form.getDateOfBirth()));
                setField(acroForm, "11.date04.y_F", form.getDay(form.getDateOfBirth()));

                addSignatureImage(document, acroForm, "text_66hkru", form.getSignatureBase64());

                acroForm.flatten();

                document.save(filePath);
                System.out.println("PDF saved successfully at: " + filePath);
                File generatedFile = new File(filePath);
                if (generatedFile.exists()) {
                    System.out.println("PDF file exists: " + filePath);
                } else {
                    System.err.println("Failed to create PDF file: " + filePath);
                }
            } else {
                throw new IOException("Failed to get AcroForm from document");
            }
        }
    }

    private static void addSignatureImage(PDDocument document, PDAcroForm acroForm, String fieldName, String base64Image) throws IOException {
        PDField field = acroForm.getField(fieldName);
        if (field != null) {
            // Decode base64 to byte array
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

            // Convert byte array to PDImageXObject
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, decodedBytes, "signature");

            // Get the position and size of the field
            PDRectangle rect = field.getWidgets().get(0).getRectangle();

            // Add image to the page
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(3), PDPageContentStream.AppendMode.APPEND, true);
            contentStream.drawImage(pdImage, rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getWidth(), rect.getHeight());
            contentStream.close();
        } else {
            System.out.println("Field \"" + fieldName + "\" not found");
        }
    }

    private static void setField(PDAcroForm acroForm, String fieldName, String value) throws IOException {
        if (value != null) {
            value = value.replace('\u00A0', ' '); // Replace non-breaking space with regular space
        }
        PDField field = acroForm.getField(fieldName);
        if (field != null) {
            if (field instanceof PDCheckBox || field instanceof PDRadioButton) {
                field.setValue(value);
            } else if (field instanceof PDTextField) {
                ((PDTextField) field).setValue(value);
            }
        } else {
            System.out.println("Field \"" + fieldName + "\" not found");
        }
    }
}


