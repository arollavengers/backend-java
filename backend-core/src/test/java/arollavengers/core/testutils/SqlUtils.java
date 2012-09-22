package arollavengers.core.testutils;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SqlUtils {

    public static void executeScriptOn(DataSource dataSource, String sqlScript) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        for (String sql : sqlScript.split(";")) {
            jdbcTemplate.update(sql.trim());
        }
    }
}
