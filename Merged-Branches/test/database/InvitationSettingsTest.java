package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;

import entityClasses.Invitation;
import entityClasses.User;

/**
 * Focused database tests for Section 5. Run with assertions enabled or use the built-in checks.
 */
public class InvitationSettingsTest {
	private static int checks;

	public static void main(String[] args) throws Exception {
		String url = "jdbc:h2:mem:section5;DB_CLOSE_DELAY=-1";
		Database database = new Database(url);
		database.connectToDatabase();

		LocalDateTime firstExpiration = LocalDateTime.now().plusDays(3).withNano(0);
		String code = database.generateInvitationCode(
				"multiple@example.com", firstExpiration, true, true, false);
		check(!code.isEmpty(), "valid invitation should be created");

		Invitation invitation = database.getInvitation(code);
		check(invitation != null, "created invitation should be found");
		check(invitation.getAdminRole(), "Admin role should persist");
		check(invitation.getRole1(), "Role1 should persist");
		check(!invitation.getRole2(), "unselected Role2 should remain false");
		check(firstExpiration.equals(invitation.getExpiration()), "expiration should persist");
		check(!invitation.isExpired(LocalDateTime.now()), "future invitation should be valid");

		User multipleRoleUser = new User("multipleuser", "password", "", "", "", "",
				invitation.getEmailAddress(), invitation.getAdminRole(),
				invitation.getRole1(), invitation.getRole2());
		database.register(multipleRoleUser);
		check(database.getUserAccountDetails("multipleuser"),
				"multiple-role invited user should be registered");
		check(database.getCurrentAdminRole() && database.getCurrentNewRole1()
				&& !database.getCurrentNewRole2(),
				"all selected invitation roles should reach the user account");

		LocalDateTime updatedExpiration = LocalDateTime.now().plusDays(10).withNano(0);
		check(database.updateInvitationSettings(code, updatedExpiration, false, false, true),
				"invitation update should succeed");
		invitation = database.getInvitation(code);
		check(!invitation.getAdminRole() && !invitation.getRole1() && invitation.getRole2(),
				"updated roles should replace the original roles");
		check(updatedExpiration.equals(invitation.getExpiration()),
				"updated expiration should persist");

		String expiredCode = database.generateInvitationCode(
				"expired@example.com", LocalDateTime.now().minusMinutes(1), false, true, false);
		check(database.getInvitation(expiredCode).isExpired(LocalDateTime.now()),
				"past invitation should be expired");
		check(database.generateInvitationCode("missing-role@example.com",
				LocalDateTime.now().plusDays(1), false, false, false).isEmpty(),
				"invitation without a role should be rejected");

		User user = new User("section5user", "password", "", "", "", "",
				invitation.getEmailAddress(), invitation.getAdminRole(),
				invitation.getRole1(), invitation.getRole2());
		database.register(user);
		check(database.getUserAccountDetails("section5user"), "invited user should be registered");
		check(!database.getCurrentAdminRole() && !database.getCurrentNewRole1()
				&& database.getCurrentNewRole2(), "invitation roles should reach the user account");
		database.removeInvitationAfterUse(code);
		check(database.getInvitation(code) == null, "used invitation should be removed");

		database.closeConnection();
		Database restartedDatabase = new Database(url);
		restartedDatabase.connectToDatabase();
		check(restartedDatabase.getInvitation(expiredCode) != null,
				"unused invitation should remain after reconnecting");
		restartedDatabase.closeConnection();

		testLegacyInvitationMigration();

		System.out.println("InvitationSettingsTest passed " + checks + " checks.");
	}

	private static void testLegacyInvitationMigration() throws Exception {
		String legacyUrl = "jdbc:h2:mem:section5legacy;DB_CLOSE_DELAY=-1";
		try (Connection connection = DriverManager.getConnection(legacyUrl, "sa", "");
				Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE InvitationCodes (code VARCHAR(10) PRIMARY KEY, "
					+ "emailAddress VARCHAR(255), role VARCHAR(10))");
			statement.execute("INSERT INTO InvitationCodes VALUES "
					+ "('legacy', 'legacy@example.com', 'Admin')");
		}

		Database migratedDatabase = new Database(legacyUrl);
		migratedDatabase.connectToDatabase();
		Invitation legacy = migratedDatabase.getInvitation("legacy");
		check(legacy != null, "legacy invitation should survive the schema migration");
		check(legacy.getAdminRole(), "legacy role should be translated");
		check(legacy.getExpiration() != null && legacy.getExpiration().isAfter(LocalDateTime.now()),
				"legacy invitation should receive a future compatibility deadline");
		check(migratedDatabase.updateInvitationSettings("legacy",
				LocalDateTime.now().plusDays(2), true, false, true),
				"legacy invitation should accept new Section 5 settings");
		migratedDatabase.closeConnection();
	}

	private static void check(boolean condition, String message) {
		checks++;
		if (!condition) throw new AssertionError(message);
	}
}
