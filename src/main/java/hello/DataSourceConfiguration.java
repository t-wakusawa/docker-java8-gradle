package hello;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);
    
    // 環境変数を取得
    @Value("${db.name}")
    private String dbName;
    
    @Value("${db.secrets}")
    private String secretJson;
    
    @Bean
    public DriverManagerDataSource dataSource() {
        // 環境変数のSecret(JSON文字列)からDB接続情報を抽出
        String dbEngine = getJsonValue(secretJson, "engine");
        String dbHost = getJsonValue(secretJson, "host");
        String dbPort = getJsonValue(secretJson, "port");
        String dbUser = getJsonValue(secretJson, "username");
		String dbPassword = getJsonValue(secretJson, "password");
		String dbUrl = "jdbc:" + dbEngine + "://" + dbHost + ":" + dbPort + "/" + dbName
                        + "?autoReconnect=true&useSSL=false&requireSSL=false"; // SSLのWarningを抑制するための記述

        // DB接続情報をセット
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        driverManagerDataSource.setUrl(dbUrl);
        driverManagerDataSource.setUsername(dbUser);
        driverManagerDataSource.setPassword(dbPassword);
        return driverManagerDataSource;
    }
  
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
  
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    private static ObjectMapper mapper = new ObjectMapper();
    
    private static String getJsonValue(String json, String path) {
        try {
            JsonNode root = mapper.readTree(json);
            return root.path(path).asText();
        } catch (IOException e) {
            logger.error("Can't get {} from json {}", path, json, e);
            return null;
        }
    }

}