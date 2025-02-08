# Java Chess Game

A chess game implementation in Java using Swing for the GUI, object-oriented programming principles, and design patterns following clean code principles. The game features both human vs. computer gameplay with an AI opponent using the minimax algorithm.

## Features

- Graphical user interface built with Java Swing
- Player vs Computer gameplay
- Basic AI implementation using minimax algorithm
- Standard chess rules implementation
- Move validation
- Configurable search depth for AI
- Move history tracking
- Taken pieces tracking

## Technical Details

### Architecture

The project follows object-oriented programming principles with the following key components:

- **Board**: Represents the chess board and manages game state
- **Piece**: Abstract class for chess pieces with specific implementations for each piece type
- **Player**: Abstract class that handles player moves and interactions 
- **AI**: Implements minimax algorithm for computer moves
- **GUI**: Manages the graphical interface using Swing 

### AI Implementation

The computer opponent uses the minimax algorithm with the following features:
- Configurable search depth (default: 6 moves)
- Position evaluation based on:
  - Material value
  - Piece position
  - Board control
  - Stalemate
  - King safety

## How to Run

1. Ensure you have Java Development Kit (JDK) installed
2. Compile the project:
```bash
javac -d out src/**/*.java
```
3. Run the main class:
```bash
# From the out directory
cd out
java main.Main

# Or from the project root
java -cp out main.Main
```

## Game Preview

![Chess Game Preview](https://github.com/user-attachments/assets/fd131daf-1622-49b1-9be5-f833a1830d58)

## Controls

- Click pieces to make moves
- Use the menu options to:
  - Start a new game
  - Setup the game configuration:
    - Activate the AI feature (check computer)
    - Set AI difficulty (search depth)
    - Highlight legal moves
    - Flip board

## Future Improvements

- Undo moves 
- Improve the AI algorithm
- Move time constraints
- Network play support
- Save/Load game functionality

## Requirements

- Java Development Kit (JDK) 8 or higher
