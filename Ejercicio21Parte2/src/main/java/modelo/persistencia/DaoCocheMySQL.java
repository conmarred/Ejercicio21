package modelo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.entidad.Coche;
import modelo.persistencia.interfaces.DaoCoche;

public class DaoCocheMySQL implements DaoCoche{
	
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
	public boolean alta(Coche c) {
		if(!abrirConexion()){
			return false;
		}
		boolean alta = true;
		
		String query = "insert into coches (MATRICULA,MARCA,MODELO,NUMKILOMETROS) "
				+ " values(?,?,?,?)";
		try {
			
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, c.getMatricula());
			ps.setString(2, c.getMarca());
			ps.setString(3, c.getModelo());
			ps.setInt(4, c.getKilometros());
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0) {
				alta = false;
			}
		} catch (SQLException e) {
			System.out.println("alta -> Error al insertar: " + c);
			alta = false;
			e.printStackTrace();
		} finally{
			cerrarConexion();
		}
		
		return alta;
	}

	@Override
	public boolean baja(int id) {
		if(!abrirConexion()){
			return false;
		}
		
		boolean borrado = true;
		String query = "delete from coches where id = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			
			ps.setInt(1, id);
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0)
				borrado = false;
		} catch (SQLException e) {
			System.out.println("baja -> No se ha podido dar de baja"
					+ " el id " + id);
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		return borrado; 
	}

	@Override
	public int modificar(Coche c) {
		if(!abrirConexion()){
			return 3;
		}
		int modificado = 0;
		String query = "update coches set MATRICULA=?, MARCA=?, "
				+ "MODELO=?, NUMKILOMETROS=? WHERE ID=?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, c.getMatricula());
			ps.setString(2, c.getMarca());
			ps.setString(3, c.getModelo());
			ps.setInt(4, c.getKilometros());
			ps.setInt(5, c.getId());
			
			int numeroFilasAfectadas = ps.executeUpdate();
			if(numeroFilasAfectadas == 0)
				modificado = 1;
			else
				modificado = 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("modificar -> error al modificar el "
					+ " coche " + c);
			modificado = 1;
			e.printStackTrace();
		} finally{
			cerrarConexion();
		}
		
		return modificado;
	}

	@Override
	public Coche obtener(int id) {
		if(!abrirConexion()){
			return null;
		}		
		Coche coche = null;
		
		String query = "select ID,MATRICULA,MODELO,MARCA,NUMKILOMETROS from coches "
				+ "where id = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				coche = new Coche();
				coche.setId(rs.getInt(1));
				coche.setMatricula(rs.getString(2));
				coche.setModelo(rs.getString(3));
				coche.setMarca(rs.getString(4));
				coche.setKilometros(rs.getInt(5));
			}
		} catch (SQLException e) {
			System.out.println("obtener -> error al obtener el "
					+ "coche con id " + id);
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		
		return coche;
	}

	@Override
	public List<Coche> listar() {
		if(!abrirConexion()){
			return null;
		}		
		List<Coche> listarCoches = new ArrayList<>();
		
		String query = "select * from coches";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Coche coche = new Coche();
				coche.setId(rs.getInt(1));
				coche.setMatricula(rs.getString(2));
				coche.setMarca(rs.getString(3));
				coche.setModelo(rs.getString(4));
				coche.setKilometros(rs.getInt(5));
				
				listarCoches.add(coche);
			}
		} catch (SQLException e) {
			System.out.println("listar -> error al obtener los "
					+ "coches");
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		
		return listarCoches;
	}
	
	@Override
	public Coche buscaMatricula(String matricula) {
		if(!abrirConexion()){
			return null;
		}
		Coche coche = null;
		String query = "select ID,MATRICULA,MODELO,MARCA,NUMKILOMETROS from coches "
				+ "where matricula = ?";
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			ps.setString(1, matricula);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Coche coche1 = new Coche();
				coche1.setId(rs.getInt(1));
				coche1.setMatricula(rs.getString(2));
				coche1.setMarca(rs.getString(3));
				coche1.setModelo(rs.getString(4));
				coche1.setKilometros(rs.getInt(5));
				coche = coche1;
			}
		} catch (SQLException e) {
			System.out.println("listar -> error al obtener los "
					+ "coches");
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		return coche;
	}

	@Override
	public List<Coche> listarMarca(String marca) {
		if(!abrirConexion()){
			return null;
		}
		
		String query = "select ID,MATRICULA,MODELO,MARCA,NUMKILOMETROS from coches "
				+ "where marca = ?";
		List<Coche> lsCoche = new ArrayList<Coche>();
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			
			ps.setString(1, marca);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Coche coche1 = new Coche();
				coche1.setId(rs.getInt(1));
				coche1.setMatricula(rs.getString(2));
				coche1.setMarca(rs.getString(3));
				coche1.setModelo(rs.getString(4));
				coche1.setKilometros(rs.getInt(5));
				
				lsCoche.add(coche1);
			}
		} catch (SQLException e) {
			System.out.println("listar -> error al obtener los "
					+ "coches");
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		return lsCoche;
	}

	@Override
	public List<Coche> listarModelo(String modelo) {
		if(!abrirConexion()){
			return null;
		}
		
		String query = "select ID,MATRICULA,MODELO,MARCA,NUMKILOMETROS from coches "
				+ "where modelo = ?";
		List<Coche> lsCoche = new ArrayList<Coche>();
		try {
			PreparedStatement ps = conexion.prepareStatement(query);
			
			ps.setString(1, modelo);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Coche coche1 = new Coche();
				coche1.setId(rs.getInt(1));
				coche1.setMatricula(rs.getString(2));
				coche1.setMarca(rs.getString(3));
				coche1.setModelo(rs.getString(4));
				coche1.setKilometros(rs.getInt(5));
				
				lsCoche.add(coche1);
			}
		} catch (SQLException e) {
			System.out.println("listar -> error al obtener los "
					+ "coches");
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}
		
		return lsCoche;
	}

	

}
