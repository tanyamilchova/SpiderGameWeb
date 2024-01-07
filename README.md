# SpiderGameWeb
A spider moves in a cube from Start to End. On its way it gain scores. For one game the player pays a stake , for every top passed the player wins 1$.

Spider Cube Game
The Spider Cube Game is a simple text-based game where a spider moves from one top to another within a cube. Players can earn money by randomly moving through the cube, with the goal of reaching the end.
Table of Contents
Project Overview
Features
Getting Started
Database
Filters
Contributing

Project Overview
The Spider Cube Game is designed to provide an entertaining gaming experience where  the spider randomly moves within a cube. Each successful move from one top to another results in a monetary reward, and the ultimate objective is to accumulate more scores before reaching the end point.

Features
Gameplay: Players start game and the spider randomly moves through the cube, earning $1 for each successful move.
If the total wins are bigger than the stake it is a win game, otherwise it is a lost game. The player can chose to play one game round or n-game round. For one round game the player pays one stake,for n-game round the player pays n-stakes.
Database Integration: The project is connected to a database to store user information, game details, and statistics.
User Management: Users can register accounts, track their wallet balance, and participate in multiple games.
Game Summary and Filtering: The project includes filters to retrieve game outcomes and summaries for specific periods.
Getting Started
To get started with the Spider Cube Game:

1.Clone the repository to your local machine.
2.Configure your database connection settings in the application.properties file.
3.Build and run the application using your preferred Java IDE
4.Access the game through your preferred web browser at http://localhost:8080.
Database
The project uses a MySQL database with the following tables:

user: Stores user information, including registration details.
oneGame: Records individual game instances with details such as type, stake, total win, and outcome.
NGame: Tracks overall game statistics, including the sum of all stakes and wins.

Filters
getAllGamesByOutcome
Retrieve games based on their outcome.
getSummaryForPeriod
Get a summary of game statistics for a specific period.

Contributing
Contributions to the Spider Cube Game are welcome. If you have suggestions, bug reports, or want to contribute code, please follow the Contribution Guidelines.
