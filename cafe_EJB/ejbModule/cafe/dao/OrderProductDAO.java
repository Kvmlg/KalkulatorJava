package cafe.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import cafe.entities.Order;
import cafe.entities.Orderproduct;
import cafe.entities.ShopingCart;
import cafe.entities.User;


//DAO - Data Access Object for Person entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class OrderProductDAO {


	// Dependency injection (no setter method is needed)
	@PersistenceContext
	protected EntityManager em;

	public void create(Orderproduct orderproduct) {
		em.persist(orderproduct);
	}

	public Orderproduct merge(Orderproduct orderproduct) {
		return em.merge(orderproduct);
	}

	public void remove(Orderproduct orderproduct) {
		em.remove(em.merge(orderproduct));
	}

	public Order find(Object id) {
		return em.find(Order.class, id);
	}

	public List<Orderproduct> getAllByOrderId (Order order) {
		List<Orderproduct> orderProd = null;
        Query query = em.createQuery("SELECT o FROM Orderproduct o WHERE o.order = :order_id");
        query.setParameter("order_id", order);
        
		try {
			orderProd = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return orderProd;
    }
	
	
	public void deleteProductByOrder(Order order) {
		List<Orderproduct> orderProd = null;
        Query query = em.createQuery("SELECT o FROM Orderproduct o WHERE o.order = :order_id");
        query.setParameter("order_id", order);
        
		try {
			orderProd = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < orderProd.size(); i++) {
			Orderproduct item = orderProd.get(i);
			remove(item);
		}


    }
	
	

}
