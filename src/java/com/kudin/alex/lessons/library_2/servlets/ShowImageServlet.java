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
public class ShowImageServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String imageName = req.getPathInfo().substring(1);
        String[] tokens = imageName.split("\\.");
        byte[] imageBytes = new BooksDAO().getBookImageById(Long.valueOf(tokens[0]));

        try {
            if (imageBytes != null && imageBytes.length > 0) {
                resp.setContentType(getServletContext().getMimeType(imageName));
                resp.setContentLength(imageBytes.length);
                resp.getOutputStream().write(imageBytes);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            Logger.getLogger(ShowImageServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
