package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

public class UserSpringDaoJdbc implements UserSpringDao {
	private JdbcTemplate jdbcTemplate;
	private SqlService sqlService;
	
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void setSqlService(SqlService sqlService){
		this.sqlService = sqlService;
	}
	
	public void add(User user){
		this.jdbcTemplate.update(
			sqlService.getSql("userAdd"),
			user.getId(), user.getName(), user.getPassword(), 
			user.getLevel().intValue(), user.getLogin(), 
			user.getRecommend(), user.getEmail()
		);			
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
	}
	
	public Integer getCount(){
		return this.jdbcTemplate.queryForInt(sqlService.getSql("userGetCount"));
	}
	
	public User get(String id){
		return this.jdbcTemplate.queryForObject(sqlService.getSql("userGet"), 
			new Object[] {id}, 
		this.userMapper
		);
	}
	
	public List<User> getAll(){
		return this.jdbcTemplate.query(sqlService.getSql("userGetAll"),
				this.userMapper);
	}	
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
		public User mapRow(ResultSet rs, int rownum) throws SQLException{
			User user = new User();
			user.setId(rs.getString("userid"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			
			return user;
		}
	};
	
	public void update(User user) {
		this.jdbcTemplate.update(
			sqlService.getSql("userUpdate"),
			user.getName(), user.getPassword(), user.getLevel().intValue(), 
			user.getLogin(), user.getRecommend(), user.getEmail(), user.getId()
		);
	}
}
