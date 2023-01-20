package cafe.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the shoping_cart database table.
 * 
 */
@Entity
@Table(name="shoping_cart")
@NamedQuery(name="ShopingCart.findAll", query="SELECT s FROM ShopingCart s")
public class ShopingCart implements Serializable {
	private static final long serialVersionUID = 1L;


	private ShopingCartPK id;

	private int quantity;


	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="products_id", nullable = false, insertable=false, updatable=false, referencedColumnName = "id")
	private Product product;


	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id", nullable = false, insertable=false, updatable=false, referencedColumnName = "id")
	private User user;

	public ShopingCart() {
	}

	public ShopingCartPK getId() {
		return this.id;
	}

	public void setId(ShopingCartPK id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}