package cafe.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the orderproducts database table.
 * 
 */
@Embeddable
public class OrderproductPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int id;

	@Column(name="order_id", insertable=false, updatable=false)
	private int orderId;

	@Column(name="products_id", insertable=false, updatable=false)
	private int productsId;

	public OrderproductPK() {
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return this.orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getProductsId() {
		return this.productsId;
	}
	public void setProductsId(int productsId) {
		this.productsId = productsId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OrderproductPK)) {
			return false;
		}
		OrderproductPK castOther = (OrderproductPK)other;
		return 
			(this.id == castOther.id)
			&& (this.orderId == castOther.orderId)
			&& (this.productsId == castOther.productsId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id;
		hash = hash * prime + this.orderId;
		hash = hash * prime + this.productsId;
		
		return hash;
	}
}