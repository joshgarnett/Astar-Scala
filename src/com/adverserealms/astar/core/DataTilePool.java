package com.adverserealms.astar.core;

public class DataTilePool {
	protected final int MAX_FREE_OBJECT_INDEX;
	protected DataTile[] freeObjects;
	protected int freeObjectIndex = -1;

	public DataTilePool(int maxSize) {
		freeObjects = new DataTile[maxSize];
		MAX_FREE_OBJECT_INDEX = maxSize - 1;
	}

	public DataTile borrowObject() {
		DataTile obj = null;

		if (freeObjectIndex == -1) {
			obj = new DataTile();
		} else {
			obj = freeObjects[freeObjectIndex];
			freeObjectIndex--;
		}

		return obj;
	}

	public void returnObject(DataTile obj) {
		if (obj != null) {
			obj.reset();

			if (freeObjectIndex < MAX_FREE_OBJECT_INDEX) {
				freeObjectIndex++;
				freeObjects[freeObjectIndex] = obj;
			}
		}
	}

	public int numberOfFreeObjects() {
		return freeObjectIndex;
	}
}
