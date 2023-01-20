package cafe.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;


import cafe.entities.ShopingCart;
import cafe.entities.User;
import cafe.entities.Product;


//DAO - Data Access Object for Person entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class ShopingCartDAO {
	private User loaded = null;
	private Product loadedd = null;
	private User u = new User();
	private Product product= new Product();


	// Dependency injection (no setter method is needed)
	@PersistenceContext
	protected EntityManager em;
	
	@Inject
	FacesContext context;
	
	@EJB
	UserDAO userDAO;

	public void create(ShopingCart shopingCart) {
		em.persist(shopingCart);
	}

	public ShopingCart merge(ShopingCart shopingCart) {
		return em.merge(shopingCart);
	}

	public void remove(ShopingCart shopingCart) {
		em.remove(em.merge(shopingCart));
	}

	public ShopingCart find(Object id) {
		return em.find(ShopingCart.class, id);
	}
	
	public User getUser() {
		return u;
	}

	public List<ShopingCart> getList(Map<String, Object> searchParams) {
		List<ShopingCart> list = null;

		// 1. Build query string with parameters
		String select = "SELECT s ";
		String from = "FROM ShopingCart s ";
		String where = "where s.category = :item";

		String item = (String) searchParams.get("item");
		
		Query query = em.createQuery(select + from + where);
		
			query.setParameter("item", item);


		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return list;
	}
	
	public ShopingCart getCart() {
		

		ShopingCart item = new ShopingCart();
		
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		loaded = (User) session.getAttribute("user");
		u=loaded;
		
		loadedd = (Product) session.getAttribute("product");
		product = loadedd;


		Query query = em.createQuery("SELECT s FROM ShopingCart s WHERE s.user = :user_id AND s.product = :products_id");
	         	query.setParameter("user_id", u);
		        query.setParameter("products_id", product);
		         
		try {         
			item=(ShopingCart) query.getSingleResult();
			
		} catch (Exception e) {	
			 item = null;
		}
		return item;
	}
	
	public List<ShopingCart> getFullList() {
		List<ShopingCart> list = null;

		Query query = em.createQuery("SELECT s FROM ShopingCart s");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<ShopingCart> getAllByUserId() {
		List<ShopingCart> cartItems = null;
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		loaded = (User) session.getAttribute("user");
		u=loaded;
        Query query = em.createQuery("SELECT s FROM ShopingCart s WHERE s.user = :userId");
        query.setParameter("userId", u);
        
		try {
			 cartItems = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
        

        return cartItems;
    }

}
