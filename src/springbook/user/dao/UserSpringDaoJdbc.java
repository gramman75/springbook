package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserSpringDaoJdbc implements UserSpringDao {
	private JdbcTemplate jdbcTemplate;
	
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(User user){
		this.jdbcTemplate.update(
			"insert into users(userid, name, password, level, login, recommend, email) "
			+ "values (?,?,?,?,?,?,?)",
			user.getId(), user.getName(), user.getPassword(), 
			user.getLevel().intValue(), user.getLogin(), 
			user.getRecommend(), user.getEmail()
		);			
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update("delete from users");
	}
	
	public Integer getCount(){
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public User get(String id){
		return this.jdbcTemplate.queryForObject("select * from users where userid = ?", 
			new Object[] {id}, 
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
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			
			return user;
		}
	};
	
	public void update(User user) {
		this.jdbcTemplate.update(
			"update users set name = ? , password = ?, level = ? , login = ? , recommend = ?, email=? "
			+ "where userid = ?",
			user.getName(), user.getPassword(), user.getLevel().intValue(), 
			user.getLogin(), user.getRecommend(), user.getEmail(), user.getId()
		);
	}
}
