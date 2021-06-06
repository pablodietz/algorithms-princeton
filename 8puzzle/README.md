# 8 Puzzle / Slider Puzzle

<p align="center">
  <img height="300" src="images/logo.png">
</p>

Write a program to solve the 8-puzzle problem (and its natural generalizations) using the A* search algorithm.

<div class="container">
    <div id="game-container"><div data-reactid=".0"><div id="game-board" data-reactid=".0.0"><div class="tile" data-reactid=".0.0.$0">1</div><div class="tile" data-reactid=".0.0.$1">8</div><div class="tile" data-reactid=".0.0.$2">2</div><div class="tile" data-reactid=".0.0.$3">3</div><div class="tile" data-reactid=".0.0.$4">6</div><div class="tile" data-reactid=".0.0.$5">5</div><div class="tile" data-reactid=".0.0.$6"></div><div class="tile" data-reactid=".0.0.$7">7</div><div class="tile" data-reactid=".0.0.$8">4</div></div><div id="menu" data-reactid=".0.1"><h3 id="subtitle" data-reactid=".0.1.0">Solve the puzzle.</h3><a class="button" data-reactid=".0.1.1">Shuffle</a></div></div></div>
</div>

Given a composite systems comprised of randomly distributed insulating and metallic materials: what fraction of the materials need to be metallic so that the composite system is an electrical conductor? Given a porous landscape with water on the surface (or oil below), under what conditions will the water be able to drain through to the bottom (or the oil to gush through to the surface)? Scientists have defined an abstract process known as percolation to model such situations.

## A* search
Now, we describe a solution to the 8-puzzle problem that illustrates a general artificial intelligence methodology known as the A* search algorithm. We define a search node of the game to be a board, the number of moves made to reach the board, and the previous search node. First, insert the initial search node (the initial board, 0 moves, and a null previous search node) into a priority queue. Then, delete from the priority queue the search node with the minimum priority, and insert onto the priority queue all neighboring search nodes (those that can be reached in one move from the dequeued search node). Repeat this procedure until the search node dequeued corresponds to the goal board.
The efficacy of this approach hinges on the choice of priority function for a search node. We consider two priority functions:
* The Hamming priority function is the Hamming distance of a board plus the number of moves made so far to get to the search node. Intuitively, a search node with a small number of tiles in the wrong position is close to the goal, and we prefer a search node if has been reached using a small number of moves.
* The Manhattan priority function is the Manhattan distance of a board plus the number of moves made so far to get to the search node.
To solve the puzzle from a given search node on the priority queue, the total number of moves we need to make (including those already made) is at least its priority, using either the Hamming or Manhattan priority function. Why? Consequently, when the goal board is dequeued, we have discovered not only a sequence of moves from the initial board to the goal board, but one that makes the fewest moves. (Challenge for the mathematically inclined: prove this fact.)
