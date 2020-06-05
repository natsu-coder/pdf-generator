package com.natsu.pdf.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;

@RestController
public class GeneratePdfController {

	@GetMapping("/pdf")
	public void generatePdf(HttpServletResponse response) {
		try {
			ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
			templateResolver.setSuffix(".html");
			templateResolver.setTemplateMode("HTML");
			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(templateResolver);

			Context context = new Context();
			context.setVariable("name", "natsu");

			String html = templateEngine.process("html/natsu-thymeleaf", context);

			String fileName = "natsu.pdf";
			response.addHeader("Content-disposition", "attachment;filename=" + fileName);
			response.setContentType("application/pdf");

			OutputStream outputStream = response.getOutputStream();

			// pdf encryption
			PDFEncryption pdfEncryption = new PDFEncryption();
			String password = "natsu";
			pdfEncryption.setUserPassword(password.getBytes());

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.setPDFEncryption(pdfEncryption); // encrypt pdf file
			renderer.createPDF(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
