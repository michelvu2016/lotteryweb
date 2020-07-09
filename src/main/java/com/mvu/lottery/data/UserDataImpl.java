package com.mvu.lottery.data;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mvu.lottery.data.model.User;

@Transactional
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDataImpl implements UserData {
	
	private static Logger log = LoggerFactory.getLogger(UserDataImpl.class);
	
	
	@PersistenceContext
	private EntityManager em;

	public UserDataImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.UserData#getUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(value=TxType.REQUIRED)
	public User getUserByUsername(String username) {
		//em.getTransaction().begin();
		TypedQuery<User> query = em.createQuery("FROM User u where u.username=:username", User.class);
		query.setParameter("username", username);
			
		User user = query.getSingleResult();
	
				
		//em.getTransaction().commit();
		
		return user;
	}
	
	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.UserData#saveUser(com.mvu.lottery.data.model.User)
	 */
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void saveUser(User user) {
		//em.getTransaction().begin();
		User existingUser = null;
		try {
			existingUser = this.getUserByUsername(user.getUsername());
		} catch (NoResultException e) {
			log.info(">>>"+ String.format("User %s not found.", user.getUsername()));
			
		}
		if(existingUser != null && user.getPassword() != existingUser.getPassword()) {
			existingUser.setPassword(user.getPassword());
			//em.merge(user);
		} else {
			em.persist(user);
		}
		
		//em.getTransaction().commit();
		
	}
}
