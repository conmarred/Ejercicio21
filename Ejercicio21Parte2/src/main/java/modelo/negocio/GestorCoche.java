package modelo.negocio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import modelo.entidad.Coche;
import modelo.persistencia.DaoCocheMySQL;
import modelo.persistencia.interfaces.DaoCoche;

public class GestorCoche {
	private DaoCoche daoCoche = new DaoCocheMySQL();
	
	
	public int alta(Coche c){
		if(c.getMatricula().length()==7 && c.getKilometros()>0) {
			boolean alta = daoCoche.alta(c);
			if(alta) {
				return 0;
			}else {
				return 1;
			}
		}else {
			return 2;
		}
	}
	
	public boolean baja(int id){
		boolean baja = daoCoche.baja(id);
		return baja;
	}
	
	
	public int modificar(Coche c){
		//aplicamos la regla de negocio
		if(c.getMatricula().length()==7 && c.getKilometros()>0) {
			int modificada = daoCoche.modificar(c);
			return modificada;
		
		}else {
			return 0;
		}
	}
	
	public Coche obtener(int id){
		Coche coche = daoCoche.obtener(id);
		return coche;
	}
	
	public List<Coche> listar(){
		List<Coche> listarCoches = daoCoche.listar();
		return listarCoches;
	}	
	
	public Coche buscaMatricula(String Matricula){
		Coche c1 = daoCoche.buscaMatricula(Matricula);
		return c1;
	}
	
	public List<Coche> listarMarca(String Marca){
		List<Coche> listaMarca = daoCoche.listarMarca(Marca);
		return listaMarca;
	}
	
	public List<Coche> listarModelo(String Modelo){
		List<Coche> listaModelo = daoCoche.listarModelo(Modelo);
		return listaModelo;
	}
	
	public void creaFicheroPDF() {
	Document doc = new Document();
	List<Coche> coches = daoCoche.listar();
	PdfWriter pdfw = null;
	try {
		pdfw = PdfWriter.getInstance(doc, new FileOutputStream("../10_Maven/documentos/coches.pdf"));
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (DocumentException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	};
	
	pdfw.flush();
	pdfw.close();
	doc.open();
	for(Coche c: coches) {
		try {
			doc.add(new Paragraph(c.toString()));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	doc.close();
		
	}
	
	public void creaFicheroJson() throws IOException {
		JsonFactory factoria= new JsonFactory();
        JsonGenerator generator= factoria.createGenerator(new FileOutputStream("../10_Maven/documentos/coches.json"));
        List<Coche> coches = daoCoche.listar();
        generator.writeStartArray();
        for(Coche c: coches) {
        	generator.writeStartObject();
            generator.writeNumberField("id", c.getId());
            generator.writeStringField("matricula", c.getMatricula());
            generator.writeStringField("marca", c.getMarca());
            generator.writeStringField("modelo", c.getModelo());
            generator.writeNumberField("numeroKilometros", c.getKilometros());
            generator.writeEndObject();
            
        }
        generator.writeEndArray();
        generator.flush();
        
	}
	
	public boolean validar(String email, String password) {
		try {
			//Se utiliza principalmente para consumir servicios REST
			//Tambien podemos consumir cualquier tipo de servidor web
			HttpRequest request = HttpRequest.newBuilder()
					  .uri(new URI("http://localhost:8080/Ejercicio21/usuarios/login?email="+email+"&password="
							  +password))
					  .GET()
					  .build();
			
			HttpClient client = HttpClient.newHttpClient();
			
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			JSONObject jo = new JSONObject(response.body());
			return jo.getBoolean("validado");
		
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	
}
