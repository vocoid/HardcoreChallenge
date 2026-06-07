# HardcoreChallenge

A Minecraft Spigot plugin that enforces hardcore-mode challenge gameplay with automatic server restart on player death and death tracking statistics.

## Overview

HardcoreChallenge is a Minecraft server plugin designed for players who want to experience an intense hardcore survival challenge. When any player dies, the plugin triggers a server restart countdown, ending the session and forcing all players to start fresh. The plugin also maintains a persistent death leaderboard to track player survival performance across sessions.

## Features

- **Automatic Server Restart**: When a player dies, the server automatically restarts after a 10-second countdown
- **Death Tracking**: Records all player deaths and persists statistics across server restarts
- **Death Leaderboard**: `/deathtop` command displays a ranked leaderboard of players by death count
- **Visual Countdown**: Shows a title screen countdown to all players when a death occurs
- **Sound Effects**: Plays dramatic sound effects during the death and countdown sequence
- **World Reset**: Optionally resets the world folder on server restart
- **Color-Coded Rankings**: Death leaderboard uses colors to highlight top 3 positions (Yellow for 1st, Green for 2nd, Blue for 3rd)

## Installation

1. Download the compiled plugin JAR file or build it from source
2. Place the JAR file in your server's `plugins` directory
3. Start or restart your server
4. The plugin will automatically generate configuration files

### Building from Source

Requirements:
- Java 21 or higher
- Gradle

Build the plugin:
```bash
./gradlew build
```

The compiled JAR will be in the `build/libs` directory.

## Configuration

The plugin uses `config.yml` located in the `plugins/HardcoreChallenge/` directory:

```yaml
world: world  # The world folder name to reset on restart

# Do not manually edit these values:
scheduled: false  # Whether a restart is currently scheduled
deaths:           # Death count tracking (auto-managed)
```

### Configuration Options

- **world**: Specifies which world folder to delete when the server restarts (default: `world`)
- **scheduled**: Internal flag indicating if a restart countdown is in progress (do not edit manually)
- **deaths**: Internal tracking of player death counts (auto-populated by the plugin)

## Commands

### `/deathtop`

Displays a ranked leaderboard of all player deaths, sorted from highest to lowest death count.

**Usage**: 
```
/deathtop
```

**Output Example**:
```
Most Deaths Leaderboard (23 total deaths)
1. Player1 - 5 deaths
2. Player2 - 4 deaths
3. Player3 - 3 deaths
4. Player4 - 2 deaths
5. Player5 - 2 deaths
```

**Features**:
- Color-coded rankings (1st place = Yellow, 2nd = Green, 3rd = Blue, others = Gray)
- Shows total death count across all players
- Only accessible to players (console cannot use this command)

## How It Works

### Death Flow

1. **Player Dies**: Any player death event is detected
2. **Death Recorded**: Player's death count is incremented and saved to config
3. **Countdown Initiated**: If no restart is already scheduled:
   - All online players see a title announcing the death
   - A 10-second countdown timer begins
4. **Server Shutdown**: After the countdown ends, the server shuts down gracefully
5. **World Reset** (Optional): On next server start, if `scheduled` flag is set, the world folder is deleted
6. **Fresh Start**: Server restarts cleanly for the next challenge attempt

### Persistent Death Statistics

After a world reset is completed, the plugin clears the `scheduled` flag but **preserves all death counts**. This means the death leaderboard continuously tracks player survival performance across multiple challenge attempts, creating a persistent record of who has survived the longest throughout the server's lifetime.

## Technical Details

- **Language**: Java 21
- **Build Tool**: Gradle
- **Server Version**: Paper/Spigot 1.21
- **API Version**: 26.1
- **Dependencies**:
  - Spigot API (version 26.1.2-R0.1-SNAPSHOT)
  - Lombok (for annotations)

### Plugin Components

- **ChallengePlugin**: Main plugin class, handles initialization and world deletion
- **ChallengeListener**: Event listener for player death detection
- **DeathTopCommand**: Command executor for the `/deathtop` leaderboard command
- **RestartCountdownTask**: Async task that manages the 10-second countdown with visual effects

## Data Storage

The plugin stores all data in `plugins/HardcoreChallenge/config.yml`:

- Death counts are persisted in YAML format
- Configuration is automatically reloaded after updates
- Data survives server restarts

## Permissions

This plugin does not implement permission nodes. All players can:
- Trigger the death countdown (by dying)
- Use the `/deathtop` command

## Troubleshooting

### World Not Resetting

Ensure the `world` config value matches your actual world folder name. Check the `logs/latest.log` for any error messages related to world deletion.

### Deaths Not Saving

Verify that the plugin has write permissions to `plugins/HardcoreChallenge/config.yml`. Check server logs for any permission or I/O errors.

### No Sound Effects or Title

Ensure players are within a valid world with the entity manager enabled. Some server configurations may disable client-side features like title and sound effects.

## Author

- **Author**: vocoid
- **Website**: https://vocoid.xyz

## License

For license information, see the LICENSE file in the repository (if applicable).

## Contributing

If you'd like to contribute improvements or bug fixes, feel free to submit pull requests!

## Version

Current Version: **1.0-SNAPSHOT**
