CREATE TABLE "profiles" (
	"id"	INTEGER,
	"login"	TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "settings" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"default"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "global_settings" (
	"profile_id"	INTEGER,
	"setting_id"	INTEGER,
	"value"	INTEGER,
	PRIMARY KEY("profile_id","setting_id"),
	FOREIGN KEY("profile_id") REFERENCES "profiles"("id"),
	FOREIGN KEY("setting_id") REFERENCES "settings"("id")
);

CREATE TABLE "chats" (
	"id"	INTEGER,
	"profile_id"	INTEGER NOT NULL,
	"title"	TEXT,
	"last_access" INTEGER,
	FOREIGN KEY("profile_id") REFERENCES "profiles"("id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "messages" (
	"id"	INTEGER,
	"chat_id"	INTEGER,
	"role"	TEXT,
	"timestamp"	INTEGER,
	"content"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("chat_id") REFERENCES "chats"("id")
)

CREATE TABLE "chat_settings" (
	"chat_id"	INTEGER,
	"setting_id"	INTEGER,
	"value"	TEXT,
	PRIMARY KEY("chat_id","setting_id"),
	FOREIGN KEY("setting_id") REFERENCES "settings"("id"),
	FOREIGN KEY("chat_id") REFERENCES "chats"("id")
);