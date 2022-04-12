package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entidad.Usuario;

public class DaoUsuarioMySQL implements DaoUsuario{

	
	static{
		try {
			//Esta sentencia carga del jar que hemos importado
			//una clase que se llama Driver que esta en el paqueta
			//com.mysql.jdbc. Esta clase se carga previamente en
			//java para más adelante ser llamada
			//Esto solo es necesario si utilizamos una versión java anterior
			//a la 1.7 ya que desde esta versión java busca automaticamente 
			//los drivers
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver cargado");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver NO cargado");
			e.printStackTrace();
		}}
private Connection conexion;
	
	public boolean abrirConexion(){
		String url = "jdbc:mysql://localhost:3306/bbdd";
		String usuario = "root";
		String password = "";
		try {
			conexion = DriverManager.getConnection(url,usuario,password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean cerrarConexion(){
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean alta(Usuario u) {
		if(!abrirConexion()){
			return false;
		}
		boolean alta = true;
		
		String query = "insert into usuarios (EMAIL,PASSWORD) "
				+ " values(?,?)";
		try {
			
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, u.getEmail());
			ps.setString(2, u.getPassword());
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0) {
				alta = false;
			}
		} catch (SQLException e) {
			System.out.println("alta -> Error al insertar: " + u);
			alta = false;
			e.printStackTrace();
		} finally{
			cerrarConexion();
		}
		
		return alta;
	}

	@Override
	public boolean autenticado(Usuario u) {
		if(!abrirConexion()){
			return false;
		}
		boolean validado = false;
		
		String query = "select EMAIL,PASSWORD from usuarios "
				+ " where email = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, u.getEmail());
			ResultSet result = ps.executeQuery();
			Usuario usuario1 = new Usuario();
			
			while (result.next()) {
				usuario1.setEmail(result.getString(1));
				usuario1.setPassword(result.getString(2));
			}
			if(u.getPassword().equals(usuario1.getPassword())) {
				validado = true;
			}
			}catch (SQLException e) {
				System.out.println("autenticado -> Error al buscar el usuario");
				e.printStackTrace();
			} finally{
				cerrarConexion();
			}
			
		return validado;
	}

}
