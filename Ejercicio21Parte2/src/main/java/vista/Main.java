package vista;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import modelo.entidad.Coche;
import modelo.negocio.GestorCoche;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		boolean fin = false;
		GestorCoche gp = new GestorCoche();
		
		String email;
		String password;
		boolean validado = false;
		int cont = 0;
		
		do {
			
			System.out.println("Email");
			email=sc.next();
			System.out.println("Password");
			password=sc.next();
			cont++;
			validado = validar(email,password);
			if(validado && cont <3) {
				System.out.println("Identificacion correcta");
			}
			}while (!validado && cont<3);
		
			while(!fin && validado) {
			menu();
			int opcion = sc.nextInt();
			switch (opcion) {
			case 1:
				System.out.println("Introduzca los datos del coche (MATRICULA/MARCA/MODELO/NUMKILOMETROS)");
				String matricula = sc.next();
				String marca = sc.next();
				String modelo = sc.next();
				int km = sc.nextInt();
				
				Coche coche = new Coche();
				coche.setMatricula(matricula);
				coche.setMarca(marca);
				coche.setModelo(modelo);
				coche.setKilometros(km);
				
				int alta = gp.alta(coche);
				if(alta == 0) {
					System.out.println("Coche dado de alta con éxito");
				}else if(alta == 1) {
					System.out.println("Error de conexión con la BBDD");
				}else if(alta == 2){
					System.out.println("El usuario tiene menos de tres carateres");
				}
				break;
				
				
			case 2:
				System.out.println("Introduzca el Id del coche que desea eliminar");
				int id = sc.nextInt();
				boolean baja = gp.baja(id);
				if(baja) {
					System.out.println("Coche dado de baja con éxito");
				}else {
					System.out.println("No se ha eliminado el coche con id: " + id);
				}
				
				break;
				
			case 3:
				System.out.println("Introduzca los datos del coche a modificar (ID/MATRICULA/MARCA/MODELO/NUMKILOMETROS)");
				int id1 = sc.nextInt();
				String matricula1 = sc.next();
				String marca1 = sc.next();
				String modelo1 = sc.next();
				int km1 = sc.nextInt();
				
				Coche coche1 = new Coche(id1, matricula1, marca1, modelo1, km1);
				int modificar = gp.modificar(coche1);
				
				if(modificar==0) {
					System.out.println("Coche modificado con éxito");
				}else {
					System.out.println("El coche no se ha podido modificar");
				}
				break;
				
			case 4:
				System.out.println("Introduzca el id del coche que quiere obtener");
				int id2 = sc.nextInt();
				Coche coche2 = gp.obtener(id2);
				System.out.println("El coche con id:" + id2 + " es: " +coche2.toString());
				break;
			//marca
				
			case 5:
				System.out.println("Introduzca la matricula del coche que quiere obtener");
				String matricula2 = sc.next();
				Coche coche4 = gp.buscaMatricula(matricula2);
				if(coche4!=null) {
					System.out.println("El coche con la matricula: " + matricula2 + " es:");
					System.out.println("\n " +coche4.toString());
				}else {
					System.out.println("No existe coche con la matricula: " + matricula2);
				}
				break;
			case 6:
				System.out.println("Introduzca la marca del coche que quiere obtener");
				String marca2 = sc.next();
				List<Coche> lsCoche1 = gp.listarMarca(marca2);
				if(lsCoche1!=null) {
					System.out.println("Los coches con la marca: " + marca2 + " son los siguieentes:");
					for(int i =0; i<lsCoche1.size(); i++) {
						System.out.println(lsCoche1.get(i).toString());
					}
				}else {
					System.out.println("No existen coches de esa marca");
				}
				break;
			//modelo
			case 7:
				System.out.println("Introduzca el modelo del coche que quiere obtener");
				String modelo2 = sc.next();
				List<Coche> lsCoche2 = gp.listarModelo(modelo2);
				if(lsCoche2!=null) {
					System.out.println("Los coches con la marca: " + modelo2 + " son los siguieentes:");
					for(int i =0; i<lsCoche2.size(); i++) {
						System.out.println(lsCoche2.get(i).toString());
					}
				}else {
					System.out.println("No existen coches de esa marca");
				}
				break;
			case 8:
				List<Coche> lsCoches = gp.listar();
				if(lsCoches!=null) {
					System.out.println("El listado de coches es el siguiente: ");
					for(int i = 0; i<lsCoches.size(); i++) {
					System.out.println("\n " + lsCoches.get(i).toString());
					}
				}else {
					System.out.println("No existe ningun coche");
				}
				
				break;
				
			case 10:
				List<Coche> coches1 = gp.listar();
				if(coches1!=null) {
					try {
						gp.creaFicheroJson();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					System.out.println("No existe ningún coche");
				}
				System.out.println("Exportado JSON");
				break;
			case 11:
				List<Coche> coches = gp.listar();
				if(coches!=null) {
					gp.creaFicheroPDF();
				}else {
					System.out.println("No existe ningún coche");
				}
				System.out.println("Exportado PDF");
				break;
			case 9:
				fin = true;
				break;
			}
		}//while(!fin);
		
		System.out.println("Fin de programa");

	}
	
	private static boolean validar(String email, String password) {
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

	private static void menu() {
		System.out.println("Elija una opción:");
		System.out.println("1- Alta de coche");
		System.out.println("2 -Eliminar coche por id");
		System.out.println("3- Modificar coche por id");
		System.out.println("4- buscar coche por id");
		System.out.println("5- buscar coche por matricula");
		System.out.println("6- buscar coches por marca");
		System.out.println("7- buscar coches por modelo");
		System.out.println("8- listar todos los coches");
		System.out.println("9- Salir del programa");
		System.out.println("10 - Exportar los coches a un fichero en formato JSON");
		System.out.println("11 - Exportar los coches a un fichero PDF");
	}

}
