package cafe.items;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import cafe.dao.ShopingCartDAO;
import cafe.dao.UserDAO;
import cafe.entities.Product;
import cafe.entities.User;

@Named
@RequestScoped
public class UserBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_LOGIN = "/pages/login";
	private static final String PAGE_Admin = "adminPage.xhtml";
	private String name;
	private String surname;
	private String email;
	private String password;
	private String addres;
	private String city;
	private String phone;
	private String role;
	private User user = new User();
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	
	@EJB
	ShopingCartDAO shopingCartDAO;
	
	@EJB
	UserDAO userDAO;
	
	
	@Inject
	FacesContext context;
	

	public User getUser() {
		return user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
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
	public String getAddres() {
		return addres;
	}
	public void setAddres(String addres) {
		this.addres = addres;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public void onLoad() throws IOException {
		user = (User) flash.get("user");
		HttpSession session = (HttpSession) extcontext.getSession(true);
		session.setAttribute("userid", user.getId());
	}
	
	 public void toEditUser(User user) throws IOException {
		 	flash.put("user", user);
		 	context.getExternalContext().redirect("adminEditUser.xhtml");
	 }
	 
	
	public List<User> getList(){
		List<User> list = null;
		list = userDAO.getFullList();
        return list;		
	}

	public String doRegister(){
		User newuser = new User();
		
		newuser.setName(name);
		newuser.setSurname(surname);
		newuser.setEmail(email);
		newuser.setPassword(password);
		newuser.setAddres(addres);
		newuser.setCity(city);
		newuser.setPhone(phone);
		newuser.setRole("user");
		
		try {
				userDAO.create(newuser);

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		
		
		return PAGE_LOGIN;
	}
	
	public String saveChanges() {
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		user.setId((int)session.getAttribute("userid"));
		try {
			userDAO.merge(user);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dokonano zmian", null));

	} catch (Exception e) {
		e.printStackTrace();
		context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
		return PAGE_STAY_AT_THE_SAME;
	}
		
	    return PAGE_Admin;
	}
	
	public void deleteUser(User user) {
		try {
			if (user == null ) {
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd edycji", null));
			} else {
					userDAO.remove(user);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usunięto uzytkownika", null));
			}

				

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas usuwania uzytkownika", null));
		}


 }
	
}
