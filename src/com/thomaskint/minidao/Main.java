package com.thomaskint.minidao;

import com.thomaskint.minidao.model.User;
import com.thomaskint.minidao.query.MDCondition;

import java.util.List;

/**
 * Created by tkint on 19/01/2018.
 */
public class Main {

	public static void main(String... args) {
		User user;
		List<User> users;
		MDCondition mdCondition;
		boolean deleted;
		try {
			// CREATE


			// READ
			users = MiniDAO.getEntities(User.class);

			mdCondition = new MDCondition("pseudo", "Test");
			users = MiniDAO.getEntities(User.class, mdCondition);

			user = MiniDAO.getEntityById(User.class, 5);

			// UPDATE


			// DELETE
			deleted = MiniDAO.deleteEntities(User.class);

			deleted = MiniDAO.deleteEntities(User.class, mdCondition);

			deleted = MiniDAO.deleteEntity(User.class, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
