package net.catenax.traceability.common.support

import org.springframework.test.jdbc.JdbcTestUtils

trait DatabaseSupport implements DatabaseProvider {

	private static final List<String> TABLES = [
		"asset_entity_child_descriptors",
		"assets_investigations",
		"assets_notifications",
		"asset_entity",
		"shell_descriptor",
		"bpn_storage",
		"notification",
		"investigation"
	]

	void clearAllTables() {
		TABLES.each {
			JdbcTestUtils.deleteFromTables(jdbcTemplate(), it)
		}
	}
}
