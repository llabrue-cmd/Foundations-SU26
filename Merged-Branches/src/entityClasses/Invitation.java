package entityClasses;

import java.time.LocalDateTime;

/**
 * Stores the settings that belong to an invitation.
 */
public class Invitation {
	private final String code;
	private final String emailAddress;
	private final LocalDateTime expiration;
	private final boolean adminRole;
	private final boolean role1;
	private final boolean role2;

	public Invitation(String code, String emailAddress, LocalDateTime expiration,
			boolean adminRole, boolean role1, boolean role2) {
		this.code = code;
		this.emailAddress = emailAddress;
		this.expiration = expiration;
		this.adminRole = adminRole;
		this.role1 = role1;
		this.role2 = role2;
	}

	public String getCode() { return code; }
	public String getEmailAddress() { return emailAddress; }
	public LocalDateTime getExpiration() { return expiration; }
	public boolean getAdminRole() { return adminRole; }
	public boolean getRole1() { return role1; }
	public boolean getRole2() { return role2; }

	public boolean isExpired(LocalDateTime currentTime) {
		return expiration != null && currentTime.isAfter(expiration);
	}

	public boolean hasAtLeastOneRole() {
		return adminRole || role1 || role2;
	}

	public String getRoleNames() {
		String roles = "";
		if (adminRole) roles = "Admin";
		if (role1) roles += roles.isEmpty() ? "Role1" : ", Role1";
		if (role2) roles += roles.isEmpty() ? "Role2" : ", Role2";
		return roles;
	}

	@Override
	public String toString() {
		return emailAddress + " (" + code + ")";
	}
}
