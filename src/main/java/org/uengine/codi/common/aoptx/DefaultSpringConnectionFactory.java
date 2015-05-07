package org.uengine.codi.common.aoptx;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.uengine.util.dao.DefaultConnectionFactory;

/**
 * 
 * @author zachary
 * 
 */
public class DefaultSpringConnectionFactory extends DefaultConnectionFactory {
    private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
    
    @Autowired
    DataSource dataSource;

    public Connection getConnection() {
    	return DataSourceUtils.getConnection(dataSource);
    }
}
