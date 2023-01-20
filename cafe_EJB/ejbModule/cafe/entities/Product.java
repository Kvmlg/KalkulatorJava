package cafe.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the products database table.
 * 
 */
@Entity
@Table(name="products")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String category;

	private String image;

	private String name;

	private float price;

	//bi-directional many-to-one association to Orderproduct
	@OneToMany(mappedBy="product")
	private List<Orderproduct> orderproducts;

	//bi-directional many-to-one association to ShopingCart
	@OneToMany(mappedBy="product")
	private List<ShopingCart> shopingCarts;

	public Product() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Orderproduct> getOrderproducts() {
		return this.orderproducts;
	}

	public void setOrderproducts(List<Orderproduct> orderproducts) {
		this.orderproducts = orderproducts;
	}

	public Orderproduct addOrderproduct(Orderproduct orderproduct) {
		getOrderproducts().add(orderproduct);
		orderproduct.setProduct(this);

		return orderproduct;
	}

	public Orderproduct removeOrderproduct(Orderproduct orderproduct) {
		getOrderproducts().remove(orderproduct);
		orderproduct.setProduct(null);

		return orderproduct;
	}

	public List<ShopingCart> getShopingCarts() {
		return this.shopingCarts;
	}

	public void setShopingCarts(List<ShopingCart> shopingCarts) {
		this.shopingCarts = shopingCarts;
	}

	public ShopingCart addShopingCart(ShopingCart shopingCart) {
		getShopingCarts().add(shopingCart);
		shopingCart.setProduct(this);

		return shopingCart;
	}

	public ShopingCart removeShopingCart(ShopingCart shopingCart) {
		getShopingCarts().remove(shopingCart);
		shopingCart.setProduct(null);

		return shopingCart;
	}

}