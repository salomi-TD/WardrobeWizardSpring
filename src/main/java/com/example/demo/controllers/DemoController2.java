/*
 * package com.example.demo.controllers;
 * 
 * import java.io.*; import java.io.IOException; import java.io.InputStream;
 * import java.util.ArrayList; import java.util.List; import
 * java.util.stream.Collectors;
 * 
 * import org.apache.http.NameValuePair; import
 * org.apache.http.message.BasicNameValuePair; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import com.example.demo.config.ApiConfiguration; import
 * com.example.demo.model.ImageUploadResponse; import
 * com.example.demo.model.JsonResponse; import
 * com.example.demo.model.SimilarProduct; import
 * com.example.demo.service.CaptureAndUploadProcess; import
 * com.fasterxml.jackson.databind.ObjectMapper;
 * 
 * import jakarta.servlet.ServletException; import
 * jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.Part;
 * 
 * @Controller
 * 
 * @RequestMapping("/") public class DemoController2 {
 * 
 * private final CaptureAndUploadProcess captureAndUploadProcess;
 * 
 * @Autowired public DemoController2(CaptureAndUploadProcess
 * captureAndUploadProcess) { this.captureAndUploadProcess =
 * captureAndUploadProcess; }
 * 
 * @Autowired ApiConfiguration ApiConfiguration; private final ObjectMapper
 * objectMapper = new ObjectMapper(); ImageUploadResponse imageKitResponse;
 * 
 * @GetMapping("/test") public String showUploadForm(Model model,
 * HttpServletRequest httpServletRequest) { List<NameValuePair> params = new
 * ArrayList<>(); params.add(new BasicNameValuePair("api_key",
 * ApiConfiguration.getLykdatKey())); params.add(new
 * BasicNameValuePair("image_url", imageKitResponse.getUrl()));
 * 
 * String responseBody =
 * captureAndUploadProcess.performHttpPost(ApiConfiguration.getLykdatUrl(),
 * params);
 * 
 * if (responseBody != null) { try { JsonResponse jsonResponse =
 * objectMapper.readValue(responseBody, JsonResponse.class);
 * 
 * httpServletRequest.setAttribute("Similar_products",
 * Similar_products(jsonResponse)); } catch (IOException e) {
 * e.printStackTrace(); } } return "upload-form"; }
 * 
 * 
 * @GetMapping("/logInPage") public String logInPage() { return "logIn";
 * 
 * }
 * 
 * @GetMapping("/ApparelAI") public String page() { return "resultPage";
 * 
 * }
 * 
 * private static final String UPLOAD_DIRECTORY =
 * "/home/vkumbar/Documents/demo";
 * 
 * @PostMapping("/upload")
 * 
 * @ResponseBody public String uploadImage(HttpServletRequest request, Model
 * model) { try { // Get the uploaded file from the request Part filePart =
 * request.getPart("imageFile");
 * 
 * if (filePart == null || filePart.getSize() == 0) { return
 * "Please select a file to upload."; }
 * 
 * // Extract the file name String fileName = getFileName(filePart);
 * 
 * // Specify the destination directory String filePath = UPLOAD_DIRECTORY +
 * File.separator + fileName; File dest = new File(filePath);
 * 
 * // Copy the file content to the destination try (InputStream fileContent =
 * filePart.getInputStream(); FileOutputStream outputStream = new
 * FileOutputStream(dest)) { byte[] buffer = new byte[4096]; int bytesRead;
 * while ((bytesRead = fileContent.read(buffer)) != -1) {
 * outputStream.write(buffer, 0, bytesRead); } }
 * 
 * // Perform your image upload processing here (e.g., using the
 * `captureAndUploadProcess`)
 * 
 * return "File uploaded successfully."; } catch (IOException | ServletException
 * e) { e.printStackTrace(); return "Error uploading the file."; } }
 * 
 * private String getFileName(Part part) { String contentDisposition =
 * part.getHeader("content-disposition"); String[] tokens =
 * contentDisposition.split(";"); for (String token : tokens) { if
 * (token.trim().startsWith("filename")) { return
 * token.substring(token.indexOf('=') + 1).trim().replace("\"", ""); } } return
 * null; }
 * 
 * 
 * public List<SimilarProduct> Similar_products(JsonResponse jsonResponse) {
 * List<SimilarProduct> Similar_products =
 * jsonResponse.getData().getResult_groups().stream() .flatMap(resultGroup ->
 * resultGroup.getSimilar_products().stream()) // Corrected the usage of flatMap
 * .collect(Collectors.toList()); // Collect the results into a List return
 * Similar_products; }
 * 
 * }
 */