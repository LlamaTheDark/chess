package dataaccess.mysql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public
class ManagerTests {

    @Test
    @DisplayName("Successfully Read Props and Connect to Database")
    void successfullyStartDatabase() {
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
    }
}
