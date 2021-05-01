package ca.sheridancollege.serramai.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseConfig {
	
	@Bean
	public NamedParameterJdbcTemplate namedParemterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	// method that configures the H2 database
	@Bean
	public DataSource loadSchema() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:table_schema.sql")
				.addScript("classpath:data.sql")
				.build();
	}
	
}
