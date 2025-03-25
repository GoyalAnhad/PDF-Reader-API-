package com.example.pdfreader.controller;

import com.example.pdfreader.model.PdfDataResponse;
import com.example.pdfreader.service.PdfParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class PdfController {

    @Autowired
    private PdfParserService pdfParserService;

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PdfDataResponse parsePdf(@RequestParam("file") MultipartFile file) {
        return pdfParserService.parsePdf(file);
    }
    
    @PostMapping("/createPassword")
    public String createPassword(@RequestParam("firstname") String firstname,
                                 @RequestParam("dateOfBirth") String dateOfBirth,
                                 @RequestParam("accountType") String accountType) {
        return pdfParserService.createPassword(firstname, dateOfBirth, accountType);
    }
}
