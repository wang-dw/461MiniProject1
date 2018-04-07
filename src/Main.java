
/*
 * David Wang
 * ECE 461
 * Mini-Project 1
 */

import java.util.*;

public class Main {

	static int packets = 100000;
	static int lambda = 8;
	static int mu1 = 5;
	static int mu2 = 5;
	final static int MAXBUFFER = 5;

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

	public static boolean chooseQueue1() {
		Random rand = new Random();
		double phi = 0.5;
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
		int blocked1 = 0;
		int blocked2 = 0;

		for (int i = 0; i < packets; ++i) { // go through each packet
			double arrival = getPoissonRandom(lambda); // arrival time of packet
			double service1 = serviceTime(mu1); // service times of each queue
			double service2 = serviceTime(mu2);

			if (i > 0) {
				double arrival1 = arrival;
				double arrival2 = arrival;
				if (chooseQueue1()) { // if queue 1 is chosen
					if (queue1.size() < MAXBUFFER) { // if buffer is not full
						queue1.add(service1); // add packet servicing time to queue
						while (arrival1 > 0 && queue1.size() > 0) { // while packet still arriving
							arrival1 = arrival1 - queue1.peek(); // subtract servicing time from arrival
							if (arrival1 > 0) { // if packet still arriving
								queue1.poll(); // remove packet in queue
							} else { // if packet arrives
								queue1.add(arrival + queue1.peek()); // remove packet and add time difference
								queue1.poll();
							}
						}
						while (arrival2 > 0 && queue2.size() > 0) { // while packet still arriving
							if (arrival2 > 0) { // if packet still arriving
								queue2.poll(); // remove packet in queue
							} else { // if packet arrives
								queue2.add(arrival2 + queue2.peek()); // remove packet and add time difference
								queue2.poll();
							}
						}
					} else {
						blocked1++;
					}
				} else {
					if (queue2.size() < MAXBUFFER) { // if buffer is not full
						queue2.add(service2); // add packet servicing time to queue
						while (arrival2 > 0 && queue2.size() > 0) { // while packet still arriving
							arrival2 = arrival2 - queue2.peek(); // subtract servicing time from arrival
							if (arrival2 > 0) { // if packet still arriving
								queue2.poll(); // remove packet in queue
							} else { // if packet arrives
								queue2.add(arrival2 + queue2.peek()); // remove packet and add time difference
								queue2.poll();
							}
						}
						while (arrival1 > 0 && queue1.size() > 0) { // while packet still arriving
							if (arrival1 > 0) { // if packet still arriving
								queue1.poll(); // remove packet in queue
							} else { // if packet arrives
								queue1.add(arrival1 + queue1.peek()); // remove packet and add time difference
								queue1.poll();
							}
						}
					} else {
						blocked2++;
					}
				}
			}
		}

		System.out.println(queue1);
		System.out.println(getPoissonRandom(lambda));
		System.out.println("Blocked 1: " + blocked1);
		System.out.println(queue2);
		System.out.println(serviceTime(mu2));
		System.out.println("Blocked 2: " + blocked2);
	}
}
