CREATE TABLE "users" (
	"id"	INTEGER,
	"login"	TEXT,
	"password"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "global_settings" (
	"user_id"	INTEGER,
	"setting_id"	INTEGER,
	"value"	INTEGER,
	PRIMARY KEY("user_id","setting_id"),
	FOREIGN KEY("user_id") REFERENCES "users"("id"),
	FOREIGN KEY("setting_id") REFERENCES "settings"("id")
);