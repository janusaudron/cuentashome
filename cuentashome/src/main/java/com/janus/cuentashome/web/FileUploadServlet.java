package com.janus.cuentashome.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.janus.cuentashome.business.CuentasUtil;

import lombok.extern.java.Log;

@WebServlet(name = "FileUploadServlet", urlPatterns = { "/fileuploadservlet" })
@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
  maxFileSize = 1024 * 1024 * 50,      // 10 MB
  maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
@Log
public class FileUploadServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    log.info("Entro a doPost");

    /* Receive file uploaded to the Servlet from the HTML5 form */
    Part filePart = request.getPart("file");
    //String fileName = filePart.getSubmittedFileName();
    for (Part part : request.getParts()) {
      //part.write(CuentasUtil.rutaArchivo + fileName);
      part.write(CuentasUtil.rutaArchivo);
    }
    //response.getWriter().print("The file uploaded sucessfully.");
    response.sendRedirect("pages/gastosCargue.xhtml");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    log.info("Entro doGet");
  }

}