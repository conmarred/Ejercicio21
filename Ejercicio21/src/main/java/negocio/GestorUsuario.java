package negocio;

import dao.DaoUsuario;
import dao.DaoUsuarioMySQL;
import entidad.Usuario;

public class GestorUsuario {
	private DaoUsuario daoUsuario = new DaoUsuarioMySQL();

	
	
	public int alta(Usuario u){
		if(u.getEmail()!=null && u.getPassword()!=null) {
			boolean alta = daoUsuario.alta(u);
			if(alta) {
				return 0;
			}else {
				return 1;
			}
		}else {
			return 2;
		}
	}
	
}
