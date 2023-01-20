package cafe.items;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import cafe.dao.OrderDAO;
import cafe.dao.OrderProductDAO;
import cafe.dao.ShopingCartDAO;
import cafe.dao.UserDAO;
import cafe.entities.Order;
import cafe.entities.Orderproduct;
import cafe.entities.ShopingCart;
import cafe.entities.User;

@Named
@RequestScoped
public class OrderBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_Admin = "adminPage.xhtml";
	private User user = new User();
	private User loadedd = null;
	private List<ShopingCart> cartItems;
	private List<Order> orderItems;
	private List<Orderproduct> orderProds;
	private Order order = new Order();
	private Order loaded = null;
	private int quantity;
	private List<Order> filteredOrders;
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	ShopingCartDAO shopingCartDAO;
	
	@EJB
	UserDAO userDAO;

	@EJB
	OrderDAO orderDAO;
	
	@EJB
	UserDAO productDAO;
	
	@EJB
	OrderProductDAO orderProductsDAO;
	
	@Inject
	FacesContext context;
	
	public User getUser() {
		return user;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public int getQuantity() {
	    return quantity;
	}

	public void setQuantity(int quantity) {
	    this.quantity = quantity;
	}
	
    public List<Order> getFilteredOrders() {
        return filteredOrders;
    }
 
    public void setFilteredOrders(List<Order> filteredOrders) {
        this.filteredOrders = filteredOrders;
    }

	public void nullCart()  {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dodaj produkty do koszyka", null));
	}
	
	public void createOrder(float sumPrice) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Order order = new Order();
		Calendar calobj = Calendar.getInstance();
		java.util.Date utilDate = df.parse(df.format(calobj.getTime()));
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		order.setAmount(sumPrice);
		order.setDateOrder(sqlDate);
		

		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		loadedd = (User) session.getAttribute("user");
		user=loadedd;
		
		order.setUser(user);
		
		try {
				orderDAO.create(order);	

		} catch (Exception e) {			
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
		}
		Orderproduct orderProduct = new Orderproduct();
		cartItems = shopingCartDAO.getAllByUserId();
		for(int i = 0; i < cartItems.size(); i++) {
			
			ShopingCart item = cartItems.get(i);
			orderProduct.setOrder(order);
			orderProduct.setProduct(item.getProduct());
			orderProduct.setQuantity(item.getQuantity());
				try {
					orderProductsDAO.create(orderProduct);
					shopingCartDAO.remove(item);
	
				} catch (Exception e) {			
				e.printStackTrace();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			}
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zamówienie zostało złożone!", null));
	}
	
	 public List<Order> showOrder() {  
			orderItems = orderDAO.getAllByUserId();
	        return orderItems;
			
	    
	}
	 
	 public List<Orderproduct> showProducts(Order order) {  
		 	orderProds = orderProductsDAO.getAllByOrderId(order);
	        return orderProds;
 
	}
	
		
	 public List<Order> showOrderAdmin() {  
			orderItems = orderDAO.getAllByAdmin();
	        return orderItems;		
	    
	}
	 
	 public void deleteOrder(Order order) {
			try {
				if (order == null ) {
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd edycji", null));
				} else {
						orderProductsDAO.deleteProductByOrder(order);
						orderDAO.remove(order);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usunięto zmówienie", null));
				}

					

			} catch (Exception e) {
				e.printStackTrace();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas usuwania zamówienia", null));
			}


	 }
	 
	 public void toEditOrder(Order order) throws IOException {
		 	flash.put("edited", order);
		 	context.getExternalContext().redirect("adminEditOrder.xhtml");
	 }
	 
	public void onLoad() throws IOException {
			order = (Order) flash.get("edited");

	}
	
	public String saveChanges() {
	    return PAGE_Admin;
	}
	
}
