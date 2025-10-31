# Online Tournament System

A RESTful Java Spring Boot backend for managing online gaming tournaments with an H2 in-memory database.

## Features

- Full CRUD for users, games, tournaments, participations, and matches
- Role-based access control (ADMIN, ORGANIZER, PLAYER)
- Secure authentication with JWT tokens and BCrypt password hashing
- Business rule enforcement:
  - Usernames must be unique and alphanumeric
  - Games must have unique title/platform combinations
  - Organizers cannot join their own tournaments as players
  - Players can only join tournaments that have not reached capacity
  - Matches can only be created between players in the same tournament
  - Player rankings update automatically based on match results
- Comprehensive input validation and error handling
- In-memory H2 database with sample data loaded at startup
- RESTful API with DTO pattern and MVC architecture
## Roles and Permissions
The system defines three roles with distinct permissions:

| Role      | Permissions |
|-----------|-------------|
| **ADMIN**     | - Manage all users, games, tournaments, participations, and matches<br>- Create, update, and delete any entity<br>- View all data<br>- Access all endpoints |
| **ORGANIZER** | - Create and manage tournaments they organize<br>- Add or remove players to/from their tournaments<br>- Create and manage matches in their tournaments<br>- View all games and tournaments<br>- Cannot join their own tournaments as a player<br>- Cannot manage users or games |
| **PLAYER**    | - View and join available tournaments<br>- View games, tournaments, participations, and matches<br>- Participate in matches<br>- Cannot create or manage tournaments, games, or users |
## Authentication Process

1. Login with username and password at `/api/auth/login`
2. Receive JWT token in response
3. Include token in `Authorization: Bearer <token>` header for all subsequent requests

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Spring Security
- H2 Database (in-memory)
- Maven

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

## How to Run

1. Install Java 17+ and Maven 3.6+
2. Clone the repository
3. In the project directory, run:
	 ```bash
	 mvn spring-boot:run
	 ```
4. The app will start at `http://localhost:8080`

## How to Test

Run all tests:
```bash
mvn test
```

The app will start at `http://localhost:8080`

## Entities

### User
- `username`: Unique, alphanumeric, required
- `password`: Required, minimum 6 characters, stored encrypted
- `role`: One of ADMIN, ORGANIZER, PLAYER (required)
- `ranking`: Integer, defaults to 0

### Game
- `title`: Required, unique per platform
- `genre`: Required
- `platform`: One of PC, CONSOLE, MOBILE, WEB, CROSS_PLATFORM, VR (required)

### Tournament
- `name`: Required, unique
- `maxPlayers`: Integer, required, > 0
- `currentPlayers`: Integer, managed by system
- `game`: Reference to Game, required
- `organizer`: Reference to User (ORGANIZER or ADMIN), required

### Participation
- `tournament`: Reference to Tournament, required
- `player`: Reference to User (PLAYER or ADMIN), required
- `joinDate`: Set automatically
- `score`: Integer, defaults to 0

### Match
- `tournament`: Reference to Tournament, required
- `round`: Integer, required, > 0
- `player1`: Reference to User, required
- `player2`: Reference to User, required
- `result`: One of PLAYER1_WIN, PLAYER2_WIN, DRAW, PENDING (required)

## Field Validations

- **User**
  - Username: required, unique, alphanumeric (letters and numbers only)
  - Password: required, minimum 6 characters
  - Role: required, must be ADMIN, ORGANIZER, or PLAYER
- **Game**
  - Title: required, unique per platform
  - Genre: required
  - Platform: required, must be a valid enum value
- **Tournament**
  - Name: required, unique
  - maxPlayers: required, integer > 0
  - game: required, must reference existing game
  - organizer: required, must reference existing user with ORGANIZER or ADMIN role
- **Participation**
  - tournament: required, must reference existing tournament
  - player: required, must reference existing user with PLAYER or ADMIN role
  - score: integer (default 0)
- **Match**
  - tournament: required, must reference existing tournament
  - round: required, integer > 0
  - player1/player2: required, must reference users in the tournament
  - result: required, must be PLAYER1_WIN, PLAYER2_WIN, DRAW, or PENDING

### Sample Data

Dummy data is loaded on startup:

**Users**

| Username    | Password     | Role       | Ranking |
|-------------|--------------|------------|---------|
| admin       | password123  | ADMIN      | 1000    |
| organizer1  | password123  | ORGANIZER  | 500     |
| organizer2  | password123  | ORGANIZER  | 450     |
| player1     | password123  | PLAYER     | 850     |
| player2     | password123  | PLAYER     | 720     |
| player3     | password123  | PLAYER     | 680     |
| player4     | password123  | PLAYER     | 590     |
| player5     | password123  | PLAYER     | 420     |

**Games**

- Counter-Strike: Global Offensive (FPS, PC)
- League of Legends (MOBA, PC)
- FIFA 24 (Sports, CONSOLE)
- Valorant (FPS, PC)
- Rocket League (Sports, CROSS_PLATFORM)

**Tournaments**

- CS:GO Championship (8 players, game: CS:GO, organizer: organizer1, currentPlayers: 4)
- LoL Spring Cup (6 players, game: LoL, organizer: organizer2, currentPlayers: 3)
- FIFA World Cup (4 players, game: FIFA 24, organizer: organizer1, currentPlayers: 4)

**Participations**

- CS:GO Championship: player1 (150), player2 (120), player3 (90), player4 (80)
- LoL Spring Cup: player1 (200), player3 (180), player5 (100)
- FIFA World Cup: player2 (75), player4 (85), player5 (65), admin (95)

**Matches**

- CS:GO Championship:
  - Round 1: player1 vs player2 (PLAYER1_WIN)
  - Round 1: player3 vs player4 (PLAYER2_WIN)
  - Round 2: player1 vs player4 (PENDING)
- LoL Spring Cup:
  - Round 1: player1 vs player3 (PLAYER1_WIN)
  - Round 2: player1 vs player5 (DRAW)
- FIFA World Cup:
  - Round 1: player2 vs player4 (PLAYER1_WIN)
  - Round 1: player5 vs admin (PLAYER2_WIN)
  - Round 2: player2 vs admin (PENDING)

## API Overview

### Users
- `GET /api/users` — List users
- `POST /api/users` — Create user
- `PUT /api/users/{id}` — Update user
- `DELETE /api/users/{id}` — Delete user

### Games
- `GET /api/games` — List games
- `POST /api/games` — Create game
- `PUT /api/games/{id}` — Update game
- `DELETE /api/games/{id}` — Delete game

### Tournaments
- `GET /api/tournaments` — List tournaments
- `POST /api/tournaments` — Create tournament
- `PUT /api/tournaments/{id}` — Update tournament
- `DELETE /api/tournaments/{id}` — Delete tournament

### Participations
- `GET /api/participations` — List participations
- `POST /api/participations` — Join tournament
- `DELETE /api/participations/{id}` — Leave tournament

### Matches
- `GET /api/matches` — List matches
- `POST /api/matches` — Create match
- `PUT /api/matches/{id}` — Update match
- `DELETE /api/matches/{id}` — Delete match

## Example JSON Requests

### Create User
```json
{
  "username": "newplayer123",
  "password": "securepass456",
  "role": "PLAYER",
  "ranking": 100
}
```

### Create Game
```json
{
  "title": "Apex Legends",
  "genre": "Battle Royale",
  "platform": "PC"
}
```

### Create Tournament
```json
{
  "name": "Spring Championship 2025",
  "maxPlayers": 16,
  "gameTitle": "Counter-Strike: Global Offensive",
  "organizerUsername": "organizer1"
}
```

### Join Tournament (Participation)
```json
{
  "tournamentName": "CS:GO Championship",
  "playerUsername": "player1",
  "score": 0
}
```

### Create Match
```json
{
  "tournamentName": "CS:GO Championship",
  "round": 1,
  "player1Username": "player1",
  "player2Username": "player2",
  "result": "PENDING"
}
```


### Create User
```bash
POST /api/users
Content-Type: application/json

{
    "username": "newplayer123",
    "password": "securepass456",
    "role": "PLAYER",
    "ranking": 100
}
```

### Create Game
```bash
POST /api/games
Content-Type: application/json

{
    "title": "Apex Legends",
    "genre": "Battle Royale",
    "platform": "PC"
}
```

### Create Tournament
```bash
POST /api/tournaments
Content-Type: application/json

{
    "name": "Spring Championship 2025",
    "maxPlayers": 16,
    "gameTitle": "Counter-Strike: Global Offensive",
    "organizerUsername": "organizer1"
}
```

### Create Participation (Join Tournament)
```bash
POST /api/participations
Content-Type: application/json

{
    "tournamentName": "CS:GO Championship",
    "playerUsername": "player1",
    "score": 0
}
```

### Create Match
```bash
POST /api/matches
Content-Type: application/json

{
    "tournamentName": "CS:GO Championship",
    "round": 1,
    "player1Username": "player1",
    "player2Username": "player2",
    "result": "PENDING"
}
```