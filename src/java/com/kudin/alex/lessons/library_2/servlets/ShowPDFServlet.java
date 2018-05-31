package com.kudin.alex.lessons.library_2.servlets;

import com.kudin.alex.lessons.library_2.daos.BooksDAO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by KUDIN ALEKSANDR on 25.08.2017.
 */
public class ShowPDFServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getPathInfo().substring(1);
        
        byte[] pdfBytes = new BooksDAO().getBookInPDFById(Long.valueOf(id));

        try {
            if (pdfBytes != null && pdfBytes.length > 0) {
                resp.setContentType("application/pdf");
                resp.setContentLength(pdfBytes.length);
                resp.getOutputStream().write(pdfBytes);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            Logger.getLogger(ShowPDFServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
