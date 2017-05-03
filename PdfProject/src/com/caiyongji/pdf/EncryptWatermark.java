package com.caiyongji.pdf;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class EncryptWatermark {
	private static final Logger logger = LoggerFactory.getLogger(EncryptWatermark.class);

	public static void main(String[] args) {
		process("t:/pdf/in.pdf", "t:/pdf/out.pdf", null,
				"caicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicaicai"
						.getBytes(),
				"CAIYONGJI CAIYONGJI CAIYONGJI CAIYONGJI");
	}

	/**
	 * encrypt & water mark
	 * 
	 * @param src
	 *            source path
	 * @param dest
	 *            destination path
	 * @param ownerPassword
	 *            open password
	 * @param permissions
	 *            modify password
	 * @param content
	 *            water mark content
	 */
	@SuppressWarnings("resource")
	public static void process(String src, String dest, byte[] ownerPassword, byte[] permissions, String content) {
		try {
			File file = new File(dest);
			file.getParentFile().mkdirs();
			PdfReader reader = new PdfReader(src, new ReaderProperties().setPassword(permissions));
			PdfWriter writer = new PdfWriter(dest, new WriterProperties().setStandardEncryption(ownerPassword,
					permissions, EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ENCRYPTION_AES_256));
			PdfDocument pdfDoc = new PdfDocument(reader, writer);
			for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
				PdfCanvas under = new PdfCanvas(pdfDoc.getPage(i).newContentStreamBefore(), new PdfResources(), pdfDoc);
				under.setFillColor(Color.LIGHT_GRAY);
				under.saveState();
				PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA));
				Paragraph p = new Paragraph(content).setFont(font).setFontSize(50);
				new Canvas(under, pdfDoc, pdfDoc.getDefaultPageSize()).showTextAligned(p,
						pdfDoc.getDefaultPageSize().getWidth() / 2, pdfDoc.getDefaultPageSize().getHeight() / 2, 1,
						TextAlignment.CENTER, VerticalAlignment.TOP, -50);
			}
			pdfDoc.close();
		} catch (IOException e) {
			logger.error("EncryptWatermark process error: {} ", e);
		}
	}

}
