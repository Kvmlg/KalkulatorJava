package cafe.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import cafe.entities.Order;
import cafe.entities.Product;
import cafe.entities.ShopingCart;
import cafe.entities.User;


//DAO - Data Access Object for Person entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class OrderDAO {

	private User loaded = null;
	private User u = new User();
	// Dependency injection (no setter method is needed)
	@PersistenceContext
	protected EntityManager em;
	
	@Inject
	FacesContext context;

	public void create(Order order) {
		em.persist(order);
	}

	public Order merge(Order order) {
		return em.merge(order);
	}

	public void remove(Order order) {
		em.remove(em.merge(order));
	}

	public Order find(Object id) {
		return em.find(Order.class, id);
	}

	public List<Order> getList(Map<String, Object> searchParams) {
		List<Order> list = null;

		// 1. Build query string with parameters
		String select = "select p ";
		String from = "FROM Product p ";
		String where = "where p.category = :item";

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
	
	public List<Order> getFullList() {
		List<Order> list = null;

		Query query = em.createQuery("SELECT p FROM Product p");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Order> getAllByUserId() {
		List<Order> orderItems = null;
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		loaded = (User) session.getAttribute("user");
		u=loaded;
        Query query = em.createQuery("SELECT o FROM Order o WHERE o.user = :userId");
        query.setParameter("userId", u);
        
		try {
			orderItems = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}


        return orderItems;
    }
	
	public List<Order> getAllByAdmin() {
		List<Order> orderItems = null;
        Query query = em.createQuery("SELECT o FROM Order o");
        
		try {
			orderItems = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}


        return orderItems;
    }

}
