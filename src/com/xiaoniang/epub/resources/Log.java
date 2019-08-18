package com.xiaoniang.epub.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

abstract public class Log {
	static private PrintStream stream;
	public static void println(String line) {
		startIfDead();
		stream.println(line);
	}

	public static void println(List<String> list) {
		for (String line : list) {
			println(line);
		}
	}

	public static void print(String string) {
		startIfDead();
		stream.print(string);
	}

	public static void start() {
		try {
			stream = new PrintStream(new File("src/com/xiaoniang/epub/resources/log.txt"));
			
		} catch (FileNotFoundException e) {
			System.out.println("[!] Didn't find the log file");
		}
	}

	public static void close() {
		stream.close();
	}

	public static PrintStream stream() {
		return stream;
	}
	private static void startIfDead() {
		if (stream==null) {
			start();
		}
	}
}
