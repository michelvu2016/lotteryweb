package com.mvu.lottery.data;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.mvu.lottery.data.model.User;

public interface UserData {

	User getUserByUsername(String username);

	void saveUser(User user);

}