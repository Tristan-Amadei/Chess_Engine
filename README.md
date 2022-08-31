# chess-engine
This project allows the user to play chess, either against themselves or somebody else, or against an artificial engine which chooses the best play on any given position. 
The engine can either play with the white pieces or the black ones. 

The GUI (Graphical User Interface) was built with JavaFX and SceneBuilder. You'll need them to run the GUI of the project, but should you not have them, you may still play against the engine directly in the console. 

To make it work : 
1. Launch the program present in chess/src/gui/RunningGUI. A window will open and show a chess board.
2. On this board, you can play either side. Clicking on any piece displays dots on the squares on which said piece can go on the next move. 
3. To summon the engine and make it play, press the Enter key. It will play a move for the color which is to play. 
4. To take back one move, press the left arrow key on your keyboard. 
5. To castle, if permitted in your position, click on the square which is two squares away on the right or left of your king, depending of which side you want to castle on. 
