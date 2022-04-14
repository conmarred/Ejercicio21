package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.DaoUsuario;
import dao.DaoUsuarioMySQL;
import entidad.Usuario;



/**
 * Servlet implementation class principal
 */

//http://localhost:8080/Ejercicio21/usuarios/login?email=concemar&password=12345

@WebServlet("/usuarios/login")
public class validarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DaoUsuario daoUsuario = null ;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		daoUsuario = new DaoUsuarioMySQL();
		boolean validado;
		Usuario usuario = new Usuario(request.getParameter("email").toString(), request.getParameter("password").toString());
	
		validado = daoUsuario.autenticado(usuario);
		JSONObject jo = new JSONObject();
		jo.put("validado", validado);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jo.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	}
    
}
