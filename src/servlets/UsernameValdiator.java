/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Owner
 */
@WebServlet(name = "UsernameValidator", urlPatterns = {"/UsernameValidator"})
public class UsernameValidator extends HttpServlet {
    
    
    private void reportError(HttpServletRequest request, String errorMessage)
    {
        if(request != null)
        {
            HttpSession session = request.getSession(true);
            session.setAttribute("errorMessage", errorMessage);
        }
        else
        {
            System.err.println("Could not report error: No HTTP request was " +
                    "provided.");
        }
    }

    private boolean validateUsername(HttpServletRequest request,
            HttpServletResponse response)
    {
        String newUsername = "";
        if(request != null && response != null)
        {
            newUsername = (String)request.getParameter("username");
            if(newUsername != null && newUsername.length() > 0)
            {
                if(newUsername.length() > 9)
                {
                    reportError(request, "Username is longer than 9 " +
                            "characters: " + newUsername);
                    return false;
                }
                if(!newUsername.matches("^[a-zA-Z0-9]+$"))
                {
                    Pattern badCharRegex =
                            Pattern.compile("([^a-zA-Z0-9]+)");
                    Matcher badCharMatcher =
                            badCharRegex.matcher(newUsername);
                    if(badCharMatcher.find())
                    {
                        reportError(request, "Invalid character found "
                                + "at index " + badCharMatcher.start() +
                                ": " + newUsername);
                        return false;
                    }
                }
                return true;
            }
        }
        else
        {
            System.err.println("Could not report error: No HTTP response was" +
                    " provided.");
        }
        return false;
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
        }
        catch(IOException ioExc)
        {
            System.out.println("IO Exception: " + ioExc.getMessage());
        }
        if(out != null) {
            if(!validateUsername(request, response))
            {
                response.sendRedirect("error.jsp");
                return;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
