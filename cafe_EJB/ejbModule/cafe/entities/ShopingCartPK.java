package cafe.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the shoping_cart database table.
 * 
 */
@Embeddable
public class ShopingCartPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int id;

	@Column(name="user_id", insertable=false, updatable=false)
	private int userId;

	@Column(name="products_id", insertable=false, updatable=false)
	private int productsId;

	public ShopingCartPK() {
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
		if (!(other instanceof ShopingCartPK)) {
			return false;
		}
		ShopingCartPK castOther = (ShopingCartPK)other;
		return 
			(this.id == castOther.id)
			&& (this.userId == castOther.userId)
			&& (this.productsId == castOther.productsId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.productsId;
		
		return hash;
	}
}