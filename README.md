# Introduction
9 Apples Morris is a variation on the traditional game, called 9 Men's Morris.
In this game, the men are represented by apples, and the board is represented by an apple tree. 
The objective of the game is to create "branches of apples", called mills in the traditional game.
When a player has created a branch of apples, they are able to remove one of their opponent's apple from the tree, 
granted that apple isn't already in a branch of apples. A player wins when their opponent is reduced to two apples, or
there are no legal moves left for their opponent. The game currently allows players to play against a computer, or another player.

# Current Bugs 
Listed below are the current bugs that are present in the game, from the most harmful to the least harmful.
1. The computer can create a branch of apples, but sometimes the player needs to press on an apple for the computer to remove one of their apples. 
2.	If the first player makes a branch of apples but the only apples they can remove are already in a branch of apples, the game becomes stuck.
3.	When the game is in phase 3 and either the player or computer have been reduced to three apples, they aren’t able to remove apples from the board.
4.	If the computer has created a branch of apples, but all that the player has left are branches of apples, the computer cannot remove anything. 
5.	The game highlights the current apple, but if the player highlights an apple that has no available moves, it doesn’t remove the highlight from that apple.
6.	When the player clicks on the restart button to start the game, the game screen goes blank for a second. This happens every time they restart the game. 
7.	When the game is in the first phase and the last player creates a branch of apples, the game still displays the all the spaces that the players can place their apples, even though there are no more apples left to place on the board. Since this doesn’t happen quite often, the team decided that this bug didn’t need to be fixed.
8.	After the player and computer have been reduced to one apple left in the first phase, the highlighting stops working, however, the game still works. This happens very rarely, and the team decided that it wasn’t an important bug that needed to be fixed.
9.	Sometimes, the game will display Player 2’s apple when it is Player 1’s turn, and vice versa. This happens when a mill is created, and a player is able to remove an opponent’s apple from the board. Due to the implementation of the game, it would be too hard to fix and would cause more bugs in the game.
10.	If two branches of apples are created on the same turn, the player is only allowed to remove one of their opponent’s apples.
11.	In the third phase, when the text displays that restrictions have been lifted on a certain player, it sometimes displays the wrong player.
