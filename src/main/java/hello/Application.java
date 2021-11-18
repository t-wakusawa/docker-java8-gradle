package hello;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    // GET /
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> body = new HashMap<>();
        body.put("Status", "Success");
        body.put("Message", "Books REST API v2");
        return createResponse(body);
    }

    // GET /books/
    @RequestMapping(path = "/books", method = RequestMethod.GET)
        public ResponseEntity<Map<String, String>> books() {
        String queryResult = jdbcTemplate.queryForList("SELECT * FROM books").toString();
        Map<String, String> body = new HashMap<>();
        body.put("Status", "Success");
        body.put("Message", queryResult);
        return createResponse(body);
    }

    // GET /books/{id}
    @RequestMapping(path = "/book/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> bookById(@PathVariable(value="id") String id) {
        String queryResult = jdbcTemplate.queryForList("SELECT * FROM books WHERE id = ?", id).toString();
        Map<String, String> body = new HashMap<>();
        body.put("Status", "Success");
        body.put("Message", queryResult);
        return createResponse(body);
    }

    private ResponseEntity<Map<String, String>> createResponse(Map<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map<String, String>> ret = ResponseEntity.ok().headers(headers).body(body);
        logger.info(ret.toString());
        return ret;
    }
  
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}