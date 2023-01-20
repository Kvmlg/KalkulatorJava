package cafe.items;

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

import cafe.dao.ShopingCartDAO;
import cafe.dao.UserDAO;
import cafe.entities.Product;
import cafe.entities.ShopingCart;
import cafe.entities.User;

@Named
@RequestScoped
public class ShopingCartBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private List<ShopingCart> cartItems;
	private User user = new User();
	private ShopingCart shopingCart = new ShopingCart();
	private Product product = new Product();
	private User loadedd = null;
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	
	@EJB
	ShopingCartDAO shopingCartDAO;
	
	@EJB
	UserDAO userDAO;
	
	@EJB
	UserDAO productDAO;
	
	@Inject
	FacesContext context;
	
	public Product getProduct() {
		return product;
	}
	
	public User getUser() {
		return user;
	}
	
	public ShopingCart getShopingCart() {
		return shopingCart;
	}

    public List<ShopingCart> retrieveCartItems() {
        cartItems = shopingCartDAO.getAllByUserId();
        return cartItems;
    }
    
    public float sumOfCart(List<ShopingCart> cartItems) {
    	float sumPrice=0;
    	for(ShopingCart item : cartItems) {
    		sumPrice+=(item.getQuantity()*item.getProduct().getPrice());
    	}
        return sumPrice;
    }

    
	
	public String addToCart(Product product){
		ShopingCart item = new ShopingCart();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		loadedd = (User) session.getAttribute("user");
		user=loadedd;


		item.setUser(user);
		item.setProduct(product);
		item.setQuantity(1);
		session.setAttribute("product", product);
		ShopingCart itemOfBase = shopingCartDAO.getCart();
	
		
		try {
			if (itemOfBase == null ) {
				// new record
				shopingCartDAO.create(item);
			} else {
				// existing record
				int newqua = itemOfBase.getQuantity();
				newqua++;
				itemOfBase.setQuantity(newqua);
				shopingCartDAO.merge(itemOfBase);
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dodano do koszyka", null));
				

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	public String quantityMinus(ShopingCart shopingCart){
	
		try {
			if (shopingCart == null ) {
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd edycji", null));
			} else {
				int newqua = shopingCart.getQuantity();
				newqua--;
				if(newqua==0) {
					shopingCartDAO.remove(shopingCart);
				}else {
				shopingCart.setQuantity(newqua);
				shopingCartDAO.merge(shopingCart);
				}
			}

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zmiejszono ilość", null));

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
		}

		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String deleteProductFromCart(ShopingCart shopingCart){

		
		try {
			if (shopingCart == null ) {
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd edycji", null));
			} else {
					shopingCartDAO.remove(shopingCart);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usunięto produkt z koszyka", null));
			}

				

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
		}

		return PAGE_STAY_AT_THE_SAME;
	}
	
	public List<ShopingCart> getFullList(){
		return shopingCartDAO.getFullList();
	}

	
}
