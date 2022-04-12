package dao;

import entidad.Usuario;

public interface DaoUsuario {
	
	boolean autenticado(Usuario u);

	boolean alta(Usuario u);

}
