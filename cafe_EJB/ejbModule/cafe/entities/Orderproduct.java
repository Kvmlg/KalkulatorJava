package cafe.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the orderproducts database table.
 * 
 */
@Entity
@Table(name="orderproducts")
@NamedQuery(name="Orderproduct.findAll", query="SELECT o FROM Orderproduct o")
public class Orderproduct implements Serializable {
	private static final long serialVersionUID = 1L;


	private OrderproductPK id;

	private int quantity;

	//bi-directional many-to-one association to Order
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="order_id", nullable = false, insertable=false, updatable=false, referencedColumnName = "id")
	private Order order;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="products_id", nullable = false, insertable=false, updatable=false, referencedColumnName = "id")
	private Product product;

	public Orderproduct() {
	}

	public OrderproductPK getId() {
		return this.id;
	}

	public void setId(OrderproductPK id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}