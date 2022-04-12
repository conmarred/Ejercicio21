package vista;

import java.util.Scanner;

import entidad.Usuario;
import negocio.GestorUsuario;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean fin = false;
		GestorUsuario gp = new GestorUsuario();
		
		do {
			menu();
			int opcion = sc.nextInt();
			switch (opcion) {
			case 1:
				System.out.println("Introduzca los datos del usuario (EMAIL/PASSWORD)");
				String email = sc.next();
				String password = sc.next();
				
				Usuario usuario = new Usuario();
				usuario.setEmail(email);
				usuario.setPassword(password);
				
				int alta = gp.alta(usuario);
				if(alta == 0) {
					System.out.println("Usuario dado de alta con éxito");
				}else if(alta == 1) {
					System.out.println("Error de conexión con la BBDD");
				}else if(alta == 2){
					System.out.println("El usuario tiene menos de tres carateres");
				}
				break;
			}
			}while(!fin);
			
			System.out.println("Fin de programa");

		}

		private static void menu() {
			System.out.println("Elija una opción:");
			System.out.println("1- Alta de usuario");
		}


}
