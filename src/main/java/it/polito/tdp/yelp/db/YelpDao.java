package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<String> getAllCity(){
		String sql = "SELECT DISTINCT b.city as c "
				+ "FROM business b";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				
				result.add(res.getString("c"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(Map<String, Business> idMap, int anno, String citta) {
		String sql = "SELECT distinct b.* "
				+ "FROM business b,reviews r "
				+ "WHERE b.city=? AND r.business_id=b.business_id AND year(r.review_date)=?";
		
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				
				idMap.put(res.getString("business_id"), business);
				
			}
			res.close();
			st.close();
			conn.close();
			return ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ;
		}
		
	}

	public void getArchi(Map<String, Business> idMap, int anno, String citta, List<Adiacenza> archi) {
		String sql = "SELECT  r1.business_id AS id1 , r2.business_id AS id2 ,(AVG(r1.stars)-AVG(r2.stars))AS peso "
				+ "FROM reviews r1,reviews r2,business b1,business b2 "
				+ "WHERE b1.city=? AND r1.business_id=b1.business_id AND YEAR(r1.review_date)=? "
				+ "AND b2.city=b1.city AND r2.business_id = b2.business_id AND  YEAR(r2.review_date) =YEAR(r1.review_date) "
				+ "AND b1.business_id> b2.business_id "
				+ "GROUP BY id1,id2 "
				+ "HAVING peso <> 0 ";
				
//		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Business b1= idMap.get(res.getString("id1"));
				Business b2= idMap.get(res.getString("id2"));
//				if(b1!=null&&b2!=null)
				archi.add(new Adiacenza(b1,b2,res.getInt("peso")));
				
			
				
			}
			res.close();
			st.close();
			conn.close();
			return ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}
	}
	
}
