package com.example.pdfreader.service;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pdfreader.model.PdfDataResponse;
import com.example.pdfreader.util.LlmClient;

@Service
public class PdfParserService {

    @Value("${llm.api.key}")
    private String llmApiKey;

    @Value("${llm.api.url}")
    private String llmApiUrl;
    
    public PdfDataResponse parsePdf(MultipartFile file) {
        String pdfText = extractTextFromPdf(file);
        String prompt = "Extract the following details from the text: name, email, opening balance, closing balance. " +
                "Return the result as JSON with keys 'name', 'email', 'openingBalance', 'closingBalance'.\n\nText:\n" + pdfText;
        String llmResponse = LlmClient.callLlmApi(prompt, llmApiKey, llmApiUrl);
        return PdfDataResponse.fromJson(llmResponse);
    }
    
    private String extractTextFromPdf(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting text from PDF: " + e.getMessage(), e);
        }
    }
    
    public String createPassword(String firstname, String dateOfBirth, String accountType) {
        String dobFormatted = dateOfBirth.replaceAll("-", "");
        String accountInitials = accountType.substring(0, Math.min(3, accountType.length())).toUpperCase();
        int randomNum = (int)(Math.random() * 1000);
        return firstname + dobFormatted + accountInitials + randomNum;
    }
}
