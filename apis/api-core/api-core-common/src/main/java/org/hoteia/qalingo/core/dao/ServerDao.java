/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.dao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.hoteia.qalingo.core.domain.ServerStatus;

public interface ServerDao {

    ServerStatus getServerStatusById(Long serverStatusId);

    List<ServerStatus> findServerStatus(String serverName);
    
    List<ServerStatus> findServerStatus();
    
    List<ServerStatus> getServerList();
    
    ServerStatus saveOrUpdateServerStatus(ServerStatus serverStatus, String message) throws IOException;
    
    ServerStatus saveOrUpdateServerStatus(ServerStatus serverStatus);

	void deleteServerStatus(ServerStatus serverStatus);

	int deleteSendedServerStatus(Timestamp before);
}
