import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
            
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        UserDao usDao = new UserDao();
        
        try
        {           

             UserBean user = new UserBean();
             user.setUsername(request.getParameter("un"));

             // Hashing della password fornita dall'utente prima del confronto
             String password = request.getParameter("pw");
             String hashedPassword = hashPassword(password);
             user.setPassword(hashedPassword);

             user = usDao.doRetrieve(request.getParameter("un"), hashedPassword);
                            
             String checkout = request.getParameter("checkout");
             
             if (user.isValid())
             {
                    
                  HttpSession session = request.getSession(true);       
                  session.setAttribute("currentSessionUser",user); 
                  if(checkout!=null)
                      response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                      
                  else
                      response.sendRedirect(request.getContextPath() + "/Home.jsp");
             }
                    
             else 
                  response.sendRedirect(request.getContextPath() +"/Login.jsp?action=error"); //error page 
        } 
                
                
        catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
          }

    private String hashPassword(String password) {
        // Implementa qui l'algoritmo di crittografia per la password (es. SHA-256, bcrypt, etc.)
        // Assicurati di utilizzare un algoritmo di hash sicuro e di salare la password se necessario
        // Per esempio:
        // String hashedPassword = tuaFunzioneDiHash(password);
        return hashedPassword;
    }
}
