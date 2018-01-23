package com.thomaskint.minidao;

import com.thomaskint.minidao.config.MDConnectionConfig;
import com.thomaskint.minidao.model.User;
import com.thomaskint.minidao.query.MDCondition;

import java.util.List;

import static com.thomaskint.minidao.enumeration.MDConditionLink.*;
import static com.thomaskint.minidao.enumeration.MDConditionOperator.*;

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
			MDConnectionConfig mdConnectionConfig = new MDConnectionConfig("home.thomaskint.com", "3306", "minidao", "minidao", "minidao");
			MiniDAO.init(mdConnectionConfig);

			// CREATE

			// READ
			System.out.println("Test all");

			users = MiniDAO.getEntities(User.class);

			for (User u : users) {
				System.out.println(u.toString());
			}

			System.out.println("Test filtre");

			mdCondition = new MDCondition(new MDCondition("pseudo", LIKE, "Shaman", true), OR, new MDCondition("id_user", 1408232367));
			users = MiniDAO.getEntities(User.class, mdCondition);

			for (User b : users) {
				System.out.println(b.toString());
			}

			System.out.println("Test id");

			user = MiniDAO.getEntityById(User.class, 2);

			System.out.println(user);

			// UPDATE


			// DELETE
//			deleted = MiniDAO.deleteEntities(Book.class);
//
//			deleted = MiniDAO.deleteEntities(Book.class, mdCondition);
//
//			deleted = MiniDAO.deleteEntity(Book.class, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
