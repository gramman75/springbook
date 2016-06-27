package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

public class UserSpringDao {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}
	
	public void add(User user) throws SQLException{
		this.jdbcTemplate.update("insert into users(userid, name, password) values (?,?,?)",
				user.getId(), user.getName(), user.getPassword()
				);			
	}
	
	public void deleteAll() throws SQLException{
		this.jdbcTemplate.update("delete from users");
	}
	
	public Integer getCount() throws SQLException{
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public User get(String id){
		return this.jdbcTemplate.queryForObject("select * from users where userid = ?", 
			new Object[] {id}, 
//			new RowMapper<User>(){
//				public User mapRow(ResultSet rs, int rowNum) throws SQLException{
//					User user = new User();
//					user.setId(rs.getString("userid"));
//					user.setName(rs.getString("name"));
//					user.setPassword(rs.getString("password"));
//					
//					return user;
//			}
		
//		}
		this.userMapper
		);
	}
	
	public List<User> getAll(){
		return this.jdbcTemplate.query("select * from users order by userid",
				this.userMapper);
	}	
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
		public User mapRow(ResultSet rs, int rownum) throws SQLException{
			User user = new User();
			user.setId(rs.getString("userid"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		
			return user;
		}
	};
}
