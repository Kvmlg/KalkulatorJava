package cafe.items;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import cafe.dao.ProductDAO;
import cafe.entities.Product;
import cafe.entities.User;

@Named
@RequestScoped
public class ProductBB {
	private int id;
	private String name;
	private	Float price;
	private String category;
	private Product product = new Product();
	private List<Product> prod;

		
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	ProductDAO productDAO;
	@Inject
	FacesContext context;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_PROD = "adminPageProd.xhtml";
	private static final String PAGE_Admin = "adminPage.xhtml";

	public Product getProduct() {
		return product;
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void onLoad() throws IOException {
		product = (Product) flash.get("product");
		HttpSession session = (HttpSession) extcontext.getSession(true);
		session.setAttribute("prodid", product.getId());
	}
	
	public List<Product> getList(){
		List<Product> list = null;
		list = productDAO.getFullList();
        return list;		
    
	}
	
	public List<Product> getListKawy(){
		List<Product> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		searchParams.put("item", "Kawy");

		list = productDAO.getList(searchParams);
		
		return list;
	}

	public List<Product> getListDesery(){
		List<Product> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		searchParams.put("item", "Desery");

		list = productDAO.getList(searchParams);
		
		return list;
	}
	
	public List<Product> getFullList(){
		return productDAO.getFullList();
	}

	public void deleteProduct(Product product) {
		try {
			if (product == null ) {
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd edycji", null));
			} else {
					productDAO.remove(product);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usunięto produkt", null));
			}

				

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas usuwania zamówienia", null));
		}


 }
	 public void toEditProduct(Product product) throws IOException {
		 	flash.put("product", product);
		 	context.getExternalContext().redirect("adminEditProduct.xhtml");
	 }
	 
		public String addProduct(){
			Product newproduct = new Product();
			
			newproduct.setName(name);
			newproduct.setPrice(price);
			newproduct.setCategory(category);

			
			try {
					productDAO.create(newproduct);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dodano produkt", null));

			} catch (Exception e) {
				e.printStackTrace();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
				return PAGE_STAY_AT_THE_SAME;
			}
			
			
			return PAGE_PROD;
		}
		
		public String saveChanges() {
			HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
			product.setId((int)session.getAttribute("prodid"));
			try {
				productDAO.merge(product);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dokonano zmian", null));

		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}
			
		    return PAGE_Admin;
		}
		


 
	
}
