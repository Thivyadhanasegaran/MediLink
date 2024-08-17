package com.company.neuheathcaremanagement.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import com.company.neuheathcaremanagement.email.EmailService;
import com.company.neuheathcaremanagement.pojo.Appointment;
import com.company.neuheathcaremanagement.pojo.HealthReport;
import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.HealthReportService;
import com.company.neuheathcaremanagement.validator.HealthReportValidator;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;

@Controller
@Tag(name = "Health Reports", description = "Operations related to health reports")

public class HealthReportController {

	private static final Logger logger = LoggerFactory.getLogger(HealthReportController.class);

	@Autowired
	private HealthReportService healthReportService;

	@Autowired
	private HealthReportValidator healthReportValidator;

	@Autowired
	private EmailService emailService;

	@GetMapping("/doctor/createHealthReport")
	@Operation(summary = "Show Create Health Report Page", description = "Displays the form to create a health report based on appointment ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully fetched appointment details"),
			@ApiResponse(responseCode = "404", description = "Appointment not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public String showCreateHealthReportPage(@RequestParam("appointmentId") int appointmentId, Model model) {
		try {
			Appointment appointment = healthReportService.getAppointmentById(appointmentId);

			HealthReport healthReport = new HealthReport();
			healthReport.setAppointment(appointment);
			model.addAttribute("healthReport", healthReport);
			model.addAttribute("appointment", appointment);
			logger.info("Appointment details fetched successfully for appointmentId: {}", appointmentId);
			return "healthReport";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occurred while fetching appointment details for appointmentId: {}", appointmentId, e);
			model.addAttribute("message", "An error occurred while fetching the appointment details.");
			return "viewAppointment";
		}
	}

	@PostMapping("/doctor/saveHealthReport")
	@Operation(summary = "Save Health Report", description = "Saves the health report for an appointment.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully saved health report"),
			@ApiResponse(responseCode = "400", description = "Validation errors occurred"),
			@ApiResponse(responseCode = "404", description = "Appointment not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public String saveHealthReport(@ModelAttribute("healthReport") @Validated HealthReport healthReport,
			BindingResult result, Model model, HttpSession session) {
		try {
			Appointment appointment = healthReportService
					.getAppointmentById(healthReport.getAppointment().getAppointmentId());

			healthReport.setAppointment(appointment);
			healthReportValidator.validate(healthReport, result);

			if (result.hasErrors()) {
				model.addAttribute("appointment", appointment);
				return "healthReport";
			}

			User loginUser = (User) session.getAttribute("loginuser");
			healthReport.setDoctor(loginUser);
			healthReportService.saveHealthReport(healthReport);
			logger.info("Health report created successfully for appointmentId: {}",
			healthReport.getAppointment().getAppointmentId());
			appointment.setStatus("Visited");
			healthReportService.updateAppointment(appointment);
			logger.info("Appointment status updated to 'Visited' for appointmentId: {}",
			healthReport.getAppointment().getAppointmentId());

			ByteArrayOutputStream pdfOutputStream = generateHealthReportPDF(healthReport);
			ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());

			String emailSubject = "Doctor Visit Confirmation";
			String emailText = String.format("<html>" + "<body>" + "<p>Dear %s,</p>"
					+ "<p>Your appointment with <strong>Dr. %s</strong> has been completed.</p>"
					+ "<p>Please find the attached health report for your reference.</p>" + "<br>"
					+ "<p>Best regards,<br> NEU Student Healthcare Management System</p>" + "</body>" + "</html>",
					appointment.getStudent().getName(), appointment.getDoctor().getName());

			String patientName = appointment.getStudent().getName().replaceAll(" ", "_");
			String fileName = patientName + "_HealthReport.pdf";

			String studentEmail = appointment.getStudent().getEmailId();
			System.out.println(studentEmail);
			emailService.sendEmailWithAttachment(studentEmail, emailSubject, emailText, pdfInputStream, fileName);

			model.addAttribute("appointment", appointment);
			model.addAttribute("successMessage", "Health report created and Email sent.");
			return "healthReport";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occurred while saving health report for appointmentId: {}",
					healthReport.getAppointment().getAppointmentId(), e);
			model.addAttribute("message", "An error occurred while saving the health report.");
			return "healthReport";
		}
	}

	private ByteArrayOutputStream generateHealthReportPDF(HealthReport healthReport)
			throws DocumentException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();

		ClassPathResource logoResource = new ClassPathResource("static/images/neulogin.jpg");
		if (logoResource.exists()) {
			Image logo = Image.getInstance(logoResource.getURL());
			logo.setAlignment(Element.ALIGN_CENTER);
			document.add(logo);
		}

		LineSeparator separator = new LineSeparator();
		separator.setOffset(-2);
		document.add(new Chunk(separator));
		document.add(new Paragraph("\n"));

		Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Paragraph title = new Paragraph("NEU Healthcare Health Report", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\n"));

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

		Paragraph patientName = new Paragraph();
		patientName.add(new Chunk("Patient Name: ", boldFont));
		patientName.add(new Chunk(healthReport.getAppointment().getStudent().getName(), normalFont));
		document.add(patientName);

		Paragraph patientNeuid = new Paragraph();
		patientNeuid.add(new Chunk("Patient NEU ID: ", boldFont));
		patientNeuid.add(new Chunk(String.valueOf(healthReport.getAppointment().getStudent().getNeuid()), normalFont));
		document.add(patientNeuid);

		document.add(new Paragraph("\n"));

		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		float[] columnWidths = { 1f, 1f, 1.5f, 1.5f, 2f, 2f, 1.5f, 1.5f };
		table.setWidths(columnWidths);

		Font headFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
		String[] headers = { "Record ID", "Appointment ID", "Doctor Name", "Reason for visit", "Diagnosis",
				"Prescription", "Appointment Status", "Visited Date" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell);
		}

		Font cellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
		PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(healthReport.getRecordId()), cellFont));
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(
				new Phrase(String.valueOf(healthReport.getAppointment().getAppointmentId()), cellFont));
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase(healthReport.getAppointment().getDoctor().getName(), cellFont));
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Phrase(healthReport.getAppointment().getReasonForVisit(), cellFont));
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell4);

		PdfPCell cell5 = new PdfPCell(new Phrase(healthReport.getDiagnosis(), cellFont));
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell5);

		PdfPCell cell6 = new PdfPCell(new Phrase(healthReport.getPrescription(), cellFont));
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell6);

		PdfPCell cell7 = new PdfPCell(new Phrase(healthReport.getAppointment().getStatus(), cellFont));
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell7);

		PdfPCell cell8 = new PdfPCell(
				new Phrase(String.valueOf(healthReport.getAppointment().getAppointmentDate()), cellFont));
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell8);

		document.add(table);

		document.close();
		return outputStream;
	}

	@GetMapping("/patient/viewHealthHistory")
	@Operation(summary = "View Health History", description = "Displays the health history of the logged-in user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully fetched health history"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public String viewHealthHistory(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size, HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginuser");
		if (loginUser == null) {
			return "redirect:/login.jsp";
		}

		Pageable pageable = PageRequest.of(page, size);
		try {
			Page<HealthReport> healthReportPage = healthReportService.getHealthReportsByUserId(loginUser.getNeuid(),
					pageable);
			model.addAttribute("healthReports", healthReportPage.getContent());
			model.addAttribute("currentPage", healthReportPage.getNumber());
			model.addAttribute("totalPages", healthReportPage.getTotalPages());
			model.addAttribute("size", size);
			logger.info("Health history fetched successfully for userId: {}", loginUser.getNeuid());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occurred while fetching health history for userId: {}", loginUser.getNeuid(), e);
			model.addAttribute("message", "An error occurred while fetching health history.");
		}
		return "viewHealthHistory";
	}

	@GetMapping("/patient/downloadAllHealthReport")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<InputStreamResource> downloadAllHealthReports(HttpSession session)
			throws DocumentException, IOException {
		User loginUser = (User) session.getAttribute("loginuser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		List<HealthReport> healthReports = healthReportService.getHealthReportsByUserId(loginUser.getNeuid());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();

		ClassPathResource logoResource = new ClassPathResource("static/images/neulogin.jpg");
		if (logoResource.exists()) {
			Image logo = Image.getInstance(logoResource.getURL());
			logo.setAlignment(Element.ALIGN_CENTER);
			document.add(logo);
		}

		LineSeparator separator = new LineSeparator();
		separator.setOffset(-2);
		document.add(new Chunk(separator));
		document.add(new Paragraph("\n"));

		Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Paragraph title = new Paragraph("NEU Healthcare History Report", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(new Paragraph("\n"));
		document.add(new Paragraph("\n"));

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

		Paragraph patientName = new Paragraph();
		patientName.add(new Chunk("Patient Name: ", boldFont));
		patientName.add(new Chunk(loginUser.getName(), normalFont));
		document.add(patientName);

		Paragraph patientNeuid = new Paragraph();
		patientNeuid.add(new Chunk("Patient NEU ID: ", boldFont));
		patientNeuid.add(new Chunk(String.valueOf(loginUser.getNeuid()), normalFont));
		document.add(patientNeuid);

		Paragraph generatedDate = new Paragraph();
		generatedDate.add(new Chunk("Generated Date: ", boldFont));
		generatedDate.add(new Chunk(LocalDate.now().toString(), normalFont));
		document.add(generatedDate);

		document.add(new Paragraph("\n"));

		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		float[] columnWidths = { 1f, 1f, 1.5f, 1.5f, 2f, 2f, 1.5f, 1.5f };
		table.setWidths(columnWidths);

		Font headFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
		String[] headers = { "Record ID", "Appointment ID", "Doctor Name", "Reason for visit", "Diagnosis",
				"Prescription", "Appointment Status", "Visited Date" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell);
		}

		Font cellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
		for (HealthReport report : healthReports) {
			PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(report.getRecordId()), cellFont));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(
					new Phrase(String.valueOf(report.getAppointment().getAppointmentId()), cellFont));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell2);

			PdfPCell cell3 = new PdfPCell(new Phrase(report.getAppointment().getDoctor().getName(), cellFont));
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell3);

			PdfPCell cell4 = new PdfPCell(new Phrase(report.getAppointment().getReasonForVisit(), cellFont));
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell4);

			PdfPCell cell5 = new PdfPCell(new Phrase(report.getDiagnosis(), cellFont));
			cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell5);

			PdfPCell cell6 = new PdfPCell(new Phrase(report.getPrescription(), cellFont));
			cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell6);

			PdfPCell cell7 = new PdfPCell(new Phrase(report.getAppointment().getStatus(), cellFont));
			cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell7);

			PdfPCell cell8 = new PdfPCell(
					new Phrase(String.valueOf(report.getAppointment().getAppointmentDate()), cellFont));
			cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell8);
		}

		document.add(table);

		document.close();
		logger.info("PDF generated successfully for userId: {}", loginUser.getNeuid());

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		String filename = loginUser.getName().replaceAll("\\s+", "_") + "_AllHealthReports.pdf";
		HttpHeaders headersHttp = new HttpHeaders();
		headersHttp.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

		return ResponseEntity.ok().headers(headersHttp).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(inputStream));
	}
}
