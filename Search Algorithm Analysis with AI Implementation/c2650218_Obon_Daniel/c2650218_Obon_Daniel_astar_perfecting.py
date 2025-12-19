from pyamaze import maze, agent, textLabel
from queue import PriorityQueue
import matplotlib.pyplot as plt
import time
import random

# Set the seed value before creating the maze (Used for demo)
# random.seed(1234)

def h(cell1, cell2):
    x1, y1 = cell1
    x2, y2 = cell2
    return abs(x1 - x2) + abs(y1 - y2)

def aStar(m):
    start = (m.rows, m.cols)
    g_score = {cell: float('inf') for cell in m.grid}
    g_score[start] = 0
    f_score = {cell: float('inf') for cell in m.grid}
    f_score[start] = h(start, (1, 1))

    open = PriorityQueue()
    open.put((h(start, (1, 1)), h(start, (1, 1)), start))
    aPath = {}
    num_explored_paths = 0  # Track the number of explored paths

    while not open.empty():
        currCell = open.get()[2]
        num_explored_paths += 1  # Increment for each explored path

        if currCell == (5, 5):
            break

        for d in 'ESNW':
            if m.maze_map[currCell][d] == True:
                if d == 'E':
                    childCell = (currCell[0], currCell[1] + 1)
                if d == 'W':
                    childCell = (currCell[0], currCell[1] - 1)
                if d == 'N':
                    childCell = (currCell[0] - 1, currCell[1])
                if d == 'S':
                    childCell = (currCell[0] + 1, currCell[1])

                temp_g_score = g_score[currCell] + 1
                temp_f_score = temp_g_score + h(childCell, (1, 1))

                if temp_f_score < f_score[childCell]:
                    g_score[childCell] = temp_g_score
                    f_score[childCell] = temp_f_score
                    open.put((temp_f_score, h(childCell, (1, 1)), childCell))
                    aPath[childCell] = currCell

    fwdPath = {}
    cell = (5, 5)
    while cell != start:
        fwdPath[aPath[cell]] = cell
        cell = aPath[cell]

    totalPath = list(aPath.keys())  # Storing the total exploration path

    # return fwdPath, totalPath
    return num_explored_paths, fwdPath

# Main code for maze display    
# if __name__=='__main__':
#     m = maze(10, 10)
#     m.CreateMaze(5, 5, loopPercent=50)
#     shortest_path, total_path = aStar(m)
#     a_total = agent(m, footprints=True, color='red')  # Agent for total exploration path
#     m.tracePath({a_total: total_path}, delay=100)  # Display the red dots first
#     l_total = textLabel(m, 'A* Total Exploration Path Length', len(total_path) + 1)

#     time.sleep(0.5)  # Adjust the delay time (in seconds) as needed

#     a_shortest = agent(m, footprints=True, color='green', filled=True)  # Agent for shortest path
#     m.tracePath({a_shortest: shortest_path}, delay=100)  # Display the green dots after a delay
#     l_shortest = textLabel(m, 'A* Shortest Path Length', len(shortest_path) + 1)

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
        num_paths, path = aStar(m)  # Retrieve number of paths explored and the path

        end_time = time.time()
        run_times.append(end_time - start_time)
        explored_paths.append(num_paths)

    plt.plot(run_times)
    plt.xlabel('Maze Run')
    plt.ylabel('Time Taken (seconds)')
    plt.title('A* Time Taken for Each Maze Run')
    plt.show()
    
    plt.plot(explored_paths)
    plt.xlabel('Maze Run')
    plt.ylabel('Number of Paths')
    plt.title('A* Number of Paths for Each Maze Run')
    plt.show()

    average_time = sum(run_times) / len(run_times)
    average_explored_paths = sum(explored_paths) / len(explored_paths)

    print(f'A* Average time taken for {maze_runs} maze runs: {average_time:.5f} seconds')
    print(f'A* Average number of paths explored: {average_explored_paths:.2f}')
