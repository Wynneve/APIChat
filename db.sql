CREATE TABLE "users" (
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
	"user_id"	INTEGER,
	"setting_id"	INTEGER,
	"value"	INTEGER,
	PRIMARY KEY("user_id","setting_id"),
	FOREIGN KEY("user_id") REFERENCES "users"("id"),
	FOREIGN KEY("setting_id") REFERENCES "settings"("id")
);

CREATE TABLE "chats" (
	"id"	INTEGER,
	"user_id"	INTEGER NOT NULL,
	"title"	TEXT,
	"last_access" INTEGER,
	FOREIGN KEY("user_id") REFERENCES "users"("id"),
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