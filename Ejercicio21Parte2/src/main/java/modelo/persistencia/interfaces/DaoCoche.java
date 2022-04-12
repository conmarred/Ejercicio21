package modelo.persistencia.interfaces;

import java.util.List;

import modelo.entidad.Coche;

public interface DaoCoche {

	boolean alta(Coche p);
	
	boolean baja(int id);
	
	int modificar(Coche p);
	
	Coche obtener(int id);

	Coche buscaMatricula(String matricula);
	
	List<Coche> listarMarca(String marca);
	
	List<Coche> listarModelo(String modelo);
	
	List<Coche> listar();
}
