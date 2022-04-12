package entidad;

public class Usuario {
	
	String email;
	String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Usuario() {
		super();
	}
	public Usuario(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Usuario [email=" + email + ", password=" + password + "]";
	}
	
	

}
