package deerhunter.utilities;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.BinaryHeap.Node;
import com.badlogic.gdx.utils.IntArray;

import deerhunter.entities.components.Bounds;
import deerhunter.level.Grid;

/** @author Nathan Sweet */
public class AStar {
	private final int width, height;
	private final BinaryHeap<PathNode> open;
	private final PathNode[] nodes;
	int runID;
	private final IntArray path = new IntArray();
	private int targetX, targetY;
	private Grid grid;

	public AStar(int width, int height, Grid grid) {
		this.width = width;
		this.height = height;
		open = new BinaryHeap(width * 4, false);
		nodes = new PathNode[width * height];
		this.grid = grid;
	}

	/** Returns x,y pairs that are the path from the target to the start. */
	public IntArray getPath(Entity entity, int startX, int startY, int targetX, int targetY) {
		this.targetX = targetX;
		this.targetY = targetY;

		path.clear();
		open.clear();

		runID++;
		if (runID < 0)
			runID = 1;

		int index = startY * width + startX;
		PathNode root = nodes[index];
		if (root == null) {
			root = new PathNode(0);
			root.x = startX;
			root.y = startY;
			nodes[index] = root;
		}
		root.parent = null;
		root.pathCost = 0;
		open.add(root, 0);

		int lastColumn = width - 1, lastRow = height - 1;
		int i = 0;
		while (open.size > 0) {
			PathNode node = open.pop();
			if (node.x == targetX && node.y == targetY) {
				while (node != root) {
					path.add(node.x);
					path.add(node.y);
					node = node.parent;
				}
				break;
			}
			node.closedID = runID;
			int x = node.x;
			int y = node.y;
			if (x < lastColumn) {
				addNode(entity, node, x + 1, y, 10);
	/*			if (y < lastRow)
					addNode(entity, node, x + 1, y + 1, 14); // Diagonals cost more, roughly equivalent to sqrt(2).
				if (y > 0)
					addNode(entity, node, x + 1, y - 1, 14);*/
			}
			if (x > 0) {
				addNode(entity, node, x - 1, y, 10);
/*				if (y < lastRow)
					addNode(entity, node, x - 1, y + 1, 14);
				if (y > 0)
					addNode(entity, node, x - 1, y - 1, 14);*/
			}
			if (y < lastRow)
				addNode(entity, node, x, y + 1, 10);
			if (y > 0)
				addNode(entity, node, x, y - 1, 10);
			i++;
		}

		return path;
	}

	private void addNode(Entity entity, PathNode parent, int x, int y, int cost) {
		if (!isValid(entity, x, y))
			return;

		int pathCost = parent.pathCost + cost;
		float score = pathCost + Math.abs(x - targetX) + Math.abs(y - targetY);

		int index = y * width + x;
		PathNode node = nodes[index];
		if (node != null && node.runID == runID) { // Node already encountered for this run.
			if (node.closedID != runID && pathCost < node.pathCost) { // Node isn't closed and new cost is lower.
				// Update the existing node.
				open.setValue(node, score);
				node.parent = parent;
				node.pathCost = pathCost;
			}
		} else {
			// Use node from the cache or create a new one.
			if (node == null) {
				node = new PathNode(0);
				node.x = x;
				node.y = y;
				nodes[index] = node;
			}
			open.add(node, score);
			node.runID = runID;
			node.parent = parent;
			node.pathCost = pathCost;
		}
	}

	protected boolean isValid(Entity entity, int x, int y) {
		Rectangle rect = new Rectangle(x,y,1,1);
		
		//for (Entity entity2 : grid.getEntitiesInRect(rect)) {
		for (Entity entity2 : grid.getEntitiesInRect(new Rectangle(x-1,y-1,2,2))) {
			//System.out.println(rect + " " + new Rectangle(x-1,y,2,2));
			if (entity == entity2)
				continue;
			if (x == targetX && y == targetY)
				continue;
			Rectangle rect2 = entity2.getComponent(Bounds.class).rect;
			if (rect.overlaps(rect2)) {
				//System.out.println("true");
				return false;
			}
		}
		return true;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	static private class PathNode extends Node {
		int runID, closedID, x, y, pathCost;
		PathNode parent;

		public PathNode(float value) {
			super(value);
		}
	}
}