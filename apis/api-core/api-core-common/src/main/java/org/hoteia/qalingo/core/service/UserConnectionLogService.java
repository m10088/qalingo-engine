/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.service;

import org.hoteia.qalingo.core.domain.UserConnectionLog;

public interface UserConnectionLogService {

    UserConnectionLog getUserConnectionLogById(Long userConnectionLogId);

    UserConnectionLog getUserConnectionLogById(String userConnectionLogId);
	
	void saveOrUpdateUserConnectionLog(UserConnectionLog userConnectionLog);
	
	void deleteUserConnectionLog(UserConnectionLog userConnectionLog);

}
