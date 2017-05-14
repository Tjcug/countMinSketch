package com.basic.core.model;

import java.util.Comparator;

public class Heap<T> {
	private int size;
	private int currentSize;
	private Comparator<? super T> c;
	private T[] heap;
	
	public Heap(T[] heap, Comparator<? super T> c) {
		this.heap = heap;
		this.c = c;
		size = heap.length;
		currentSize = 0;
	}
	
	public Heap(int size, Comparator<? super T> c) {
		this.size = size;
		this.c = c;
		currentSize = 0;
	}
	
	private int parent(int i) {
		return (i - 1) >> 1;
	}
	private int leftChild(int i) {
		return (i << 1) + 1;
	}
	private int rightChild(int i) {
		return (i + 1) << 1;
	}
	
	private void shiftUp(int i) {
		
	}
	private void shiftDown(int i) {
		int l = leftChild(i);
		int r = rightChild(i);
		int next = i;
		if (l < currentSize && c.compare(heap[l], heap[i]) > 0) {
			next = l;
		}
		if (r < currentSize && c.compare(heap[r], heap[next]) > 0) {
			next = r;
		}
		if (i == next) return;
		swap(i, next);
		shiftDown(next);
	}
	
	private void heapify(int i) {
		heapify(i, currentSize);
	}
	
	private void heapify(int i, int size) {
		int l = leftChild(i);
		int r = rightChild(i);
		int next = i;
		if (l < size && c.compare(heap[l], heap[i]) > 0) {
			next = l;
		}
		if (r < size && c.compare(heap[r], heap[next]) > 0) {
			next = r;
		}
		if (i == next) return;
		swap(i, next);
		heapify(next, size);
	}
	
	private void swap (int i, int j) {
		T tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}
	
	private void buildHeap() {
		for (int i = currentSize/2 - 1; i>=0; i--) {
			heapify(i);
		}
	}
	
	public void insert(T t) {
		if (currentSize >= size) {
			System.out.println("heap is full.");
			return;
		}
		heap[currentSize] = t;
		currentSize ++;
		buildHeap();
	}
	
	public T delete() {
		T root = heap[0];
		swap(0, currentSize-1);
		currentSize --;
		shiftDown(0);
		return root;
	}
	
	public void sort() {
		buildHeap();
		for (int i = currentSize - 1; i>0; i--) {
			swap(0, i);
			heapify(0, i);
		}
	}
	
	public int contains(T t) {
		for (int i=0; i<currentSize; i++) {
			if (heap[i].equals(t)) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isFull() {
		return currentSize == size;
	}
	
	public void replace(T t, int position) {
		if (position == 0) {
			heap[0] = t;
			shiftDown(0);
			return;
		}
		heap[position] = t;
		buildHeap();
	}
	
	public T getRoot() {
		return heap[0];
	}
	
	public void show() {
		sort();
		for (int i=0; i< currentSize; i++) {
			System.out.println(heap[i]);
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
}
