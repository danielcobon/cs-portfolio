from pyamaze import maze, agent, COLOR, textLabel
import matplotlib.pyplot as plt
import time
import random

# Set the seed value before creating the maze (Used for demo)
# random.seed(1234)

def BFS(m):
    start = (m.rows, m.cols)
    frontier = [start]
    explored = [start]
    bfsPath = {}
    shortestPath = {}
    num_explored_paths = 0  # Track the number of explored paths

    while len(frontier) > 0:
        currCell = frontier.pop(0)
        num_explored_paths += 1  # Increment for each explored path

        if currCell == (5, 5):
            break

        for d in 'ESNW':
            if m.maze_map[currCell][d] == True:
                if d == 'E':
                    childCell = (currCell[0], currCell[1] + 1)
                elif d == 'W':
                    childCell = (currCell[0], currCell[1] - 1)
                elif d == 'N':
                    childCell = (currCell[0] - 1, currCell[1])
                elif d == 'S':
                    childCell = (currCell[0] + 1, currCell[1])
                if childCell in explored:
                    continue
                explored.append(childCell)
                frontier.append(childCell)
                bfsPath[childCell] = currCell

    # Backtrack to find the shortest path
    fwdPath = {}
    cell = (5, 5)
    while cell != start:
        fwdPath[bfsPath[cell]] = cell
        cell = bfsPath[cell]
    
    # Storing the total exploration path
    totalPath = explored

    # return fwdPath, totalPath
    return num_explored_paths, fwdPath

# Main code for maze display    
# if __name__=='__main__':
#     m = maze(10, 10)
#     m.CreateMaze(5, 5, loopPercent=50)
#     shortest_path, total_path = BFS(m)
    
#     a_total = agent(m, footprints=True, color=COLOR.red)  # Agent for total exploration path
#     m.tracePath({a_total: total_path}, delay=100)  # Display the red dots first
#     l_total = textLabel(m, 'BFS Total Exploration Path Length', len(total_path) + 1)

#     time.sleep(0.5)  # Adjust the delay time (in seconds) as needed

#     a_shortest = agent(m, footprints=True, color=COLOR.green, filled=True)  # Agent for shortest path
#     m.tracePath({a_shortest: shortest_path}, delay=100)  # Display the green dots after a delay
#     l_shortest = textLabel(m, 'BFS Shortest Path Length', len(shortest_path) + 1)

#     m.run()

# Main code with Graphs and values
if __name__ == '__main__':
    maze_runs = 500
    run_times = []
    explored_paths = []

    for _ in range(maze_runs):
        start_time = time.time()

        m = maze(10, 10)
        m.CreateMaze(5, 5, loopPercent=50)
        num_paths, path = BFS(m)  # Retrieve number of paths explored and the path

        end_time = time.time()
        run_times.append(end_time - start_time)
        explored_paths.append(num_paths)

    plt.plot(run_times)
    plt.xlabel('Maze Run')
    plt.ylabel('Time Taken (seconds)')
    plt.title('BFS Time Taken for Each Maze Run')
    plt.show()
    
    plt.plot(explored_paths)
    plt.xlabel('Maze Run')
    plt.ylabel('Number of Paths')
    plt.title('BFS Number of Paths for Each Maze Run')
    plt.show()

    average_time = sum(run_times) / len(run_times)
    average_explored_paths = sum(explored_paths) / len(explored_paths)

    print(f'BFS Average time taken for {maze_runs} maze runs: {average_time:.5f} seconds')
    print(f'BFS Average number of paths explored: {average_explored_paths:.2f}')