import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Product extends Tables {
	
	private Long pk_id;
	private Double price;
	private Integer qty;
	private String label;
	
	

	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	public Double getPrice() {
		return price;
	}

	public boolean setPrice(Double price) {
		this.price = price;
		return update();
	}

	public Integer getQty() {
		return qty;
	}

	public boolean setQty(Integer qty) {
		this.qty = qty;
		return update();
	}
	
	public String getLabel() {
		return label;
	}

	public boolean setLabel(String label) {
		this.label = label;
		return update();
	}

	

	

	@Override
	public String toString() {
		return "Product [pk_id=" + pk_id + ", label=" + label + ", price=" + price + ", qty=" + qty + "]";
	}

	private Product() {
		this.pk_id= -1L;
		this.price= 0.0;
		this.qty= 0;
	}
	
	
	private Product(String label, Double price, Integer qty) {
		this.label= label;
		this.price= price;
		this.qty= qty;
	}
	
	private Product(Long pk_id, String label, Double price, Integer qty) {
		this.pk_id= pk_id;
		this.label= label;
		this.price= price;
		this.qty= qty;
	}
	
	private Product(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	public static Product findProductById(Long pk_id) {
		Product product= new Product(pk_id);
		return  product;
	}
	
	public static Product create(String label,Double price, Integer qty) {
		Product product= new Product(label, price, qty);
		product.insert();
		return  product;
	}
	
	
	public void refresh() {
		readFromDB();
	}
	
	public static void truncate() {
		String sqlCmd = "TRUNCATE product CASCADE;";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.execute();
				//System.out.println("Truncate Table product OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de Truncate Table product");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private boolean insert() {
		boolean res= false;
		String sqlCmd = "INSERT INTO product (label, price, qty) VALUES(?, ?, ?);";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setString(1, this.label);
				preparedStatement.setDouble(2, this.price);
				preparedStatement.setInt(3, this.qty);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				long key = -1L;
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					key = rs.getLong("pk_id");
				}
				this.pk_id = key;
				//System.out.println("Enregistrement en base OK : "  + key + " : " + this);
				res = true;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de l'enregistrement en base de " + this);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	private boolean update() {
		boolean res= false;
		String sqlCmd = "update product set label = ?, price = ?, qty = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, this.label);
				preparedStatement.setDouble(2, this.price);
				preparedStatement.setInt(3, this.qty);
				preparedStatement.setLong(4, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				//System.out.println("Mise a jour en base OK de " + this);
				res= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	private void readFromDB() {

		String sqlCmd = "select label, price, qty from product where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.label= rs.getString("label");
					this.price = rs.getDouble("price");
					this.qty= rs.getInt("qty");
					//System.out.println("Lecture OK de " + this);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Product findProductByLabel(String label) {
		Product result= new Product();
		String sqlCmd = "select pk_id, label, price, qty from product where upper(label) = upper(?);";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, label);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					result= new Product(rs.getLong("pk_id"),  rs.getString("label"), rs.getDouble("price"), rs.getInt("qty"));
					//System.out.println("Lecture OK de " + result);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche de " + label);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  result;
	}
	
}
