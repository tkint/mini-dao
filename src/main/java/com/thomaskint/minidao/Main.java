package com.thomaskint.minidao;

import com.thomaskint.minidao.connection.MDConnectionConfig;
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
		boolean ok;
		try {
			MDConnectionConfig mdConnectionConfig = new MDConnectionConfig("home.thomaskint.com", "3306", "minidao", "minidao", "minidao");
			MiniDAO.config(mdConnectionConfig);

			// CREATE

			System.out.println("Test create");

			user = new User();
			user.pseudo = "Test pseudo 02";
			user.login = "Test Login 02";
			user.password = "Test Password 02";

//			ok = MiniDAO.createEntity(user);

//			System.out.println(ok);

			// READ
			System.out.println("Test get all");

			users = MiniDAO.getEntities(User.class);

			for (User u : users) {
				System.out.println(u.toString());
			}

			System.out.println("Test get filtre");

			mdCondition = new MDCondition(new MDCondition("pseudo", LIKE, "Shaman", true), OR, new MDCondition("id_user", 1408232367));
			users = MiniDAO.getEntities(User.class, mdCondition);

			for (User b : users) {
				System.out.println(b.toString());
			}

			System.out.println("Test get id");

			user = MiniDAO.getEntityById(User.class, 7);

			System.out.println(user);

			// UPDATE

			System.out.println("Test update");

			user.pseudo = "Test pseudo mescouilless";
			ok = MiniDAO.updateEntity(user);

			System.out.println(ok);

			// DELETE
//			System.out.println("Test delete all");
//
//			ok = MiniDAO.deleteEntities(User.class);
//
//			System.out.println(ok);
//
//			System.out.println("Test delete filtre");
//
//			ok = MiniDAO.deleteEntities(User.class, new MDCondition("pseudo", null));
//
//			System.out.println(ok);
//
//			System.out.println("Test delete id");
//
//			ok = MiniDAO.deleteEntity(User.class, 2);
//
//			System.out.println(ok);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
