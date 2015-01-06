package com.ronald8192.playingwiththreads;

public class TestMain {
	public static void main(String[] args) {
		for (int i = 0; i < 8; i++) {
			new ThreadWithQueue().start();
		}
	}
}
