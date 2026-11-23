package wildwestoutlaw.ai;

import java.awt.Rectangle;
import java.util.ArrayList;

import wildwestoutlaw.GamePanel;
import wildwestoutlaw.entity.Npc;

public class PathFinder {
    
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {

        this.gp = gp;
        instantiateNodes();

    }

    public void instantiateNodes() {

        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            if (gp.tileCollision.isTileBlocked(col, row)) {
                node[col][row].solid = true;
            }

            // Set cost
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row ++;
            }
        }

        // Go around the townsfolk instead of getting stuck behind them
        for (Npc npc : gp.npc) {
            if (npc != null) {
                Rectangle box = npc.worldHitbox();
                for (int c = box.x / gp.tileSize; c <= (box.x + box.width - 1) / gp.tileSize; c++) {
                    for (int r = box.y / gp.tileSize; r <= (box.y + box.height - 1) / gp.tileSize; r++) {
                        node[c][r].solid = true;
                    }
                }
            }
        }

        // The player is standing in the goal tile, so it can be reached even if part of it is a wall
        goalNode.solid = false;
    }

    public void getCost(Node node) {

        // G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {

        while (goalReached == false && step <= 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            // Check current node
            currentNode.checked = true;
            openList.remove(currentNode);

            // Open up node
            if (row - 1 >= 0) {
                openNode(node[col][row-1]);
            }
            // Open left node
            if (col - 1 >= 0) {
                openNode(node[col-1][row]);
            }
            // Open down node
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row+1]);
            }
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[col+1][row]);
            }

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 1000;

            for (int i = 0; i < openList.size(); i++) {

                // Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }

                // if F cost is equal, check G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // If no node in openList, end loop
            if (openList.size() == 0)
                break;
            
            // After the loop, openList[bestNodeIndex] is the next step (= currentNode)
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    public void openNode(Node node) {

        if (node.open == false && node.checked == false && node.solid == false) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {

        Node current = goalNode;

        while (current != startNode) {

            pathList.add(0, current);
            current = current.parent;
        }
    }

}
