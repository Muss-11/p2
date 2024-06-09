package it.unisa.control;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.*;

/**
 * Servlet implementation class RegistraziomeServlet
 */
@WebServlet("/Registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        UserDao dao = new UserDao();
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String dataNascita = request.getParameter("nascita");
        String username = request.getParameter("us");
        String password = request.getParameter("pw");

        // Hashing della password
        String hashedPassword = hashPassword(password);
        
        String[] parti = dataNascita.split("-");
        dataNascita = parti[2] + "-" + parti[1] + "-" + parti[0];
        
        try {
            
            UserBean user = new UserBean();
            user.setNome(nome);
            user.setCognome(cognome);
            user.setEmail(email);
            user.setDataDiNascita(Date.valueOf(dataNascita));
            user.setUsername(username);
            user.setPassword(hashedPassword); // Salva la password crittografata
            user.setAmministratore(false);
            user.setCap(null);
            user.setIndirizzo(null);
            user.setCartaDiCredito(null);
            dao.doSave(user);
            
        } catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
                
        response.sendRedirect(request.getContextPath() + "/Home.jsp");
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        // Usiamo SHA-256 per hashare la password
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(password.getBytes());
        
        // Converto l'array di byte in una stringa esadecimale
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}