# Live Cricket Score Management System

A VS Code-friendly Spring Boot + MongoDB project with a responsive browser dashboard and simulated live scoring. The frontend is served by Spring Boot, so you do **not** need Node.js or a separate frontend server to run this version.

## Requirements
- Java 21 (already installed on your PC)
- Apache Maven (already installed)
- MongoDB Server running locally at `mongodb://127.0.0.1:27017` (you confirmed this works)
- VS Code (Java Extension Pack is recommended)

## Run on Windows
1. Extract this ZIP.
2. Open the extracted `CricketScoreSystem` folder in VS Code.
3. Make sure MongoDB is running.
4. Open Terminal > New Terminal in VS Code.
5. Run:
   ```cmd
   mvn spring-boot:run
   ```
   First run downloads Maven dependencies, so it needs internet access.
6. Open `http://localhost:8080` in your browser.

Or double-click `run.bat`.

## Features
- Dashboard of matches and live score card
- Create a match
- Record runs, dot balls, wickets, wides and no-balls
- Automatic overs and run-rate calculation
- Ball-by-ball commentary
- Simulated live scoring button
- REST APIs and MongoDB persistence
- Responsive layout for desktop and mobile

## Main API endpoints
- `GET /api/matches`
- `GET /api/matches/live`
- `GET /api/matches/{id}`
- `POST /api/matches`
- `POST /api/matches/{id}/balls`
- `POST /api/matches/{id}/simulate`
- `GET /api/matches/{id}/commentary`
- `POST /api/matches/{id}/finish`
- `GET /api/health`

### Example: record a delivery
`POST http://localhost:8080/api/matches/{id}/balls`
```json
{
  "runs": 4,
  "extrasType": "NONE",
  "wicket": false,
  "batter": "Rohit Sharma",
  "bowler": "Jasprit Bumrah"
}
```
Allowed `extrasType`: `NONE`, `WIDE`, `NO_BALL`, `BYE`, `LEG_BYE`.

## Notes
- The initial demo match is created automatically when the database is empty.
- This is a simulated scoring platform, not an official live-data feed. Real external match data requires a cricket API provider and API key.
- MongoDB access control is currently disabled on your local setup. Keep MongoDB bound to localhost and do not expose port 27017 publicly.
