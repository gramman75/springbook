package springbook.user.service;

import java.util.ArrayList;
import java.util.List;

import springbook.user.dao.UserSpringDao;
import springbook.user.domain.User;

public class MockUserSpringDao implements UserSpringDao {
	private List<User> users;
	private List<User> updated = new ArrayList();

	public MockUserSpringDao(List<User> users){
		this.users = users;
	}
	
	public List<User> getUpdated(){
		return this.updated;
	}

	public List<User> getAll() {
		return this.users;
	}
	
	public void update(User user) {
		updated.add(user);
	}
	
	public void add(User user) {
		throw new UnsupportedOperationException();
	}

	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	public Integer getCount() {
		throw new UnsupportedOperationException();
	}

	public User get(String id) {
		throw new UnsupportedOperationException();
	}


}
