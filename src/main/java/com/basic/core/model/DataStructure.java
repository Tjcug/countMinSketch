package com.basic.core.model;

import com.basic.core.benchmarkmodel.HotKeyUpdateTime;
import com.basic.core.handler.HashHandler;
import com.basic.core.util.FileUtil;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;

public class DataStructure {
	public static final long PRIME_MODULUS = (1L << 31) - 1;
		
	private int width;
	private int depth;
	private int topK;
	private long[][] count;
	private Heap<CountUnit> heap;
	private double epsilon;
	private double delta;
	private double support;
	private long size;

	private long startTimeSystemTime=0L;

	public void setStartTimeSystemTime(long startTimeSystemTime) {
		this.startTimeSystemTime = startTimeSystemTime;
	}

	private Queue<HotKeyUpdateTime> hotKeyUpdateTimes=new ArrayDeque<>();

	public DataStructure(int width, int depth, int topK) {
		this.width = width;
		this.depth = depth;
		this.topK = topK;
		epsilon = Math.E / width;
		delta = 1 / Math.pow(Math.E, depth);
		support = 1 / topK;
		size = 0;
		initCount(width, depth);
	}

	public DataStructure(double epsilon, double delta, double support) {
		this.epsilon = epsilon;
		this.delta = delta;
		this.support = support;
		width = (int)Math.ceil(Math.E / epsilon);
		depth = (int)Math.ceil(Math.log(1/delta));
		topK = (int)Math.ceil(1 / support);
		size = 0;
		initCount(width, depth);
	}
	
	private void initCount(int width, int depth) {
		count = new long[depth][width];
		Comparator<CountUnit> c = new Comparator<CountUnit>() {
			@Override
			public int compare(CountUnit u1, CountUnit u2) {
				// TODO Auto-generated method stub
				return (int)(u2.count - u1.count);
			}
		};
		heap = new Heap<CountUnit>(new CountUnit[topK], c);
	}
	
	public void add(String item, long cnt) {
		if (cnt<0) {
			throw new IllegalArgumentException("Negative increments not implemented");
		}
		size += cnt;
		int[] buckets = HashHandler.getHashBuckets(item, depth, width);
	    for (int i = 0; i < depth; i++) {
	        count[i][buckets[i]] += cnt;
	    }
	    if (size>10000) {
	    long est = estimateCount(item);
	    if (est >= support * size) {
	    	add2Heap(new CountUnit(item, est));
	    } 
	    }
	}
	
	private void add2Heap(CountUnit cu) {

		int position = -1;
		if ((position = heap.contains(cu)) != -1) {
			heap.replace(cu, position);
		} else {
			////记录时间和相应的key
			long nowTimeSystemTime=System.currentTimeMillis();
			hotKeyUpdateTimes.add(new HotKeyUpdateTime(cu.item,nowTimeSystemTime-startTimeSystemTime,new Timestamp(nowTimeSystemTime)));

			if (!heap.isFull()) {
				heap.insert(cu);
			} else if (cu.count > heap.getRoot().count) {
				heap.replace(cu, 0);
			}
		}
	}
	
	
	public long estimateCount(String item) {
		int[] buckets = HashHandler.getHashBuckets(item, depth, width);
		long est = Long.MAX_VALUE;
		for (int i = 0; i < depth; i++) {
			if (count[i][buckets[i]] < est) {
				est = count[i][buckets[i]];
			}
		}
		return est;
	}
	
	public void show() {
		heap.show();
	}

	public void outputKeyUpdateTimesQueue() throws Exception {
		while (!hotKeyUpdateTimes.isEmpty()){
			HotKeyUpdateTime poll = hotKeyUpdateTimes.poll();
			FileUtil.writeAppendTxtFile(new File("D://HotKeyUpdateTime.txt"),poll.getHotkey()+"\t"+poll.getUpdateTime()+"\t"+poll.getTimestamp()+"\n");
		}
	}

	public Heap<CountUnit> getHeap() {
		return heap;
	}

	public void setHeap(Heap<CountUnit> heap) {
		this.heap = heap;
	}
}
