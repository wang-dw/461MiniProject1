
/*
 * David Wang
 * ECE 461
 * Mini-Project 1
 */

import java.util.*;

public class Main {

	static int packets = 100000;
	static double lambda = 8;
	static int mu1 = 5;
	static int mu2 = 5;
	static double phi = .5;
	final static int MAXBUFFER = 6;

	public static int getPoissonRandom(double mean) {
		Random r = new Random();
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do {
			p = p * r.nextDouble();
			k++;
		} while (p > L);
		return k - 1;
	}

	public static boolean chooseQueue() {
		Random rand = new Random();
		double number = rand.nextDouble();

		if (number <= phi) {
			return true;
		} else {
			return false;
		}

	}

	public static double serviceTime(double mean) {
		Random rand = new Random();
		return Math.log(1 - rand.nextDouble()) / (-(1 / mean));
	}

	public static void main(String[] args) {
		PriorityQueue<Double> queue1 = new PriorityQueue<Double>();
		PriorityQueue<Double> queue2 = new PriorityQueue<Double>();
		double blocked1 = 0;
		double blocked2 = 0;
		double total1 = 0;
		double total2 = 0;
		double delay1 = 0;
		double delay2 = 0;

		double timePassed1 = 0;
		double timePassed2 = 0;

		for (int i = 0; i < packets; ++i) { // go through each packet
			double arrival = getPoissonRandom(1 / lambda); // arrival time of packet

			// calculate when to remove from queue, if enough time pass, remove from queue
			if (queue1.size() > 0) {
				if (timePassed1 > queue1.peek()) {
					timePassed1 -= queue1.poll();
					delay1 += timePassed1;
				}
				timePassed1 += arrival;
			} else {
				timePassed1 = 0;
			}

			// calculate when to remove from queue, if enough time pass, remove from queue
			if (queue2.size() > 0) {
				if (timePassed2 > queue2.peek()) {
					timePassed2 -= queue2.poll();
					delay2 += timePassed2;
				}
				timePassed2 += arrival;
			} else {
				timePassed2 = 0;
			}

			if (i > 10000) {
				// packet arrive, add it to a queue
				if (chooseQueue()) {
					total1++;
					if (queue1.size() <= MAXBUFFER) {
						queue1.add(serviceTime(1 / mu1));
					} else {
						blocked1++;
					}
				} else {
					total2++;
					if (queue2.size() <= MAXBUFFER) {
						queue2.add(serviceTime(1 / mu2));
					} else {
						blocked2++;
					}
				}
			}

		}

		double blockedQ1 = blocked1 / total1;
		double blockedQ2 = blocked2 / total2;
		double blockedAll = (blocked1 + blocked2) / (total1 + total2);
		double delayQ1 = delay1 / total1;
		double delayQ2 = delay2 / total2;
		double delayAll = (delay1 + delay2) / (total1 + total2);
		double throughputQ1 = (total1 - blocked1) / total1;
		double throughputQ2 = (total2 - blocked2) / total2;
		double throughputAll = ((total1 + total2) - (blocked1 + blocked2)) / (total1 + total2);
		double averagePacketsQ1 = lambda * phi * (1 - blockedQ1) * delayQ1;
		double averagePacketsQ2 = lambda * (1 - phi) * (1 - blockedQ2) * delayQ2;
		double averagePacketsAll = lambda * (1 - blockedAll) * delayAll;

		System.out.println("Blocked 1: " + blockedQ1);
		System.out.println("Blocked 2: " + blockedQ2);
		System.out.println("Blocked System: " + blockedAll);
		System.out.println("Delay 1: " + delayQ1);
		System.out.println("delay 2: " + delayQ2);
		System.out.println("Delay System: " + delayAll);
		System.out.println("Throughput 1: " + throughputQ1);
		System.out.println("Throughput 2: " + throughputQ2);
		System.out.println("Throughput System: " + throughputAll);
		System.out.println("Average Packets 1: " + averagePacketsQ1);
		System.out.println("Averqage Packets 2: " + averagePacketsQ2);
		System.out.println("Averqage Packets System: " + averagePacketsAll);
	}

}