package picker.picker_backend.dm.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/test/dm/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class DMServiceImplTest {
    @Test
    void test() {
        assertTrue(true);
    }
}