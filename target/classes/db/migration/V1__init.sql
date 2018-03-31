CREATE TABLE "User" (
  id SERIAL PRIMARY KEY,
  email TEXT COLLATE "ucs_basic" UNIQUE NOT NULL,
  password VARCHAR(256) NOT NULL,
  nickname TEXT COLLATE "ucs_basic" UNIQUE NOT NULL,
  rating INTEGER DEFAULT 0,
  avatar TEXT COLLATE "ucs_basic" NOT NULL DEFAULT 'default.jpg'
);