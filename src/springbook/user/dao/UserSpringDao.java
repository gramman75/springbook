package springbook.user.dao;

import java.util.List;

import springbook.user.domain.User;

public interface UserSpringDao {
	void add(User user);
	void deleteAll();
	Integer getCount();
	User get(String id);
	List<User> getAll();
	void update(User user);
	

}
